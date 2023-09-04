package com.kbslan.esl.vo;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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
     * 电子价签ID
     */
    private String priceTagId;

    /**
     * 唯一请求链路ID
     */
    private String sid;

    /**
     * 模版名称
     */
    private String name;

    /**
     * 数据对象
     */
    private HashMap<String, Object> data;
}
