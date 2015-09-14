package org.cpw.exception;

import org.cpw.exception.code.ExceptionCodeEnum;

public class CacheException  extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public CacheException(ExceptionCodeEnum exceptionCodeEnum) {
		super(exceptionCodeEnum.getExpMsg());
		this.errorCode = exceptionCodeEnum.getExpCode();
	}
	
	public CacheException(ExceptionCodeEnum exceptionCodeEnum,Throwable throwable) {
		super(exceptionCodeEnum.getExpMsg(),throwable);
		this.errorCode = exceptionCodeEnum.getExpCode();
	}

}
