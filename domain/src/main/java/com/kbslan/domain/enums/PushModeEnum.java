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
 * 电子价签推送模式
 *
 * @author chao.lan
 * @version 1.0
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum PushModeEnum {
    FTP(1, "FTP"),
    API(2,"API")
    ;
    private final Integer code;
    private final String desc;

    private static final Map<Integer, PushModeEnum> cache;

    static {
        cache = Arrays.stream(values()).collect(Collectors.toMap(PushModeEnum::getCode, Function.identity()));
    }

    @JsonCreator
    public static PushModeEnum get(Integer code) {
        return Objects.isNull(code) ? null : cache.get(code);
    }
}
