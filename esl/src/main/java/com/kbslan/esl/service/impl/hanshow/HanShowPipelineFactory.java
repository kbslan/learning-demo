package com.kbslan.esl.service.impl.hanshow;

import com.kbslan.esl.service.PipelineFactory;
import com.kbslan.esl.service.PriceTagPipeline;
import com.kbslan.esl.service.StationPipeline;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *     汉朔处理流程抽象工厂
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:35
 */
@Service
public class HanShowPipelineFactory implements PipelineFactory {

    @Resource
    private HanShowStationPipeline hanShowStationPipeline;
    @Resource
    private HanShowPriceTagPipeline hanShowPriceTagPipeline;

    @Override
    public StationPipeline createStationPipeline() {
        return hanShowStationPipeline;
    }

    @Override
    public PriceTagPipeline createPriceTagPipeline() {
        return hanShowPriceTagPipeline;
    }
}
