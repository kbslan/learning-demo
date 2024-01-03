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
public enum RealStockStatus  {
    IN_STOCK(1, "有库存"),
    OUT_STOCK(0, "无库存"),
    NEGATIVE_STOCK(-1, "负库存"),
    ;

    /**
     * code.
     */
    private final Integer code;

    /**
     * desc.
     */
    private final String desc;
}
