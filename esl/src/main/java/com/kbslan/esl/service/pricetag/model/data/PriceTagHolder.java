package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 价签数据对象
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/21 13:42
 */
@Getter
@Setter
public class PriceTagHolder extends HashMap<String, Object> {
    private static final long serialVersionUID = -7423667983713659303L;
    /**
     * traceId
     */
    protected String traceId;
    /**
     * 组装数据时间
     */
    protected LocalDateTime time;
    /**
     * MQ 生效时间
     */
    protected LocalDateTime effectiveTime;
    /**
     * 变更来源
     */
    private Integer changeSource;
    /**
     * 变更原因
     */
    private Integer changeType;
    /**
     * 商家信息
     */
    protected Vendor vendor;
    /**
     * 门店信息
     */
    protected Store store;
    /**
     * 商品信息
     */
    private Ware ware;
    /**
     * 课组
     */
    private Collection<Category> categories;
    /**
     * 促销信息
     */
    private List<Promotion> promotions;
    /**
     * 库存信息
     */
    private Stock stock;
    /**
     * 品牌信息
     */
    private Brand brand;
    /**
     * 陈列信息
     */
    private Display display;
    /**
     * 供应商
     */
    private WareSupplier supplier;
    /**
     * 历史售价
     */
    private HistoryPrice history;
    /**
     * 扩展属性
     */
    private Map<String, Object> extraData = new HashMap<>();

}
