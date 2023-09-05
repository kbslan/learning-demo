package com.kbslan.esl.service.pricetag;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.PriceTagInfoEntity;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.enums.YNEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.service.PriceTagInfoService;
import com.kbslan.domain.service.SysConfigService;
import com.kbslan.esl.config.RedisUtils;
import com.kbslan.esl.rpc.StoreSkuListVO;
import com.kbslan.esl.rpc.WareClientRpc;
import com.kbslan.esl.service.EslConfigService;
import com.kbslan.esl.service.pricetag.model.PriceTagParams;
import com.kbslan.esl.service.pricetag.model.convert.PriceTagParamsConvert2PriceTagRefreshParams;
import com.kbslan.esl.service.pricetag.model.convert.PriceTagRequestConvert2PriceTagParams;
import com.kbslan.esl.vo.request.pricetag.PriceTagRequest;
import com.kbslan.esl.vo.response.notice.EslNoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 电子价签处理流程抽象模版方法
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:34
 */
@Slf4j
public abstract class PriceTagPipeline implements Predicate<PriceTagRequest>,
        Function<PriceTagRequest, PriceTagParams>, DeviceLifeCycle<PriceTagParams> {
    @Resource
    private EslConfigService eslConfigService;
    @Resource
    private PriceTagServiceFactory priceTagServiceFactory;
    @Resource
    private PriceTagInfoService priceTagInfoService;
    @Resource
    private WareClientRpc wareClientRpc;
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private PriceTagRequestConvert2PriceTagParams priceTagRequestConvert2PriceTagParams;
    @Resource
    private PriceTagParamsConvert2PriceTagRefreshParams priceTagParamsConvert2PriceTagRefreshParams;

    /**
     * 支持的设备厂商
     *
     * @return 设备厂商
     */
    public abstract PriceTagDeviceSupplierEnum deviceSupplier();

    @Override
    public PriceTagParams apply(PriceTagRequest request) {
        return priceTagRequestConvert2PriceTagParams.apply(request);
    }

    @Override
    public boolean test(PriceTagRequest request) {
        return Objects.nonNull(request)
                && Objects.nonNull(request.getDeviceSupplier())
                && Objects.nonNull(request.getVendorId())
                && Objects.nonNull(request.getStoreId())
                && StringUtils.isNotBlank(request.getOriginPriceTagId())
                && CollectionUtils.isNotEmpty(request.getSkuIds())
                && Objects.nonNull(request.getUserId())
                && Objects.nonNull(request.getUserName());
    }

    /**
     * 电子价签绑定流程
     *
     * @param request 绑定参数
     * @return 绑定结果
     * @throws Exception 绑定异常
     */
    public boolean bind(PriceTagRequest request) throws Exception {
        //参数校验
        boolean bindingCheck = test(request);
        if (!bindingCheck) {
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_BIND_PARAMS_MISSING);
        }

        //参数转换处理
        PriceTagParams params = apply(request);

        PriceTagInfoEntity priceTagInfoEntity = queryPriceTagInfoEntity(params);
        if (Objects.nonNull(priceTagInfoEntity) && Objects.equals(priceTagInfoEntity.getYn(), YNEnum.YES.getCode())) {
            //已绑定
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_BEEN_BOUNDED);
        }

        //查询商品信息
        StoreSkuListVO ware = wareClientRpc.getStoreSkus(params.getVendorId(), params.getStoreId(), params.getSkuIds());
        if (Objects.isNull(ware)) {
            //TODO 检查返回值和入参是否一致
            throw new IllegalArgumentException(String.format(EslNoticeMessage.WARE_NOT_FOUND, StringUtils.join(",", params.getSkuIds())));
        }

        //是否需要检查绑定来源
        boolean needCheckBindingSource = eslConfigService.isNeedCheckBindingSource(params.getStoreId());
        if (needCheckBindingSource && Objects.nonNull(priceTagInfoEntity)
                && !Objects.equals(priceTagInfoEntity.getBingingSource(), params.getBingingSource().getCode())) {
            //绑定来源不是一致
            throw new IllegalArgumentException(String.format(EslNoticeMessage.PRICE_TAG_BEEN_BOUNDED_BY_OTHER_SOURCE, priceTagInfoEntity.getBingingSource()));
        }

        //查询厂商配置
        DeviceEslApiModel deviceEslApiModel = eslConfigService.queryAndParseEslConfig(params.getStoreId(), params.getDeviceSupplier());

        //调用厂商接口前处理逻辑
        beforeBind(params, deviceEslApiModel);

        //绑定价签
        boolean bindingSuccess = priceTagServiceFactory.create(params.getDeviceSupplier()).bind(params, deviceEslApiModel);
        if (!bindingSuccess) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_SERVICE_ERROR);
        }

        //调用厂商接口成功后处理逻辑
        afterBindSuccess(params, deviceEslApiModel);

        boolean saveSuccess = false;
        try {
            if (Objects.isNull(priceTagInfoEntity)) {
                //新增绑定关系
                saveSuccess = priceTagInfoService.save(insertBindRecord(params));
            } else {
                //更新绑定关系
                updateBindRecord(params, priceTagInfoEntity);
                saveSuccess = priceTagInfoService.updateById(priceTagInfoEntity);
            }

            if (saveSuccess && params.getNeedPush()) {
                //绑定关系入库成功后处理逻辑
                afterBindSaveSuccess(params, deviceEslApiModel);
            }
        } finally {
            if (!saveSuccess) {
                log.warn("【价签系统和厂商ESL事务一致】 电子价签绑定关系入库失败！！！ 调用厂商接口解绑电子价签");
                try {
                    //不推送数据
                    request.setNeedPush(Boolean.FALSE);
                    //回滚解绑
                    this.unbind(request);
                } catch (Exception e) {
                    //ignore 不再抛出异常
                    log.error("【价签系统和厂商ESL事务一致】 调用厂商接口解绑电子价签失败", e);
                }
            }
        }

        return saveSuccess;
    }


    /**
     * 电子价签解绑流程
     *
     * @param request 解绑参数
     * @return 解绑结果
     * @throws Exception 解绑异常
     */
    public boolean unbind(PriceTagRequest request) throws Exception {
        //参数校验
        boolean unbindCheck = test(request);
        if (!unbindCheck) {
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_UNBIND_PARAMS_MISSING);
        }

        //参数转换处理
        PriceTagParams params = apply(request);

        PriceTagInfoEntity priceTagInfoEntity = queryPriceTagInfoEntity(params);
        if (Objects.isNull(priceTagInfoEntity) || Objects.equals(priceTagInfoEntity.getYn(), YNEnum.NO.getCode())) {
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_NONE_BEEN_BOUNDED);
        }

        //是否需要检查绑定来源
        boolean needCheckBindingSource = eslConfigService.isNeedCheckBindingSource(params.getStoreId());
        if (needCheckBindingSource && !Objects.equals(priceTagInfoEntity.getBingingSource(), params.getBingingSource().getCode())) {
            //绑定来源不是一致
            throw new IllegalArgumentException(String.format(EslNoticeMessage.PRICE_TAG_BEEN_BOUNDED_BY_OTHER_SOURCE, priceTagInfoEntity.getBingingSource()));
        }

        //查询厂商配置
        DeviceEslApiModel deviceEslApiModel = eslConfigService.queryAndParseEslConfig(params.getStoreId(), params.getDeviceSupplier());

        //调用厂商接口前处理逻辑
        beforeUnbind(params, deviceEslApiModel);

        //解绑价签
        boolean unbindingSuccess = priceTagServiceFactory.create(params.getDeviceSupplier()).unbind(params, deviceEslApiModel);
        if (!unbindingSuccess) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_SERVICE_ERROR);
        }


        //调用厂商接口成功后处理逻辑
        afterUnBindSuccess(params, deviceEslApiModel);

        //更新绑定关系
        boolean saveSuccess = false;
        try {
            updateUnBindRecord(params, priceTagInfoEntity);
            saveSuccess = priceTagInfoService.updateById(priceTagInfoEntity);

            if (saveSuccess && params.getNeedPush()) {
                //解绑关系入库成功后处理
                afterUnBindSaveSuccess(params, deviceEslApiModel);
            }
        } finally {
            if (!saveSuccess) {
                log.warn("【价签系统和厂商ESL事务一致】 电子价签解绑关系入库失败！！！ 调用厂商接口绑定电子价签");
                try {
                    //sku绑定原来的sku列表
                    request.setSkuIds(priceTagInfoEntity.getSkuIds());
                    //不推送数据
                    request.setNeedPush(Boolean.FALSE);
                    //回滚绑定
                    this.bind(request);
                } catch (Exception e) {
                    //ignore 不再抛出异常
                    log.error("【价签系统和厂商ESL事务一致】 调用厂商接口绑定电子价签失败", e);
                }
            }
        }

        return saveSuccess;
    }


    /**
     * 更新绑定记录
     *
     * @param params             解绑参数
     * @param priceTagInfoEntity 绑定记录
     */
    protected void updateUnBindRecord(PriceTagParams params, PriceTagInfoEntity priceTagInfoEntity) {
        priceTagInfoEntity.setYn(YNEnum.NO.getCode());
        priceTagInfoEntity.setBingingSource(params.getBingingSource().getCode());
        priceTagInfoEntity.setModifierId(params.getUserId());
        priceTagInfoEntity.setModifierName(params.getUserName());
        priceTagInfoEntity.setModified(LocalDateTime.now());
    }

    /**
     * 更新绑定记录
     *
     * @param params             绑定参数
     * @param priceTagInfoEntity 绑定记录
     */
    protected void updateBindRecord(PriceTagParams params, PriceTagInfoEntity priceTagInfoEntity) {
        priceTagInfoEntity.setYn(YNEnum.YES.getCode());
        priceTagInfoEntity.setBingingSource(params.getBingingSource().getCode());
        priceTagInfoEntity.setModifierId(params.getUserId());
        priceTagInfoEntity.setModifierName(params.getUserName());
        priceTagInfoEntity.setModified(LocalDateTime.now());
    }

    /**
     * 新增绑定记录
     *
     * @param params 绑定参数
     * @return 绑定记录
     */
    protected PriceTagInfoEntity insertBindRecord(PriceTagParams params) {
        PriceTagInfoEntity priceTagInfoEntity = new PriceTagInfoEntity();
        priceTagInfoEntity.setVendorId(params.getVendorId());
        priceTagInfoEntity.setStoreId(params.getStoreId());
        priceTagInfoEntity.setDeviceSupplier(params.getDeviceSupplier().getCode());
        priceTagInfoEntity.setDeviceType(params.getDeviceType().getCode());
        priceTagInfoEntity.setOriginPriceTagId(params.getOriginPriceTagId());
        priceTagInfoEntity.setPriceTagId(params.getPriceTagId());
        priceTagInfoEntity.setBingingSource(params.getBingingSource().getCode());
        //TODO 补全其他字段
//        priceTagInfoEntity.setRfPower();
//        priceTagInfoEntity.setScreenSize();
//        priceTagInfoEntity.setResolutionX();
//        priceTagInfoEntity.setResolutionY();
//        priceTagInfoEntity.setFirmwareId();
        priceTagInfoEntity.setYn(YNEnum.YES.getCode());
        //绑定商品sku列表
        priceTagInfoEntity.setExtJson(JSON.toJSONString(params.getSkuIds()));
        priceTagInfoEntity.setCreatorId(params.getUserId());
        priceTagInfoEntity.setCreatorName(params.getUserName());
        priceTagInfoEntity.setCreated(LocalDateTime.now());
        priceTagInfoEntity.setModifierId(params.getUserId());
        priceTagInfoEntity.setModifierName(params.getUserName());
        priceTagInfoEntity.setModified(LocalDateTime.now());
        return priceTagInfoEntity;
    }


    /**
     * 查询绑定记录 (deviceSupplier + originPriceTagId)
     *
     * @param params 绑定参数
     * @return 绑定记录
     */
    protected PriceTagInfoEntity queryPriceTagInfoEntity(PriceTagParams params) {
        return priceTagInfoService.getOne(
                Wrappers.<PriceTagInfoEntity>lambdaQuery()
                        .eq(PriceTagInfoEntity::getDeviceSupplier, params.getDeviceSupplier().getCode())
                        .eq(PriceTagInfoEntity::getOriginPriceTagId, params.getOriginPriceTagId())
        );
    }

    @Override
    public void afterBindSaveSuccess(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) {
        try {
            priceTagServiceFactory.create(params.getDeviceSupplier())
                    .refresh(priceTagParamsConvert2PriceTagRefreshParams.apply(params), deviceEslApiModel);
        } catch (Exception e) {
            log.error("电子价签刷新失败 params={}", params, e);
        }
    }
}
