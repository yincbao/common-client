package org.cpw.exception;

import org.cpw.exception.code.ExceptionCodeEnum;

public class MemcachedOptException extends CacheException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MemcachedOptException(ExceptionCodeEnum exceptionCodeEnum) {
		super(exceptionCodeEnum);
	}
	
	public MemcachedOptException(ExceptionCodeEnum exceptionCodeEnum,Throwable throwable) {
		super(exceptionCodeEnum,throwable);
	}
}
