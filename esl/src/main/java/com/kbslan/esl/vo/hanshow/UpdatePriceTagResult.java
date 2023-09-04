package com.kbslan.esl.vo.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <p>
 * 更新价签异步返回结果
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 11:04
 */
@Getter
@Setter
@ToString
public class UpdatePriceTagResult {
    /**
     * 会话ID
     */
    private String sid;

    /**
     * 请求价签的id
     */
    @JSONField(name = "esl_id")
    private String eslId;
    /**
     * 能量值
     */
    @JSONField(name = "rf_power")
    private Integer rfPower;

    /**
     * 重试次数
     */
    private Integer retry;

    /**
     * 使用基站ID
     */
    @JSONField(name = "ap_id")
    private Integer apId;

    /**
     * base64 后 的 价 签 渲 染 图 ， 仅 在 ESL_UPDATE_ACK 中
     * errno=0时返回此值
     */
    @JSONField(name = "esl_image",serialize = false)
    private String eslImage;

    /**
     * 此价签在此基站上的最后一次心跳时间
     */
    @JSONField(name = "last_hb_time")
    private String lastHbTime;


    /**
     * 通信ack
     */
    @JSONField(name = "last_esl_ack")
    private Integer lastEslAck;

    /**
     * 最后一次通信任务
     */
    @JSONField(name = "last_payload_type")
    private String lastPayloadType;

    /**
     * 此价签绑定的salesNo, 没有时返回空串
     */
    @JSONField(name = "sales_no")
    private String salesNo;


    /**
     * 错误代码
     */
    @JSONField(name = "status_no")
    private String statusNo;


    /**
     * 错误信息。没有错误时返回空字符串
     */
    @JSONField(name = "errmsg")
    private String  errmsg;

}
