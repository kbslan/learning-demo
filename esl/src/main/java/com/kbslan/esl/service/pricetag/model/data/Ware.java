package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 商品信息
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/16 15:07
 */
@Setter
@Getter
public class Ware implements Serializable {
    private static final long serialVersionUID = -7557627775032029546L;
    //sap促销类型：1-直降促销，2-出清促销
    public static final Set<Integer> skuPromotionTypes = new HashSet<>(Arrays.asList(1, 2));

    /**
     * skuId
     */
    private Long sku;
    /**
     * 物料码
     */
    private String matnr;
    /**
     * 国条
     */
    private String itemNum;
    /**
     * 主国条
     */
    private Integer mainItemNum;
    /**
     * 标题
     */
    private String title;
    /**
     * sap商品名称
     */
    private String sapTitle;
    /**
     * 是否是称重商品
     */
    private Integer weight;
    /**
     * 基本单位描述
     */
    private String chine = "见包装";
    /**
     * 基本单位
     */
    private String unit;
    /**
     * 等级（合格等）
     */
    private String grade;
    /**
     * 产地
     */
    private String produceArea;
    /**
     * mc
     */
    private String mcCode;
    /**
     * 通过条码解析到的plu码
     */
    private String pluCode;
    /**
     * 箱装系数
     */
    private Integer ratio = 1;
    /**
     * 箱装标记
     */
    private Boolean boxed;
    /**
     * 规格数量
     */
    private String specQty;
    /**
     * 规格单位
     */
    private String specUnit;
    /**
     * 规格单位，麦德龙规格单位承接意义变更，为保证模板字段取值不变，将基本单位描述覆盖原有specUnit，用specUnit2承接原始值
     */
    private String specUnit2;
    /**
     * 商品类别
     * 00 普通商品
     * 12 展示商品
     * 01 共性商品母码
     * 02 共性商品子码
     */
    private Integer specType;
    /**
     * 线上建议价格(含税价)
     */
    private Long retailPrice;
    /**
     * 线下售价
     */
    private Long offlineOriginPrice;
    /**
     * 线上售价
     */
    private Long onlineOriginPrice;
    /**
     * 线下执行价
     */
    private Long offlineExecutePrice;
    /**
     * 变更单号
     */
    private String changeId;
    /**
     * 促销ID
     */
    private String promoId;
    /**
     * 促销类型 1-直降促销，2-出清促销
     */
    private Integer promoType;
    /**
     * 促销价
     */
    private Long promoPrice;
    /**
     * 档期价，邮报价
     */
    private Long postPromoPrice;
    /**
     * 促销标签
     */
    private String promoTag;
    /**
     * 促销开始时间
     */
    private LocalDateTime promoStartDate;
    /**
     * 促销结束时间
     */
    private LocalDateTime promoEndDate;
    /**
     * 是否是联营商品
     */
    private Boolean tagUnionSell;
    /**
     * sap商品状态为1、2、3时，电商显示有货；其它状态时显示为无货
     */
    private String mmsta;
    /**
     * sap禁售标识
     * 0：正常
     * 1：禁止销售
     */
    private String mstae;
    /**
     * 虚拟箱装
     */
    private Boolean auxiliaryWare;
    /**
     * 供应商ID
     */
    private Long supplier;

    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 新品标记
     */
    private boolean newWare;
    /**
     * 商品英文名称
     */
    private String enTitle;
    /**
     * 多包装商品包装入数
     */
    private BigDecimal packageNum;
    /**
     * 包装序号
     */
    private Integer packageSeq;
    /**
     * 子码商品线下别名
     */
    private String subOfflineAlias;
    /**
     * 净重
     */
    private BigDecimal ntgew;
    /**
     * 归属物料码
     */
    private String genRfId;
    /**
     * ABC代码
     */
    private String abcClassify;
    /**
     * 税目名称
     */
    private String mwskzName;
    /**
     * 税目值(麦德龙：增值税率)
     */
    private String mwskz;
    /**
     * 含税小包装单价
     * SapVO.retailPrice/SkuTinyVO.packageNum
     */
    private Long unitPriceWithVat;
    /**
     * 不含税包装价格
     */
    private Long priceWithoutVat;
    /**
     * 不含税小包装单价
     * 含税价/（1+增值税率）/ SkuTinyVO.packageNum
     */
    private Long unitPriceWithoutVat;
    /**
     * 邮报周期
     */
    private String proPeriod;
    /**
     * 采购代码PCD
     */
    private String purchasePcd;
    /**
     * 是否磅秤可变价
     */
    private Boolean canChgScalesPrice;
    /**
     * 扩展属性
     */
    private Map<String, Object> extraData = new HashMap<>();

