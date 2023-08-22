package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 电子价签信息
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PriceTagInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家ID
     */
    private Long vendorId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 价签厂商ID 1：汉朔
     */
    private Integer deviceSupplierId;

    /**
     * 原始价签ID
     */
    private String originPriceTagId;

    /**
     * 价签ID
     */
    private String priceTagId;

    /**
     * 价签能量值
     */
    private String rfPower;

    /**
     * 屏幕尺寸
     */
    private String screenSize;

    /**
     * 屏幕横坐标像素大小
     */
    private String resolutionX;

    /**
     * 屏幕纵坐标像素大小
     */
    private String resolutionY;

    /**
     * 价签类型ID
     */
    private String firmwareId;

    /**
     * 末次心跳时间
     */
    private LocalDateTime lastHeartbeat;

    /**
     * 绑定状态 1：绑定，0：解绑
     */
    private Integer yn;

    /**
     * 商品信息
     */
    private String extJson;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * 更新人ID
     */
    private Long modifierId;

    /**
     * 更新人名称
     */
    private String modifierName;

    /**
     * 更新时间
     */
    private LocalDateTime modified;


}
