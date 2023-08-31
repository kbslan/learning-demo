package com.kbslan.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *     电子价签厂商ESL服务配置信息
 * </p>
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2023/8/22 18:07
 */
@Getter
@Setter
@ToString
public class EslServiceConfigModel implements Serializable {

    private static final long serialVersionUID = 8277416824479261684L;

    /**
     * 服务域名或ip
     */
    private String host;
    /**
     * 服务端口
     */
    private Integer port;
    /**
     * 服务健康检查接口uri
     */
    private String healthUri;
    /**
     * 是否需要登录获取toke
     */
    private boolean needLogin = false;

    /**
     * 登录接口uri
     */
    private String loginUri;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 价签刷新结果回调地址
     */
    private String callbackUrl;

    /**
     * 额外配置
     */
    private Map<String, String> extra;
}
