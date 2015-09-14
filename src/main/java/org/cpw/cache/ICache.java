package org.cpw.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

/**
 * 
 * ClassName: ICache
 * 
 * @description
 * @author paul.yin
 * @Date Sep 8, 2015
 *
 */
public interface ICache {

	// common and base actions
	public boolean set(Object key,Object value);
	
	public boolean set(Object key,Object value,long expTime);
	
	public Object get(Object key);
	
	public <T> T get(Object key,Class<T> returnType);
	
	public boolean delete(Object key);
	
	public boolean save(Object obj);
	//common end
	
}
