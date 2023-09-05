package com.kbslan.esl.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.SysConfigEntity;
import com.kbslan.domain.enums.ConfigKeyEnum;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.enums.YNEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.model.EslServiceConfigModel;
import com.kbslan.domain.service.SysConfigService;
import com.kbslan.esl.config.RedisUtils;
import com.kbslan.esl.service.EslConfigService;
import com.kbslan.esl.service.http.HttpComponent;
import com.kbslan.esl.service.pricetag.DeviceApiParser;
import com.kbslan.esl.service.pricetag.DeviceApiParserFactory;
import com.kbslan.esl.vo.response.notice.EslNoticeMessage;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class EslConfigServiceImpl implements EslConfigService {

    //缓存key token:deviceSupplier:loginUrl:userName
    private static final String TOKEN_KEY = "token:%s:%s:%s";
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private DeviceApiParserFactory deviceApiParserFactory;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private HttpComponent httpComponent;

    @Override
    public Map<PriceTagDeviceSupplierEnum, EslServiceConfigModel> query(Long storeId) throws Exception {
        SysConfigEntity sysConfig = sysConfigService.getOne(
                Wrappers.<SysConfigEntity>lambdaQuery()
                        .eq(SysConfigEntity::getConfigKey, ConfigKeyEnum.ESL_SERVER_CONFIG.getCode())
                        .eq(SysConfigEntity::getYn, YNEnum.YES.getCode())
        );
        if (Objects.isNull(sysConfig) || StringUtils.isBlank(sysConfig.getConfigValue())) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_CONFIG_NOT_FOUND);
        }
        try {
            return JSON.parseObject(sysConfig.getConfigValue(), new TypeReference<Map<PriceTagDeviceSupplierEnum, EslServiceConfigModel>>() {
            });
        } catch (Exception e) {
            //厂商ESL服务配置信息错误，解析失败
            throw new IllegalArgumentException(EslNoticeMessage.ESL_CONFIG_PARSE_ERROR);
        }
    }

    @Override
    public Map<PriceTagDeviceSupplierEnum, DeviceEslApiModel> parse(Map<PriceTagDeviceSupplierEnum, EslServiceConfigModel> eslServiceConfigModelMap) throws Exception {
        if (MapUtils.isEmpty(eslServiceConfigModelMap)) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_CONFIG_NOT_FOUND);
        }

        Map<PriceTagDeviceSupplierEnum, DeviceEslApiModel> parseResult = new HashMap<>(eslServiceConfigModelMap.size());
        for (Map.Entry<PriceTagDeviceSupplierEnum, EslServiceConfigModel> configModelEntry : eslServiceConfigModelMap.entrySet()) {
            PriceTagDeviceSupplierEnum deviceSupplierEnum = configModelEntry.getKey();
            if (Objects.isNull(deviceSupplierEnum)) {
                throw new IllegalArgumentException(String.format(EslNoticeMessage.ESL_DEVICE_SUPPLIER_ERROR, "null"));
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
                .loginUrl(parser.parseLoginUrl(eslServiceConfigModel))
                .healthUrl(parser.parseEslHealthUrl(eslServiceConfigModel))
                .bindingStationUrl(parser.parseBindingStationUrl(eslServiceConfigModel))
                .unbindingStationUrl(parser.parseUnbindingStationUrl(eslServiceConfigModel))
                .bindingPriceTagUrl(parser.parseBindingPriceTagUrl(eslServiceConfigModel))
                .unbindingPriceTagUrl(parser.parseUnbindingPriceTagUrl(eslServiceConfigModel))
                .refreshPriceTagUrl(parser.parseRefreshPriceTagUrl(eslServiceConfigModel))
                .callbackUrl(parser.parseCallbackUrl(eslServiceConfigModel))
                .extra(eslServiceConfigModel.getExtra())
                .build();
    }

    @Override
    public String getToken(DeviceEslApiModel deviceEslApiModel) throws Exception {
        if (Objects.isNull(deviceEslApiModel) || !deviceEslApiModel.isNeedLogin()) {
            return null;
        }
        //缓存KEY
        String key = String.format(TOKEN_KEY, deviceEslApiModel.getDeviceSupplier().getCode(), deviceEslApiModel.getLoginUrl(), deviceEslApiModel.getUserName());
        String token = redisUtils.string.get(key);
        //1. 从缓存中获取token，如果存在则直接返回
        if (StringUtils.isNotBlank(token)) {
            return token;
        }

        // 2. 调用厂商服务获取token
        Map<String, String> data = new HashMap<>();
        data.put("username", deviceEslApiModel.getUserName());
        data.put("password", deviceEslApiModel.getPassword());
        String result = httpComponent.post(deviceEslApiModel.getLoginUrl().toString(), JSON.toJSONString(data));
        // 3. 如果token不存在，则抛出异常
        if (StringUtils.isBlank(result)) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_LOGIN_ERROR);
        }
        // 4. 如果token存在，则缓存token并返回
        token = JSON.parseObject(result).getString("token");

        if (StringUtils.isNotBlank(token)) {
            redisUtils.string.set(key, token);
            redisUtils.key.expire(key, 60 * 60);
        }
        return token;
    }


    @Override
    public DeviceEslApiModel queryAndParseEslConfig(Long storeId, PriceTagDeviceSupplierEnum deviceSupplier) throws Exception {
        //查询ESL服务配置
        Map<PriceTagDeviceSupplierEnum, EslServiceConfigModel> configModelMap = this.query(Objects.requireNonNull(storeId));

        //解析ESL服务配置
        Map<PriceTagDeviceSupplierEnum, DeviceEslApiModel> deviceEslApiModelMap = this.parse(configModelMap);

        DeviceEslApiModel deviceEslApiModel = deviceEslApiModelMap.get(Objects.requireNonNull(deviceSupplier));

        if (Objects.isNull(deviceEslApiModel)) {
            throw new IllegalArgumentException(EslNoticeMessage.ESL_CONFIG_NOT_FOUND);
        }

        //获取token
        if (deviceEslApiModel.isNeedLogin()) {
            String token = this.getToken(deviceEslApiModel);
            deviceEslApiModel.setToken(token);
        }

        return deviceEslApiModel;
    }

    @Override
    public long getSid(String key) {
        long result;
        try {
            result = redisUtils.string.incrBy(key, 1L);
        } catch (Exception e) {
            log.error("redis自增异常 key={}", key, e);
            result = (long) Math.ceil(Math.random() * 10000);
        }
        return result;
    }

    @Override
    public boolean isNeedCheckBindingSource(Long storeId) {
        if (Objects.isNull(storeId)) {
            return true;
        }
        SysConfigEntity sysConfig = sysConfigService.getOne(
                Wrappers.<SysConfigEntity>lambdaQuery()
                        .eq(SysConfigEntity::getStoreId, storeId)
                        .eq(SysConfigEntity::getYn, YNEnum.YES.getCode())
                        .eq(SysConfigEntity::getConfigKey, ConfigKeyEnum.ESL_CHECK_BINDING_SOURCE.getCode())
        );

        return Objects.nonNull(sysConfig) && Boolean.parseBoolean(sysConfig.getConfigValue());
    }

}
