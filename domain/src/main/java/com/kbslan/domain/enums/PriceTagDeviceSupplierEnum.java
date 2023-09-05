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
 *     电子价签厂商
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 21:33
 */
@Getter
@AllArgsConstructor
public enum PriceTagDeviceSupplierEnum {
    HAN_SHOW("HanShow", "汉朔科技"),
    ;

    private final String code;
    private final String desc;

    private static final Map<String, PriceTagDeviceSupplierEnum> cache;

    static {
        cache = Arrays.stream(values()).collect(Collectors.toMap(PriceTagDeviceSupplierEnum::getCode, Function.identity()));
    }

    @JsonCreator
    public static PriceTagDeviceSupplierEnum get(String code) {
        return Objects.isNull(code) ? null : cache.get(code);
    }
}
