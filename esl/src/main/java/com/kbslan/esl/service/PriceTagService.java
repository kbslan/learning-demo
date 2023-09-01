package com.kbslan.esl.service;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.vo.PriceTagParams;

/**
 * <p>
 * 电子价签服务
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 23:02
 */
public interface PriceTagService {

    /**
     * 支持的设备厂商
     *
     * @return 设备厂商
     */
    PriceTagDeviceSupplierEnum deviceSupplier();


    /**
     * 绑定电子价签
     *
     * @param params            绑定参数
     * @param deviceEslApiModel 厂商ESL服务接口模型
     * @return 是否绑定成功
     * @throws Exception 绑定异常
     */
    boolean bind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws Exception;


    /**
     * 解绑电子价签
     *
     * @param params            解绑参数
     * @param deviceEslApiModel 厂商ESL服务接口模型
     * @return 是否解绑成功
     * @throws Exception 解绑异常
     */
    boolean unbind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws Exception;

    /**
     * 刷新电子价签
     *
     * @param storeId           门店ID
     * @param priceTagId        电子价签ID
     * @param data              刷新数据
     * @param deviceEslApiModel 厂商ESL服务接口模型
     * @return 是否刷新成功
     * @throws Exception 刷新异常
     */
    boolean refresh(String storeId, String priceTagId, Object data, DeviceEslApiModel deviceEslApiModel) throws Exception;
}
