package com.kbslan.esl.vo.pricetag;

import com.kbslan.domain.enums.PriceTagBingingSourceEnum;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 公共参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 17:35
 */
@Getter
@Setter
@ToString
public class CommonParams {

    /**
     * 跟踪ID
     */
    private String sid;
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
    private PriceTagDeviceSupplierEnum deviceSupplier;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作来源
     */
    private PriceTagBingingSourceEnum bingingSource = PriceTagBingingSourceEnum.PRICE_TAG_APP;

}
