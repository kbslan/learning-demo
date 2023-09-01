package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 电子价签信息
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("price_tag_info")
public class PriceTagInfoEntity implements Serializable {

    private static final long serialVersionUID = 6655163075253198155L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long vendorId;

    /**
     * 门店ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long storeId;

    /**
     * 设备厂商 {@see PriceTagDeviceSupplierEnum}
     */
    private String deviceSupplier;

    /**
     * 设备类型 {@see PriceTagDeviceTypeEnum}
     */
    private String deviceType;

    /**
     * 原始价签ID
     */
    private String originPriceTagId;

    /**
     * 价签ID
     */
    private String priceTagId;

    /**
     * 绑定来源 {@see PriceTagBingingSourceEnum}
     */
    private String bingingSource;

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
     * 1：绑定，0：解绑 {@see YNEum}
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
