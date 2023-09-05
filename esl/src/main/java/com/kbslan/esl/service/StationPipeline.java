package com.kbslan.esl.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.ApStoreEntity;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.enums.YNEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.service.ApStoreService;
import com.kbslan.esl.config.RedisUtils;
import com.kbslan.esl.vo.pricetag.StationParams;
import com.kbslan.esl.vo.request.pricetag.StationRequest;
import com.kbslan.esl.vo.response.notice.EslNoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 基站处理流程抽象模版方法
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:34
 */
@Slf4j
public abstract class StationPipeline implements Predicate<StationRequest>,
        Function<StationRequest, StationParams>, DeviceLifeCycle<StationParams> {
    //门店有效基站key
    public static final String HAS_VALID_STORE_KEY = "storeStationValid:%d";
    public static final String STORE_STATION_SID_KEY = "storeStationSid:%d";
    @Resource
    private EslConfigService eslConfigService;
    @Resource
    private StoreStationServiceFactory storeStationServiceFactory;
    @Resource
    private ApStoreService apStoreService;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 支持的设备厂商
     *
     * @return 设备厂商
     */
    public abstract PriceTagDeviceSupplierEnum deviceSupplier();


    /**
     * 参数转换处理
     *
     * @param request 参数
     * @return 转换后的参数
     */
    @Override
    public StationParams apply(StationRequest request) {
        StationParams params = new StationParams();
        params.setVendorId(request.getVendorId());
        params.setStoreId(request.getStoreId());
        params.setDeviceSupplier(request.getDeviceSupplier());
        params.setOriginAp(request.getOriginAp());
        params.setUserId(request.getUserId());
        params.setUserName(request.getUserName());
        params.setBingingSource(request.getBingingSource());
        params.setApMac(request.getOriginAp());
        long sid = eslConfigService.getSid(String.format(STORE_STATION_SID_KEY, request.getStoreId()));
        params.setSid(String.join(",", request.getStoreId().toString(), sid + ""));

        return params;
    }

    /**
     * 参数校验
     *
     * @param request 参数
     * @return 校验结果
     */
    @Override
    public boolean test(StationRequest request) {
        return Objects.nonNull(request)
                && Objects.nonNull(request.getVendorId())
                && Objects.nonNull(request.getStoreId())
                && Objects.nonNull(request.getDeviceSupplier())
                && StringUtils.isNotBlank(request.getOriginAp())
                && Objects.nonNull(request.getUserId())
                && Objects.nonNull(request.getUserName())
                && Objects.nonNull(request.getBingingSource());
    }

    /**
     * 绑定基站流程
     *
     * @param request 绑定参数
     * @return 绑定是否成功
     * @throws Exception 绑定异常
     */
    public boolean bind(StationRequest request) throws Exception {
        //参数校验
        boolean bindingCheck = test(request);
        if (!bindingCheck) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_BIND_PARAMS_MISSING);
        }

        //参数转换处理
        StationParams params = apply(request);

        ApStoreEntity apStoreEntity = queryApStoreEntity(params);

        //存在绑定记录（无状态）
        if (Objects.nonNull(apStoreEntity)) {
            //状态判断
            if (Objects.equals(apStoreEntity.getYn(), YNEnum.YES.getCode())) {
                throw new IllegalArgumentException(EslNoticeMessage.STATION_BEEN_BOUNDED);
            }

            //商家和门店校验
            if (!Objects.equals(apStoreEntity.getVendorId(), params.getVendorId())
                    || !Objects.equals(apStoreEntity.getStoreId(), params.getStoreId())) {

                String formatMessage = String.format(EslNoticeMessage.STATION_BEEN_BOUNDED_BY_OTHER_STORE,
                        apStoreEntity.getVendorId(), apStoreEntity.getStoreId());
                throw new IllegalArgumentException(formatMessage);
            }
        }

        //是否需要检查绑定来源
        boolean needCheckBindingSource = eslConfigService.isNeedCheckBindingSource(params.getStoreId());
        if (needCheckBindingSource && Objects.nonNull(apStoreEntity)
                && !Objects.equals(apStoreEntity.getBingingSource(), params.getBingingSource().getCode())) {
            //绑定来源不是一致
            throw new IllegalArgumentException(String.format(EslNoticeMessage.STATION_BEEN_BOUNDED_BY_OTHER_SOURCE, apStoreEntity.getBingingSource()));
        }

        //查询厂商配置
        DeviceEslApiModel deviceEslApiModel = eslConfigService.queryAndParseEslConfig(params);

        //调用厂商接口前处理逻辑
        beforeBind(params, deviceEslApiModel);

        //绑定基站
        boolean bindingSuccess = storeStationServiceFactory.create(params.getDeviceSupplier()).bind(params, deviceEslApiModel);

        if (!bindingSuccess) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_SERVICE_ERROR);
        }

        //调用厂商接口成功后处理逻辑
        afterBindSuccess(params, deviceEslApiModel);

        boolean saveSuccess = false;
        try {
            if (Objects.isNull(apStoreEntity)) {
                //新增绑定关系
                saveSuccess = apStoreService.save(insertBindRecord(params));
            } else {
                //更新绑定关系
                updateBindRecord(params, apStoreEntity);
                saveSuccess = apStoreService.updateById(apStoreEntity);
            }

            if (saveSuccess) {
                //绑定关系入库成功后处理逻辑
                afterBindSaveSuccess(params, deviceEslApiModel);
            }

        } finally {
            if (!saveSuccess) {
                log.warn("【价签系统和厂商ESL事务一致】 基站绑定关系入库失败！！！ 调用厂商接口解绑基站");
                try {
                    //回滚解绑
                    this.unbind(request);
                } catch (Exception e) {
                    //ignore 不再抛出异常
                    log.error("【价签系统和厂商ESL事务一致】 调用厂商接口解绑基站失败", e);
                }
            }
        }

        return saveSuccess;
    }


    /**
     * 解绑基站流程
     *
     * @param request 解绑参数
     * @return 解绑是否成功
     * @throws Exception 解绑异常
     */
    public boolean unbind(StationRequest request) throws Exception {
        //参数校验
        boolean unbindCheck = test(request);
        if (!unbindCheck) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_UNBIND_PARAMS_MISSING);
        }
        //参数转换处理
        StationParams params = apply(request);

        ApStoreEntity apStoreEntity = queryApStoreEntity(params);

        if (Objects.isNull(apStoreEntity) || Objects.equals(apStoreEntity.getYn(), YNEnum.NO.getCode())) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_NONE_BEEN_BOUNDED);
        }

        //商家和门店校验
        if (Objects.equals(apStoreEntity.getYn(), YNEnum.YES.getCode())
                && (!Objects.equals(apStoreEntity.getVendorId(), params.getVendorId())
                || !Objects.equals(apStoreEntity.getStoreId(), params.getStoreId()))) {
            throw new IllegalArgumentException(String.format(EslNoticeMessage.STATION_BEEN_BOUNDED_BY_OTHER_STORE,
                    apStoreEntity.getVendorId(), apStoreEntity.getStoreId()));
        }

        //是否需要检查绑定来源
        boolean needCheckBindingSource = eslConfigService.isNeedCheckBindingSource(params.getStoreId());
        if (needCheckBindingSource && !Objects.equals(apStoreEntity.getBingingSource(), params.getBingingSource().getCode())) {
            //绑定来源不是一致
            throw new IllegalArgumentException(String.format(EslNoticeMessage.STATION_BEEN_BOUNDED_BY_OTHER_SOURCE, apStoreEntity.getBingingSource()));
        }

        //查询厂商配置
        DeviceEslApiModel deviceEslApiModel = eslConfigService.queryAndParseEslConfig(params);

        //调用厂商接口前处理逻辑
        beforeUnbind(params, deviceEslApiModel);

        boolean unbindingSuccess = storeStationServiceFactory.create(params.getDeviceSupplier()).unbind(params, deviceEslApiModel);
        if (!unbindingSuccess) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_SERVICE_ERROR);
        }

        //调用厂商接口成功后处理逻辑
        afterUnBindSuccess(params, deviceEslApiModel);

        //更新绑定关系
        boolean saveSuccess = false;
        try {
            updateUnBindRecord(params, apStoreEntity);
            saveSuccess = apStoreService.updateById(apStoreEntity);

            if (saveSuccess) {
                //解绑关系入库成功后处理
                afterUnBindSaveSuccess(params, deviceEslApiModel);
            }
        } finally {
            if (!saveSuccess) {
                log.warn("【价签系统和厂商ESL事务一致】基站解绑关系入库失败！！！ 调用厂商重新绑定基站");
                try {
                    //回滚绑定
                    this.bind(request);
                } catch (Exception e) {
                    //ignore 不再抛出异常
                    log.error("【价签系统和厂商ESL事务一致】调用厂商重新绑定基站失败", e);
                }
            }
        }

        return saveSuccess;
    }

    @Override
    public void afterBindSaveSuccess(StationParams params, DeviceEslApiModel deviceEslApiModel) {
        try {
            redisUtils.set.sadd(String.format(HAS_VALID_STORE_KEY, params.getStoreId()), params.getApMac());
        } catch (Exception e) {
            log.error("redis 添加有效基站失败 params={}", params, e);
        }
    }


    @Override
    public void afterUnBindSaveSuccess(StationParams params, DeviceEslApiModel deviceEslApiModel) {
        try {
            redisUtils.set.srem(String.format(HAS_VALID_STORE_KEY, params.getStoreId()), params.getApMac());
        } catch (Exception e) {
            log.error("redis 添加有效基站失败 params={}", params, e);
        }
    }


    /**
     * 更新绑定记录
     *
     * @param params        绑定参数
     * @param apStoreEntity 绑定记录
     */
    protected void updateBindRecord(StationParams params, ApStoreEntity apStoreEntity) {
        apStoreEntity.setYn(YNEnum.YES.getCode());
        apStoreEntity.setBingingSource(params.getBingingSource().getCode());
        apStoreEntity.setModifierId(params.getUserId());
        apStoreEntity.setModifierName(params.getUserName());
        apStoreEntity.setModified(LocalDateTime.now());
    }

    /**
     * 更新绑定记录
     *
     * @param params        解绑参数
     * @param apStoreEntity 绑定记录
     */
    protected void updateUnBindRecord(StationParams params, ApStoreEntity apStoreEntity) {
        apStoreEntity.setYn(YNEnum.NO.getCode());
        apStoreEntity.setBingingSource(params.getBingingSource().getCode());
        apStoreEntity.setModifierId(params.getUserId());
        apStoreEntity.setModifierName(params.getUserName());
        apStoreEntity.setModified(LocalDateTime.now());
    }

    /**
     * 新增绑定记录
     *
     * @param params 绑定参数
     * @return 绑定记录
     */
    protected ApStoreEntity insertBindRecord(StationParams params) {
        ApStoreEntity apStoreEntity = new ApStoreEntity();
        apStoreEntity.setVendorId(params.getVendorId());
        apStoreEntity.setStoreId(params.getStoreId());
        apStoreEntity.setDeviceSupplier(params.getDeviceSupplier().getCode());
        apStoreEntity.setOriginAp(params.getOriginAp());
        apStoreEntity.setApMac(params.getApMac());
        apStoreEntity.setBingingSource(params.getBingingSource().getCode());
        apStoreEntity.setYn(YNEnum.YES.getCode());
        apStoreEntity.setCreatorId(params.getUserId());
        apStoreEntity.setCreatorName(params.getUserName());
        apStoreEntity.setCreated(LocalDateTime.now());
        apStoreEntity.setModifierId(params.getUserId());
        apStoreEntity.setModifierName(params.getUserName());
        apStoreEntity.setModified(LocalDateTime.now());
        return apStoreEntity;
    }

    /**
     * 查询绑定记录 (deviceSupplier + originAp)
     *
     * @param params 绑定参数
     * @return 绑定记录
     */
    protected ApStoreEntity queryApStoreEntity(StationParams params) {
        return apStoreService.getOne(
                Wrappers.<ApStoreEntity>lambdaQuery()
                        .eq(ApStoreEntity::getDeviceSupplier, params.getDeviceSupplier().getCode())
                        .eq(ApStoreEntity::getOriginAp, params.getOriginAp())
        );
    }

}
