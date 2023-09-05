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
 *     处理流程抽象工厂
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 10:25
 */
@Service
public class PipelineFactory {

    private static final EnumMap<PriceTagDeviceSupplierEnum, StationPipeline> STATION_SERVICES = new EnumMap<>(PriceTagDeviceSupplierEnum.class);
    private static final EnumMap<PriceTagDeviceSupplierEnum, PriceTagPipeline> PRICE_TAG_SERVICES = new EnumMap<>(PriceTagDeviceSupplierEnum.class);

    public PipelineFactory(ObjectProvider<List<StationPipeline>> stationObjectProvider, ObjectProvider<List<PriceTagPipeline>> priceTagObjectProvider) {
        for (final StationPipeline stationPipeline : Objects.requireNonNull(stationObjectProvider.getIfAvailable())) {
            STATION_SERVICES.put(Objects.requireNonNull(stationPipeline.deviceSupplier()), stationPipeline);
        }

        for (final PriceTagPipeline priceTagPipeline : Objects.requireNonNull(priceTagObjectProvider.getIfAvailable())) {
            PRICE_TAG_SERVICES.put(Objects.requireNonNull(priceTagPipeline.deviceSupplier()), priceTagPipeline);
        }
    }


    /**
     * 获取基站处理流程
     *
     * @return 基站处理流程
     */
    public StationPipeline createStationPipeline(PriceTagDeviceSupplierEnum deviceSupplier) {
        StationPipeline service;
        if (Objects.requireNonNull(deviceSupplier) == PriceTagDeviceSupplierEnum.HAN_SHOW) {
            service = STATION_SERVICES.get(deviceSupplier);
        } else {
            throw new UnsupportedOperationException(EslNoticeMessage.ESL_DEVICE_SUPPLIER_ERROR);
        }
        return service;
    }

    /**
     * 获取价格标签处理流程
     *
     * @return 价格标签处理流程
     */
    public PriceTagPipeline createPriceTagPipeline(PriceTagDeviceSupplierEnum deviceSupplier) {
        PriceTagPipeline service;
        if (Objects.requireNonNull(deviceSupplier) == PriceTagDeviceSupplierEnum.HAN_SHOW) {
            service = PRICE_TAG_SERVICES.get(deviceSupplier);
        } else {
            throw new UnsupportedOperationException(EslNoticeMessage.ESL_DEVICE_SUPPLIER_ERROR);
        }
        return service;
    }
}
