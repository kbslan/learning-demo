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
 * 电子价签推送记录明细
 * </p>
 *
 * @author chao.lan
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PriceTagPushRecordDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 批次号
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
     * sku
     */
    private Long skuId;

    /**
     * 物料码
     */
    private String matnr;

    /**
     * 国条码
     */
    private String barCode;

    /**
     * 商品名称（多语言）
     */
    private String title;

    /**
     * 品牌名称（多语言）
     */
    private String brand;

    /**
     * 原价
     */
    private Long retailPrice;

    /**
     * 促销价
     */
    private Long proPrice;

    /**
     * 促销标签
     */
    private String proTag;

    /**
     * 促销语
     */
    private String proSlogan;

    /**
     * 经营范围
     */
    private Integer rangInd;

    /**
     * 是否可售
     */
    private Integer salesFlag;

    /**
     * 商品状态
     */
    private Integer itemStatus;

    /**
     * 扩展属性
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
