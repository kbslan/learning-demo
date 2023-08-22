package com.kbslan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author chao.lan
 * @version 0.0.1-SNAPSHOT
 * @since 2023/8/22 14:55
 */
@SpringBootApplication(scanBasePackages = "com.kbslan")
public class SentinelBootstrap extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SentinelBootstrap.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SentinelBootstrap.class);
    }
}
