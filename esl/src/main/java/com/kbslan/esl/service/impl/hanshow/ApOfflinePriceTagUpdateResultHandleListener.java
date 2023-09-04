package com.kbslan.esl.service.impl.hanshow;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.PriceTagInfoEntity;
import com.kbslan.domain.service.PriceTagInfoService;
import com.kbslan.esl.config.RedisUtils;
import com.kbslan.esl.service.HanShowApHeartbeatHandleService;
import com.kbslan.esl.vo.hanshow.HanShowResult;
import com.kbslan.esl.vo.hanshow.PassiveAPHeartbeat;
import com.kbslan.esl.vo.hanshow.UpdatePriceTagResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 汉朔基站状态变更处理逻辑
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 11:04
 */
@Slf4j
@Component
public class ApOfflinePriceTagUpdateResultHandleListener implements HanShowPriceTagUpdateResultHandleService.PriceTagUpdateResultHandleListener,
        HanShowApHeartbeatHandleService.ApStatusChangedListener {

    private static final String KEY = "price-tag:hanshow-ap-offline:user:%s:ap:%d";

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private PriceTagInfoService priceTagInfoService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static final Set<String> HANDLE_STATUS_NOS = new HashSet<>(
            Collections.singletonList(HanShowResult.AP_OFFLINE)
    );

    @Override
    public boolean matches(HanShowResult<List<UpdatePriceTagResult>> result) {
        return HanShowResult.ESL_UPDATE_ACK.equals(result.getType());
    }

    @Override
    public void onHandle(HanShowResult<List<UpdatePriceTagResult>> result) {

        try {
            for (UpdatePriceTagResult priceTagResult : result.getData()) {
                if (HANDLE_STATUS_NOS.contains(priceTagResult.getStatusNo())) {
                    final String key = getKey(result.getUser(), priceTagResult.getApId());
                    final Long size = redisUtils.set.sadd(key, priceTagResult.getEslId());
                    if (log.isInfoEnabled()) {
                        log.info("price-tag update failure: value={},size={}", JSON.toJSONString(priceTagResult), size);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("save ap-offline price tag error:key={},price-ag={}", KEY, JSON.toJSONString(result), e);
        }
    }

    @Override
    public void onStatusChanged(PassiveAPHeartbeat apHeartbeat) {
        if (HanShowResult.STATUS_ONLINE.equals(apHeartbeat.getStatus())) {
            try {
                // changed status to online
                final String key = getKey(apHeartbeat.getUser(), apHeartbeat.getApId());
                final String running = key + ":running";
                long size = redisUtils.set.scard(key);
                if (size > 0 && redisUtils.string.setnx(running, running) == 1) {
                    executorService.submit(() -> {
                        String value = null;
                        try {
                            while ((value = redisUtils.set.spop(key)) != null) {
                                try {
                                    log.info("try to refresh price tag: user={},price tag={}", apHeartbeat.getUser(), value);
                                    redisUtils.key.expire(running, 60);
                                    // find all bind goods
                                    PriceTagInfoEntity priceTagInfo = priceTagInfoService.getOne(
                                            Wrappers.<PriceTagInfoEntity>lambdaQuery()
                                                    .eq(PriceTagInfoEntity::getStoreId, Integer.parseInt(apHeartbeat.getUser()))
                                                    .eq(PriceTagInfoEntity::getPriceTagId, value)
                                    );
                                    List<Long> skuIds = JSON.parseArray(priceTagInfo.getExtJson(), Long.class);
                                    log.info("find price tag goods of price tag:{},{}", value, skuIds);
                                    if (CollectionUtils.isNotEmpty(skuIds)) {
                                        for (Long skuId : skuIds) {
                                            try {
                                                log.info("try fo refresh price tag goods: vendor={},store={},sku={}", priceTagInfo.getVendorId(), priceTagInfo.getStoreId(), skuId);
//                                                cacheServiceManager.init(good.getVendorId(), good.getStoreId().longValue(), CacheRefreshType.AP_RE_ONLINE, CacheRefreshSource.INIT, Arrays.asList(good.getSku()));
                                                //TODO 刷新商品数据
                                            } catch (Exception e) {
                                                log.error("try fo refresh price tag goods error: vendor={},store={},sku={}", priceTagInfo.getVendorId(), priceTagInfo.getStoreId(), skuId, e);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("refresh price tag error: user={},key={},value={}", apHeartbeat.getUser(), key, value, e);
                                }
                            }
                        } catch (Exception e) {
                            log.error("refresh price tag error: user={},key={},value={}", apHeartbeat.getUser(), key, value, e);
                        } finally {
                            try {
                                redisUtils.key.del(running);
                            } catch (Exception e) {
                                log.error("删除redis key失败", e);
                            }
                        }
                    });
                } else {
                    log.info("refresh price tag size is empty or having running,key={},running={}", key, running);
                }
            } catch (Exception e) {
                log.error("refresh price tag error: user={},apId={}", apHeartbeat.getUser(), apHeartbeat.getApId(), e);
            }

        }
    }

    private String getKey(String user, Integer apId) {
        return String.format(KEY, user, apId);
    }
}

