package com.kbslan.esl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.esl.service.mq.PriceTagCallbackMessageConsumerHandler;
import com.kbslan.esl.service.mq.message.PriceTagCallbackMessage;
import com.kbslan.esl.service.pricetag.PriceTagServiceFactory;
import com.kbslan.esl.service.pricetag.StoreStationServiceFactory;
import com.kbslan.esl.service.pricetag.model.hanshow.HanShowResult;
import com.kbslan.esl.vo.response.DataResponseJson;
import com.kbslan.esl.vo.response.notice.EslNoticeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>
 * 厂商回调控制器
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 15:35
 */
@Slf4j
@RestController
@RequestMapping("/device")
public class PriceTagCallbackController {

    @Resource
    private StoreStationServiceFactory storeStationServiceFactory;
    @Resource
    private PriceTagServiceFactory priceTagServiceFactory;
    @Resource
    private PriceTagCallbackMessageConsumerHandler priceTagCallbackMessageConsumerHandler;

    /**
     * 基站 & 价签心跳
     *
     * @param request  请求
     * @param response 响应
     * @return 响应结果
     * @throws IOException 异常
     */
    @RequestMapping("/heartbeat")
    public DataResponseJson heartbeat(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = parseRequest(request);
        PriceTagDeviceSupplierEnum deviceSupplier = parseDeviceSupplier(request);
        log.debug("接收到设备心跳数据 deviceSupplier={} json={}", deviceSupplier, json);

        JSONObject jsonObject = JSON.parseObject(json);

        String type = jsonObject.getString("type");
        //基站
        if (Objects.equals(type, HanShowResult.AP_STATUS)) {
            storeStationServiceFactory.create(deviceSupplier).heartbeat(json);
        } else if (Objects.equals(type, HanShowResult.ESL_HB_STATUS)) {
            priceTagServiceFactory.create(deviceSupplier).heartbeat(json);
        } else {
            throw new IllegalArgumentException("未识别的心跳类型" + type);
        }

        return DataResponseJson.ok();
    }

    @RequestMapping("/callback")
    public DataResponseJson callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String json = parseRequest(request);
        PriceTagDeviceSupplierEnum deviceSupplier = parseDeviceSupplier(request);
        log.debug("接收到价签回调数据 deviceSupplier={} json={}", deviceSupplier, json);
        //TODO 发生MQ消息

        //此处先模拟调用 start
        priceTagServiceFactory.create(deviceSupplier).callback(json);

        PriceTagCallbackMessage message = new PriceTagCallbackMessage();
        message.setDeviceSupplier(deviceSupplier);
        message.setBody(json);
        priceTagCallbackMessageConsumerHandler.onHandle(message);
        //此处先模拟调用 end

        return DataResponseJson.ok();
    }

    /**
     * 解析请求中的参数
     *
     * @param request 请求
     * @return 参数字符串
     * @throws IOException 异常
     */
    private String parseRequest(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ServletInputStream inputStream = request.getInputStream();
        IOUtils.copy(inputStream, byteArrayOutputStream, 1024);
//        IOUtils.copyBytes(inputStream, byteArrayOutputStream, 1024, true);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 获取厂商类型
     *
     * @param request 请求
     * @return 厂商类型
     */
    private PriceTagDeviceSupplierEnum parseDeviceSupplier(HttpServletRequest request) {
        String supplier = request.getParameter("supplier");
        if (StringUtils.isBlank(supplier)) {
            throw new IllegalArgumentException(String.format(EslNoticeMessage.ESL_DEVICE_SUPPLIER_ERROR, supplier));
        }

        PriceTagDeviceSupplierEnum deviceSupplier = PriceTagDeviceSupplierEnum.get(supplier);
        if (Objects.isNull(deviceSupplier)) {
            throw new IllegalArgumentException(String.format(EslNoticeMessage.ESL_DEVICE_SUPPLIER_ERROR, supplier));
        }
        return deviceSupplier;
    }
}
