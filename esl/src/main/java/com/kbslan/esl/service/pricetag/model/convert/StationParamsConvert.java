package com.kbslan.esl.service.pricetag.model.convert;

import com.kbslan.esl.service.pricetag.model.StationParams;
import com.kbslan.esl.vo.request.pricetag.StationRequest;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * <p>
 *     基站参数转换
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 17:56
 */
@Service
public class StationParamsConvert implements Function<StationRequest, StationParams> {
    @Override
    public StationParams apply(StationRequest request) {
        StationParams params = new StationParams();
        params.setVendorId(request.getVendorId());
        params.setStoreId(request.getStoreId());
        params.setDeviceSupplier(request.getDeviceSupplier());
        params.setOriginAp(request.getOriginAp());
        params.setUserId(request.getUserId());
        params.setUserName(request.getUserName());
        params.setBingingSource(request.getBingingSource());
        params.setApMac(request.getOriginAp());
        params.setSid(request.getTrace());
        return params;
    }
}
