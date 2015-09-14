package org.cpw.exception;

import org.cpw.exception.code.ExceptionCodeEnum;

public class ConfigException extends CacheException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigException(ExceptionCodeEnum exceptionCodeEnum) {
		super(exceptionCodeEnum);
	}
	
	public ConfigException(ExceptionCodeEnum exceptionCodeEnum,Throwable throwable) {
		super(exceptionCodeEnum,throwable);
	}

}
