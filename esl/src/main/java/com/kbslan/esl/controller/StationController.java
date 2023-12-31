package com.kbslan.esl.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbslan.domain.entity.ApStoreEntity;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.service.ApStoreService;
import com.kbslan.esl.service.pricetag.PipelineFactory;
import com.kbslan.esl.vo.request.ApStoreQuery;
import com.kbslan.esl.vo.request.pricetag.StationRequest;
import com.kbslan.esl.vo.response.DataResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 * 基站控制器
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 16:15
 */
@RestController
@RequestMapping("/price/tag/station")
public class StationController {
    @Resource
    private ApStoreService apStoreService;
    @Resource
    private PipelineFactory pipelineFactory;

    /**
     * 基站列表查询
     *
     * @param query 查询条件
     * @return 基站列表
     */
    @PostMapping("/page")
    public DataResponseJson page(ApStoreQuery query) {
        Page<ApStoreEntity> page = apStoreService.page(
                new Page<>(query.getCurrent(), query.getSize()),
                Wrappers.<ApStoreEntity>lambdaQuery()
                        .eq(ApStoreEntity::getVendorId, query.getVendorId())
                        .eq(ApStoreEntity::getStoreId, query.getStoreId())
                        .eq(Objects.nonNull(query.getYn()), ApStoreEntity::getYn, query.getYn())
                        .eq(StringUtils.isNotBlank(query.getOriginAp()), ApStoreEntity::getOriginAp, query.getOriginAp())
                        .eq(StringUtils.isNotBlank(query.getApMac()), ApStoreEntity::getApMac, query.getApMac())
        );
        return DataResponseJson.ok(page);
    }


    /**
     * 绑定基站
     *
     * @param request 绑定参数
     * @return 绑定结果
     * @throws Exception 绑定异常
     */
    @PostMapping("/bind")
    public DataResponseJson bind(StationRequest request) throws Exception {
        PriceTagDeviceSupplierEnum deviceSupplier = PriceTagDeviceSupplierEnum.get(request.getDeviceSupplier());
        return DataResponseJson.ok(pipelineFactory.createStationPipeline(deviceSupplier).bind(request));
    }

    /**
     * 解绑基站
     *
     * @param request 解绑参数
     * @return 解绑结果
     * @throws Exception 解绑异常
     */
    @PostMapping("/unbind")
    public DataResponseJson unbind(StationRequest request) throws Exception {
        PriceTagDeviceSupplierEnum deviceSupplier = PriceTagDeviceSupplierEnum.get(request.getDeviceSupplier());
        return DataResponseJson.ok(pipelineFactory.createStationPipeline(deviceSupplier).unbind(request));
    }
}
