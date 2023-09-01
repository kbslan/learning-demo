package com.kbslan.esl.vo.response;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.kbslan.esl.utils.MonitorUtils;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Getter
@Setter
public class DataResponseJson implements ResponseJson {

	private static SerializeConfig mapping = new SerializeConfig();
	static {
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
	}

	/** 当前服务器时间**/
	private String serverTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	/**
	 * 处理请求服务器实例ip
	 */
	private String ip = MonitorUtils.getJvmInstanceCode();

	private Boolean success;
	private String code;
    private String message;
    private Object data = new HashMap<>();

    /** 总数 **/
	private Long total;
	/** 当前页数 **/
	private Integer pageNum;
	/** 每页条数 **/
	private Integer pageSize;
	/** 总页数 **/
	private Integer totalPage;

    public static final String SUCCESS = "0000";
    public static final String MESSAGE = "success";
    public static final String FAIL = "1000";//报错
    public static final String TIPS = "601";//提示信息


	/**
	 * 成功的返回方法
	 */
	public static DataResponseJson ok(){
		return new DataResponseJson(new JSONObject());
	}

	/**
	 * 错误的返回
	 */
	public static DataResponseJson error(String message) {
		return buildFaile(FAIL, message);
	}

	/**
	 * 错误返回
	 */
	public static DataResponseJson error(String code, String message) {
		return buildFaile(code, message);
	}

	/**
	 * 成功的返回方法
	 */
	public static DataResponseJson ok(Object data){
		return new DataResponseJson(data);
	}

	public DataResponseJson() {
		this.code = SUCCESS;
	}

	public DataResponseJson(Object data) {
		this.setData(data);
		this.code = SUCCESS;
		this.message = MESSAGE;
		this.success = true;
	}

	public DataResponseJson(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public DataResponseJson(String code, String message, Object data) {
		super();
		this.setData(data);
		this.code = code;
		this.message = message;
	}

	public DataResponseJson(String code) {
		super();
		this.code = code;
	}

	public static DataResponseJson buildFaile(String code, String message) {
		DataResponseJson rest = new DataResponseJson();
        rest.setCode(code);
        rest.setMessage(message);
        rest.setData(new HashMap<>());
        rest.setSuccess(false);
        return rest;
    }

	public static DataResponseJson buildFaile(String code, String message, Object data) {
		DataResponseJson rest = new DataResponseJson();
        rest.setCode(code);
        rest.setMessage(message);
        rest.setData(data);
		rest.setSuccess(false);
        return rest;
    }
}
