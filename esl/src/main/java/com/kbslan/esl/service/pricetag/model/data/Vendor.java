package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商家信息
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/16 15:07
 */
@Setter
@Getter
public class Vendor implements Serializable {
    private static final long serialVersionUID = 5978399372372148959L;
    /**
     * ID
     */
    private Long id;
    /**
     * 商家SAP_ID
     */
    private String code;
    /**
     * 商家名称
     */
    private String name;
    /**
     * 商家类型
     * 1-大型商超 2-中小型商超 3-虚拟商家
     */
    private Integer vendorType;
}
