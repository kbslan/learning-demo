package com.kbslan.esl.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *     基站参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 11:18
 */
@Getter
@Setter
@ToString
public class StationParams extends CommonParams {

    /**
     * 原始基站id
     */
    private String originApId;
    /**
     * 基站mac
     */
    private String apMac;

}
