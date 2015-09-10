package org.cpw.cache.exception;

import org.cpw.cache.exception.code.ExceptionCodeEnum;

public class Redis3CmdException extends CacheException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Redis3CmdException(ExceptionCodeEnum exceptionCodeEnum) {
		super(exceptionCodeEnum);
	}
	
	public Redis3CmdException(ExceptionCodeEnum exceptionCodeEnum,Throwable throwable) {
		super(exceptionCodeEnum,throwable);
	}
}
