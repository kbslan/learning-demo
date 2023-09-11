package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2022/3/28 16:17
 */
@Getter
@Setter
public class FlagValue implements Serializable {
    private String flagValue;
    private String flagValueName;
}
