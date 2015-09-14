package org.cpw.cache.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * ClassName: Cache
 * @description
 * @author yin_changbao
 * @Date   Sep 8, 2015
 *
 */
public class Cache {
	private static Log logger = LogFactory.getLog( Cache.class.getName() );
	long expireTime = 30*24*3600;
	private String cacheName;
	int cacheSize = 1000;
	private Map<Object, CacheElement> elementMap=new HashMap<Object, CacheElement>();
	
	Cache(String cacheName,long expireTime, int cacheSize){
		this.cacheName = cacheName;
		this.expireTime = expireTime;
		this.cacheSize = cacheSize;
	}
	Cache(String cacheName){
		this.cacheName = cacheName;
	}

	public String getCacheName() {
		return cacheName;
	}

    public long getTimeToLiveInSeconds() {
		return expireTime;
	}

	public void put(CacheElement cacheElement) {
		if(elementMap.size() > cacheSize) {
			logger.info("Not adding element to cache " + this.getCacheName() + " as cache has reached the max capacity");
		}
		else {
			logger.debug("Adding element to Cache: " + this.getCacheName() + ". Key=" + cacheElement.getKey());
			elementMap.put(cacheElement.getKey(), cacheElement);
			long creationTime = System.currentTimeMillis();
			cacheElement.setCreationTime(creationTime);
			cacheElement.setExpiryTime(creationTime + (expireTime * 1000));
		}
	}
	
	public CacheElement get(Object key) {
		return elementMap.get(key);
	}
	
	public boolean rem(Object key){
		return elementMap.remove(key)!=null;
	}

	public Object getCacheElementValue(Object key) {
		CacheElement element =  elementMap.get(key);
		if(element!=null) {
			return element.getValue();
		}
		else {
			return null;
		}
	}
	
	public List<CacheElement> getAllCachedElements() {
		return new ArrayList<CacheElement>(elementMap.values());
	}
	
	protected void removeExpiredObjects() {
		Set<Object> keys = elementMap.keySet();
		List<Object> expiredKeys = new ArrayList<Object>();
		for(Object key:keys) {
			CacheElement cacheElement = elementMap.get(key);
			if(System.currentTimeMillis() > cacheElement.getExpiryTime()) {
				expiredKeys.add(cacheElement.getKey());
			}
		}
		for(Object key : expiredKeys) {
			logger.debug("Removing expired object from cache= " + this.getCacheName() + ". Key=" + key);
			elementMap.remove(key);
		}
	}
}
