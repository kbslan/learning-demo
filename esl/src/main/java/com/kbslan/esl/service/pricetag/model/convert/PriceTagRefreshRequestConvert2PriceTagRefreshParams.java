package com.kbslan.esl.service.pricetag.model.convert;

import com.kbslan.esl.service.pricetag.model.PriceTagRefreshParams;
import com.kbslan.esl.vo.request.pricetag.PriceTagRefreshRequest;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * <p>
 *     电子价签刷新参数转换
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 17:58
 */
@Service
public class PriceTagRefreshRequestConvert2PriceTagRefreshParams implements Function<PriceTagRefreshRequest, PriceTagRefreshParams> {
    @Override
    public PriceTagRefreshParams apply(PriceTagRefreshRequest request) {
        PriceTagRefreshParams params = new PriceTagRefreshParams();
        params.setDeviceSupplier(request.getDeviceSupplier());
        params.setVendorId(request.getVendorId());
        params.setStoreId(request.getStoreId());
        params.setOriginPriceTagId(request.getOriginPriceTagId());
        params.setPriceTagId(request.getPriceTagId());
        params.setSid(request.getTrace());
        params.setSkuIds(request.getSkuIds());

        return params;
    }
}
