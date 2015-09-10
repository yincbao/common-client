package org.cpw.cache.redis.r3cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.cache.exception.DeserializeException;
import org.cpw.cache.exception.LaunchException;
import org.cpw.cache.exception.Redis3CmdException;
import org.cpw.cache.exception.code.ExceptionCodeEnum;
import org.cpw.cache.redis.r3cluster.serializer.RedisSerializerHelper;
import org.cpw.cache.util.DstCacheConfig;
import org.cpw.cache.util.StringUtil;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;


public final class RedisClusterCache implements IRedisCache{
	
	private static final Log logger = LogFactory.getLog(RedisClusterCache.class);
	private static JedisCluster jc  ;
	
    private static class SingletonHolder {
    	private static final RedisClusterCache INSTANCE = new RedisClusterCache();
    }
    public static final RedisClusterCache getInstance() {
    	return SingletonHolder.INSTANCE;
    }
	private RedisClusterCache(){
		logger.info("launching redis3cluster client...");
		try{
			String listStr = DstCacheConfig.getProperty("redis.cluster.host.list");
			logger.debug(" confiured nodes : "+listStr);
			String[] hosts = listStr.split(",");
			if(hosts==null||hosts.length<1)
				throw new LaunchException(ExceptionCodeEnum.redis3ClusterNoValidHostPort);
			Set<HostAndPort> hostPort = new HashSet<HostAndPort>();
			for(String host:hosts){
				try{
					String[] hostIn = host.split(":");
					hostPort.add(new HostAndPort(hostIn[0],Integer.parseInt(hostIn[1])));
				}catch(Exception e){
					//try next node.
				}
			}
			JedisPoolConfig pool = new JedisPoolConfig();
			pool.setMaxTotal(DstCacheConfig.getIntProperty("pool.maxTotal",8));
			pool.setMaxIdle(DstCacheConfig.getIntProperty("pool.maxIdle",8));
			pool.setMinIdle(DstCacheConfig.getIntProperty("pool.minIdle",0));
			jc = new  JedisCluster(hostPort,DstCacheConfig.getIntProperty("redis.connectionTimeout",2000),DstCacheConfig.getIntProperty("redis.cluster.soTimeout",2000),DstCacheConfig.getIntProperty("redis.cluster.maxRedirections",5),pool);
			logger.info("redis3cluster client launch done.");
		}catch(Exception e){
			logger.error(e, e);
		}
		
	
	}
	
	
	public void set(Object key, Object value,boolean coverOnExist){
		if(key==null)
			return;
		set(RedisSerializerHelper.serializeKey(key),RedisSerializerHelper.serializeValue(value),coverOnExist);
	}
	
	public void set(String key , String value, boolean coverOnExist){
		logger.debug("key :"+ key+ " | value:"+value+" | coverOnExist:"+coverOnExist);
		if(StringUtil.isEmpty(key))
			return;
		if(coverOnExist)
			jc.set(key, value);
		else
			jc.setnx(key, value);
	}
	
	public void set(String key , String value){
		set(key,value,true);
	}
	
	public void set(Object key, Object value){
		set(key,value,true);
	}
	
	public void set(String key , String value,long second){
		if(StringUtil.isEmpty(key))
			return;
		logger.debug("opt:setex | key :"+ key+ " | value:"+value+" | second:"+second);
		jc.setex(key, (int)second/1000, value);
	}
	
	public void set(Object key , Object value,long second){
		if(key==null)
			return;
		
		set(RedisSerializerHelper.serializeKey(key),RedisSerializerHelper.serializeValue(value) ,second);
	}
	
	
	/**
	 * redis expire
	 * @param key
	 * @return
	 */
	public boolean expire (Object key,long seconds){
		return jc.pexpire(RedisSerializerHelper.serializeKey(key), seconds)==1;
	}
	
	/**
	 * redis pexpireAt
	 * @param key
	 * @return
	 */
	public boolean expireAt (Object key,long expireTime){
		return jc.expireAt(RedisSerializerHelper.serializeKey(key),expireTime)==1;
	}
	/**
	 * redis append
	 * @param key
	 * @return
	 */
	public void append(String key, String append){
		jc.append(key, append);
	}
	
