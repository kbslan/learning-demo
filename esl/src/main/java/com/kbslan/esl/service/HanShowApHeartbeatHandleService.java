package com.kbslan.esl.service;

import com.kbslan.esl.vo.hanshow.HanShowResult;
import com.kbslan.esl.vo.hanshow.PassiveAPHeartbeat;

import java.util.List;


/**
 * <p>
 * 汉朔基站状态变更处理逻辑
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 11:04
 */
public interface HanShowApHeartbeatHandleService {
    /**
     * handle the {@linkplain PassiveAPHeartbeat heartbeats }
     *
     * @param result the ap heartbeats
     */
    void handle(HanShowResult<List<PassiveAPHeartbeat>> result);


    interface ApStatusChangedListener {
        /**
         * listen the status changed of {@link PassiveAPHeartbeat}
         *
         * @param apHeartbeat heartbeat status
         */
        void onStatusChanged(PassiveAPHeartbeat apHeartbeat);
    }


}
