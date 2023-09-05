package com.kbslan.esl.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbslan.domain.entity.PriceTagInfoEntity;
import com.kbslan.domain.service.PriceTagInfoService;
import com.kbslan.esl.service.PipelineFactory;
import com.kbslan.esl.vo.request.PriceTagInfoQuery;
import com.kbslan.esl.vo.request.pricetag.PriceTagRequest;
import com.kbslan.esl.vo.response.DataResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Resource
    private PipelineFactory pipelineFactory;

    /**
     * 电子价签列表查询
     *
     * @param query 查询参数
     * @return 电子价签列表
     */
    @PostMapping("/page")
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

    /**
     * 绑定电子价签
     *
     * @param request 绑定参数
     * @return 绑定结果
     * @throws Exception 绑定异常
     */
    @PostMapping("/bind")
    public DataResponseJson bind(@RequestBody PriceTagRequest request) throws Exception {
        return DataResponseJson.ok(pipelineFactory.createPriceTagPipeline(request.getDeviceSupplier()).bind(request));
    }

    /**
     * 解绑电子价签
     *
     * @param request 解绑参数
     * @return 解绑结果
     * @throws Exception 解绑异常
     */
    @PostMapping("/unbind")
    public DataResponseJson unbind(@RequestBody PriceTagRequest request) throws Exception {
        return DataResponseJson.ok(pipelineFactory.createPriceTagPipeline(request.getDeviceSupplier()).unbind(request));
    }
}
