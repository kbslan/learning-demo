package com.kbslan.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 状态枚举
 *
 * @author chao.lan
 */
@Getter
@AllArgsConstructor
public enum YNEnum {
    //有效
    YES(1, "有效"),
    NO(0, "无效"),
    DELETE(-1, "逻辑删除");

    private final Integer code;
    private final String desc;

    /**
     * 枚举缓存
     */
    private static final Map<Integer, YNEnum> CACHE;

    static {
        CACHE = Arrays.stream(values()).collect(Collectors.toMap(YNEnum::getCode, Function.identity()));
    }

    @JsonCreator
    public static YNEnum get(Integer code) {
        return Objects.isNull(code) ? null : CACHE.get(code);
    }

    @JsonValue
    public Integer getCode() {
        return this.code;
    }

}
