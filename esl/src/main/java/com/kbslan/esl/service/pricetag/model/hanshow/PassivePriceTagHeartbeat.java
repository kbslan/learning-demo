package com.kbslan.esl.service.pricetag.model.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <p>
 * 价签心跳数据(url回调)
 * </p>
 * <p>
 *     {
 *     "status_no": 0,
 *     "type": "ESL_HB_STATUS",
 *     "user": "112",
 *     "data": [
 *         {
 *             "error_temperature_check": false,
 *             "firmware_id": 720164,
 *             "rf_power": "58",
 *             "battery": 30,
 *             "type": 0,
 *             "last_hb_time": 1693879858137,
 *             "esl_extended_id": "",
 *             "product_id": 1474446766,
 *             "battery_type": 0,
 *             "reserve": "",
 *             "temperature": 27,
 *             "esl_id": "35-6D-B9-06",
 *             "quick_refresh": false,
 *             "direction": 1,
 *             "new_esl": false,
 *             "ap_id": 2,
 *             "ap_mac": "DC:07:C1:02:FF:8B",
 *             "battery_state_start_time": "2023-06-20T10:24:42.095+00:00",
 *             "set_wor": 16,
 *             "nfc": false,
 *             "battery_status": 1,
 *             "error_temperature": false,
 *             "temperature_gathering": true,
 *             "driver_version": 0,
 *             "user": "112",
 *             "rom_version": 32,
 *             "on_temperature_gathering": false,
 *             "status": 1
 *         },
 *         {
 *             "error_temperature_check": false,
 *             "firmware_id": 344370,
 *             "rf_power": "68",
 *             "battery": 29,
 *             "type": 0,
 *             "last_hb_time": 1693879797542,
 *             "esl_extended_id": "",
 *             "product_id": 1410540096,
 *             "battery_type": 0,
 *             "reserve": "",
 *             "temperature": 0,
 *             "esl_id": "52-C6-41-40",
 *             "quick_refresh": true,
 *             "direction": 0,
 *             "new_esl": false,
 *             "ap_id": 2,
 *             "ap_mac": "DC:07:C1:02:FF:8B",
 *             "battery_state_start_time": "2023-06-20T10:24:42.108+00:00",
 *             "set_wor": 16,
 *             "nfc": false,
 *             "battery_status": 1,
 *             "error_temperature": false,
 *             "temperature_gathering": false,
 *             "driver_version": 0,
 *             "user": "112",
 *             "rom_version": 18,
 *             "on_temperature_gathering": false,
 *             "status": 1
 *         }
 *     ]
 * }
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 11:04
 */
@Getter
@Setter
@ToString
public class PassivePriceTagHeartbeat {

    /**
     * 价签id
     */
    @JSONField(name = "esl_id")
    private String eslId;
    /**
     * 基站id
     */
    @JSONField(name = "ap_id")
    private String apId;

    /**
     * user标识
     */
    private String user;
    /**
     * 价签当前电量值, `31` 指 3.1v。
     */
    private Integer battery;
    /**
     * 价签的ROM版本号。
     */
    @JSONField(name = "rom_version")
    private Integer romVersion;
    /**
     * 价签的心跳版本号。
     */
    @JSONField(name = "firmware_id")
    private Integer firmwareId;
    /**
     * 价签的能量值。
     */
    @JSONField(name = "rf_power")
    private Integer rfPower;
    /**
     * 保留， 当flash被异常删除时， 值为” flash_error”
     */
    private String reserve;
    /**
     * 最近一次心跳的时间
     */
    @JSONField(name = "last_hb_time")
    private Long lastHbTime;
    /**
     * 唤醒周期
     */
    @JSONField(name = "set_wor")
    private Integer setWor;


    /**
     * 基站mac
     */
    @JSONField(name = "ap_mac")
    private String apMac;

}
