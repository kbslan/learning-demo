package com.kbslan.domain.model;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *     厂商ESL服务Api接口模型
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 21:11
 */
@Getter
@Setter
@Builder
public class DeviceEslApiModel implements Serializable {
    private static final long serialVersionUID = -8299891074821876415L;
    /**
     * 设备厂商 {@see PriceTagDeviceSupplierEnum}
     */
    private PriceTagDeviceSupplierEnum deviceSupplier;

    /**
     * 服务域名或ip
     */
    private String host;

    /**
     * 服务端口
     */
    private Integer port;

    /**
     * 服务健康检查url
     */
    private UriComponentsBuilder healthUrl;

    /**
     * 是否需要登录获取token
     */
    private boolean needLogin;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    /**
     * token
     */
    private String token;

    /**
     * 登录url
     */
    private UriComponentsBuilder loginUrl;

    /**
     * 基站绑定url
     */
    private UriComponentsBuilder bindingStationUrl;

    /**
     * 基站解绑url
     */
    private UriComponentsBuilder unbindingStationUrl;

    /**
     * 价签绑定url
     */
    private UriComponentsBuilder bindingPriceTagUrl;

    /**
     * 价签解绑url
     */
    private UriComponentsBuilder unbindingPriceTagUrl;

    /**
     * 价签刷新url
     */
    private UriComponentsBuilder refreshPriceTagUrl;

    /**
     * 价签刷新结果回调Url
     */
    private UriComponentsBuilder callbackUrl;

    /**
     * 额外配置信息
     */
    private Map<String, String> extra;
}
