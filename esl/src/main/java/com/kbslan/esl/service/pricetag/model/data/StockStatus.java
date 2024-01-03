package com.kbslan.esl.service.pricetag.model.data;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * StockStatus.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum StockStatus {
    IN_STOCK(1, "有货"),
    OUT_STOCK(0, "无货");

    /**
     * code.
     */
    private final Integer code;

    /**
     * desc.
     */
    private final String desc;
}
