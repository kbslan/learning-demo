package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 促销信息
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/16 15:07
 */
@Setter
@Getter
public class Promotion implements Serializable, Comparable<Promotion> {
    private static final long serialVersionUID = -2960979740107785756L;
    /**
     * 促銷ID
     */
    private String id;
    /**
     * 促销code
     */
    private String code;
    /**
     * 促销批次号
     */
    private String batchNum;
    /**
     * 档期号
     */
    private String schedule;
    /**
     * 名称
     */
    private String name;
    /**
     * 主类型
     *
     * @see PromotionTypeEnum
     */
    private Integer mainType;
    /**
     * 子类型
     *
     * @see PromotionSubTypeEnum
     */
    private Integer subType;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 适用用户级别
     */
    private Set<Long> applyUserLevels;
    /**
     * 标签
     */
    private String tag;
    /**
     * 促销标语
     */
    private String slogan;
    /**
     * 促销限购语
     */
    private String limitDesc;
    /**
     * 商家促销类型
     */
    private String vendorProType;
    /**
     * 价签是否显示 为空和1表示要显示，0表示不显示
     */
    private Integer eslShow;
    /**
     * 单个商品促销价
     */
    private Long unitPrice;
    /**
     * 单个商品促销优惠金额
     */
    private Long unitDiscount;
    /**
     * 是否爆品直降
     */
    private Integer hotCut;
    /**
     * 促销价
     */
    private Long promotionPrice;
    /**
     * 会员促销价
     */
    private Long memberPrice;
    /**
     * 非会员价
     */
    private Long nonMemberPrice;
    /**
     * 商品组促销价格
     */
    private Long groupPrice;
    /**
     * 商品组数量（箱装系数）
     */
    private Long groupQty;
    /**
     * 是否能计算单个商品的优惠，true:是，false:否
     */
    private Boolean canCalcDiscount;
    /**
     * 商品组促销价格优惠金额(在商品原价基础上计算)
     */
    private Long groupDiscountOfOriginPrice;
    /**
     * 商品组促销价格优惠金额(在商品单品促销价基础上计算)
     */
    private Long groupDiscountOfPromoPrice;
    /**
     * 单个商品优惠金额(在商品原价基础上计算)
     */
    private Long unitDiscountOfOriginPrice;
    /**
     * 单个商品优惠金额(在商品单品促销价基础上计算)
     */
    private Long unitDiscountOfPromoPrice;
    /**
     * 是否为单品促销
     */
    private boolean isSinglePromotion;
    /**
     * sap促销
     */
    private boolean sapPromotion;
    /**
     * 活动促销
     */
    private boolean activityPromotion;
    /**
     * 限会员
     */
    private boolean isLimitUser;
    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;
    /**
     * 箱装标记
     */
    private boolean boxed;
    /**
     * 促销来源
     * */
//    private PromotionCreateSourceEnum createSource;

    /**
     * 扩展属性
     */
    private Map<String, Object> extraData = new HashMap<>();

    public Long getPromotionPrice() {
//        if (Objects.equals(mainType, PromotionTypeEnum.SINGLE.getValue())) {
//            //单品促
//            this.promotionPrice = unitPrice;
//        } else if (Objects.equals(mainType, PromotionTypeEnum.SINGLE_GIFT.getValue())) {
//            //单品买赠
//            this.promotionPrice = groupPrice;
//        } else if (Objects.equals(mainType, PromotionTypeEnum.CONDITION.getValue())) {
//            //条件促
//            this.promotionPrice = groupPrice;
//        }
        return promotionPrice;
    }

    @Override
    public int compareTo(final Promotion o) {
        LocalDateTime time2 = o.getCreateTime();
        LocalDateTime time1 = this.createTime;
        if (Objects.isNull(time2)) {
            return -1;
        }
        if (Objects.isNull(time1)) {
            return 1;
        }

        Long mill2 = Timestamp.valueOf(time2).getTime();
        Long mill1 = Timestamp.valueOf(time1).getTime();
        long minus = mill2 - mill1;
        if (minus > 0) {
            return 1;
        } else if (minus == 0) {
            return 0;
        } else {
            return -1;
        }
    }
}
