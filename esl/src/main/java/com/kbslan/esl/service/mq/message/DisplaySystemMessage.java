package com.kbslan.esl.service.mq.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 陈列系统电子价签操作消息
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 17:04
 */
@Getter
@Setter
@ToString
public class DisplaySystemMessage implements Serializable {

    private static final long serialVersionUID = 2387014244712531345L;
    private Integer operType;
    private List<Data> data;

    @Getter
    @Setter
    @ToString
    public static class Data implements Serializable {

        private static final long serialVersionUID = 5789166250650862545L;
        /**
         * 商家id
         */
        private Long venderId;
        /**
         * 门店id
         */
        private Long shopId;
        /**
         * 电子价签id(转码前)
         */
        private String eslId;
        /**
         * 设备供应商id
         */
        private String supplierId;
        /**
         * 商品skuId
         */
        private Long skuId;
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
        private Integer serialNumber;
        /**
         * 面宽
         */
        private Integer surfaceWidth;
        /**
         * 面高
         */
        private Integer surfaceHeight;
        /**
         * 面深
         */
        private Integer surfaceDepth;
        /**
         * 来客系统用户userId
         */
        private Long userId;
        /**
         * 来客系统userName
         */
        private String userName;

        /**
         *  仓位号：物理货架编码-陈列层序号-陈列位序号
         */
        private String cabin;

    }
}
