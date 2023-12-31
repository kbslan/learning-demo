package com.kbslan.esl.service.pricetag.hanshow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kbslan.domain.model.DeviceEslApiModel;
import com.kbslan.esl.service.http.HttpComponent;
import com.kbslan.esl.service.pricetag.model.PriceTagParams;
import com.kbslan.esl.service.pricetag.model.PriceTagRefreshParams;
import com.kbslan.esl.service.pricetag.model.StationParams;
import com.kbslan.esl.service.pricetag.model.data.PriceTagHolder;
import com.kbslan.esl.service.pricetag.model.hanshow.AllotBaseStation;
import com.kbslan.esl.service.pricetag.model.hanshow.HanShowResult;
import com.kbslan.esl.service.pricetag.model.hanshow.PriceTagScreen;
import com.kbslan.esl.service.pricetag.model.hanshow.UnbindPriceTag;
import com.kbslan.esl.vo.response.notice.EslNoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 汉朔厂商Api服务
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 10:21
 */
@Slf4j
@Service
public class HanShowApiService {

    @Resource
    private HttpComponent httpComponent;


    public boolean bindStation(StationParams params, DeviceEslApiModel deviceEslApiModel) throws IOException {
        if (Objects.isNull(params) || StringUtils.isBlank(params.getApMac())
                || Objects.isNull(params.getStoreId()) || Objects.isNull(deviceEslApiModel)
                || Objects.isNull(deviceEslApiModel.getBindingStationUrl())) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_BIND_PARAMS_MISSING);
        }

        AllotBaseStation allotBaseStation = new AllotBaseStation();
        allotBaseStation.setMac(params.getApMac());
        allotBaseStation.setAllowBindV1Esl(true);
        //{user}/user/ap PUT 为该门店分配一个基站
        String url = deviceEslApiModel.getBindingStationUrl().buildAndExpand(params.getStoreId()).toString();
        String result = httpComponent.put(url, JSON.toJSONString(allotBaseStation));
        HanShowResult<Void> hanShowResult = JSON.parseObject(result, new TypeReference<HanShowResult<Void>>() {
        });
        if (!hanShowResult.isSuccess()) {
            log.error("汉朔绑定基站失败, params={}, result={}", params, result);
        }
        return hanShowResult.isSuccess();
    }

    public boolean unbindStation(StationParams params, DeviceEslApiModel deviceEslApiModel) throws IOException {
        if (Objects.isNull(params) || Objects.isNull(params.getApMac())
                || Objects.isNull(params.getStoreId()) || Objects.isNull(deviceEslApiModel)
                || Objects.isNull(deviceEslApiModel.getUnbindingStationUrl())) {
            throw new IllegalArgumentException(EslNoticeMessage.STATION_UNBIND_PARAMS_MISSING);
        }
        //{user}/user/ap/{apmac} DELETE 从该user移除一个基站
        String url = deviceEslApiModel.getUnbindingStationUrl().buildAndExpand(params.getStoreId(), params.getApMac()).toString();
        String result = httpComponent.delete(url);
        HanShowResult<Void> hanShowResult = JSON.parseObject(result, new TypeReference<HanShowResult<Void>>() {
        });
        if (!hanShowResult.isSuccess()) {
            log.error("汉朔解绑基站失败, params={}, result={}", params, result);
        }
        return hanShowResult.isSuccess();
    }

    public boolean bindPriceTag(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) {
        return true;
    }

    public boolean unbindPriceTag(PriceTagParams params, DeviceEslApiModel deviceEslApiModel) throws IOException {
        //{user}/esls/bind DELETE 批量解绑价签
        UnbindPriceTag unbindPriceTag = new UnbindPriceTag();
        unbindPriceTag.setPriceTagId(params.getPriceTagId());
        unbindPriceTag.setSid(params.getSid());
        String callbackUrl = deviceEslApiModel.getCallbackUrl()
                .queryParam("vendor", params.getVendorId())
                .queryParam("store", params.getStoreId())
                .queryParam("supplier", params.getDeviceSupplier().getCode())
                .build().toString();
        unbindPriceTag.setBackUrl(callbackUrl);
        String url = deviceEslApiModel.getUnbindingPriceTagUrl().buildAndExpand(params.getStoreId()).toUriString();

        Map<String, List<UnbindPriceTag>> data = new HashMap<>();
        data.put("data", Collections.singletonList(unbindPriceTag));
        String result = httpComponent.delete(url, data);
        HanShowResult<Void> hanShowResult = JSON.parseObject(result, new TypeReference<HanShowResult<Void>>() {
        });
        if (!hanShowResult.isSuccess()) {
            log.error("汉朔解绑价签失败, params={}, result={}", params, result);
        }
        return hanShowResult.isSuccess();
    }

    public boolean refresh(PriceTagRefreshParams params, List<PriceTagHolder> priceTagHolderList, DeviceEslApiModel deviceEslApiModel) throws IOException {
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

        //TODO 模版和数据填充
        screen.setName("");
        screen.setArgs(priceTagHolderList.get(0));

        String result =  httpComponent.put(refreshUrl, JSON.toJSONString(screen));
        HanShowResult<Void> hanShowResult = JSON.parseObject(result, new TypeReference<HanShowResult<Void>>() {
        });
        if (!hanShowResult.isSuccess()) {
            log.error("汉朔刷新价签失败, params={}, result={}", params, result);
        }
        return hanShowResult.isSuccess();

    }
}
