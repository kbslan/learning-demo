package com.kbslan.domain.annotation;

import java.lang.annotation.*;

/**
 * 审计日志上报需要国际化的字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18NField {

	/**
	 * 需要进行国际化的keycode（唯一标识）
	 */
	String code() default "";

}
