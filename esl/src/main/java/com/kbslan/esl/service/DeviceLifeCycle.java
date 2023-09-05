package com.kbslan.esl.service;

import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.vo.pricetag.CommonParams;

/**
 * <p>
 *     厂商设备生命周期 预留处理接口微调处理流程pipeline
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 16:51
 */
public interface DeviceLifeCycle<T extends CommonParams> {
    /**
     * 调用厂商接口绑定前处理逻辑
     *
     * @param params            绑定参数
     * @param deviceEslApiModel 厂商配置
     */
    default void beforeBind(T params, DeviceEslApiModel deviceEslApiModel) {
    }

    /**
     * 调用厂商接口成功后处理逻辑
     *
     * @param params            绑定参数
     * @param deviceEslApiModel 厂商配置
     */
    default void afterBindSuccess(T params, DeviceEslApiModel deviceEslApiModel) {
    }

    /**
     * 绑定关系入库成功后处理逻辑
     *
     * @param params            绑定参数
     * @param deviceEslApiModel 厂商配置
     */
    default void afterBindSaveSuccess(T params, DeviceEslApiModel deviceEslApiModel) {
    }

    /**
     * 调用厂商接口前处理逻辑
     *
     * @param params            解绑参数
     * @param deviceEslApiModel 厂商配置
     */
    default void beforeUnbind(T params, DeviceEslApiModel deviceEslApiModel) {
    }

    /**
     * 调用厂商接口成功后处理逻辑
     *
     * @param params            解绑参数
     * @param deviceEslApiModel 厂商配置
     */
    default void afterUnBindSuccess(T params, DeviceEslApiModel deviceEslApiModel) {
    }

    /**
     * 解绑关系入库成功后处理
     *
     * @param params            解绑参数
     * @param deviceEslApiModel 厂商配置
     */
    default void afterUnBindSaveSuccess(T params, DeviceEslApiModel deviceEslApiModel) {
    }
}
