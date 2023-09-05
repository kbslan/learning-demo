package com.kbslan.esl.service.impl.pricetag.hanshow;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.esl.service.pricetag.StationPipeline;
import com.kbslan.esl.utils.MiscUtils;
import com.kbslan.esl.service.pricetag.model.StationParams;
import com.kbslan.esl.vo.request.pricetag.StationRequest;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     汉朔基站处理流程
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:36
 */
@Service
public class HanShowStationPipelineImpl extends StationPipeline {

    @Override
    public PriceTagDeviceSupplierEnum deviceSupplier() {
        return PriceTagDeviceSupplierEnum.HAN_SHOW;
    }

    @Override
    public StationParams apply(StationRequest request) {
        StationParams params = super.apply(request);
        //汉朔基站mac转换
        params.setApMac(MiscUtils.formatApBarcode(request.getOriginAp()));
        return params;
    }
}
