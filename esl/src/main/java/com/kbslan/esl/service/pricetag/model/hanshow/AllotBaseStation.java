package com.kbslan.esl.service.pricetag.model.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *     汉朔基站分配参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 10:58
 */
@Getter
@Setter
@ToString
public class AllotBaseStation {
    /**
     * (必填) mac地址
     */
    private String mac;

    /**
     * 是否允许绑定第一代价签
     */
    @JSONField(name = "allow_bind_v1esl")
    private boolean allowBindV1Esl;
}
