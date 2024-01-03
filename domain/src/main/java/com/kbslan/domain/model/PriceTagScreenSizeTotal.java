package com.kbslan.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/12/28 17:54
 */
@Getter
@Setter
@ToString
public class PriceTagScreenSizeTotal implements Serializable {
    private static final long serialVersionUID = 1072312515217594065L;
    /**
     * 总数量
     */
    private Long total = 0L;

    /**
     * 离线数量
     */
    private Long offline = 0L;
}
