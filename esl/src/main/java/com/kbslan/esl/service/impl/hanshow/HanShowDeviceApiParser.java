package com.kbslan.esl.service.impl.hanshow;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.EslServiceConfigModel;
import com.kbslan.esl.service.DeviceApiParser;
import com.kbslan.esl.vo.response.notice.EslNoticeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 22:21
 */
@Service
public class HanShowDeviceApiParser implements DeviceApiParser {

    @Override
    public PriceTagDeviceSupplierEnum deviceSupplier() {
        return PriceTagDeviceSupplierEnum.HAN_SHOW;
    }

    @Override
    public UriComponentsBuilder parseEslHealthUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel) || StringUtils.isBlank(eslServiceConfigModel.getHealthUri())) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_HEALTH_ERROR);
        }
        return builder(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), eslServiceConfigModel.getHealthUri());
    }

    @Override
    public UriComponentsBuilder parseLoginUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_LOGIN_ERROR);
        }

        if (!eslServiceConfigModel.isNeedLogin()) {
            return null;
        }

        if (StringUtils.isBlank(eslServiceConfigModel.getLoginUri())) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_LOGIN_ERROR);
        }
        return builder(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), eslServiceConfigModel.getLoginUri());
    }

    @Override
    public UriComponentsBuilder parseBindingStationUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_BINDING_STATION_ERROR);
        }
        //{user}/user/ap PUT 为该门店分配一个基站
        return builder(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api3/{user}/user/ap");
    }

    @Override
    public UriComponentsBuilder parseUnbindingStationUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_UNBINDING_STATION_ERROR);
        }
        //{user}/user/ap/{apMac} DELETE 从该user移除一个基站
        return builder(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api3/{user}/user/ap/{apMac}");
    }


    @Override
    public UriComponentsBuilder parseBindingPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_BINDING_PRICE_TAG_ERROR);
        }
        return builder(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api/v1/tag/bind");
    }

    @Override
    public UriComponentsBuilder parseUnbindingPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_UNBINDING_PRICE_TAG_ERROR);
        }
        //{user}/esls/bind DELETE 批量解绑价签
        return builder(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api3/{user}/esls/bind");
    }

    @Override
    public UriComponentsBuilder parseRefreshPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_REFRESH_PRICE_TAG_ERROR);
        }
        return builder(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api3/{user}/esls/{id}/screen");
    }

    @Override
    public UriComponentsBuilder parseCallbackUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel) || StringUtils.isBlank(eslServiceConfigModel.getCallbackUrl())) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_PRICE_TAG_CALLBACK_ERROR);
        }
        return UriComponentsBuilder.fromHttpUrl(eslServiceConfigModel.getCallbackUrl());
    }

    private UriComponentsBuilder builder(String host, Integer port, String path) {
        return UriComponentsBuilder.fromHttpUrl(host + ":" + port + "/" + path);
    }
}
