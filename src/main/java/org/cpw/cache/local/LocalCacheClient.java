package org.cpw.cache.local;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.serializer.SerializerHelper;

/**
 * 
 * ClassName: LocalCacheClient
 * @description
 * @author yin_changbao
 * @Date   Sep 11, 2015
 *
 */
public class LocalCacheClient implements ILocalCache{
	
	private static Log logger = LogFactory.getLog( LocalCacheClient.class.getName() );
	private static LocalCacheClient instance = new LocalCacheClient();
	private Map<String, Cache> cacheMap=new HashMap<String, Cache>();
	private Maintainer maintainer;
	
	private static final String defaultCache = "DEFAULT_CACHE";
	
	public static LocalCacheClient getInstance() {
		return instance;
	}
	
	private LocalCacheClient() {
		maintainer = new Maintainer(this);
		logger.info("init default local cache....");
		Cache defaultCache = new Cache("DEFAULT_CACHE",30*3600*24,100000);
		cacheMap.put(defaultCache.getCacheName(),defaultCache);
		logger.info("init default local cache done!");
	}
	
	void addCache(Cache cache) {
		logger.info("Adding Cache:" + cache.getCacheName());
		cacheMap.put(cache.getCacheName(), cache);
	}
	
	
	Cache getCache(String name) {
		return cacheMap.get(name);
	}
	
   void maintainAllCache() {
    	logger.info("Removing expired objects from Cache. Interval in seconds:" + maintainer.getPeriodInSeconds());
    	Collection<Cache> caches = cacheMap.values();
    	for(Cache cache : caches) {
    		cache.removeExpiredObjects();
    	}
    }
	
	
	public void set(String cacheGroup,String key,Object value){
		Cache cache = LocalCacheClient.getInstance().getCache(cacheGroup);
		if(cache==null){
			cache = new Cache(cacheGroup);
			LocalCacheClient.getInstance().addCache(cache);
		}
		cache.put(new CacheElement(key,value));
	}
	
	public Object get(String cacheGroup,String key){
		Cache cache = LocalCacheClient.getInstance().getCache(cacheGroup);
		if(cache==null)
			return null;
		else
			return cache.getCacheElementValue(key);
	}
	
	public void deleteCache(String cacheName){
		cacheMap.remove(cacheName);
	}
	
	public void delete(String cacheGroup, String key) {
		Cache cache = cacheMap.get(cacheGroup);
		if(cache!=null)
			cache.rem(key);
		
	}

	public boolean  delete(Object key) {
		Cache cache = cacheMap.get(defaultCache);
		try{
			if(cache!=null)
				cache.rem(SerializerHelper.serializeKey(key));
			else
				return false;
			return false;
		}catch(Exception e){
			
			logger.error(e.getMessage(),e);
			return false;
		}
		
		
	}

	public void set(String key, String value) {
		LocalCacheClient.getInstance().getCache(defaultCache).put(new CacheElement(key,value));
	}
	
	public boolean set(Object key, Object value) {
		try{
			LocalCacheClient.getInstance().getCache(defaultCache).put(new CacheElement(SerializerHelper.serializeKey(key),value));
			return true;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return false;
	}
	public boolean set(Object key, Object value,long expire) {
		try{
			LocalCacheClient.getInstance().getCache(defaultCache).put(new CacheElement(SerializerHelper.serializeKey(key),value,System.currentTimeMillis(),expire));
			return true;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return false;
	}
	
	public Object get(Object key){
		return LocalCacheClient.getInstance().getCache(defaultCache).getCacheElementValue(SerializerHelper.serializeKey(key));
	}
	
	public <T> T get(Object key,Class<T> returnType){
		try{
			return (T) LocalCacheClient.getInstance().getCache(defaultCache).getCacheElementValue(SerializerHelper.serializeKey(key));
		}catch(Exception e){
			try{
				return SerializerHelper.deserializeValue((String)LocalCacheClient.getInstance().getCache(defaultCache).getCacheElementValue(SerializerHelper.serializeKey(key)), returnType);
			}catch(Exception ie){
				logger.error(ie.getMessage(),ie);
			}
			
		}
		return null;
		
	}
	
	
	public boolean save(Object obj) {
		try{
			LocalCacheClient.getInstance().getCache(defaultCache).put(new CacheElement(SerializerHelper.serializeKey(obj),obj));
			return true;
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return false;
	}
}
