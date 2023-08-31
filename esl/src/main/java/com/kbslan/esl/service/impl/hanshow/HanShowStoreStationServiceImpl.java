package com.kbslan.esl.service.impl.hanshow;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.service.OkHttpService;
import com.kbslan.esl.service.StoreStationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 22:55
 */
@Service
public class HanShowStoreStationServiceImpl implements StoreStationService {
    @Resource
    private OkHttpService okHttpService;

    @Override
    public PriceTagDeviceSupplierEnum deviceSupplier() {
        return PriceTagDeviceSupplierEnum.HAN_SHOW;
    }

    @Override
    public boolean bindStation(String storeId, String apMac, DeviceEslApiModel deviceEslApiModel) throws Exception {
        okHttpService.get("汉朔基站绑定请求...");
        return true;
    }

    @Override
    public boolean unbindStation(String storeId, String apMac, DeviceEslApiModel deviceEslApiModel) throws Exception {
        okHttpService.get("汉朔基站解绑请求...");
        return true;
    }
}
