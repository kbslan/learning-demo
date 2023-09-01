package com.kbslan.domain.enums;

import com.kbslan.domain.model.EslServiceConfigModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * 配置编码KEY
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2022/7/8 17:17
 */
@Getter
@AllArgsConstructor
public enum ConfigKeyEnum {
    ESL_SERVER_CONFIG("esl_server_config", "电子价签厂商ESL服务配置", EslServiceConfigModel.class),
    ESL_SUPPORT_SUPPLIER_ID_LIST("esl_support_supplier_list", "支持的电子价签厂商列表", List.class),
    ESL_CHECK_BINDING_SOURCE("esl_check_binding_source", "电子价签绑定校验来源", Boolean.class),
    ;

    private final String code;
    private final String desc;
    private final Class<?> clazz;


    private static final Map<String, ConfigKeyEnum> cache;
    private static final List<Map<String, String>> configList;

    static {
        cache = new HashMap<>(ConfigKeyEnum.values().length);
        configList = new ArrayList<>(ConfigKeyEnum.values().length);
        for (ConfigKeyEnum item : ConfigKeyEnum.values()) {
            cache.put(item.getCode(), item);
            Map<String, String> map = new HashMap<>(2);
            map.put("code", item.getCode());
            map.put("desc", item.getDesc());
            configList.add(map);
        }
    }

    public static ConfigKeyEnum get(String code) {
        return Objects.isNull(code) ? null : cache.get(code);
    }

    public static Map<String, ConfigKeyEnum> getCache() {
        return cache;
    }

    public static List<Map<String, String>> getConfigList() {
        return configList;
    }
}
