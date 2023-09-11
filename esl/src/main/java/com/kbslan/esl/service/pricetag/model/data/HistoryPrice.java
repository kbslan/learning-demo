package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 历史售价
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/30 15:29
 */
@Getter
@Setter
public class HistoryPrice implements Serializable {
    private static final long serialVersionUID = -8543118533699014988L;
    /**
     * 是否开启历史售价查询
     */
    private boolean limitPrice;

    /**
     * 指定区间内商品最低销售价，单位：分
     */
    private Long lowestSalePrice;

    /**
     * 商品末次销售流水中订单商品最低销售价，单位：分
     * 注意：如果存在指定区间最低价，则此值为null
     */
    private Long lastSalePrice;

    /**
     * 历史最低售价订单号
     */
    private Long hisOrderId;
}
