package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.kbslan.domain.model.PriceTagInfoExtJson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
@TableName(value = "price_tag_info", autoResultMap = true)
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
     * 设备厂商
     *
     */
    private String deviceSupplier;

    /**
     * 设备类型
     *
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
     * 绑定来源
     *
     */
    private String bindingSource;

    /**
     * 价签电量
     */
    private String battery;

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
     * 1：绑定，0：解绑
     *
     */
    private Integer yn;

    /**
     * 商品信息
     */
    @TableField(value = "ext_json", typeHandler = FastjsonTypeHandler.class)
    private PriceTagInfoExtJson extJson;

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

    /**
     * 程序刷新数据时间
     */
    private LocalDateTime scheduleTime;
    /**
     * 是否在线
     */
    @TableField(exist = false)
    private Boolean online;

    /**
     * 获取推送的商品skuId列表
     *
     * @return skuId列表
     */
    public List<Long> getSkuIds() {
        if (Objects.isNull(this.extJson)) {
            return new ArrayList<>(0);
        }
        return this.extJson.getSkuIds();
    }

    public void setSkuIds(List<Long> skuIds) {
        if (Objects.isNull(this.extJson)) {
            this.extJson = new PriceTagInfoExtJson();
        }
        this.extJson.setSkuIds(skuIds);
    }


}
