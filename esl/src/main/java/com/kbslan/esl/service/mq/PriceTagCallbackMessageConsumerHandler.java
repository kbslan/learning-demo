package com.kbslan.esl.service.mq;

import com.kbslan.esl.service.mq.message.PriceTagCallbackMessage;
import com.kbslan.esl.service.pricetag.PriceTagServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <p>
 *     电子价签刷新回调消息消费者处理器
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/5 18:38
 */
@Slf4j
@Service
public class PriceTagCallbackMessageConsumerHandler /* extends AbsMessageHandler<PriceTagCallbackMessage> */{
    @Resource
    private PriceTagServiceFactory priceTagServiceFactory;

    //    @Override
    public void onHandle(/*MessageContext context,*/ PriceTagCallbackMessage message) {
        if (Objects.isNull(message) || Objects.isNull(message.getDeviceSupplier()) || StringUtils.isBlank(message.getBody())) {
            log.warn("电子价签刷新回调消息为空");
            return;
        }
        try {
            priceTagServiceFactory.create(message.getDeviceSupplier()).callback(message.getBody());
        } catch (Exception e) {
            log.error("电子价签刷新回调消息处理异常", e);
        }

    }
}
