package com.kbslan.esl.service;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.vo.PriceTagParams;
import com.kbslan.esl.vo.PriceTagRefreshParams;

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
     * @param params            刷新参数
     * @param deviceEslApiModel 厂商ESL服务接口模型
     * @return 是否刷新成功
     * @throws Exception 刷新异常
     */
    boolean refresh(PriceTagRefreshParams params, DeviceEslApiModel deviceEslApiModel) throws Exception;


    /**
     * 基站心跳处理逻辑
     *
     * @param json 心跳参数
     * @throws Exception 异常
     */
    void heartbeat(String json) throws Exception;


    /**
     * 价签刷新结果回调处理逻辑
     *
     * @param json 回调参数
     * @throws Exception 异常
     */
    void callback(String json) throws Exception;
}
