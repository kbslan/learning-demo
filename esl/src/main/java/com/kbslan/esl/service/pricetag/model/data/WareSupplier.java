package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商品供应商
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2021/1/29 18:21
 */
@Getter
@Setter
public class WareSupplier implements Serializable {
    private static final long serialVersionUID = -437259308947008074L;
    /**
     * 供应商编码
     */
    private String code;
    /**
     * 供应商名称
     */
    private String name;
}
