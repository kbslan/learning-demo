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
 * 电子价签推送类型
 *
 * @author chao.lan
 * @version 1.0
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum PushTypeEnum {

    INCREMENT(1, "增量"),
    ALL(2, "全量")
    ;

    private final Integer code;
    private final String desc;

    private static final Map<Integer, PushTypeEnum> cache;

    static {
        cache = Arrays.stream(values()).collect(Collectors.toMap(PushTypeEnum::getCode, Function.identity()));
    }

    @JsonCreator
    public static PushTypeEnum get(Integer code) {
        return Objects.isNull(code) ? null : cache.get(code);
    }
}
