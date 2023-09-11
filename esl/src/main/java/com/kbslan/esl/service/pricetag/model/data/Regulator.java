package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 监管信息
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/16 15:07
 */
@Setter
@Getter
public class Regulator implements Serializable {

    private static final long serialVersionUID = -5825575757997644119L;
    /**
     * 门店计价员
     */
    private String priceKeeper;
    /**
     * 监督电话
     */
    private String monitorTelephone;
    /**
     * 举报电话
     */
    private String reportPhone;

    /**
     * 线下开店时间, 只需精确到年月日，为了避免日期处理的缺陷，直接用字符串来存储年月日，形如 2018-01-01
     */
    private String offlineOpenStoreDate;
    /**
     * 是否是24h店， 1是，0不是
     */
    private Integer twentyFourHoursStore;
    /**
     * 营业开始时间，只关注时分秒，忽略年月日的值，为了避免日期处理的缺陷，直接用字符串存储时分秒，形如 21:01:01
     */
    private String openingTime;
    /**
     * 营业结束时间，只关注时分秒，忽略年月日的值，为了避免日期处理的缺陷，直接用字符串存储时分秒，形如 21:01:01
     */
    private String closingTime;
    /**
     * -1：已冻结，1-正常
     */
    private Integer status;

}
