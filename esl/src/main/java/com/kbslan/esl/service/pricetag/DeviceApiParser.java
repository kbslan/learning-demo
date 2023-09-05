package com.kbslan.esl.service.pricetag;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.EslServiceConfigModel;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 22:19
 */
public interface DeviceApiParser {

    /**
     * 解析器支持的设备厂商
     *
     * @return 设备厂商
     */
    PriceTagDeviceSupplierEnum deviceSupplier();

    /**
     * 解析厂商ESL服务健康检查URL
     *
     * @param eslServiceConfigModel 配置信息
     * @return 健康检查URL
     * @throws Exception 解析异常
     */
    UriComponentsBuilder parseEslHealthUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception;

    /**
     * 解析登录URL
     *
     * @param eslServiceConfigModel 配置信息
     * @return 登录URL
     * @throws Exception 解析异常
     */
    UriComponentsBuilder parseLoginUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception;

    /**
     * 解析绑定基站URL
     *
     * @param eslServiceConfigModel 配置信息
     * @return 绑定基站URL
     * @throws Exception 解析异常
     */
    UriComponentsBuilder parseBindingStationUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception;

    /**
     * 解析解绑基站URL
     *
     * @param eslServiceConfigModel 配置信息
     * @return 解绑基站URL
     * @throws Exception 解析异常
     */
    UriComponentsBuilder parseUnbindingStationUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception;

    /**
     * 解析刷新价签URL
     *
     * @param eslServiceConfigModel 配置信息
     * @return 刷新价签URL
     * @throws Exception 解析异常
     */
    UriComponentsBuilder parseBindingPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception;

    /**
     * 解析解绑价签URL
     *
     * @param eslServiceConfigModel 配置信息
     * @return 解绑价签URL
     * @throws Exception 解析异常
     */
    UriComponentsBuilder parseUnbindingPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception;

    /**
     * 解析刷新价签URL
     *
     * @param eslServiceConfigModel 配置信息
     * @return 刷新价签URL
     * @throws Exception 解析异常
     */
    UriComponentsBuilder parseRefreshPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception;

    /**
     * 解析刷新价签回调URL
     *
     * @param eslServiceConfigModel 配置信息
     * @return 刷新价签回调URL
     * @throws Exception 解析异常
     */
    UriComponentsBuilder parseCallbackUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception;
}
