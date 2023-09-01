package com.kbslan.esl.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbslan.domain.entity.PriceTagInfoEntity;
import com.kbslan.domain.service.PriceTagInfoService;
import com.kbslan.esl.service.PipelineFactory;
import com.kbslan.esl.service.impl.hanshow.HanShowPipelineFactory;
import com.kbslan.esl.vo.PriceTagInfoQuery;
import com.kbslan.esl.vo.PriceTagParams;
import com.kbslan.esl.vo.response.DataResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 16:16
 */
@RestController
@RequestMapping("/price/tag/esl")
public class PriceTagController {
    @Resource
    private PriceTagInfoService priceTagInfoService;

    @GetMapping("/page")
    public DataResponseJson page(@RequestBody PriceTagInfoQuery query) {

        Page<PriceTagInfoEntity> page = priceTagInfoService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.<PriceTagInfoEntity>lambdaQuery()
                        .eq(PriceTagInfoEntity::getVendorId, query.getVendorId())
                        .eq(PriceTagInfoEntity::getStoreId, query.getStoreId())
                        .eq(StringUtils.isNotBlank(query.getPriceTagId()), PriceTagInfoEntity::getPriceTagId, query.getPriceTagId())
                        .eq(Objects.nonNull(query.getYn()), PriceTagInfoEntity::getYn, query.getYn()));
        return DataResponseJson.ok(page);
    }

    @PostMapping("/bind")
    public DataResponseJson bind(@RequestBody PriceTagParams stationParams) throws Exception {
        PipelineFactory pipelineFactory = new HanShowPipelineFactory();
        return DataResponseJson.ok(pipelineFactory.createPriceTagPipeline().bind(stationParams));
    }

    @PostMapping("/unbind")
    public DataResponseJson unbind(@RequestBody PriceTagParams unbindingParams) throws Exception {
        PipelineFactory pipelineFactory = new HanShowPipelineFactory();
        return DataResponseJson.ok(pipelineFactory.createPriceTagPipeline().unbind(unbindingParams));
    }
}
