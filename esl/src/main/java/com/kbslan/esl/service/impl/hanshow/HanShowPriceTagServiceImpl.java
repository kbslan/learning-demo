package com.kbslan.esl.service.impl.hanshow;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.service.OkHttpService;
import com.kbslan.esl.service.PriceTagService;
import com.kbslan.esl.vo.PriceTagParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 23:06
 */
@Service
public class HanShowPriceTagServiceImpl implements PriceTagService {

    @Resource
    private OkHttpService okHttpService;
    @Override
    public PriceTagDeviceSupplierEnum deviceSupplier() {
        return PriceTagDeviceSupplierEnum.HAN_SHOW;
    }

    @Override
    public boolean bind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        okHttpService.get("汉朔电子价签绑定请求..."+ params.toString());
        return true;
    }

    @Override
    public boolean unbind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        okHttpService.get("汉朔电子价签解绑请求..."+ params.toString());
        return true;
    }

    @Override
    public boolean refresh(String storeId, String priceTagId, Object data, DeviceEslApiModel deviceEslApiModel) throws Exception {
        okHttpService.get("汉朔电子价签刷新请求...");
        return true;
    }
}
