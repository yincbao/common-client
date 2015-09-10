package org.cpw.cache.exception;

import org.cpw.cache.exception.code.ExceptionCodeEnum;

public class DeserializeException extends CacheException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeserializeException(ExceptionCodeEnum exceptionCodeEnum) {
		super(exceptionCodeEnum);
	}
	
	public DeserializeException(ExceptionCodeEnum exceptionCodeEnum,Throwable throwable) {
		super(exceptionCodeEnum,throwable);
	}

}
