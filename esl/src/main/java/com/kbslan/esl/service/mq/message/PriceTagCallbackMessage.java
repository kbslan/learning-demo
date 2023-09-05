package com.kbslan.esl.service.mq.message;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *     电子价签刷新回调消息
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 18:36
 */
@Getter
@Setter
@ToString
public class PriceTagCallbackMessage implements Serializable {
    private static final long serialVersionUID = -7786866466407740964L;

    /**
     * 设备厂商
     */
    private PriceTagDeviceSupplierEnum deviceSupplier;

    /**
     * 消息体内容
     */
    private String body;
}