	/**
	 * redis del
	 * @param key
	 * @return
	 */
	public boolean delete(Object key ){
		if(key==null)
			return false;
		return jc.del(RedisSerializerHelper.serializeKey(key)).longValue()>0;
	}
	
	/**
	 * redus type
	 * @param key
	 * @return
	 */
	public String type(Object key){
		return jc.type(RedisSerializerHelper.serializeKey(key));
	}
	
	/**
	 * redis get
	 * @param key
	 * @param objectType
	 * @return
	 */
	public <T> T get(String key,Class<T> objectType){
		try{
			String result = jc.get(key);
			logger.debug("opt: get | key: "+key+" | value: "+result);
			return RedisSerializerHelper.deserializeValue(result,objectType);
		}catch(DeserializeException e){
			logger.error(e.getMessage(), e);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
		
	}
	
	/**
	 * redis get
	 * @param key
	 * @param objectType
	 * @return
	 */
	public  <T> T get(Object key,Class<T> objectType){
		try{
			return get(RedisSerializerHelper.serializeKey(key),objectType);
		}catch(Exception e ){
			logger.error(e.getMessage(), e);
		}
		return null;
		
	}
	
	/**
	 * redis get
	 * @param key
	 * @return
	 */
	public String get(String key){
		return jc.get(key);
	}
	
	
	/**
	 * redis ttl
	 * @param key
	 * @return
	 */
	public long ttl(Object key){
		return jc.ttl(RedisSerializerHelper.serializeKey(key));
	}
	
	
	/**
	 * redis hset
	 * @param tableName
	 * @param key
	 * @param value
	 */
	public void setHashtable(String tableName,String key, String value,boolean coverOnExist){
		if(StringUtil.isEmpty(tableName)||StringUtil.isEmpty(key))
			return;
		if(coverOnExist)
			jc.hset(tableName, key, value);
		else
			jc.hsetnx(tableName, key, value);
	}
	
	/**
	 * redis hset
	 * @param tableName
	 * @param key
	 * @param value
	 */
	public void setHashtable(Object tableName,Object key, Object value,boolean coverOnExist){
		if(tableName==null||key==null)
			return;
		
		setHashtable(RedisSerializerHelper.serializeKey(tableName), RedisSerializerHelper.serializeKey(key), RedisSerializerHelper.serializeValue(value),coverOnExist);
	}
	
	/**
	 * redis hmset
	 * @param tableName
	 * @param dataMap
	 */
	public void setHashtable(String tableName, Map<String,String> dataMap){
		jc.hmset(tableName, dataMap);
	}
	
	/**
	 * redis hmset
	 * @param tableName
	 * @param dataMap
	 */
	public <K, V> void setHashtable(Object tableName, Map<K,V> dataMap){
		if(tableName==null||dataMap==null||dataMap.isEmpty())
			return;
		Map<String,String> sMap = new HashMap<String,String>();
		for(K key:dataMap.keySet()){
			V value = dataMap.get(key);
			try{
				sMap.put(RedisSerializerHelper.serializeKey(key), RedisSerializerHelper.serializeValue(value));
			}catch(Exception e){
				logger.error("class not cast ",e);
			}
			
		}
		setHashtable(RedisSerializerHelper.serializeKey(tableName), sMap);
	}
	
	/**
	 * redis hmget
	 * @param tableName
	 * @param objectType
	 * @param keys
	 * @return
	 */
	public <T> List<T> getValueListFromHashtable(Object tableName,Class<T> objectType,String...keys){
		
		if(tableName==null)
			return null;
		
		List<String> list = jc.hmget(RedisSerializerHelper.serializeKey(tableName), keys);
		List<T> resultList = new ArrayList<T>();
		for(String value:list){
			try {
				resultList.add(RedisSerializerHelper.deserializeValue(value,objectType));
			} catch(DeserializeException e){
				logger.error(e.getMessage(), e);
			}
		}
		return resultList;
	}
	/**
	 * redis hget
	 * @param tableName
	 * @param key
	 * @param objectType
	 * @return
	 */
	public <T> T getFromHashtable(String tableName,String key,Class<T> objectType){
		if(StringUtil.isEmpty(tableName)){
			return null; 
		}
		String result = jc.hget(tableName, key);
		logger.debug("opt: get | key: "+key+" | value: "+result);
		try {
			return RedisSerializerHelper.deserializeValue(result,objectType);
		} catch (DeserializeException e) {
			logger.error(e.getMessage(),e);
		}
		return null; 
	}
	
	/**
	 * redis hget
	 * @param tableName
	 * @param key
	 * @param objectType
	 * @return
	 */
	public <T> T getFromHashtable(Object tableName,Object key,Class<T> objectType){
		if(tableName==null){
			return null; 
		}
		return getFromHashtable(RedisSerializerHelper.serializeKey(tableName),RedisSerializerHelper.serializeKey(key),objectType);
	}
	
	
	/**
	 * redis hgetall
	 * @param tableName
	 * @return
	 */
	public Map<String,String>  getWholeHashtable(String tableName){
		if(StringUtil.isEmpty(tableName)){
			return null; 
		}
		return jc.hgetAll(tableName);
	}
	
	/**
	 * redis hgetall
	 * @param tableName
	 * @param mapKeyType
	 * @param mapValueType
	 * @return
	 */
	public <K, V> Map<K, V> getWholeHashtable(Object tableName,Class<K> mapKeyType,Class<V> mapValueType){
		if(tableName==null)
			return null;
		return RedisSerializerHelper.elStringTranster(jc.hgetAll(RedisSerializerHelper.serializeKey(tableName)), mapKeyType, mapValueType);
	}
	
	/**
	 * redis hdel
	 * @param tableName
	 * @param keys
	 */
	public void deleteFromHashtable(String tableName,String... keys){
		if(StringUtil.isEmpty(tableName)||keys==null||keys.length<1)
			return;
		jc.hdel(tableName, keys);
	}
	
	/**
	 * redus hdel
	 * @param tableName
	 * @param keys
	 */
	public void deleteFromHashtable(Object tableName,Object... keys){
		if(tableName==null||keys==null||keys.length<1)
			return;
		String[] redisKeys = new String[keys.length];
		for(int i = 0;i<keys.length;i++){
			redisKeys[i] = RedisSerializerHelper.serializeKey(keys[i]);
		}
		deleteFromHashtable(RedisSerializerHelper.serializeKey(tableName),redisKeys);
	}
	
	/**
	 *redis hvals
	 *
	 */
	public <T> List<T> getValFromHashtable(Object tableName,Class<T> objectType){
		
		List<String> metaData = jc.hvals(RedisSerializerHelper.serializeKey(tableName));
		return RedisSerializerHelper.elStringTranster(metaData,objectType);
	}
	/**
	 * redis hkeys
	 * @param tableName
	 * @param objectType
	 * @return
	 */
	public <T> Set<T> getKeySetFromHashtable(Object tableName,Class<T> objectType){
		Set<String> metaData = jc.hkeys(RedisSerializerHelper.serializeKey(tableName));
		return RedisSerializerHelper.elStringTranster(metaData,objectType);
	}
	
	/**
	 * redis list put latest data into head
	 * redis lpush/lpushx [key] [element][element]... 
	 * @param listName
	 * @param elements
	 * @param createIfNoSuchList true will create a new list when list not exist
	 * @return
	 */
	public boolean setIntoList(Object listName,boolean createIfNoSuchList,Object...elements){
		if(listName==null||elements==null||elements.length<1)
			return false;
		if(createIfNoSuchList)
			return jc.lpush(RedisSerializerHelper.serializeKey(listName), RedisSerializerHelper.elStringTranster(elements))>-1;
		else
			return jc.lpushx(RedisSerializerHelper.serializeKey(listName), RedisSerializerHelper.elStringTranster(elements))>-1;
	}
	
	/**
	 * INSERT value INTO listName after/before  FIRST FOUND pivot FROM THE HEAD OF listName
	 * @param listName
	 * @param position default will be LIST_POSITION.BEFORE
	 * @param pivot
	 * @param value
	 * @return
	 */
	public boolean setIntoList(Object listName,LIST_POSITION position,Object pivot,Object value){
		if(listName==null||pivot==null||value==null)
			throw new Redis3CmdException(ExceptionCodeEnum.linsertCMD);
		return jc.linsert(RedisSerializerHelper.serializeValue(listName), position, RedisSerializerHelper.serializeValue(listName), RedisSerializerHelper.serializeValue(value))>-1;
	}
	
	/**
	 * lindex listName index
	 * @param listName
	 * @param index start form 0, if a minus number input means index from the end a list. -1 mean last one.
	 * @return
	 */
	public  String getFromList(Object listName,long index){
		if(listName==null||index<0)
			throw new Redis3CmdException(ExceptionCodeEnum.lindexCMD);
		return jc.lindex(RedisSerializerHelper.serializeKey(listName),index);
	}
	
	/**
	 * 
	 * lindex listName index
	 * @param listName
	 * @param index
	 * @param objectType
	 * @return
	 */
	public <T> T getFromList(Object listName,long index,Class<T> objectType){
		if(listName==null||index<0)
			throw new Redis3CmdException(ExceptionCodeEnum.lindexCMD);
		if(objectType==null)
			return (T) getFromList(RedisSerializerHelper.serializeKey(listName) , index);
		return RedisSerializerHelper.deserializeValue(jc.lindex(RedisSerializerHelper.serializeKey(listName),index), objectType);
	}
	
	/**
	 * this method may be blocked.
	 * @param timeout
	 * @param key
	 * @return
	 */
	public List<String> bpopFromList(int timeout,Object key){
		if(key==null)
			throw new Redis3CmdException(ExceptionCodeEnum.blpopCMD);
		return jc.blpop(timeout,RedisSerializerHelper.serializeKey(key));
	}
	/**
	 * redis rpop: remove and return the TAIL element from listName
	 * @param listName
	 * @return
	 */
	public String popFromList(Object listName){
		if(listName==null)
			throw new Redis3CmdException(ExceptionCodeEnum.rpopCMD);
		return jc.rpop(RedisSerializerHelper.serializeKey(listName));
	}
	/**
	 * redis rpop: remove and return the tail element from listName
	 * @param listName
	 * @return
	 */
	public <T> T popFromList(Object listName,Class<T> objectType){
		if(listName==null)
			throw new Redis3CmdException(ExceptionCodeEnum.rpopCMD);
		if(objectType==null)
			return (T) popFromList( listName);
		return RedisSerializerHelper.deserializeKey(jc.rpop(RedisSerializerHelper.serializeKey(listName)), objectType);
	}
	
	/**
	 * warning: if start > list.size(), all data will be removed and list will be cleaned
	 * remove those elements which not in start-end
	 * @param listName
	 * @param start
	 * @param end
	 * @return
	 */
	public void trimList(Object listName, long start , long end){
		if(listName==null)
			throw new Redis3CmdException(ExceptionCodeEnum.ltrimCMD);
		jc.ltrim(RedisSerializerHelper.serializeKey(listName), start, end);
	}
	
	/**
	 * from tail to head
	 * @param listName
	 * @param start
	 * @param value
	 * @return
	 */
	public boolean remFromList(Object listName,long start,Object value){
		if(listName==null)
			throw new Redis3CmdException(ExceptionCodeEnum.lremCMD);
		return jc.lrem(RedisSerializerHelper.serializeKey(listName), start, RedisSerializerHelper.serializeKey(value))>0;
	}
	
	/**
	 * list.size()
	 * @param listName
	 * @return
	 */
	public long getLenOfList(Object listName){
		if(listName==null)
			throw new Redis3CmdException(ExceptionCodeEnum.llenCMD);
		return jc.llen(RedisSerializerHelper.serializeKey(listName));
	}
	
	/**
	 * redis lrange listname start end
	 * @param listName
	 * @param start
	 * @param stop
	 * @param objectType
	 * @return
	 */
	public  <T> List<T> getList(Object listName, long start, long stop,Class<T> objectType){
		if(listName==null)
			throw new Redis3CmdException(ExceptionCodeEnum.lrangeCMD);
		return RedisSerializerHelper.elStringTranster(jc.lrange(RedisSerializerHelper.serializeKey(listName), start, stop), objectType);
	}
	
	
	
	
}
