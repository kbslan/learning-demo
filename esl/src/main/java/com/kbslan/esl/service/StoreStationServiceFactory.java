package com.kbslan.esl.service;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.esl.service.notice.EslNoticeMessage;
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
public class StoreStationServiceFactory {

    private static final EnumMap<PriceTagDeviceSupplierEnum, StoreStationService> PARSERS = new EnumMap<>(PriceTagDeviceSupplierEnum.class);

    public StoreStationServiceFactory(ObjectProvider<List<StoreStationService>> objectProvider) {
        for (final StoreStationService parser : Objects.requireNonNull(objectProvider.getIfAvailable())) {
            PARSERS.put(Objects.requireNonNull(parser.deviceSupplier()), parser);
        }
    }

    public StoreStationService get(PriceTagDeviceSupplierEnum deviceSupplierEnum) {
        return PARSERS.get(deviceSupplierEnum);
    }

    public StoreStationService create(PriceTagDeviceSupplierEnum deviceSupplierEnum) {
        StoreStationService parser;
        if (Objects.requireNonNull(deviceSupplierEnum) == PriceTagDeviceSupplierEnum.HAN_SHOW) {
            parser = this.get(deviceSupplierEnum);
        } else {
            throw new UnsupportedOperationException(EslNoticeMessage.DEVICE_API_PARSER_NOT_FOUNT);
        }
        return parser;
    }
}
