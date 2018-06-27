package com.ylzinfo.fj.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 出参配置
 * @author zero
 *
 */
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OutPara {
	/**
	 * 参数索引号，从0开始
	 * @return
	 */
	int index();
	/**
	 * 结构中文说明
	 * @return
	 */
	String label();
}
