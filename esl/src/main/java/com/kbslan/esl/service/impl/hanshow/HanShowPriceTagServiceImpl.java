package com.kbslan.esl.service.impl.hanshow;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.config.RedisUtils;
import com.kbslan.esl.service.PriceTagService;
import com.kbslan.esl.vo.request.pricetag.PriceTagParams;
import com.kbslan.esl.vo.request.pricetag.PriceTagRefreshParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *     汉朔价签处理逻辑
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 23:06
 */
@Service
public class HanShowPriceTagServiceImpl implements PriceTagService {

    @Resource
    private HanShowApiService hanShowApiService;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public PriceTagDeviceSupplierEnum deviceSupplier() {
        return PriceTagDeviceSupplierEnum.HAN_SHOW;
    }

    @Override
    public boolean bind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        return hanShowApiService.bindPriceTag(params, deviceEslApiModel);
    }

    @Override
    public boolean unbind(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        return hanShowApiService.unbindPriceTag(params, deviceEslApiModel);
    }

    @Override
    public boolean refresh(PriceTagRefreshParams params, DeviceEslApiModel deviceEslApiModel) throws Exception {
        return hanShowApiService.refresh(params, deviceEslApiModel);
    }

    @Override
    public void heartbeat(String json) throws Exception {

    }

    @Override
    public void callback(String json) throws Exception {

    }
}
