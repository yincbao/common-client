package org.cpw.serializer;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.cache.local.LocalCacheClient;
import org.cpw.cache.util.AnnotationProcessor;
import org.cpw.cache.util.DstCacheConfig;
import org.cpw.cache.util.StringUtil;
import org.cpw.exception.DeserializeException;
import org.cpw.exception.code.ExceptionCodeEnum;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * 
 * ClassName: RedisSerializerHolder
 * 
 * @description
 * @author yin_changbao
 * @Date Sep 8, 2015
 *
 */
@SuppressWarnings("unchecked")
public class SerializerHelper {
	
	private static final Log logger = LogFactory.getLog(SerializerHelper.class);

	private final static String KEY = "REDISKEYSERIALIZERCACHE";
	private final static String VALUE = "REDISVALUESERIALIZERCACHE";
	
	private final static String defaulSerializer = "org.springframework.data.redis.serializer.JacksonJsonRedisSerializer";

	private SerializerHelper() {
	}

	private static <T> RedisSerializer<T> getKeySerializer(Class<T> objectType) {
		RedisSerializer<T> keySerializer = (RedisSerializer<T>) LocalCacheClient.getInstance().get(KEY, objectType.getName());

		if (keySerializer == null){
			try {
				String costomedKeySerializerName = DstCacheConfig.getProperty("common.cache.keySerializer");
				Class<T> clazz = conifguredClazz(costomedKeySerializerName);
				if(clazz!=null)
					keySerializer = (RedisSerializer<T>) instanceSerializer(clazz);
			} catch (ClassNotFoundException e) {
				logger.error("failed loading coustomed keySerializer, use default instead",e);
			}
		}
		keySerializer = keySerializer == null ? getDefaultSerializer(objectType) : keySerializer;
		LocalCacheClient.getInstance().set(KEY, objectType.getName(), keySerializer);

		return keySerializer;
	}

	
	private static <T> Class<T> conifguredClazz(String className) throws ClassNotFoundException {
		if(StringUtil.isEmpty(className))
			return null;
		if(className.equalsIgnoreCase(defaulSerializer))
			return null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Class<T> clazz = (Class<T>) Class.forName(className, true, classLoader);
		return clazz;
	}

	private static <T> RedisSerializer<T> getValueSerializer(Class<T> objectType) {
		RedisSerializer<T> valueSerializer = (RedisSerializer<T>) LocalCacheClient.getInstance().get(VALUE, objectType.getName());

		if (valueSerializer == null){
			try {
				String costomedValueSerializerName = DstCacheConfig.getProperty("common.cache.valueSerializer");
				Class<T> clazz = conifguredClazz(costomedValueSerializerName);
				if(clazz!=null)
					valueSerializer = (RedisSerializer<T>) instanceSerializer(clazz);
			} catch (ClassNotFoundException e) {
				logger.error("failed loading coutomed valueSerializer, use default instead",e);
			}
		}
		valueSerializer = valueSerializer == null ? getDefaultSerializer(objectType) : valueSerializer;
		LocalCacheClient.getInstance().set(VALUE, objectType.getName(), valueSerializer);
		return valueSerializer;
	}

	private static <T> RedisSerializer<T> getDefaultSerializer(Class<T> objectType) {
		return new JacksonJsonRedisSerializer<T>(objectType);
	}

	public static <T> String serializeKey(T key) {
		return doSerialize(key, (RedisSerializer<T>) getKeySerializer(key.getClass()));
	}

	public static <T> String serializeValue(T value) {
		return doSerialize(value, (RedisSerializer<T>) getValueSerializer(value.getClass()));
	}

	public static <T> T deserializeKey(String meta, Class<T> objectType) throws DeserializeException {
		return doDeserialize(meta, (RedisSerializer<T>) getKeySerializer(objectType), objectType);
	}

	public static <T> T deserializeValue(String meta, Class<T> objectType) throws DeserializeException {
		return doDeserialize(meta, (RedisSerializer<T>) getValueSerializer(objectType), objectType);
	}

	private static <T> String doSerialize(T target, RedisSerializer<T> serializer) {
		String result = "";
		if (!target.getClass().getName().equals("java.lang.String")) {
			result = AnnotationProcessor.processSerializable(target);
			result = StringUtil.isEmpty(result)? new String(serializer.serialize(target)):result;
		} else
			result = (String) target;
		return result;
	}

	private static <T> T doDeserialize(String target, RedisSerializer<T> deserializer, Class<T> objectType)
			throws DeserializeException {
		try {
			if (!objectType.getName().equals("java.lang.String")) {
				return (T) deserializer.deserialize(target.getBytes());
			} else
				return (T) target;
		} catch (Exception e) {
			throw new DeserializeException(ExceptionCodeEnum.deserializeExp, e);
		}
	}

	public static <T, E> Set<T> elStringTranster(Set<String> metaData, Class<T> objectType) {
		Set<T> result = new HashSet<T>();
		for (String meta : metaData) {
			result.add(SerializerHelper.deserializeKey(meta, objectType));
		}
		return result;
	}

	public static <T> List<T> elStringTranster(List<String> metaData, Class<T> objectType) {
		List<T> result = new ArrayList<T>();
		for (String meta : metaData) {
			result.add(SerializerHelper.deserializeKey(meta, objectType));
		}
		return result;
	}

	public static <K, V> Map<K, V> elStringTranster(Map<String, String> metaData, Class<K> keyType, Class<V> valueType) {
		Map<K, V> result = new HashMap<K, V>();
		for (String key : metaData.keySet()) {
			String value = metaData.get(key);
			result.put(SerializerHelper.deserializeKey(key, keyType),
					SerializerHelper.deserializeValue(value, valueType));
		}
		return result;
	}

	public static String[] elStringTranster(Object[] elements) {
		String[] redisKey = new String[elements.length];
		for (int i = 0; i < elements.length; i++) {
			redisKey[i] = SerializerHelper.serializeValue(elements[i]);
		}
		return redisKey;
	}
	
	private static <T> RedisSerializer<T> instanceSerializer(Class<T> clazz){
			
		try {
			Constructor<T> costr = clazz.getConstructor();
			return (RedisSerializer<T>) costr.newInstance();
		} catch (Exception e) {
			logger.error("class "+clazz.getName()+" can't recogenized as a RedisSerializer, is it a subclass of org.springframework.data.redis.serializer.RedisSerializer",e);
		}
		return null;
	}
}
