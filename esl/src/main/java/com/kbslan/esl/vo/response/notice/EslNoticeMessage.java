package com.kbslan.esl.vo.response.notice;

/**
 * <p>
 * 电子价签错误消息提示
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 18:16
 */
public class EslNoticeMessage {

    public static final String ESL_CONFIG_NOT_FOUND = "厂商ESL服务配置信息未配置";
    public static final String ESL_DEVICE_SUPPLIER_ERROR = "厂商编码不合法";
    public static final String DEVICE_API_PARSER_NOT_FOUNT = "厂商ESL服务接口解析器未找到";
    public static final String PARSE_HEALTH_ERROR = "厂商ESL服务健康检查API解析失败";
    public static final String PARSE_LOGIN_ERROR = "厂商ESL服务登录API解析失败";
    public static final String PARSE_BINDING_STATION_ERROR = "厂商ESL服务绑定基站API解析失败";
    public static final String PARSE_UNBINDING_STATION_ERROR = "厂商ESL服务解绑基站API解析失败";
    public static final String PARSE_BINDING_PRICE_TAG_ERROR = "厂商ESL服务绑定电子价签API解析失败";
    public static final String PARSE_UNBINDING_PRICE_TAG_ERROR = "厂商ESL服务解绑电子价签API解析失败";
    public static final String PARSE_REFRESH_PRICE_TAG_ERROR = "厂商ESL服务刷新电子价签API解析失败";
    public static final String ESL_LOGIN_ERROR = "厂商ESL服务登录失败";
    public static final String STATION_BEEN_BOUNDED = "基站已绑定，请勿重复绑定";
    public static final String STATION_BEEN_BOUNDED_BY_OTHER_STORE = "基站已被其他门店绑定, 如需操作请先在原门店解除绑定关系! vendor:%s, store:%s";
    public static final String ESL_SERVICE_ERROR = "厂商接口异常";
    public static final String STATION_BIND_PARAMS_MISSING = "基站绑定参数缺失";
    public static final String STATION_UNBIND_PARAMS_MISSING = "基站解绑参数缺失";
    public static final String STATION_NONE_BEEN_BOUNDED = "基站未绑定, 无需解绑";
    public static final String PRICE_TAG_BIND_PARAMS_MISSING = "电子价签绑定参数缺失";
    public static final String PRICE_TAG_BEEN_BOUNDED = "电子价签已绑定，请勿重复绑定";
    public static final String WARE_NOT_FOUND = "商品不存在 sku:%s";

    public static final String STATION_BEEN_BOUNDED_BY_OTHER_SOURCE = "基站已被其他来源绑定[%s], 如需操作请先在原来源解除绑定关系!";
    public static final String PRICE_TAG_BEEN_BOUNDED_BY_OTHER_SOURCE = "电子价签已被其他来源绑定[%s], 如需操作请先在原来源解除绑定关系!";
    public static final String PRICE_TAG_UNBIND_PARAMS_MISSING = "电子价签解绑参数缺失";
    public static final String PRICE_TAG_NONE_BEEN_BOUNDED = "电子价签未绑定, 无需解绑";
    public static final String PARSE_PRICE_TAG_CALLBACK_ERROR = "厂商电子价签刷新结果回调解析失败";
    public static final String ESL_CONFIG_PARSE_ERROR = "厂商ESL服务配置信息错误，解析失败";

}
