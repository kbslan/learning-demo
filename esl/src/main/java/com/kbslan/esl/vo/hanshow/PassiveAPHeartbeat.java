package com.kbslan.esl.vo.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 汉朔基站心跳
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
