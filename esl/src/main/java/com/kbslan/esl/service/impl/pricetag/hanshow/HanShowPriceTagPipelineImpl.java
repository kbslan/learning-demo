package com.kbslan.esl.service.impl.pricetag.hanshow;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.service.pricetag.PriceTagPipeline;
import com.kbslan.esl.utils.MiscUtils;
import com.kbslan.esl.service.pricetag.model.PriceTagParams;
import com.kbslan.esl.vo.request.pricetag.PriceTagRequest;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     汉朔电子价签处理流程
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:38
 */
@Service
public class HanShowPriceTagPipelineImpl extends PriceTagPipeline {
    @Override
    public PriceTagDeviceSupplierEnum deviceSupplier() {
        return PriceTagDeviceSupplierEnum.HAN_SHOW;
    }

    @Override
    public PriceTagParams apply(PriceTagRequest request) {
        PriceTagParams params = super.apply(request);
        //汉朔价签ID转换
        params.setPriceTagId(MiscUtils.formatOriginPriceTagId(request.getOriginPriceTagId()));
        return params;
    }

    @Override
    public void beforeUnbind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) {
        //汉朔解绑不需要推送价签数据
        params.setNeedPush(false);
    }
}
