package com.kbslan.esl.service;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;

/**
 * <p>
 * 基站服务
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 18:00
 */
public interface StoreStationService {

    /**
     * 支持的设备厂商
     *
     * @return 设备厂商
     */
    PriceTagDeviceSupplierEnum deviceSupplier();

    /**
     * 绑定基站
     *
     * @param storeId           门店ID
     * @param apMac             基站MAC
     * @param deviceEslApiModel 厂商ESL服务接口模型
     * @return 是否绑定成功
     * @throws Exception 绑定异常
     */
    boolean bindStation(String storeId, String apMac, DeviceEslApiModel deviceEslApiModel) throws Exception;

    /**
     * 解绑基站
     *
     * @param storeId           门店ID
     * @param apMac             基站MAC
     * @param deviceEslApiModel 厂商ESL服务接口模型
     * @return 是否解绑成功
     * @throws Exception 解绑异常
     */
    boolean unbindStation(String storeId, String apMac, DeviceEslApiModel deviceEslApiModel) throws Exception;
}
