package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 门店信息
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/16 15:07
 */
@Setter
@Getter
public class Store implements Serializable {
    private static final long serialVersionUID = -9197076467163426450L;
    /**
     * ID
     */
    private Long id;
    /**
     * SAP_ID
     */
    private String code;
    /**
     * 门店名称
     */
    private String name;
    /**
     * 新门店标记
     */
    private Boolean newStore;
    /**
     * 门店类型  1大卖场;2连锁店;3尚佳会员店;4便利店;5物美参;6虚拟门店;7标超;8卫星站
     */
    private Integer storeType;
    /**
     * 监管信息
     */
    private Regulator regulator;
    /**
     * 麦德龙会员店标记
     */
    private boolean memberStore;
    /**
     * 门店配置信息
     */
    private Map<String, String> configs;

}
