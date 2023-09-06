package com.kbslan.esl.controller;

import com.alibaba.fastjson.JSON;
import com.kbslan.esl.vo.request.sentinel.SentinelRulesRequest;
import com.kbslan.esl.vo.response.DataResponseJson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/6 17:46
 */
@RestController
@RequestMapping("/zookeeper")
public class ZookeeperController {

    @Resource
    private CuratorFramework curatorFramework;

    @PostMapping("/write")
    public DataResponseJson writeData(@RequestBody SentinelRulesRequest request) throws Exception {
        if (Objects.isNull(request) || StringUtils.isBlank(request.getPath())) {
            return DataResponseJson.error("参数错误");
        }
        String path = request.getPath();
        String rules;
        if (CollectionUtils.isNotEmpty(request.getFlowRules())) {
            rules = JSON.toJSONString(request.getFlowRules());
        } else if (CollectionUtils.isNotEmpty(request.getDegradeRules())) {
            rules = JSON.toJSONString(request.getDegradeRules());
        } else if (CollectionUtils.isNotEmpty(request.getSystemRules())) {
            rules = JSON.toJSONString(request.getSystemRules());
        } else {
            rules = "";
        }
        Stat stat = curatorFramework.setData().forPath(path, rules.getBytes());
        return Objects.isNull(stat) ? DataResponseJson.error("操作失败"): DataResponseJson.ok();
    }

}
