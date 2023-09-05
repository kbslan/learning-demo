package com.kbslan.esl.service.pricetag.model.convert;

import com.kbslan.esl.service.pricetag.model.PriceTagParams;
import com.kbslan.esl.service.pricetag.model.PriceTagRefreshParams;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * <p>
 *     电子价签刷新参数转换
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 18:24
 */
@Service
public class PriceTagParamsConvert2PriceTagRefreshParams implements Function<PriceTagParams, PriceTagRefreshParams> {
    @Override
    public PriceTagRefreshParams apply(PriceTagParams priceTagParams) {
        PriceTagRefreshParams params = new PriceTagRefreshParams();
        params.setDeviceSupplier(priceTagParams.getDeviceSupplier());
        params.setVendorId(priceTagParams.getVendorId());
        params.setStoreId(priceTagParams.getStoreId());
        params.setOriginPriceTagId(priceTagParams.getOriginPriceTagId());
        params.setPriceTagId(priceTagParams.getPriceTagId());
        params.setSid(params.getSid());
        params.setSkuIds(params.getSkuIds());

        return params;
    }
}
