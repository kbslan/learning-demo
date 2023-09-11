package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 品牌
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/16 15:07
 */
@Setter
@Getter
public class Brand implements Serializable {
    private static final long serialVersionUID = -6063589294178021556L;
    /**
     * ID
     */
    private Long id;
    /**
     * 品牌名称
     */
    private String name;
    /**
     * logo
     */
    private String logo;
}
