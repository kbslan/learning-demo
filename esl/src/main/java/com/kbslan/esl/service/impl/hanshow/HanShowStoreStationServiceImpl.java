package com.kbslan.esl.service.impl.hanshow;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.service.StoreStationService;
import com.kbslan.esl.vo.StationParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *     汉朔基站处理逻辑
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 22:55
 */
@Slf4j
@Service
public class HanShowStoreStationServiceImpl implements StoreStationService {
    @Resource
    private HanShowApiService hanShowApiService;

    @Override
    public PriceTagDeviceSupplierEnum deviceSupplier() {
        return PriceTagDeviceSupplierEnum.HAN_SHOW;
    }

    @Override
    public boolean bind(StationParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        return hanShowApiService.bindStation(params, deviceEslApiModel);
    }

    @Override
    public boolean unbind(StationParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        return hanShowApiService.unbindStation(params, deviceEslApiModel);
    }

    @Override
    public void heartbeat(String json) throws Exception {

    }
}
