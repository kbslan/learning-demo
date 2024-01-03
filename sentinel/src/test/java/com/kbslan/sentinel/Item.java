package com.kbslan.sentinel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/18 16:51
 */
@Getter
@Setter
@ToString
public class Item {
    /**
     * 期数
     */
    private String issue;
    /**
     * 开奖时间
     */
    private Date openTime;
    /**
     * 开奖号码
     */
    private String frontWinningNum;
    /**
     * 开奖号码
     */
    private String backWinningNum;
    private String seqFrontWinningNum;
    private String seqBackWinningNum;
    private String saleMoney;
    private String r9SaleMoney;
    private String prizePoolMoney;
    private String week;
}
