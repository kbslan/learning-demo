package com.kbslan.esl.vo.pricetag.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 汉朔基站心跳
 * </p>
 * <p>
 *     {
 *     "status_no": 0,
 *     "type": "AP_STATUS",
 *     "data": [
 *         {
 *             "ap_id": "4",
 *             "ap_management": "false",
 *             "listen_port": "43606",
 *             "online_begin_time": "1688142077204",
 *             "ip": "10.16.244.89",
 *             "channel": "151",
 *             "hb_status": "0",
 *             "version": "6.1.22",
 *             "work_mode": "standby",
 *             "mac": "98:6D:35:70:8D:5C",
 *             "sequence": "3",
 *             "netmask": "NONE",
 *             "last_work_time": "1689754800688",
 *             "sn": "907019001593878370",
 *             "user": "112",
 *             "gateway": "NONE",
 *             "status": "offline",
 *             "desc": "NONE"
 *         },
 *         {
 *             "ap_id": "5",
 *             "ap_management": "false",
 *             "listen_port": "43536",
 *             "online_begin_time": "1688142076108",
 *             "ip": "10.16.244.89",
 *             "channel": "165",
 *             "hb_status": "24",
 *             "version": "3.3.0",
 *             "work_mode": "standby",
 *             "mac": "DC:07:C1:01:89:41",
 *             "sequence": "2",
 *             "netmask": "NONE",
 *             "last_work_time": "1689707010622",
 *             "sn": "HS90308102E0000290",
 *             "user": "112",
 *             "gateway": "NONE",
 *             "status": "offline",
 *             "desc": "NONE"
 *         },
 *         {
 *             "ap_id": "2",
 *             "ap_management": "false",
 *             "listen_port": "37266",
 *             "online_begin_time": "1693239929414",
 *             "ip": "10.16.244.89",
 *             "channel": "159",
 *             "hb_status": "16",
 *             "version": "3.3.0",
 *             "work_mode": "standby",
 *             "mac": "DC:07:C1:02:FF:8B",
 *             "sequence": "1",
 *             "netmask": "NONE",
 *             "last_work_time": "1692360738602",
 *             "sn": "903804001593837968",
 *             "user": "112",
 *             "gateway": "NONE",
 *             "status": "online",
 *             "desc": "NONE"
 *         },
 *         {
 *             "ap_id": "1",
 *             "ap_management": "false",
 *             "listen_port": "0",
 *             "online_begin_time": "",
 *             "ip": "10.12.211.170",
 *             "channel": "NONE",
 *             "hb_status": "0",
 *             "version": "6.1.16",
 *             "work_mode": "standby",
 *             "mac": "98:6D:35:70:8B:44",
 *             "sequence": "2",
 *             "netmask": "NONE",
 *             "last_work_time": "",
 *             "sn": "907018001593877834",
 *             "user": "9",
 *             "gateway": "NONE",
 *             "status": "offline",
 *             "desc": "NONE"
 *         },
 *         {
 *             "ap_id": "3",
 *             "ap_management": "false",
 *             "listen_port": "0",
 *             "online_begin_time": "",
 *             "ip": "10.12.211.171",
 *             "channel": "165",
 *             "hb_status": "0",
 *             "version": "6.1.16",
 *             "work_mode": "standby",
 *             "mac": "98:6D:35:70:8A:FB",
 *             "sequence": "1",
 *             "netmask": "NONE",
 *             "last_work_time": "",
 *             "sn": "907018001593877761",
 *             "user": "17337",
 *             "gateway": "NONE",
 *             "status": "offline",
 *             "desc": "NONE"
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
public class PassiveAPHeartbeat {

    /**
     * 基站ID。
     */
    @JSONField(name = "ap_id")
    private Integer apId;
    /**
     * user标识。
     */
    private String user;

    /**
     * 该基站在此user中的序号， 用于基站屏幕显示。
     */
    private Integer sequence;
    /**
     * 基站状态。 online（在线） 、 offline（掉线） 。
     */
    private String status;
    /**
     * 监听ap的端口。
     */
    @JSONField(name = "listen_port")
    private String listenPort;

    /**
     * ap上线时间
     */
    @JSONField(name = "online_begin_time")
    private String onlineBeginTime;
    /**
     * ap版本号
     */
    private String version;
    /**
     * ap当前状态， transmitting:工作中； standby:闲置中
     */
    @JSONField(name = "work_mode")
    private String workMode;

    /**
     * ap的mac地址
     */
    private String mac;
    /**
     * 最后一次工作时间
     */
    @JSONField(name = "last_work_time")
    private String lastWorkTime;
    /**
     * 子网掩码
     */
    private String netmask;

    /**
     * ap的ip地址
     */
    private String ip;
    /**
     * ap的序列号
     */
    private String sn;
    /**
     * ap的网关
     */
    private String gateway;
    /**
     * ap的描述信息
     */
    private String desc;


}
