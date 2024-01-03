package com.kbslan.domain.model;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 电子价签集中监控扩展属性
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/12/27 0:47
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApMonitorExtJson implements Serializable {
    private static final long serialVersionUID = -3410829559613229046L;

    /**
     * key: screenSize, value: PriceTagScreenSizeTotal
     */
    private Map<String, PriceTagScreenSizeTotal> priceTagCountInfo;

}
