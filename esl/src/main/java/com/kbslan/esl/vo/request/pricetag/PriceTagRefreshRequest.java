package com.kbslan.esl.vo.request.pricetag;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     电子价签刷新请求参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 17:52
 */
@Getter
@Setter
@ToString
public class PriceTagRefreshRequest implements Serializable {
    private static final long serialVersionUID = -757503321375615812L;
    /**
     * 唯一请求链路ID
     */
    private String trace;
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
     * 模版名称
     */
    private String name;

    /**
     * 刷新商品sku列表
     */
    private List<Long> skuIds;
}
