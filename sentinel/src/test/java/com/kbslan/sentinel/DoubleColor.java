package com.kbslan.sentinel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/18 16:52
 */
@Getter
@Setter
@ToString
public class DoubleColor {
    private String pageNum;
    private String pageSize;
    private String total;
    private String pages;
    private List<Item> data;
    private String resCode;
    private String message;
}
