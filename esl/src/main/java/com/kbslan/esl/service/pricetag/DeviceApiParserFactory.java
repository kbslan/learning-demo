package com.kbslan.esl.service.pricetag;

import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.esl.service.pricetag.DeviceApiParser;
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
 * @since 2023/8/31 22:22
 */
@Service
public class DeviceApiParserFactory {
    private static final EnumMap<PriceTagDeviceSupplierEnum, DeviceApiParser> PARSERS = new EnumMap<>(PriceTagDeviceSupplierEnum.class);

    public DeviceApiParserFactory(ObjectProvider<List<DeviceApiParser>> objectProvider) {
        for (final DeviceApiParser parser : Objects.requireNonNull(objectProvider.getIfAvailable())) {
            PARSERS.put(Objects.requireNonNull(parser.deviceSupplier()), parser);
        }
    }

    public DeviceApiParser get(PriceTagDeviceSupplierEnum deviceSupplierEnum) {
        return PARSERS.get(deviceSupplierEnum);
    }

    public DeviceApiParser create(PriceTagDeviceSupplierEnum deviceSupplierEnum) {
        DeviceApiParser parser;
        if (Objects.requireNonNull(deviceSupplierEnum) == PriceTagDeviceSupplierEnum.HAN_SHOW) {
            parser = this.get(deviceSupplierEnum);
        } else {
            throw new UnsupportedOperationException(EslNoticeMessage.DEVICE_API_PARSER_NOT_FOUNT);
        }
        return parser;
    }
}
