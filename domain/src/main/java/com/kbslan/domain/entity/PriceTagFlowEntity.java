package com.kbslan.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 价签流水实体类
 *
 * @author gang.qin
 * @date 2022/5/9
 */
@Data
@TableName("price_tag_flow")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceTagFlowEntity implements Serializable {

    private static final long serialVersionUID = 7389564402580071900L;

    /**
     * 主键id
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
     * 时间线时间戳
     */
    private Long timelineUtc;

    /**
     * 生效时间
     */
    private LocalDateTime effective;

    /**
     * 生效时间时间戳
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
     * 促销主类型
     */
    private Integer proMainType;

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
    private Integer version;

    /**
     * 是否已打印，0：未打印 1：已打印
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
     * 多级课组
     */
    private String multiCategory;

    /**
     * 是否为当前门店商品，0：不是 1：是
     * {@link com.dmall.pricetag.enums.RangeIndEnum}
     */
    private Integer rangeInd;

    /**
     * 小分类
     */
    private String mcCode;

    /**
     * 是否可售，1：可售 2：不可售
     * {@link com.dmall.pricetag.enums.SaleFlagEnum}
     */
    private Integer saleFlag;

    /**
     * 推送电子价签系统的状态，0：未推送、1、已推送
     */
    private Integer pushEsl;

    /**
     * 流水状态，0：变价可用 1：丢弃 2：强制结束 3：过期
     */
    private Integer status;

    /**
     * 关联的丢弃流水id列表
     */
    private String abandonIdList;

    /**
     * 相对上一条，价格变更类型 1：升价，2：降价，3：平价
     * {@link com.dmall.pricetag.enums.PriceChangeTypeEnum}
     */
    private Integer priceChangeType;

    /**
     * 品牌id
     */
    private Integer brandId;

    /**
     * 药品标识
     */
    private Integer rxTag;

    /**
     * 商品类型
     * {@link com.dmall.pricetag.enums.WareTypesEnum}
     */
    private Integer wareType;

    /**
     * 每日低价标签
     * {@link com.dmall.pricetag.enums.LowPriceFlagEnum}
     */
    private Integer lowPriceFlag;

    /**
     * 专有品牌标识
     * {@link com.dmall.pricetag.enums.ExclusiveBrandFlagEnum}
     */
    private Integer exclusiveBrandFlag;

    /**
     * 商品创建时间
     */
    private LocalDateTime skuCreateTime;

}
