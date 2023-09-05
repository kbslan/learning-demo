package com.kbslan.esl.service.pricetag.model.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * <p>
 *     价签刷新参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 14:58
 */
@Getter
@Setter
public class PriceTagScreen {

    /**
     * (必填) 指令的会话ID, 要求一个价签指令一条，异步返回ACK使用
     */
    @NotNull
    private String sid;

    /**
     * （选填） 优先级。 0最低，1最高，随着数字增大优先级逐渐降低
     */
    private Integer priority = 5;

    /**
     * 回调URL
     */
    @JSONField(name = "back_url")
    private String backUrl;

    /**
     * 模版名称
     */
    private String name;

    /**
     * 参数
     */
    private Map<String, Object> args;
}
