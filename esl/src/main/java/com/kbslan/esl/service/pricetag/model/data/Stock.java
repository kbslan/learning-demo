package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 库存
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/16 15:07
 */
@Setter
@Getter
public class Stock implements Serializable {

    private static final long serialVersionUID = -7256440993165875394L;
    /**
     * 总库存
     */
    private Integer totalStock;
    /**
     * 后场库存
     */
    private Integer backStoreStock;
    /**
     * 卖场库存
     */
    private Integer mallStoreStock;
    /**
     * 库存状态
     */
    private StockStatus stockStatus;
    /**
     * 实际库存状态
     */
    private RealStockStatus realStockStatus;

}
