package org.cpw.cache.redis.r3cluster.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cpw.cache.CommonCache;
import org.cpw.cache.exception.DeserializeException;
import org.cpw.cache.exception.code.ExceptionCodeEnum;
import org.cpw.cache.local.LocalCacheManager;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 
 * ClassName: RedisSerializerHolder
 * @description
 * @author yin_changbao
 * @Date   Sep 8, 2015
 *
 */
public class RedisSerializerHelper {
	
private final static String KEY = "REDISKEYSERIALIZERCACHE";
private final static String VALUE = "REDISVALUESERIALIZERCACHE";



	

	private RedisSerializerHelper(){}
	
	private static <T> RedisSerializer<T> getKeySerializer(Class<T> objectType){
		RedisSerializer<T> keySerializer = (RedisSerializer<T>) LocalCacheManager.get(KEY,objectType.getName());
		
		if(keySerializer==null)
			keySerializer = (RedisSerializer<T>) CommonCache.getApplicationContext().getBean(CommonCache.class).getKeySerializer();
		keySerializer = keySerializer==null?getDefaultSerializer(objectType):keySerializer;
		LocalCacheManager.set(KEY, objectType.getName(), keySerializer);
		
		
		return 	keySerializer;
	}
	
	private static <T> RedisSerializer<T> getValueSerializer(Class<T> objectType){
		RedisSerializer<T> valueSerializer = (RedisSerializer<T>) LocalCacheManager.get(VALUE,objectType.getName());
		
		
		if(valueSerializer==null)
			valueSerializer = (RedisSerializer<T>) CommonCache.getApplicationContext().getBean(CommonCache.class).getValueSerializer();
		valueSerializer = valueSerializer==null?getDefaultSerializer(objectType):valueSerializer;
		LocalCacheManager.set(VALUE, objectType.getName(), valueSerializer);
		return 	valueSerializer;
	}
	

	private static <T> RedisSerializer<T> getDefaultSerializer(Class<T> objectType){
		return new JacksonJsonRedisSerializer<T>(objectType);
	}
	
	public static <T> String serializeKey(T key){
		return doSerialize( key,(RedisSerializer<T>)getKeySerializer(key.getClass()));
	}
	public static <T> String serializeValue(T value){
		return doSerialize( value,(RedisSerializer<T>)getValueSerializer(value.getClass()));
	}
	
	public static <T> T deserializeKey(String meta,Class<T> objectType) throws DeserializeException {
		return doDeserialize(meta,(RedisSerializer<T>)getKeySerializer(objectType), objectType);
	}
	
	public static <T> T deserializeValue(String meta,Class<T> objectType) throws DeserializeException {
		return doDeserialize(meta,(RedisSerializer<T>)getValueSerializer(objectType), objectType);
	}
	
	private static <T> String doSerialize(T target,RedisSerializer<T> serializer){
		String result = "";
		if(!target.getClass().getName().equals("java.lang.String")){
			
			result = new String(serializer.serialize(target));
		}else
			result = (String) target;
		return result;
	}
	
	private static <T> T doDeserialize(String target,RedisSerializer<T> deserializer,Class<T> objectType) throws DeserializeException{
		try{
			if(!objectType.getName().equals("java.lang.String")){
				return (T) deserializer.deserialize(target.getBytes());
			}else
				return (T) target;
		}catch(Exception e){
			throw new DeserializeException(ExceptionCodeEnum.deserializeExp,e);
		}
	}
	
	
	public static <T,E> Set<T> elStringTranster(Set<String> metaData,Class<T> objectType) {
		Set<T> result = new HashSet<T>();
		for(String meta:metaData){
			result.add(RedisSerializerHelper.deserializeKey(meta, objectType));
		}
		return result;
	}
	
	public static <T> List<T> elStringTranster(List<String> metaData,Class<T> objectType) {
		List<T> result = new ArrayList<T>();
		for(String meta:metaData){
			result.add(RedisSerializerHelper.deserializeKey(meta, objectType));
		}
		return result;
	}

	public static <K,V> Map<K,V> elStringTranster(Map<String,String> metaData,Class<K> keyType,Class<V> valueType) {
		Map<K,V> result = new HashMap<K,V>();
		for(String key:metaData.keySet()){
			String value = metaData.get(key);
			result.put(RedisSerializerHelper.deserializeKey(key,keyType), RedisSerializerHelper.deserializeValue(value,valueType));
		}
		return result;
	}

	public static String[] elStringTranster(Object[] elements) {
		String[] redisKey = new String[elements.length];
		for(int i=0;i<elements.length;i++){
			redisKey[i] = RedisSerializerHelper.serializeValue( elements[i]);
		}
		return redisKey;
	}
}
