package com.kbslan.esl.config.exception;

import com.kbslan.esl.vo.response.DataResponseJson;
import com.kbslan.esl.vo.response.ResponseJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {
	
	/**
	 * 业务异常
	 */
	@ExceptionHandler(value = EslBusinessException.class)
    public <T> DataResponseJson businessException(Exception e) {
		log.error("businessException", e);
		EslBusinessException businessException = (EslBusinessException)e;
        return DataResponseJson.buildFaile(businessException.getErrCode(), businessException.getMessage());
    }


	/**
	 * 对象参数校验异常处理
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = BindException.class)
	@ResponseBody
	public ResponseJson exceptionHandler2(BindException e) {
		log.error("发生异常！", e);
		return DataResponseJson.buildFaile(DataResponseJson.TIPS, e.getBindingResult().getAllErrors().stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(", ")));
	}

	/**
	 * 所有异常
	 * @param e 异常
	 * @return 返回信息
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseJson sqlExceptionHandler(Exception e) {
		log.error("Exception", e);
		return DataResponseJson.buildFaile(DataResponseJson.FAIL, e.getMessage());
	}
}
