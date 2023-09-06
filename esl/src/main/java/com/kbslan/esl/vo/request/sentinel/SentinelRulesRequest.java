package com.kbslan.esl.vo.request.sentinel;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Sentinel规则请求参数
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/6 18:01
 */
@Getter
@Setter
@ToString
public class SentinelRulesRequest implements Serializable {
    private static final long serialVersionUID = 7670189797713260629L;
    /**
     * zk节点路径
     */
    private String path;

    /**
     * 流控规则
     */
    private List<FlowRule> flowRules;

    /**
     * 熔断规则
     */
    private List<DegradeRule> degradeRules;

    /**
     * 系统规则
     */
    private List<SystemRule> systemRules;
}
