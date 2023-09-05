package com.kbslan.esl.service;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.model.EslServiceConfigModel;
import com.kbslan.esl.vo.pricetag.CommonParams;

import java.util.Map;


/**
 * <p>
 * 电子价签配置服务
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 18:00
 */
public interface EslConfigService {


    /**
     * <p>
     * 查询门店厂商配置信息
     * </p>
     *
     * @param storeId 门店ID
     * @return 设备厂商code -> 门店配置信息
     * @throws Exception 查询异常
     */
    Map<PriceTagDeviceSupplierEnum, EslServiceConfigModel> query(Long storeId) throws Exception;

    /**
     * <p>
     * 解析厂商ESL服务接口地址
     * </p>
     *
     * @param eslServiceConfigModelMap 电子价签服务配置信息
     * @return 设备厂商 -> 厂商ESL服务接口模型
     * @throws Exception 解析异常
     */
    Map<PriceTagDeviceSupplierEnum, DeviceEslApiModel> parse(Map<PriceTagDeviceSupplierEnum, EslServiceConfigModel> eslServiceConfigModelMap) throws Exception;


    /**
     * <p>
     * 获取厂商ESL接口token,如果厂商需要登录
     * {@see EsServiceConfigModel#isNeedLogin()} 为true，则需要提供用户名和密码
     * </p>
     * <p>
     * 1. 从缓存中获取token，如果存在则直接返回
     * 2. 调用厂商服务获取token
     * 3. 如果token不存在，则抛出异常
     * 4. 如果token存在，则缓存token并返回
     * </p>
     *
     * @param deviceEslApiModel 厂商ESL服务接口模型
     * @return token
     * @throws Exception 获取token异常
     */
    default String getToken(DeviceEslApiModel deviceEslApiModel) throws Exception {
        return null;
    }

    /**
     * <p>
     * 查询并解析厂商ESL服务接口地址
     * </p>
     *
     * @param params 公共参数
     * @return 厂商ESL服务接口模型
     * @throws Exception 查询异常
     */
    DeviceEslApiModel queryAndParseEslConfig(CommonParams params) throws Exception;


    /**
     * 获取Sid
     *
     * @param key redis key
     * @return sid
     */
    long getSid(String key);

    /**
     * 是否需要检查绑定来源
     *
     * @param storeId 门店ID
     * @return true: 需要检查绑定来源 false: 不需要检查绑定来源
     */
    boolean isNeedCheckBindingSource(Long storeId);
}
