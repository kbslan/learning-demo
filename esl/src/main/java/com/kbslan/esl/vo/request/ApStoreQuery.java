package com.kbslan.esl.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 16:24
 */
@Getter
@Setter
@ToString
public class ApStoreQuery extends PageRequest {

    /**
     * 商家ID
     */
    private Long vendorId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 原始基站地址
     */
    private String originAp;

    /**
     * 基站mac
     */
    private String apMac;

    /**
     * 状态
     */
    private Integer yn;

}
