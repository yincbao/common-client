package org.cpw.cache.local;

import org.cpw.cache.ICache;


/**
 * 
 * ClassName: ILocalCache
 * @description
 * @author yin_changbao
 * @Date   Sep 11, 2015
 *
 */
public interface ILocalCache extends ICache {

	public void set(String cacheGroup, String key, Object value);

	public Object get(String cacheGroup, String key);

	/**
	 * delete specified cache and contained all cache element
	 * @param cacheName
	 */
	public void deleteCache(String cacheGroup);
	
	/**
	 * delete specified cache element from  specified cache
	 * @param cacheGroup
	 * @param key
	 */
	public void delete(String cacheGroup,String key);
	
	
}
