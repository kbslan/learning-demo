package com.kbslan.esl.service.impl.hanshow;

import com.alibaba.fastjson.JSON;
import com.kbslan.esl.config.RedisUtils;
import com.kbslan.esl.service.HanShowApHeartbeatHandleService;
import com.kbslan.esl.vo.hanshow.HanShowResult;
import com.kbslan.esl.vo.hanshow.PassiveAPHeartbeat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
@Service
public class HanShowApHeartbeatHandleServiceImpl implements HanShowApHeartbeatHandleService {

    private static final String AP_STATUS_KEY = "pricetag:hanshow:ap-status:%s:%d";

    private final List<HanShowApHeartbeatHandleService.ApStatusChangedListener> apStatusChangedListeners = new ArrayList<>();

    public HanShowApHeartbeatHandleServiceImpl(ObjectProvider<List<ApStatusChangedListener>> provider) {
        this.apStatusChangedListeners.addAll(Objects.requireNonNull(provider.getIfAvailable()));
    }

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void handle(HanShowResult<List<PassiveAPHeartbeat>> result) {

        try {
            for (PassiveAPHeartbeat apHeartbeat : result.getData()) {
                final String key = String.format(AP_STATUS_KEY, apHeartbeat.getUser(), apHeartbeat.getApId());
                final String value = JSON.toJSONString(apHeartbeat);
                switch (apHeartbeat.getStatus()) {
                    case HanShowResult.STATUS_OFFLINE: {
                        long setIfAbsent = redisUtils.string.setnx(key, value);
                        if (setIfAbsent == 1) {
                            log.info("基站首次离线，记录状态数据：key={},value={}", key, value);
                            for (ApStatusChangedListener listener : apStatusChangedListeners) {
                                //基站首次离线状态变更
                                listener.onStatusChanged(apHeartbeat);
                            }
                        } else {
                            log.info("基站非首次离线，{}", value);
                        }
                        break;
                    }
                    case HanShowResult.STATUS_ONLINE: {
                        if (redisUtils.key.exists(key)) {
                            try {
                                for (ApStatusChangedListener listener : apStatusChangedListeners) {
                                    //基站首次上线状态变更
                                    listener.onStatusChanged(apHeartbeat);
                                }
                            } catch (Exception e) {
                                log.warn("基站上线处理异常：{}", value, e);
                            } finally {
                                // remove the key
                                redisUtils.key.del(key);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("基站状态变更处理异常", e);
        }

    }
}

