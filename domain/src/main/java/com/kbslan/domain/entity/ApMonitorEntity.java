package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.kbslan.domain.model.ApMonitorExtJson;
import com.kbslan.domain.model.PriceTagScreenSizeTotal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 电子价签集中监控信息
 * </p>
 *
 * @author chao.lan
 * @since 2023-12-26 22:27:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "ap_monitor", autoResultMap = true)
public class ApMonitorEntity implements Serializable {

    private static final long serialVersionUID = -3025999818595555697L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家ID
     */
    @TableField("vendor_id")
    private Long vendorId;

    /**
     * 门店ID
     */
    @TableField("store_id")
    private Long storeId;

    /**
     * 设备厂商
     */
    @TableField("device_supplier")
    private String deviceSupplier;

    /**
     * 厂商ESL状态
     */
    @TableField("esl_server_status")
    private Integer eslServerStatus;

    /**
     * 门店绑定基站数量
     */
    @TableField("ap_total")
    private Long apTotal;

    /**
     * 离线基站数量
     */
    @TableField("offline_ap_total")
    private Long offlineApTotal;

    /**
     * 门店绑定价签数量
     */
    @TableField("price_tag_total")
    private Long priceTagTotal;

    /**
     * 离线价签数量
     */
    @TableField("offline_price_tag_total")
    private Long offlinePriceTagTotal;

    /**
     * 推送中的价签数量
     */
    @TableField("pushing_price_total")
    private Long pushingPriceTotal;

    /**
     * 推送失败价签数量
     */
    @TableField("push_failed_total")
    private Long pushFailedTotal;

    /**
     * 扩展字段
     */
    @TableField(value = "ext_json", typeHandler = FastjsonTypeHandler.class)
    private ApMonitorExtJson extJson;

    /**
     * 创建人ID
     */
    @TableField("creator_id")
    private Long creatorId;

    /**
     * 创建人名称
     */
    @TableField("creator_name")
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField("created")
    private LocalDateTime created;

    /**
     * 创建时间UTC
     */
    @TableField("created_utc")
    private Long createdUtc;

    /**
     * 更新人ID
     */
    @TableField("modifier_id")
    private Long modifierId;

    /**
     * 更新人名称
     */
    @TableField("modifier_name")
    private String modifierName;

    /**
     * 更新时间
     */
    @TableField("modified")
    private LocalDateTime modified;

    /**
     * 更新时间UTC
     */
    @TableField("modified_utc")
    private Long modifiedUtc;

    public Map<String, PriceTagScreenSizeTotal> getPriceTagCountInfo() {
        if (Objects.isNull(this.extJson)) {
            return new HashMap<>(0);
        }
        return this.extJson.getPriceTagCountInfo();
    }

    public void setPriceTagCountInfo(Map<String, PriceTagScreenSizeTotal> priceTagCountInfo) {
        if (Objects.isNull(this.extJson)) {
            this.extJson = new ApMonitorExtJson();
        }
        this.extJson.setPriceTagCountInfo(priceTagCountInfo);
    }


}
