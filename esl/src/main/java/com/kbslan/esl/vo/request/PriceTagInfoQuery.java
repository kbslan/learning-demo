package com.kbslan.esl.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *     电子价签查询
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 17:10
 */
@Getter
@Setter
@ToString
public class PriceTagInfoQuery extends PageRequest {
    /**
     * 商家ID
     */
    private Long vendorId;

    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 设备厂商
     */
    private String deviceSupplier;

    /**
     * 操作来源
     */
    private String bingingSource;

    /**
     * 原始价签ID
     */
    private String originPriceTagId;

    /**
     * 电子价签ID
     */
    private String priceTagId;

    /**
     * 状态
     */
    private Integer yn;
}
