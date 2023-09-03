package com.kbslan.esl.service.impl.hanshow;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.EslServiceConfigModel;
import com.kbslan.esl.service.DeviceApiParser;
import com.kbslan.esl.service.notice.EslNoticeMessage;
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
    public String parseEslHealthUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel) || StringUtils.isBlank(eslServiceConfigModel.getHealthUri())) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_HEALTH_ERROR);
        }
        return fromHttpUrl(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), eslServiceConfigModel.getHealthUri());
    }

    @Override
    public String parseLoginUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_LOGIN_ERROR);
        }

        if (!eslServiceConfigModel.isNeedLogin()) {
            return null;
        }

        if (StringUtils.isBlank(eslServiceConfigModel.getLoginUri())) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_LOGIN_ERROR);
        }
        return fromHttpUrl(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), eslServiceConfigModel.getLoginUri());
    }

    @Override
    public String parseBindingStationUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_BINDING_STATION_ERROR);
        }
        return fromHttpUrl(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api/v1/station/bind");
    }

    @Override
    public String parseUnbindingStationUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_UNBINDING_STATION_ERROR);
        }
        return fromHttpUrl(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api/v1/station/unbind");
    }


    @Override
    public String parseBindingPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_BINDING_PRICE_TAG_ERROR);
        }
        return fromHttpUrl(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api/v1/tag/bind");
    }

    @Override
    public String parseUnbindingPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_UNBINDING_PRICE_TAG_ERROR);
        }
        return fromHttpUrl(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api/v1/tag/unbind");
    }

    @Override
    public String parseRefreshPriceTagUrl(EslServiceConfigModel eslServiceConfigModel) throws Exception {
        if (Objects.isNull(eslServiceConfigModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.PARSE_REFRESH_PRICE_TAG_ERROR);
        }
        return fromHttpUrl(eslServiceConfigModel.getHost(), eslServiceConfigModel.getPort(), "/api/v1/tag/refresh");
    }


    private String fromHttpUrl(String host, Integer port, String path) {
        return UriComponentsBuilder.fromHttpUrl(host + ":" + port + "/" + path).toUriString();
    }
}
