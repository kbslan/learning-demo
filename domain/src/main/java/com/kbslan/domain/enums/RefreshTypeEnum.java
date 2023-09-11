package com.kbslan.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 电子价签触发类型
 *
 * @author chao.lan
 * @version 1.0
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum RefreshTypeEnum {
    AUTO(1, "自动触发"),
    MANUAL(2,"手动触发"),
    COMPENSATION(3, "补偿触发")
    ;
    private final Integer code;
    private final String desc;

    private static final Map<Integer, RefreshTypeEnum> cache;

    static {
        cache = Arrays.stream(values()).collect(Collectors.toMap(RefreshTypeEnum::getCode, Function.identity()));
    }

    @JsonCreator
    public static RefreshTypeEnum get(Integer code) {
        return Objects.isNull(code) ? null : cache.get(code);
    }
}
