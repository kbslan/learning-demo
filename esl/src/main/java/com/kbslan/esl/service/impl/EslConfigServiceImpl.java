package com.kbslan.esl.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.SysConfigEntity;
import com.kbslan.domain.enums.ConfigKeyEnum;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.model.EslServiceConfigModel;
import com.kbslan.domain.service.SysConfigService;
import com.kbslan.esl.service.DeviceApiParser;
import com.kbslan.esl.service.DeviceApiParserFactory;
import com.kbslan.esl.service.EslConfigService;
import com.kbslan.esl.service.notice.EslNoticeMessage;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * <p>
 * 电子价签配置服务
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 17:58
 */
@Service
public class EslConfigServiceImpl implements EslConfigService {
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private DeviceApiParserFactory deviceApiParserFactory;

    @Override
    public Map<String, EslServiceConfigModel> query(Long storeId) throws Exception {
        SysConfigEntity sysConfig = sysConfigService.getOne(
                Wrappers.<SysConfigEntity>lambdaQuery()
                        .eq(SysConfigEntity::getConfigKey, ConfigKeyEnum.ESL_SERVER_CONFIG.getCode())
        );
        if (Objects.isNull(sysConfig) || StringUtils.isBlank(sysConfig.getConfigValue())) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_CONFIG_NOT_FOUND);
        }
        return JSON.parseObject(sysConfig.getConfigValue(), new TypeReference<Map<String, EslServiceConfigModel>>() {
        });
    }


    @Override
    public Map<PriceTagDeviceSupplierEnum, DeviceEslApiModel> parse(Map<String, EslServiceConfigModel> eslServiceConfigModelMap) throws Exception {
        if (MapUtils.isEmpty(eslServiceConfigModelMap)) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_CONFIG_NOT_FOUND);
        }

        Map<PriceTagDeviceSupplierEnum, DeviceEslApiModel> parseResult = new HashMap<>(eslServiceConfigModelMap.size());
        for (Map.Entry<String, EslServiceConfigModel> configModelEntry : eslServiceConfigModelMap.entrySet()) {
            PriceTagDeviceSupplierEnum deviceSupplierEnum = PriceTagDeviceSupplierEnum.get(configModelEntry.getKey());
            if (Objects.isNull(deviceSupplierEnum)) {
                throw new IllegalArgumentException(EslNoticeMessage.ESL_DEVICE_SUPPLIER_ERROR);
            }
            parseResult.put(deviceSupplierEnum, this.parse(deviceSupplierEnum, configModelEntry.getValue()));
        }

        return parseResult;
    }

    private DeviceEslApiModel parse(PriceTagDeviceSupplierEnum deviceSupplierEnum, EslServiceConfigModel eslServiceConfigModel) throws Exception {
        DeviceApiParser parser = deviceApiParserFactory.create(deviceSupplierEnum);
        return DeviceEslApiModel.builder()
                .deviceSupplier(deviceSupplierEnum)
                .host(eslServiceConfigModel.getHost())
                .port(eslServiceConfigModel.getPort())
                .needLogin(eslServiceConfigModel.isNeedLogin())
                .userName(eslServiceConfigModel.getUserName())
                .password(eslServiceConfigModel.getPassword())
                .login(parser.parseLoginUrl(eslServiceConfigModel))
                .health(parser.parseEslHealthUrl(eslServiceConfigModel))
                .bindingStation(parser.parseBindingStationUrl(eslServiceConfigModel))
                .unbindingStation(parser.parseUnbindingStationUrl(eslServiceConfigModel))
                .bindingPriceTag(parser.parseBindingPriceTagUrl(eslServiceConfigModel))
                .unbindingPriceTag(parser.parseUnbindingPriceTagUrl(eslServiceConfigModel))
                .refreshPriceTag(parser.parseRefreshPriceTagUrl(eslServiceConfigModel))
                .extra(eslServiceConfigModel.getExtra())
                .build();
    }


}
