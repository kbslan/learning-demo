package com.kbslan.esl.service.pricetag.model.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 陈列信息
 *
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2020/12/21 11:10
 */
@Getter
@Setter
public class Display implements Serializable {
    private static final long serialVersionUID = 5800846273902837551L;
    /**
     * 是否绑定货架
     */
    private Boolean shelves;
    /**
     * 物理货架编码
     */
    private String entityShelvesCode;
    /**
     * 陈列层序号
     */
    private String layerNumber;
    /**
     * 陈列位序号
     */
    private String serialNumber;
    /**
     * 面宽数
     */
    private String surfaceWidth;
    /**
     * 面高数
     */
    private String surfaceHeight;
    /**
     * 面深数
     */
    private String surfaceDepth;

    private String cabin;
}
