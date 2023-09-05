package com.kbslan.esl.vo.request.pricetag;

import com.kbslan.domain.enums.PriceTagBingingSourceEnum;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *     基站页面请求参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 15:08
 */
@Getter
@Setter
@ToString
public class StationRequest implements Serializable {
    private static final long serialVersionUID = -7243539319914871735L;
    /**
     * 跟踪ID
     */
    private String trace;
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
     * 原始基站mac
     */
    private String originAp;
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
    private PriceTagBingingSourceEnum bingingSource;
}
