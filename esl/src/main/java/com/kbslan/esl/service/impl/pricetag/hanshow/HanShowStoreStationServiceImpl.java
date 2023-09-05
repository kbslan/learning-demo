package com.kbslan.esl.service.impl.pricetag.hanshow;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.ApStoreEntity;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.service.ApStoreService;
import com.kbslan.esl.service.pricetag.hanshow.HanShowApHeartbeatHandleService;
import com.kbslan.esl.service.pricetag.StoreStationService;
import com.kbslan.esl.service.pricetag.hanshow.HanShowApiService;
import com.kbslan.esl.service.pricetag.model.hanshow.HanShowResult;
import com.kbslan.esl.service.pricetag.model.hanshow.PassiveAPHeartbeat;
import com.kbslan.esl.service.pricetag.model.StationParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    @Resource
    private HanShowApHeartbeatHandleService hanshowApHeartbeatHandleService;
    @Resource
    private ApStoreService apStoreService;

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
        log.info("汉朔基站心跳 json={}", json);
        HanShowResult<List<PassiveAPHeartbeat>> passiveAPHeartbeatHslResult
                = JSONObject.parseObject(json, new TypeReference<HanShowResult<List<PassiveAPHeartbeat>>>() {
        });
        // handle ap status heartbeats
        hanshowApHeartbeatHandleService.handle(passiveAPHeartbeatHslResult);

        List<PassiveAPHeartbeat> passiveAPHeartbeatHslResultData = passiveAPHeartbeatHslResult.getData();
        for (PassiveAPHeartbeat passiveAPHeartbeatHslResultDatum : passiveAPHeartbeatHslResultData) {
            String mac = passiveAPHeartbeatHslResultDatum.getMac();

            if (Objects.equals(passiveAPHeartbeatHslResultDatum.getStatus(), HanShowResult.STATUS_ONLINE)) {
                ApStoreEntity update = new ApStoreEntity();
                update.setLastHeartbeat(LocalDateTime.now());
                LambdaQueryWrapper<ApStoreEntity> queryWrapper = Wrappers.<ApStoreEntity>lambdaQuery()
                        .eq(ApStoreEntity::getApMac, mac);
                apStoreService.update(update, queryWrapper);
            }
        }
    }
}
