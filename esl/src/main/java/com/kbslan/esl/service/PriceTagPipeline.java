package com.kbslan.esl.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.PriceTagInfoEntity;
import com.kbslan.domain.entity.SysConfigEntity;
import com.kbslan.domain.enums.ConfigKeyEnum;
import com.kbslan.domain.enums.YNEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.service.PriceTagInfoService;
import com.kbslan.domain.service.SysConfigService;
import com.kbslan.esl.rpc.StoreSkuListVO;
import com.kbslan.esl.rpc.WareClientRpc;
import com.kbslan.esl.service.notice.EslNoticeMessage;
import com.kbslan.esl.vo.PriceTagParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  电子价签处理流程抽象模版方法
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:34
 */
@Slf4j
public abstract class PriceTagPipeline {
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

    public boolean bind(PriceTagParams params) throws Exception {
        //参数校验
        boolean bindingCheck = bindCheck(params);
        if (!bindingCheck) {
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_BIND_PARAMS_MISSING);
        }
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

        //查询厂商配置
        DeviceEslApiModel deviceEslApiModel = eslConfigService.queryAndParseEslConfigByDeviceSupplier(params);

        //是否需要检查绑定来源
        boolean needCheckBindingSource = isNeedCheckBindingSource(params);
        if (needCheckBindingSource && Objects.nonNull(priceTagInfoEntity)
                && !Objects.equals(priceTagInfoEntity.getBingingSource(), params.getBingingSource().getCode())) {
            //绑定来源不是一致
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_BEEN_BOUNDED_BY_OTHER_SOURCE);
        }

        boolean bindingSuccess = priceTagServiceFactory.create(params.getDeviceSupplier()).bind(params, deviceEslApiModel);
        if (!bindingSuccess) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_SERVICE_ERROR);
        }

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

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!saveSuccess) {
                //TODO 保证入库成功 否则和厂商会存在事务不一致问题
                log.error("解绑关系入库失败！！！ 必须要保证入库成功 否则和厂商会存在事务不一致问题");
            }
        }

        return saveSuccess;
    }

    public boolean unbind(PriceTagParams params) throws Exception {
        boolean unbindCheck = unbindCheck(params);
        if (!unbindCheck) {
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_UNBIND_PARAMS_MISSING);
        }
        PriceTagInfoEntity priceTagInfoEntity = queryPriceTagInfoEntity(params);
        if (Objects.isNull(priceTagInfoEntity) || Objects.equals(priceTagInfoEntity.getYn(), YNEnum.NO.getCode())) {
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_NONE_BEEN_BOUNDED);
        }

        //查询厂商配置
        DeviceEslApiModel deviceEslApiModel = eslConfigService.queryAndParseEslConfigByDeviceSupplier(params);
        //是否需要检查绑定来源
        boolean needCheckBindingSource = isNeedCheckBindingSource(params);
        if (needCheckBindingSource && !Objects.equals(priceTagInfoEntity.getBingingSource(), params.getBingingSource().getCode())) {
            //绑定来源不是一致
            throw new IllegalArgumentException(EslNoticeMessage.PRICE_TAG_BEEN_BOUNDED_BY_OTHER_SOURCE);
        }

        boolean unbindingSuccess = priceTagServiceFactory.create(params.getDeviceSupplier()).unbind(params, deviceEslApiModel);
        if (!unbindingSuccess) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_SERVICE_ERROR);
        }


        //更新绑定关系
        boolean saveSuccess = false;
        try {
            updateUnBindRecord(params, priceTagInfoEntity);
            saveSuccess = priceTagInfoService.updateById(priceTagInfoEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!saveSuccess) {
                //TODO 保证入库成功 否则和厂商会存在事务不一致问题
                log.error("解绑关系入库失败！！！ 必须要保证入库成功 否则和厂商会存在事务不一致问题");
            }
        }

        return saveSuccess;
    }

    /**
     * 解绑参数校验
     *
     * @param params 解绑参数
     * @return 校验结果
     */
    private boolean unbindCheck(PriceTagParams params) {
        return Objects.nonNull(params)
                && Objects.nonNull(params.getDeviceSupplier())
                && Objects.nonNull(params.getVendorId())
                && Objects.nonNull(params.getStoreId())
                && StringUtils.isNotBlank(params.getPriceTagId())
                && Objects.nonNull(params.getUserId())
                && Objects.nonNull(params.getUserName());
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

    private void updateBindRecord(PriceTagParams params, PriceTagInfoEntity priceTagInfoEntity) {
        priceTagInfoEntity.setYn(YNEnum.YES.getCode());
        priceTagInfoEntity.setBingingSource(params.getBingingSource().getCode());
        priceTagInfoEntity.setModifierId(params.getUserId());
        priceTagInfoEntity.setModifierName(params.getUserName());
        priceTagInfoEntity.setModified(LocalDateTime.now());
    }

    private PriceTagInfoEntity insertBindRecord(PriceTagParams params) {
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

    private boolean isNeedCheckBindingSource(PriceTagParams params) {
        SysConfigEntity sysConfig = sysConfigService.getOne(
                Wrappers.<SysConfigEntity>lambdaQuery()
                        .eq(SysConfigEntity::getVendorId, params.getVendorId())
                        .eq(SysConfigEntity::getStoreId, params.getStoreId())
                        .eq(SysConfigEntity::getYn, YNEnum.YES.getCode())
                        .eq(SysConfigEntity::getConfigKey, ConfigKeyEnum.ESL_CHECK_BINDING_SOURCE.getCode())
        );

        return Objects.nonNull(sysConfig) && Boolean.parseBoolean(sysConfig.getConfigValue());
    }

    /**
     * 查询绑定记录
     *
     * @param params 绑定参数
     * @return 绑定记录
     */
    protected PriceTagInfoEntity queryPriceTagInfoEntity(PriceTagParams params) {
        return priceTagInfoService.getOne(
                Wrappers.<PriceTagInfoEntity>lambdaQuery()
                        .eq(PriceTagInfoEntity::getDeviceSupplier, params.getDeviceSupplier().getCode())
                        .eq(PriceTagInfoEntity::getPriceTagId, params.getPriceTagId())
        );
    }

    /**
     * 参数校验
     *
     * @param params 绑定参数
     * @return 校验结果
     */
    private boolean bindCheck(PriceTagParams params) {
        return Objects.nonNull(params)
                && Objects.nonNull(params.getDeviceSupplier())
                && Objects.nonNull(params.getVendorId())
                && Objects.nonNull(params.getStoreId())
                && StringUtils.isNotBlank(params.getPriceTagId())
                && CollectionUtils.isNotEmpty(params.getSkuIds())
                && Objects.nonNull(params.getUserId())
                && Objects.nonNull(params.getUserName());
    }
}
