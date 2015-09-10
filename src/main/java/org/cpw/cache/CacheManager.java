package org.cpw.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.cache.redis.r3cluster.RedisClusterCache;

public final class CacheManager {

	private static final Log logger = LogFactory.getLog(CacheManager.class);
	
	public static ICache getRedis3ClusterCache(){
		return RedisClusterCache.getInstance()==null?getLocalCache():RedisClusterCache.getInstance();
	}
	
	public static  ICache getRedisCache(){
		return null;
	}
	
	public static ICache getMemCachedCache(){
		return null;
	}
	
	public static ICache getLocalCache(){
		logger.warn("WARIN: you are using local cache, which is using in-heap memeory. This may allocate an OOM Exception in mass data");
		return null;
	}
	
}
