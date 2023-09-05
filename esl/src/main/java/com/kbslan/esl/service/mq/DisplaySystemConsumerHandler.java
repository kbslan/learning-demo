package com.kbslan.esl.service.mq;

import com.alibaba.fastjson.JSON;
import com.kbslan.domain.enums.PriceTagBingingSourceEnum;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.esl.service.pricetag.PipelineFactory;
import com.kbslan.esl.service.mq.message.DisplaySystemMessage;
import com.kbslan.esl.vo.request.pricetag.PriceTagRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 陈列系统电子价签MQ消费类
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 17:04
 */
@Slf4j
@Service
public class DisplaySystemConsumerHandler /* extends AbsMessageHandler<DisplaySystemMessage> */ {

    public static final String DISPLAY_MESSAGE_COUNT = "displayMessageCount";
    public static final String DISPLAY_MESSAGE_COUNT_FAILURE = "displayMessageCountFailure";
    public static final Integer BIND = 1;
    public static final Integer UNBIND = 2;
    @Resource
    private PipelineFactory pipelineFactory;


    //    @Override
    protected void onHandle(/*MessageContext context,*/ DisplaySystemMessage message) {

        log.info("陈列系统电子价签MQ, message={}", JSON.toJSONString(message));

        List<DisplaySystemMessage.Data> dataList = message.getData();
        if (CollectionUtils.isEmpty(dataList)) {
            log.warn("陈列系统电子价签MQ data为空");
            return;
        }

        for (DisplaySystemMessage.Data data : dataList) {
            try {
                log.info("陈列系统mq，vendorId={} storeId={} eslId={} 开始电子价签处理逻辑", data.getVenderId(), data.getShopId(), data.getEslId());

                if (Objects.isNull(data.getVenderId()) || Objects.isNull(data.getShopId()) || StringUtils.isBlank(data.getEslId())) {
                    continue;
                }
                Integer opType = message.getOperType();
                PriceTagDeviceSupplierEnum deviceSupplier = PriceTagDeviceSupplierEnum.get(data.getSupplierId());
                if (Objects.isNull(deviceSupplier)) {
                    log.error("陈列系统mq deviceSupplier不合法:{}", data.getSupplierId());
                    continue;
                }
                PriceTagRequest request = new PriceTagRequest();
                request.setTrace(UUID.randomUUID().toString().replace("-", ""));
                request.setVendorId(data.getVenderId());
                request.setStoreId(data.getShopId());
                request.setDeviceSupplier(deviceSupplier);
                request.setUserId(-1L);
                request.setUserName("displaySystem");
                request.setBingingSource(PriceTagBingingSourceEnum.DISPLAY_MQ);
                request.setNeedPush(true);
                request.setOriginPriceTagId(data.getEslId());
                request.setSkuIds(Collections.singletonList(data.getSkuId()));

                if (Objects.equals(opType, BIND)) {
                    //绑定价签
                    pipelineFactory.createPriceTagPipeline(deviceSupplier).bind(request);
                } else if (Objects.equals(opType, UNBIND)) {
                    //解绑价签
                    pipelineFactory.createPriceTagPipeline(deviceSupplier).unbind(request);
                } else {
                    log.error("陈列系统MQ消息vendorId={} storeId={} operType不合法:[{}]", data.getVenderId(), data.getShopId(), opType);
                }
            } catch (Exception e) {
                log.error("处理异常MQ异常: ", e);
//                AlarmService.alarm("DisplaySystemConsumerHandler#onHandler", "处理陈列MQ异常：message=" + JSON.toJSONString(message) + ",error=" + e.getMessage());
            }

        }
    }


}
