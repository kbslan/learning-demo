package com.kbslan.esl.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.ApStoreEntity;
import com.kbslan.domain.enums.YNEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.service.ApStoreService;
import com.kbslan.esl.service.notice.EslNoticeMessage;
import com.kbslan.esl.vo.StationParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  基站处理流程抽象模版方法
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:34
 */
@Slf4j
public abstract class StationPipeline {
    @Resource
    private EslConfigService eslConfigService;
    @Resource
    private StoreStationServiceFactory storeStationServiceFactory;
    @Resource
    private ApStoreService apStoreService;

    /**
     * 绑定基站流程
     *
     * @param params 绑定参数
     * @return 绑定是否成功
     * @throws Exception 绑定异常
     */
    public boolean bind(StationParams params) throws Exception {
        //参数校验
        boolean bindingCheck = bindCheck(params);
        if (!bindingCheck) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_BIND_PARAMS_MISSING);
        }

        ApStoreEntity apStoreEntity = queryApStoreEntity(params);

        if (Objects.nonNull(apStoreEntity)) {
            if (Objects.equals(apStoreEntity.getYn(), YNEnum.YES.getCode())) {
                //已绑定
                throw new IllegalArgumentException(EslNoticeMessage.STATION_BEEN_BOUNDED);
            } else if (!Objects.equals(apStoreEntity.getVendorId(), params.getVendorId())
                    || !Objects.equals(apStoreEntity.getStoreId(), params.getStoreId())) {
                //商家和门店校验
                throw new IllegalArgumentException(String.format(EslNoticeMessage.STATION_BEEN_BOUNDED_BY_OTHER_STORE, apStoreEntity.getVendorId(), apStoreEntity.getStoreId()));
            }
        }

        //查询厂商配置
        DeviceEslApiModel deviceEslApiModel = eslConfigService.queryAndParseEslConfigByDeviceSupplier(params);

        //绑定基站
        boolean bindingSuccess = storeStationServiceFactory.create(params.getDeviceSupplier()).bind(params, deviceEslApiModel);

        if (!bindingSuccess) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_SERVICE_ERROR);
        }

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
     * 解绑基站流程
     *
     * @param params 解绑参数
     * @return 解绑是否成功
     * @throws Exception 解绑异常
     */
    public boolean unbind(StationParams params) throws Exception {
        boolean unbindCheck = unbindCheck(params);
        if (!unbindCheck) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_UNBIND_PARAMS_MISSING);
        }

        ApStoreEntity apStoreEntity = queryApStoreEntity(params);

        if (Objects.isNull(apStoreEntity) || Objects.equals(apStoreEntity.getYn(), YNEnum.NO.getCode())) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_NONE_BEEN_BOUNDED);
        }

        //商家和门店校验
        if (Objects.equals(apStoreEntity.getYn(), YNEnum.YES.getCode())
                && (!Objects.equals(apStoreEntity.getVendorId(), params.getVendorId())
                || !Objects.equals(apStoreEntity.getStoreId(), params.getStoreId()))) {
            throw new IllegalArgumentException(String.format(EslNoticeMessage.STATION_BEEN_BOUNDED_BY_OTHER_STORE, apStoreEntity.getVendorId(), apStoreEntity.getStoreId()));
        }

        //查询厂商配置
        DeviceEslApiModel deviceEslApiModel = eslConfigService.queryAndParseEslConfigByDeviceSupplier(params);

        boolean unbindingSuccess = storeStationServiceFactory.create(params.getDeviceSupplier()).unbind(params, deviceEslApiModel);
        if (!unbindingSuccess) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_SERVICE_ERROR);
        }

        //更新绑定关系
        boolean saveSuccess = false;
        try {
            updateUnBindRecord(params, apStoreEntity);
            saveSuccess = apStoreService.updateById(apStoreEntity);
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
    private boolean unbindCheck(StationParams params) {
        return Objects.nonNull(params)
                && Objects.nonNull(params.getDeviceSupplier())
                && Objects.nonNull(params.getVendorId())
                && Objects.nonNull(params.getStoreId())
                && Objects.nonNull(params.getApMac())
                && Objects.nonNull(params.getUserId())
                && Objects.nonNull(params.getUserName());
    }

    /**
     * 更新绑定记录
     *
     * @param params        绑定参数
     * @param apStoreEntity 绑定记录
     */
    protected void updateBindRecord(StationParams params, ApStoreEntity apStoreEntity) {
        apStoreEntity.setYn(YNEnum.YES.getCode());
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
        apStoreEntity.setOriginAp(params.getOriginApId());
        apStoreEntity.setApMac(params.getApMac());
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
     * 查询绑定记录
     *
     * @param params 绑定参数
     * @return 绑定记录
     */
    protected ApStoreEntity queryApStoreEntity(StationParams params) {
        return apStoreService.getOne(
                Wrappers.<ApStoreEntity>lambdaQuery()
                        .eq(ApStoreEntity::getDeviceSupplier, params.getDeviceSupplier().getCode())
                        .eq(ApStoreEntity::getApMac, params.getApMac())
        );
    }

    /**
     * 参数校验
     *
     * @param params 绑定参数
     * @return 校验结果
     */
    protected boolean bindCheck(StationParams params) {
        return Objects.nonNull(params)
                && Objects.nonNull(params.getDeviceSupplier())
                && Objects.nonNull(params.getVendorId())
                && Objects.nonNull(params.getStoreId())
                && StringUtils.isNotBlank(params.getApMac())
                && Objects.nonNull(params.getUserId())
                && Objects.nonNull(params.getUserName());

    }


}
