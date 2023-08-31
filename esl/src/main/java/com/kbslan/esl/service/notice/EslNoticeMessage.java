package com.kbslan.esl.service.notice;

/**
 * <p>
 *    电子价签错误消息提示
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
}
