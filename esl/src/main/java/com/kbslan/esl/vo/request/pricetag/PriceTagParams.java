package com.kbslan.esl.vo.request.pricetag;

import com.kbslan.domain.enums.PriceTagDeviceTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 *     电子价签参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 17:17
 */
@Getter
@Setter
@ToString
public class PriceTagParams extends CommonParams {
    /**
     * 原始价签ID
     */
    private String originPriceTagId;
    /**
     * 电子价签ID
     */
    private String priceTagId;
    /**
     * 商品SKU
     */
    private List<Long> skuIds;

    /**
     * 电子价签设备类型
     */
    private PriceTagDeviceTypeEnum deviceType = PriceTagDeviceTypeEnum.EPD;

}
