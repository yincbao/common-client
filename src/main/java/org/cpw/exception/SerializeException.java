package org.cpw.exception;

import org.cpw.exception.code.ExceptionCodeEnum;

public class SerializeException extends CacheException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SerializeException(ExceptionCodeEnum exceptionCodeEnum) {
		super(exceptionCodeEnum);
	}
	
	public SerializeException(ExceptionCodeEnum exceptionCodeEnum,Throwable throwable) {
		super(exceptionCodeEnum,throwable);
	}

}
