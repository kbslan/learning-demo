package com.kbslan.esl.vo.request.pricetag;

import com.kbslan.domain.enums.PriceTagDeviceTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     电子价签页面请求参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 15:37
 */
@Getter
@Setter
@ToString
public class PriceTagRequest implements Serializable {
    private static final long serialVersionUID = -3573970427967970207L;

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
    private String deviceSupplier;
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
    private String bingingSource;
    /**
     * 绑定、解绑操作成功后是否需要推送价签数据
     */
    private Boolean needPush;

    /**
     * 原始价签ID
     */
    private String originPriceTagId;
    /**
     * 商品SKU
     */
    private List<Long> skuIds;

    /**
     * 电子价签设备类型
     */
    private String deviceType = PriceTagDeviceTypeEnum.EPD.getCode();
}
