package com.kbslan.domain.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kbslan.domain.entity.ApMonitorEntity;
import com.kbslan.domain.enums.PriceTagDeviceSupplierEnum;
import com.kbslan.domain.enums.YNEnum;
import com.kbslan.domain.model.PriceTagScreenSizeTotal;
import com.kbslan.domain.service.ApMonitorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/12/28 21:49
 */
@RestController
@RequestMapping("/ap-monitor")
public class ApMonitorController {
    @Resource
    private ApMonitorService apMonitorService;
    private static final Long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_NAME = "ADMIN";

    @GetMapping("/test")
    public String test() {
        Long vendorId = 1L;
        Long storeId = 112L;
        // 构建监控数据
        ApMonitorEntity monitorData = new ApMonitorEntity();
        monitorData.setVendorId(vendorId);
        monitorData.setStoreId(storeId);
        monitorData.setDeviceSupplier(PriceTagDeviceSupplierEnum.HAN_SHOW.getCode());
        monitorData.setEslServerStatus(YNEnum.YES.getCode());
        monitorData.setApTotal(1L);
        monitorData.setOfflineApTotal(0L);
        monitorData.setPriceTagTotal(2L);
        monitorData.setOfflinePriceTagTotal(1L);
        monitorData.setPushingPriceTotal(0L);
        monitorData.setPushFailedTotal(0L);
        Map<String, PriceTagScreenSizeTotal> priceTagCountInfo = new HashMap<>(6);
        PriceTagScreenSizeTotal p213 = new PriceTagScreenSizeTotal();
        p213.setTotal(213L);
        p213.setOffline(13L);
        priceTagCountInfo.put("213", p213);
        PriceTagScreenSizeTotal p420 = new PriceTagScreenSizeTotal();
        p420.setTotal(420L);
        p420.setOffline(42L);
        priceTagCountInfo.put("420", p420);
        monitorData.setPriceTagCountInfo(priceTagCountInfo);
        // 保存或更新监控数据
        saveMonitorData(vendorId, storeId, PriceTagDeviceSupplierEnum.HAN_SHOW, monitorData);
        System.out.println("test");
        return "OK";
    }

    private void saveMonitorData(Long vendorId, Long storeId, PriceTagDeviceSupplierEnum deviceSupplier, ApMonitorEntity monitor) {
        ApMonitorEntity apMonitor = apMonitorService.getOne(
                Wrappers.lambdaQuery(ApMonitorEntity.class)
                        .eq(ApMonitorEntity::getVendorId, vendorId)
                        .eq(ApMonitorEntity::getStoreId, storeId)
                        .eq(ApMonitorEntity::getDeviceSupplier, deviceSupplier.getCode())
        );
        if (Objects.isNull(apMonitor)) {
            monitor.setCreated(LocalDateTime.now());
            monitor.setCreatorId(DEFAULT_USER_ID);
            monitor.setCreatedUtc(System.currentTimeMillis());
            monitor.setCreatorName(DEFAULT_USER_NAME);
            monitor.setModified(LocalDateTime.now());
            monitor.setModifiedUtc(System.currentTimeMillis());
            monitor.setModifierId(DEFAULT_USER_ID);
            monitor.setModifierName(DEFAULT_USER_NAME);
            apMonitorService.save(monitor);
        } else {
            monitor.setId(apMonitor.getId());
            monitor.setModified(LocalDateTime.now());
            monitor.setModifiedUtc(System.currentTimeMillis());
            monitor.setModifierId(DEFAULT_USER_ID);
            monitor.setModifierName(DEFAULT_USER_NAME);
            apMonitorService.updateById(monitor);
        }
    }
}
