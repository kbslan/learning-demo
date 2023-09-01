package com.kbslan.esl.service;

/**
 * <p>
 *     处理流程抽象工厂
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:25
 */
public interface PipelineFactory {

    /**
     * 获取基站处理流程
     *
     * @return 基站处理流程
     */
    StationPipeline createStationPipeline();

    /**
     * 获取价格标签处理流程
     *
     * @return 价格标签处理流程
     */
    PriceTagPipeline createPriceTagPipeline();
}
