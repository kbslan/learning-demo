package com.kbslan.esl.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2023/08/10 15:48
 */
@Configuration
public class SentinelZookeeperConfig implements InitializingBean {
    private static final int RETRY_TIMES = 3;
    private static final int SLEEP_TIME = 1000;

    /**
     * ZooKeeper 监听路径
     */
    private static final String groupId = "priceTagSentinelRules";
    private static final String flowDataId = "flow";
    private static final String degradeDataId = "degrade";
    private static final String systemDataId = "system";

    /**
     * ZooKeeper 地址
     */
    @Value("${spring.cloud.sentinel.datasource.ds.zk.server-addr}")
    private String remoteAddress;

    @Bean
    public CuratorFramework curatorFramework() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(remoteAddress, new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
        curatorFramework.start();
        return curatorFramework;
    }

    @Bean
    public NodeCache flowRuleNodeCache() throws Exception {
        NodeCache flowRuleNodeCache = new NodeCache(curatorFramework(), getPath(groupId, flowDataId), false);
        flowRuleNodeCache.start(true);
        flowRuleNodeCache.getListenable().addListener(() -> {
            ChildData currentData = flowRuleNodeCache.getCurrentData();
            String data = currentData == null ? "" : new String(currentData.getData());
            System.out.println("flowRuleNodeCache =====> Node data update, new Data: "+data);
        });
        return flowRuleNodeCache;
    }
    @Bean
    public NodeCache degradeRuleNodeCache() throws Exception {
        NodeCache degradeRuleNodeCache = new NodeCache(curatorFramework(), getPath(groupId, degradeDataId), false);
        degradeRuleNodeCache.start(true);
        degradeRuleNodeCache.getListenable().addListener(() -> {
            ChildData currentData = degradeRuleNodeCache.getCurrentData();
            String data = currentData == null ? "" : new String(currentData.getData());
            System.out.println("degradeRuleNodeCache =====> Node data update, new Data: "+data);
        });
        return degradeRuleNodeCache;
    }

    @Bean
    public NodeCache systemRuleNodeCache() throws Exception {
        NodeCache systemRuleNodeCache = new NodeCache(curatorFramework(), getPath(groupId, systemDataId), false);
        systemRuleNodeCache.start(true);
        systemRuleNodeCache.getListenable().addListener(() -> {
            ChildData currentData = systemRuleNodeCache.getCurrentData();
            String data = currentData == null ? "" : new String(currentData.getData());
            System.out.println("systemRuleNodeCache =====> Node data update, new Data: "+data);
        });
        return systemRuleNodeCache;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String flowRulePath = getPath(groupId, flowDataId);
        createPersistent(flowRulePath);
        String degradeRulePath = getPath(groupId, degradeDataId);
        createPersistent(degradeRulePath);
        String systemRulePath = getPath(groupId, systemDataId);
        createPersistent(systemRulePath);

        // 配置 ZooKeeper 数据源
        // 规则会持久化到zk的/groupId/flowDataId节点
        // groupId和和flowDataId可以用/开头也可以不用
        // 建议不用以/开头，目的是为了如果从Zookeeper切换到Nacos的话，只需要改数据源类名就可以
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(remoteAddress, groupId, flowDataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ZookeeperDataSource<>(remoteAddress, groupId, degradeDataId,
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {}));
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

        ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ZookeeperDataSource<>(remoteAddress, groupId, systemDataId,
                source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {}));
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
    }

    private String getPath(String groupId, String dataId) {
        return String.format("/%s/%s", groupId, dataId);
    }

    private void createPersistent(String path) throws Exception {
        CuratorFramework curatorFramework = curatorFramework();
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat == null) {
            //节点不存在，则创建节点
            curatorFramework.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, null);
        }
    }
}

