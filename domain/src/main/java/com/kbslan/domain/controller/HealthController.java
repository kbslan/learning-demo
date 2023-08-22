package com.kbslan.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2023/8/22 16:54
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
