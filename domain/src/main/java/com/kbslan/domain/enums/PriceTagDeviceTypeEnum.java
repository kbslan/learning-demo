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
 * 电子价签设备类型
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 21:00
 */
@Getter
@AllArgsConstructor
public enum PriceTagDeviceTypeEnum {
    EPD("EPD", "EPD墨水屏电子价签"),
    ;

    private final String code;
    private final String desc;

    private static final Map<String, PriceTagDeviceTypeEnum> cache;

    static {
        cache = Arrays.stream(values()).collect(Collectors.toMap(PriceTagDeviceTypeEnum::getCode, Function.identity()));
    }

    @JsonCreator
    public static PriceTagDeviceTypeEnum get(String code) {
        return Objects.isNull(code) ? null : cache.get(code);
    }
}
