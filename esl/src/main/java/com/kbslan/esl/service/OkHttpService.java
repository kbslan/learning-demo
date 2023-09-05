package com.kbslan.esl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 22:58
 */
@Slf4j
@Service
public class OkHttpService {

    public String get(String url) {
        log.info("OkHttpService.get url={}", url);
        return "{\"status_no\":0,\"type\":\"ESL_HB_STATUS\",\"user\":\"112\"}";
    }

    public String post(String url, Object data) {
        log.info("OkHttpService.post url={}, data={}", url, data);
        return "{\"status_no\":0,\"type\":\"ESL_HB_STATUS\",\"user\":\"112\"}";
    }
    public String put(String url, Object data) {
        log.info("OkHttpService.put url={}, data={}", url, data);
        return "{\"status_no\":0,\"type\":\"ESL_HB_STATUS\",\"user\":\"112\"}";
    }
    public String delete(String url) {
        return delete(url, null);
    }
    public String delete(String url, Object data) {
        log.info("OkHttpService.delete url={}, data={}", url, data);
        return "{\"status_no\":0,\"type\":\"ESL_HB_STATUS\",\"user\":\"112\"}";
    }
}
