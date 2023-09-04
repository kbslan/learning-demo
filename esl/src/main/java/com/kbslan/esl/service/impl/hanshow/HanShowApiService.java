package com.kbslan.esl.service.impl.hanshow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.service.OkHttpService;
import com.kbslan.esl.vo.response.notice.EslNoticeMessage;
import com.kbslan.esl.vo.request.pricetag.PriceTagParams;
import com.kbslan.esl.vo.request.pricetag.PriceTagRefreshParams;
import com.kbslan.esl.vo.request.pricetag.StationParams;
import com.kbslan.esl.vo.hanshow.AllotBaseStation;
import com.kbslan.esl.vo.hanshow.HanShowResult;
import com.kbslan.esl.vo.hanshow.PriceTagScreen;
import com.kbslan.esl.vo.hanshow.UnbindPriceTag;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;

/**
 * <p>
 * 汉朔厂商Api服务
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 10:21
 */
@Service
public class HanShowApiService {

    @Resource
    private OkHttpService okHttpService;


    public boolean bindStation(StationParams params, DeviceEslApiModel deviceEslApiModel) {
        if (Objects.isNull(params) || Objects.isNull(params.getApMac())
                || Objects.isNull(params.getStoreId()) || Objects.isNull(deviceEslApiModel)
                || Objects.isNull(deviceEslApiModel.getBindingStationUrl())) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_BIND_PARAMS_MISSING);
        }

        AllotBaseStation allotBaseStation = new AllotBaseStation();
        allotBaseStation.setMac(params.getApMac());
        allotBaseStation.setAllowBindV1Esl(true);
        //{user}/user/ap PUT 为该门店分配一个基站
        String url = deviceEslApiModel.getBindingStationUrl().buildAndExpand(params.getStoreId()).toString();
        String result = okHttpService.put(url, allotBaseStation);
        HanShowResult<Void> hanShowResult = JSON.parseObject(result, new TypeReference<HanShowResult<Void>>() {
        });
        return hanShowResult.isSuccess();
    }

    public boolean unbindStation(StationParams params, DeviceEslApiModel deviceEslApiModel) {
        if (Objects.isNull(params) || Objects.isNull(params.getApMac())
                || Objects.isNull(params.getStoreId()) || Objects.isNull(deviceEslApiModel)
                || Objects.isNull(deviceEslApiModel.getUnbindingStationUrl())) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_UNBIND_PARAMS_MISSING);
        }
        //{user}/user/ap/{apmac} DELETE 从该user移除一个基站
        String url = deviceEslApiModel.getUnbindingStationUrl().buildAndExpand(params.getStoreId(), params.getApMac()).toString();
        String result = okHttpService.delete(url);
        HanShowResult<Void> hanShowResult = JSON.parseObject(result, new TypeReference<HanShowResult<Void>>() {
        });
        return hanShowResult.isSuccess();
    }

    public boolean bindPriceTag(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) {
        return true;
    }

    public boolean unbindPriceTag(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) {
        //{user}/esls/bind DELETE 批量解绑价签
        UnbindPriceTag unbindPriceTag = new UnbindPriceTag();
        unbindPriceTag.setPriceTagId(params.getPriceTagId());
        unbindPriceTag.setSid(params.getTrace());
        String url = deviceEslApiModel.getUnbindingPriceTagUrl().buildAndExpand(params.getStoreId()).toUriString();

        String result = okHttpService.delete(url, Collections.singleton(unbindPriceTag));
        HanShowResult<Void> hanShowResult = JSON.parseObject(result, new TypeReference<HanShowResult<Void>>() {
        });
        return hanShowResult.isSuccess();
    }

    public boolean refresh(PriceTagRefreshParams params, DeviceEslApiModel deviceEslApiModel) {
        //{user}/esls/{id}/screen PUT 更新指定价签屏幕
        String refreshUrl = deviceEslApiModel.getRefreshPriceTagUrl().buildAndExpand(params.getStoreId(), params.getPriceTagId()).toString();

        String callbackUrl = deviceEslApiModel.getCallbackUrl()
                .queryParam("vendor", params.getVendorId())
                .queryParam("store", params.getStoreId())
                .queryParam("supplier", params.getDeviceSupplier().getCode())
                .build().toString();
        PriceTagScreen screen = new PriceTagScreen();
        screen.setSid(params.getSid());
        screen.setBackUrl(callbackUrl);
        screen.setName(params.getName());
        screen.setArgs(params.getData());

        String result = okHttpService.put(refreshUrl, screen);
        HanShowResult<Void> hanShowResult = JSON.parseObject(result, new TypeReference<HanShowResult<Void>>() {
        });
        return hanShowResult.isSuccess();

    }
}
