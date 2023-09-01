package com.kbslan.esl.vo;

import lombok.Data;

/**
 * @author Panbo.Guo
 */
@Data
public class PageRequest {
    /**
     * 当前页
     */
    private Long current = 1L;
    /**
     * 每页条数
     */
    private Long size = 20L;
}
