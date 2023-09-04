package com.kbslan.esl.vo.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *     价签解绑参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 14:15
 */
@Getter
@Setter
@ToString
public class UnbindPriceTag {

    /**
     * (必填) 价签ID
     */
    @JSONField(name = "esl_id")
    private String priceTagId;

    /**
     *  (必填) 此次指令的会话ID, 要求一个价签指令一条，异步返回ACK使用
     */
    private String sid;
    /**
     * (必填) 异步结果回调URL
     */
    @JSONField(name = "back_url")
    private String backUrl;

    /**
     * (选填) 解绑模板，默认：_UNBIND
     */
    private String template;


}
