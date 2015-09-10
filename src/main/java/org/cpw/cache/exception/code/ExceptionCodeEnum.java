package org.cpw.cache.exception.code;


public enum ExceptionCodeEnum {
	deserializeExp("10000","deserialize exception"),
	getCacheProxy("10001","get cache proxy exception"),
	
	configFileNotFound("2000","a config file named \"dst-caches.properties\" should be configured."),
	numberFormatException("2001","numberFormatException"),
	
	redis3ClusterNoValidHostPort("3000","a configure item 'redis.cluster.host.list' in dst-caches.proeprties is not valid, should be like '127.0.0.1:6379,127.0.0.1:6389'  "),
	
	linsertCMD("5000","none of parameters could be null or empty. cmd should be :[linsert listName before/after pivot value]"),
	lindexCMD("5001","none of parameters could be null or empty. cmd should be :[lindex listName index]"), 
	rpopCMD("5002","none of parameters could be null or empty, cmd shoud be :[rpop listName]"),
	ltrimCMD("5003","none of parameters could be null or empty, cmd shoud be :[ltrim listName startindex stopindex]"),
	lremCMD("5004","none of parameters could be null or empty, cmd shoud be :[lrem listName startindex value]"),
	blpopCMD("5005","none of parameters could be null or empty, cmd shoud be :[blpop listName  value]"),
	llenCMD("5006","none of parameters could be null or empty, cmd shoud be :[llen listName ]"), 
	lrangeCMD("5007","none of parameters could be null or empty, cmd shoud be :[lrange listName start end]"),
	;
	
	private String expCode;
	private String expMsg;
	public String getExpCode() {
		return expCode;
	}
	public void setExpCode(String expCode) {
		this.expCode = expCode;
	}
	public String getExpMsg() {
		return expMsg;
	}
	public void setExpMsg(String expMsg) {
		this.expMsg = expMsg;
	}
	
	private ExceptionCodeEnum(String expCode, String expMsg) {
		this.expCode = expCode;
		this.expMsg = expMsg;
	}
	
	public static ExceptionCodeEnum fromName(String name) {
		return valueOf(name);
	}

	public static ExceptionCodeEnum fromElem(String code) {
		ExceptionCodeEnum[] types = ExceptionCodeEnum.values();
		for (int x = 0; x < types.length; x++) {
			if (types[x].expCode.equals(code)||types[x].expMsg.equals(code)) {
				return types[x];
			}
		}
		throw new java.lang.IllegalArgumentException(
				"No enum found for the passed code: " + code);
	}
}
