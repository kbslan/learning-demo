package com.kbslan.esl.service;

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
@Service
public class OkHttpService {

    public String get(String url) {
        return "okhttp get: " + url;
    }

    public String post(String url, Object data) {
        return "okhttp post: " + url;
    }
    public String put(String url, Object data) {
        return "okhttp put: " + url;
    }
    public String delete(String url) {
        return delete(url, null);
    }
    public String delete(String url, Object data) {
        return "okhttp put: " + url;
    }
}
