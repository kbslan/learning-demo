package com.kbslan.esl.vo.hanshow;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * <p>
 * 汉朔厂商Api返回结果
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/4 11:04
 */
@Getter
@Setter
public class HanShowResult<T> {
    public static final String AP_OFFLINE = "201";
    public static final String STATUS_ONLINE = "online";
    public static final String STATUS_OFFLINE = "offline";
    /**
     * 基站心跳
     */
    public static final String AP_STATUS = "AP_STATUS";

    /**
     * 价签心跳
     */
    public static final String ESL_HB_STATUS = "ESL_HB_STATUS";

    /**
     * 当” ESL_UPDATE” 命令传输结束后返回更新结果和相关信息。
     */
    public static final String ESL_UPDATE_ACK = "ESL_UPDATE_ACK";

    /**
     * 错误代码。参考APIv3返回值。
     * 0 成功完成
     * 1 请求已收到并处理。后续通过HTTP POST异步返回处理结果
     * 2 指令被取消
     * 100 不支持的操作
     * 101 参数错误
     * 102 请求的资源不存在
     * 103 系统正在处理全局指令
     * 104 请求的任务一部分成功，一部分失败。
     * 105 不允许的HTTP METHOD
     * 200 无心跳
     * 201 基站离线
     * 202 价签状态异常
     * 203 达到系统最大重试次数，通信失败
     * 204 数据错误
     * 205 模板错误
     * 206 通信超时
     * 207 价签不存在
     * 208 基站不存在
     * 209 user不存在
     * 500 内部处理错误
     */
    @JSONField(name = "status_no")
    private Integer statusNo;
    /**
     * 错误信息。仅在接口调用出错时返回。
     */
    @JSONField(name = "errmsg")
    private String errMsg;

    /**
     * 仅在异步回调和主动上行时表示资源类型
     */
    private String type;

    /**
     * URI中传入的user标识
     * 同步返回中原样返回请求中指定的user，没有则不返回该属性
     * 异步回调中没有指定user时返回default
     */
    private String user;

    /**
     * 具体数据
     */
    private T data;

    /**
     * 接口是否调用成功
     *
     * @return true:成功 false:失败
     */
    public boolean isSuccess() {
        return Objects.equals(statusNo, 0);
    }
}
