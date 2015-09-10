package org.cpw.cache.redis.r3cluster;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cpw.cache.ICache;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

public interface IRedisCache extends ICache {

	public void set(Object key, Object value, boolean coverOnExist);

	public boolean expire(Object key, long seconds);

	public boolean expireAt(Object key, long expireTime);

	public void append(String key, String append);

	public String type(Object key);

	public long ttl(Object key);

	public void setHashtable(String tableName, String key, String value, boolean coverOnExist);

	public void setHashtable(Object tableName, Object key, Object value, boolean coverOnExist);

	public void setHashtable(String tableName, Map<String, String> dataMap);

	public <K, V> void setHashtable(Object tableName, Map<K, V> dataMap);

	public <T> List<T> getValueListFromHashtable(Object tableName, Class<T> objectType, String... keys);

	public <T> T getFromHashtable(String tableName, String key, Class<T> objectType);

	public <T> T getFromHashtable(Object tableName, Object key, Class<T> objectType);

	public Map<String, String> getWholeHashtable(String tableName);

	public <K, V> Map<K, V> getWholeHashtable(Object tableName, Class<K> mapKeyType, Class<V> mapValueType);

	public void deleteFromHashtable(String tableName, String... keys);

	public void deleteFromHashtable(Object tableName, Object... keys);

	public <T> List<T> getValFromHashtable(Object tableName, Class<T> objectType);

	public <T> Set<T> getKeySetFromHashtable(Object tableName, Class<T> objectType);

	public boolean setIntoList(Object listName, boolean createIfNoSuchList, Object... elements);

	public boolean setIntoList(Object listName, LIST_POSITION position, Object pivot, Object value);

	public String getFromList(Object listName, long index);

	public <T> T getFromList(Object listName, long index, Class<T> objectType);

	public List<String> bpopFromList(int timeout, Object key);

	public String popFromList(Object listName);

	public <T> T popFromList(Object listName, Class<T> objectType);

	public void trimList(Object listName, long start, long end);

	public boolean remFromList(Object listName, long start, Object value);

	public long getLenOfList(Object listName);

	public <T> List<T> getList(Object listName, long start, long stop, Class<T> objectType);

}