    /**
     * 商品打标
     */
    private Integer wareTag;

    private boolean sapPromotion = false;
    private boolean postPromotion = false;

    /**
     * 商品卖点信息（原始数据）
     */
    private String sellingPoint;
    /**
     * 商品卖点信息
     * （将中文分号统一处理成英文分号，按照英文分号进行分割处理后的数据）
     */
    private List<String> sellingPoints;

    /**
     * 堆头信息
     */
    private String heapInfo;
    /**
     * 短名称
     */
    private String shortName;
    /**
     * 存储稳层，保温层
     */
    private Integer storageType;
    /**
     * 保质期值
     */
    private Float wareLife;
    /**
     * 保质期单位 DAY(1,"天"), HOUR(2,"小时")
     */
    private Integer wareLifeUnit;
    /**
     * 备注
     */
    private String remark;
    /**
     * 商品评估类编码
     */
    private String estimateType;
    /**
     * 品牌ID
     */
    private Integer brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 品牌标识
     */
    private String privateBrandTag;
    /**
     * 上市时间
     */
    private Date marketTime;
    /**
     * 门店特配税率
     */
    private BigDecimal shopMwskz;
    /**
     * 商家自有商品类型
     */
    private String privateWareType;

    /**
     * 商品打标信息，标识系统
     */
    private List<WareTagInfo> tagInfos;

    public void setRemark(String remark) {
        this.remark = remark;
        this.extraData.put("remark", remark);
    }

    public void setEstimateType(String estimateType) {
        this.estimateType = estimateType;
        this.extraData.put("estimateType", estimateType);
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
        this.extraData.put("brandId", brandId);
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
        this.extraData.put("brandName", brandName);
    }

    public void setPrivateBrandTag(String privateBrandTag) {
        this.privateBrandTag = privateBrandTag;
        this.extraData.put("privateBrandTag", privateBrandTag);
    }

    public void setMarketTime(Date marketTime) {
        this.marketTime = marketTime;
        this.extraData.put("marketTime", marketTime);
    }

    public void setShopMwskz(BigDecimal shopMwskz) {
        this.shopMwskz = shopMwskz;
        this.extraData.put("shopMwskz", shopMwskz);
    }

    /**
     * 返回商品是否有促销
     * 1. 促销类型不为空且被包含
     * 2. 线下原价和线下售价不为空且线下售价< 线下原价
     */
    public boolean isSapPromotion() {
        return Objects.nonNull(promoType) && skuPromotionTypes.contains(promoType)
                && (Objects.nonNull(offlineOriginPrice) && Objects.nonNull(promoPrice) && promoPrice < offlineOriginPrice);
    }

    /**
     * 是否是邮报促销
     * 邮报价不为空且不等于0
     */
    public boolean isPostPromotion() {
        return Objects.nonNull(postPromoPrice) && postPromoPrice != 0L;
    }

    /**
     * 处理后的卖点信息
     */
    public List<String> getSellingPoints() {
        if (StringUtils.hasText(sellingPoint)) {
            List<String> result = new ArrayList<>();
            for (final String s : sellingPoint.split("；")) {
                result.add(s.trim());
            }
            return result;
        }
        return sellingPoints;
    }


}
