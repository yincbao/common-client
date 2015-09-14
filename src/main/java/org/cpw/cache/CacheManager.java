package org.cpw.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.cache.local.LocalCacheClient;
import org.cpw.cache.memCached.XMemcachedCache;
import org.cpw.cache.redis.r3cluster.RedisClusterCache;

public final class CacheManager {

	private static final Log logger = LogFactory.getLog(CacheManager.class);
	
	
	/**
	 * fit for redis 3.* or plus cluster version , no multi nor transaction opts supports till now.
	 * return an instance of RedisClusterCache
	 * if null return local cache client: @see CacheManager.getLocalCache 
	 * @return
	 */
	public static ICache getRedis3ClusterCache(){
		return RedisClusterCache.getInstance()==null?getLocalCache():RedisClusterCache.getInstance();
	}
	
	/**
	 * fit for redis single node, support redis multi operation and transaction opts
	 * * return an instance of RedisClusterCache
	 * * if null return local cache client: @see CacheManager.getLocalCache 
	 * @return
	 */
	public static  ICache getRedisCache(){
		return getLocalCache();
	}
	
	/**
	 * * return an instance of XMemcachedCache
	 * * if null return local cache client: @see CacheManager.getLocalCache 
	 * @return
	 */
	public static ICache getMemCachedCache(){
		return XMemcachedCache.getInstance()==null?getLocalCache():XMemcachedCache.getInstance();
	}
	
	/**
	 * NOTE:using unsafe to allocation off-heap memory size in jdk 1.7 or later is impossible.
	 * so store to much data in a local cache may cause a OOM exception.
	 * and local memory won't perform well in a cluster environment.
	 *  return an instance of LocalCacheClient
	 * @return
	 */
	public static ICache getLocalCache(){
		logger.warn("WARIN: you are using local cache, which is using in-heap memeory. This may allocate an OOM Exception in mass data");
		return LocalCacheClient.getInstance();
	}
	
}
