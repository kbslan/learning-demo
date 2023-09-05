package com.kbslan.esl.service.pricetag.model.convert;

import com.kbslan.esl.service.pricetag.model.PriceTagParams;
import com.kbslan.esl.vo.request.pricetag.PriceTagRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 *     电子价签参数转换
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 17:54
 */
@Service
public class PriceTagRequestConvert2PriceTagParams implements Function<PriceTagRequest, PriceTagParams> {
    @Override
    public PriceTagParams apply(PriceTagRequest request) {
        PriceTagParams params = new PriceTagParams();
        params.setNeedPush(Objects.isNull(request.getNeedPush()) ? Boolean.TRUE : request.getNeedPush());
        params.setOriginPriceTagId(request.getOriginPriceTagId());
        params.setPriceTagId(request.getOriginPriceTagId());
        params.setSkuIds(request.getSkuIds());
        params.setDeviceType(request.getDeviceType());
        params.setSid(request.getTrace());
        params.setVendorId(request.getVendorId());
        params.setStoreId(request.getStoreId());
        params.setDeviceSupplier(request.getDeviceSupplier());
        params.setUserId(request.getUserId());
        params.setUserName(request.getUserName());
        params.setBingingSource(request.getBingingSource());
        return params;
    }
}
