package com.kbslan.esl.service;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.esl.vo.response.notice.EslNoticeMessage;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 16:50
 */
@Service
public class PriceTagServiceFactory {

    private static final EnumMap<PriceTagDeviceSupplierEnum, PriceTagService> SERVICES = new EnumMap<>(PriceTagDeviceSupplierEnum.class);

    public PriceTagServiceFactory(ObjectProvider<List<PriceTagService>> objectProvider) {
        for (final PriceTagService parser : Objects.requireNonNull(objectProvider.getIfAvailable())) {
            SERVICES.put(Objects.requireNonNull(parser.deviceSupplier()), parser);
        }
    }

    public PriceTagService get(PriceTagDeviceSupplierEnum deviceSupplierEnum) {
        return SERVICES.get(deviceSupplierEnum);
    }

    public PriceTagService create(PriceTagDeviceSupplierEnum deviceSupplierEnum) {
        PriceTagService service;
        if (Objects.requireNonNull(deviceSupplierEnum) == PriceTagDeviceSupplierEnum.HAN_SHOW) {
            service = this.get(deviceSupplierEnum);
        } else {
            throw new UnsupportedOperationException(EslNoticeMessage.DEVICE_API_PARSER_NOT_FOUNT);
        }
        return service;
    }
}
