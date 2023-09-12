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
 * <p>
 * 电子价签绑定来源
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 20:48
 */
@Getter
@AllArgsConstructor
public enum PriceTagBingingSourceEnum {
    PRICE_TAG("price_tag", "价签系统"),
    DISPLAY("display", "陈列系统"),
    ;

    private final String code;
    private final String desc;

    private static final Map<String, PriceTagBingingSourceEnum> cache;

    static {
        cache = Arrays.stream(values()).collect(Collectors.toMap(PriceTagBingingSourceEnum::getCode, Function.identity()));
    }

    @JsonCreator
    public static PriceTagBingingSourceEnum get(String code) {
        return Objects.isNull(code) ? null : cache.get(code);
    }

}
