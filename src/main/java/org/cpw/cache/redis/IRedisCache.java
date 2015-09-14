package org.cpw.cache.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cpw.cache.ICache;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

public interface IRedisCache extends ICache {

	// redis-common
		public boolean expire (Object key,long seconds);
		
		public boolean expireAt (Object key,long expireTime);
		
		public void append(String key, String append);
		
		public String type(Object key);
		
		public long ttl(Object key);
		
		public Map<String,String>  getWholeHashtable(String tableName);
		
		public <T> T getFromHashtable(Object tableName,Object key,Class<T> objectType);
		
		public void deleteFromHashtable(Object tableName,Object... keys);
		
		public <T> List<T> getValFromHashtable(Object tableName,Class<T> objectType);
		
		public <T> Set<T> getKeySetFromHashtable(Object tableName,Class<T> objectType);
		
		public boolean appendToList(Object listName,boolean createIfNoSuchList,Object...elements);
		
		public boolean appendToList(Object listName,LIST_POSITION position,Object pivot,Object value);
		
		public  String getListValByIndex(Object listName,long index);
		
		public <T> T getListValByIndex(Object listName,long index,Class<T> objectType);
		
		public <T> T popFromList(Object listName,Class<T> objectType);
		
		public void trimList(Object listName, long start , long end);
		
		public boolean remFromList(Object listName,long start,Object value);
		
		public long getLenOfList(Object listName);
		
		public  <T> List<T> getList(Object listName, long start, long stop,Class<T> objectType);
		//redis-comm end
}
