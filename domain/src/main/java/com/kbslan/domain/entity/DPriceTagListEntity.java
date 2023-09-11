package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 价签记录列表表 
 * </p>
 *
 * @author zhuo.zhang
 * @since 2021-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("d_price_tag_list")
@NoArgsConstructor
public class DPriceTagListEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家id
     */
    private Long vendorId;

    /**
     * 商家code
     */
    private String vendorCode;

    /**
     * 商家名称
     */
    private String vendorName;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 门店code
     */
    private String storeCode;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 商品sku
     */
    private Long skuId;

    /**
     * 时间线
     */
    private LocalDateTime timeline;

    /**
     * 时间线时间错
     */
    private Long timelineUtc;

    /**
     * 生效时间
     */
    private LocalDateTime effective;

    /**
     * 生效时间時間錯
     */
    private Long effectiveUtc;

    /**
     * 1=开档、2=回档、3=调价、4=信息变更 5=资料变更
     */
    private String orderType;

    /**
     * 促销单号或者调价单号
     */
    private Long orderId;

    /**
     * 促銷主類型
     */
    private Integer proMantype;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 1:有效 0:无效
     */
    private Integer yn;

    /**
     * 版本号
     */
    @TableField("VERSION")
    private Integer version;

    /**
     * 是否打rta_price_tag印
     */
    private Integer printed;

    /**
     * 一级分类
     */
    private String category1;

    /**
     * 二级分类
     */
    private String category2;

    /**
     * 三级分类
     */
    private String category3;

    /**
     * 四级分类
     */
    private String category4;

    /**
     * 五级分类
     */
    private String category5;

    /**
     * 多级分类（一级-二级-四级）
     */
    private String multiCategory;

    /**
     * DF专属字段，0-Not in range,1-in range
     */
    private Integer rangeInd;

    /**
     * 小分类
     */
    private String mcCode;

    /**
     * 是否可售
     */
    private Byte saleFlag;

    /**
     * 品牌id (流水表、店品表中才有的字段，此处只为接收该值)
     */
    private Integer brandId;

    /**
     * 药品标识 (流水表、店品表中才有的字段，此处只为接收该值)
     */
    private Integer rxTag;

    /**
     * 商品类型 (流水表、店品表中才有的字段，此处只为接收该值)
     */
    private Integer wareType;

    /**
     * 低价标识 (流水表、店品表中才有的字段，此处只为接收该值)
     */
    private Integer lowPriceFlag;

    /**
     * 专有品牌标识 (流水表、店品表中才有的字段，此处只为接收该值)
     */
    private Integer exclusiveBrandFlag;

    /**
     * 商品创建时间 (流水表、店品表中才有的字段，此处只为接收该值)
     */
    private LocalDateTime skuCreateTime;

    /**
     * 推送电子价签系统的状态，0：未推送、1、准备中、2：已推送
     */
    private Integer pushEsl;


    public DPriceTagListEntity(PriceTagFlowEntity flowEntity) {
        this.id = flowEntity.getId();
        this.vendorId = flowEntity.getVendorId();
        this.vendorCode = flowEntity.getVendorCode();
        this.vendorName = flowEntity.getVendorName();
        this.storeId = flowEntity.getStoreId();
        this.storeCode = flowEntity.getStoreCode();
        this.storeName = flowEntity.getStoreName();
        this.skuId = flowEntity.getSkuId();
        this.timeline = flowEntity.getTimeline();
        this.timelineUtc = flowEntity.getTimelineUtc();
        this.effective = flowEntity.getEffective();
        this.effectiveUtc = flowEntity.getEffectiveUtc();
        this.orderType = flowEntity.getOrderType();
        this.orderId = flowEntity.getOrderId();
        this.proMantype = flowEntity.getProMainType();
        this.createTime = flowEntity.getCreateTime();
        this.updateTime = flowEntity.getUpdateTime();
        this.yn = flowEntity.getYn();
        this.version = flowEntity.getVersion();
        this.printed = flowEntity.getPrinted();
        this.category1 = flowEntity.getCategory1();
        this.category2 = flowEntity.getCategory2();
        this.category3 = flowEntity.getCategory3();
        this.category4 = flowEntity.getCategory4();
        this.category5 = flowEntity.getCategory5();
        this.multiCategory = flowEntity.getMultiCategory();
        this.rangeInd = flowEntity.getRangeInd();
        this.mcCode = flowEntity.getMcCode();
        this.saleFlag = flowEntity.getSaleFlag().byteValue();
        this.brandId = flowEntity.getBrandId();
        this.rxTag = flowEntity.getRxTag();
    }
}
