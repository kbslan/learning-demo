package com.kbslan.esl.config.exception;

import java.util.HashMap;

/**
 * 业务异常
 *
 */
public class EslBusinessException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String errCode;

	private HashMap<String, Object> extMap;//错误扩展字段

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public HashMap<String, Object> getExtMap() {
		return extMap;
	}

	public void setExtMap(HashMap<String, Object> extMap) {
		this.extMap = extMap;
	}

	public EslBusinessException() {
		super();
	}

	public EslBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EslBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public EslBusinessException(String message) {
		super(message);
	}
	public EslBusinessException(Throwable cause) {
		super(cause);
	}

	public EslBusinessException(String message, String errCode) {
		super(message);
	   	this.errCode = errCode;
	}

	public EslBusinessException(String message, String errCode, HashMap<String, Object> extMap) {
		super(message);
	   	this.errCode = errCode;
	   	this.extMap = extMap;
	}

	public EslBusinessException(String message, HashMap<String, Object> extMap) {
		super(message);
	   	this.extMap = extMap;
	}

}
