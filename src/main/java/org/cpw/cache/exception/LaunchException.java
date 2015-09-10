package org.cpw.cache.exception;

import org.cpw.cache.exception.code.ExceptionCodeEnum;

public class LaunchException extends CacheException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LaunchException(ExceptionCodeEnum exceptionCodeEnum) {
		super(exceptionCodeEnum);
	}
	
	public LaunchException(ExceptionCodeEnum exceptionCodeEnum,Throwable throwable) {
		super(exceptionCodeEnum,throwable);
	}

}
