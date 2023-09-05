package com.kbslan.esl.service.pricetag.model;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 *     电子价签刷新参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 14:43
 */
@Getter
@Setter
public class PriceTagRefreshParams {
    /**
     * 设备厂商
     */
    private PriceTagDeviceSupplierEnum deviceSupplier;
    /**
     * 商家ID
     */
    private Long vendorId;

    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 原始价签ID
     */
    private String originPriceTagId;

    /**
     * 电子价签ID
     */
    private String priceTagId;

    /**
     * 唯一请求链路ID
     */
    private String sid;

    /**
     * 刷新商品sku列表
     */
    private List<Long> skuIds;
}
