package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 门店基站
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ap_store")
public class ApStoreEntity implements Serializable {


    private static final long serialVersionUID = 7113849092261033839L;

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
     * 原始基站id
     */
    private String originApId;

    /**
     * 基站mac
     */
    private String apMac;

    /**
     * 绑定来源 {@see PriceTagBingingSourceEnum}
     */
    private String bingingSource;

    /**
     * 末次心跳时间
     */
    private LocalDateTime lastHeartbeat;

    /**
     * 1：绑定，0：解绑 {@see YNEum}
     */
    private Integer yn;

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
