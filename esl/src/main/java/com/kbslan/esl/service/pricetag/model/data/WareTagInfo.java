package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2022/3/14 11:15
 */
@Getter
@Setter
public class WareTagInfo implements Serializable {
    private String flag;
    private String flagName;
    private List<FlagValue> flagValueList;
}
