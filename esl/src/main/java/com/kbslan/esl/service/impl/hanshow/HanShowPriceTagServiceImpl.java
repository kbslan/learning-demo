package com.kbslan.esl.service.impl.hanshow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.PriceTagInfoEntity;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.domain.service.PriceTagInfoService;
import com.kbslan.esl.config.RedisUtils;
import com.kbslan.esl.service.PriceTagService;
import com.kbslan.esl.service.hanshow.HanShowApiService;
import com.kbslan.esl.service.hanshow.HanShowPriceTagUpdateResultHandleService;
import com.kbslan.esl.vo.pricetag.hanshow.HanShowResult;
import com.kbslan.esl.vo.pricetag.hanshow.PassivePriceTagHeartbeat;
import com.kbslan.esl.vo.pricetag.hanshow.UpdatePriceTagResult;
import com.kbslan.esl.vo.pricetag.PriceTagParams;
import com.kbslan.esl.vo.pricetag.PriceTagRefreshParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 汉朔价签处理逻辑
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/8/31 23:06
 */
@Slf4j
@Service
public class HanShowPriceTagServiceImpl implements PriceTagService {

    @Resource
    private HanShowApiService hanShowApiService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private PriceTagInfoService priceTagInfoService;
    @Resource
    private HanShowPriceTagUpdateResultHandleService hanShowPriceTagUpdateResultHandleService;

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
        log.info("汉朔价签心跳 json={}", json);
        HanShowResult<List<PassivePriceTagHeartbeat>> passivePriceTagHeartbeatHslResult = JSON.parseObject(json, new TypeReference<HanShowResult<List<PassivePriceTagHeartbeat>>>() {
        });
        List<PassivePriceTagHeartbeat> data = passivePriceTagHeartbeatHslResult.getData();

        for (PassivePriceTagHeartbeat heartbeat : data) {
            String eslId = heartbeat.getEslId();
            try {
                if (StringUtils.isNotEmpty(eslId)) {
                    PriceTagInfoEntity update = new PriceTagInfoEntity();
                    update.setLastHeartbeat(LocalDateTime.now());

                    LambdaQueryWrapper<PriceTagInfoEntity> queryWrapper = Wrappers.<PriceTagInfoEntity>lambdaQuery()
                            .eq(PriceTagInfoEntity::getPriceTagId, eslId.trim())
                            .eq(PriceTagInfoEntity::getStoreId, Long.parseLong(heartbeat.getUser()));

                    priceTagInfoService.update(update, queryWrapper);
                }

            } catch (Exception ex) {
                log.error("{} eslId={}", ex.getMessage(), eslId, ex);
            }
        }

    }

    @Override
    public void callback(String json) throws Exception {
        log.info("汉朔价签回调 json={}", json);
        final HanShowResult<List<UpdatePriceTagResult>> updatePriceTagResultHslResult = JSONObject.parseObject(json, new TypeReference<HanShowResult<List<UpdatePriceTagResult>>>() {
        });
        // 价签更新结果处理
        hanShowPriceTagUpdateResultHandleService.handle(updatePriceTagResultHslResult);
    }
}
