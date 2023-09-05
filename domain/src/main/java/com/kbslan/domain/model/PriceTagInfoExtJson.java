package com.kbslan.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *     电子价签信息表扩展属性
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 21:26
 */
@Getter
@Setter
@ToString
public class PriceTagInfoExtJson implements Serializable {
    private static final long serialVersionUID = -1943416383488107442L;

    private List<Long> skuIds;
}
