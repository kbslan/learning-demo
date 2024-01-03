package com.kbslan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kbslan.domain.entity.PriceTagInfoEntity;
import com.kbslan.domain.service.PriceTagInfoService;
import com.kbslan.esl.vo.request.PriceTagInfoQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2024/1/2 18:19
 */
@Slf4j
@SpringBootTest(classes = EslBootstrap.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PriceTagInfoQueryTest {

    @Resource
    private PriceTagInfoService priceTagInfoService;


    @Test
    public void testQueryPriceTagInfo() {
        PriceTagInfoQuery query = new PriceTagInfoQuery();
        query.setVendorId(5L);
        query.setStoreId(21L);
        query.setDeviceSupplier("HanShow");
        query.setSkuIds(Arrays.asList(1L, 2L));

        LambdaQueryWrapper<PriceTagInfoEntity> queryWrapper = Wrappers.lambdaQuery(PriceTagInfoEntity.class)
                .eq(PriceTagInfoEntity::getVendorId, query.getVendorId())
                .eq(PriceTagInfoEntity::getStoreId, query.getStoreId())
                .eq(StringUtils.isNotBlank(query.getDeviceSupplier()), PriceTagInfoEntity::getDeviceSupplier, query.getDeviceSupplier())
                .eq(StringUtils.isNotBlank(query.getOriginPriceTagId()), PriceTagInfoEntity::getOriginPriceTagId, query.getOriginPriceTagId())
                .eq(StringUtils.isNotBlank(query.getPriceTagId()), PriceTagInfoEntity::getPriceTagId, query.getPriceTagId())
                .eq(Objects.nonNull(query.getYn()), PriceTagInfoEntity::getYn, query.getYn());
        if (CollectionUtils.isNotEmpty(query.getSkuIds())) {
            queryWrapper.and(subQuery -> {
                List<Long> skuIds = query.getSkuIds();
                for (int i = 0; i < skuIds.size(); i++){
                    Long skuId = skuIds.get(i);
                    if (i == 0) {
                        subQuery.apply("JSON_CONTAINS(ext_json, json_array({0}), '$.skuIds')", skuId);
                    } else {
                        subQuery.or(wrapper -> wrapper.apply("JSON_CONTAINS(ext_json, json_array({0}), '$.skuIds')", skuId));
                    }
                }
            });
        }
        Page<PriceTagInfoEntity> page = priceTagInfoService.page(new Page<>(query.getCurrent(), query.getSize()), queryWrapper);
        System.out.println(page.getRecords());
    }
}
