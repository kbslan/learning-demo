package com.kbslan.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 电子价签下发状态
 *
 * @author chao.lan
 * @version 1.0
 * @since 0.0.1-SNAPSHOT
 */
@Getter
public enum PushStatusEnum {
    INIT(0, "待下发"),
    PUSHING(1, "下发中"),
    SUCCESS(2, "下发成功"),
    FAILED(3, "下发失败"),
    REISSUE(4, "补发成功"),
    ;

    private final Integer code;
    private final String desc;

    PushStatusEnum(final Integer code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, PushStatusEnum> cache;

    static {
        cache = Arrays.stream(values()).collect(Collectors.toMap(PushStatusEnum::getCode, Function.identity()));
    }

    @JsonCreator
    public static PushStatusEnum get(Integer code) {
        return Objects.isNull(code) ? null : cache.get(code);
    }

}
