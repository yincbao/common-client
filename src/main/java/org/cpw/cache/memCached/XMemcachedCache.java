package org.cpw.cache.memCached;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.cache.util.DstCacheConfig;
import org.cpw.exception.LaunchException;
import org.cpw.exception.MemcachedOptException;
import org.cpw.exception.code.ExceptionCodeEnum;
import org.cpw.serializer.SerializerHelper;

public class XMemcachedCache implements IMemcachedCache {

	private static final Log logger = LogFactory.getLog(XMemcachedCache.class);

	private static MemcachedClient client;

	private static class SingletonHolder {
		private static final XMemcachedCache INSTANCE = new XMemcachedCache();
	}

	public static final XMemcachedCache getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private XMemcachedCache() {
		logger.info("launching memcached client...");
		try {
			String listStr = DstCacheConfig.getProperty("memcached.section.list");
			logger.debug(" confiured nodes are: " + listStr);
			String[] hosts = listStr.split(",");
			if (hosts == null || hosts.length < 1)
				throw new LaunchException(ExceptionCodeEnum.memcachedNoValidHostPort);
			List<InetSocketAddress> hostPort = new ArrayList<InetSocketAddress>();
			for (String host : hosts) {
				try {
					String[] hostIn = host.split(":");
					hostPort.add(new InetSocketAddress(hostIn[0], Integer.parseInt(hostIn[1])));
				} catch (Exception e) {
					logger.error("faild connect to>> " + host + " abandon and try next one", e);
				}
			}

			String weightStr = DstCacheConfig.getProperty("memecached.section.weight");
			String[] weights = weightStr.split(",");

			int[] w = null;
			if (hosts != null && hosts.length > 0) {
				w = new int[weights.length];
				for (int i = 0; i < hosts.length; i++) {
					try {
						w[i] = Integer.parseInt(weights[i]);
					} catch (Exception e) {
						w[i] = 1;
					}
				}
			}
			client = new XMemcachedClientBuilder(hostPort, w).build();
			if (hostPort.size() > 1)
				logger.info("memcached section cluster client launched done.");
			else
				logger.info("memcached single section launched done.");
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	public long incr(Object key, long value) {
		try {
			return client.incr(SerializerHelper.serializeKey(key), value);
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.incrExp, e);
		}
	}

	public long incr(Object key, long value, long defaultValue) {
		try {
			return client.incr(SerializerHelper.serializeKey(key), value);
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.incrExp, e);
		}
	}

	public long decr(Object key, long value) {
		try {
			return client.decr(SerializerHelper.serializeKey(key), value);
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.decrExp, e);
		}

	}

	private boolean set(String key, Object value) {
		try {
			return client.set(key, 0, value);
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.settingExp, e);
		}
	}

	public boolean set(Object key, Object value) {
		return set(SerializerHelper.serializeKey(key), value);
	}

	/**
	 * memcached set
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 */
	private boolean set(String key, Object value, long expireTime) {
		try {
			return client.set(key, (int) expireTime / 1000, value);
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.settingExp, e);
		}
	}

	public boolean set(Object key, Object value, long expireTime) {
		return set(SerializerHelper.serializeKey(key), value, expireTime);
	}

	/**
	 * cas
	 * 
	 * @param key
	 * @param value
	 * @param casUnique
	 * @return
	 */
	public boolean cas(Object key, Object value) {
		try {
			return client.cas(SerializerHelper.serializeKey(key), 0, value, keyVersion(key));
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.casExp, e);
		}
	}

	public long keyVersion(Object key) {
		try {
			return (Long) client.gets(SerializerHelper.serializeKey(key)).getValue();
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.getsExp, e);
		}
	}

	/**
	 * cas
	 * 
	 * @param key
	 * @param value
	 * @param casUnique
	 * @return
	 */
	public boolean cas(Object key, Object value, long exptime) {
		try {
			return client.cas(SerializerHelper.serializeKey(key), (int) exptime / 1000, value, keyVersion(key));
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.casExp, e);
		}
	}

	/**
	 * get
	 * 
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
		try {
			return client.get(SerializerHelper.serializeKey(key));
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.getExp, e);
		}
	}

	public <T> T get(Object key, Class<T> returnType) {
		try {
			return client.get(SerializerHelper.serializeKey(key));
		} catch (Exception e) {
			try {
				return SerializerHelper.deserializeValue((String) client.get(SerializerHelper.serializeKey(key)),
						returnType);
			} catch (Exception ie) {
				throw new MemcachedOptException(ExceptionCodeEnum.getExp, e);
			}
		}
	}

	/**
	 * memecached delete
	 * 
	 * @param key
	 * @return
	 */
	public boolean delete(Object key) {
		try {
			return client.delete(SerializerHelper.serializeKey(key));
		} catch (Exception e) {
			throw new MemcachedOptException(ExceptionCodeEnum.deleteExp, e);
		}
	}

	public boolean save(Object obj) {
		return set(SerializerHelper.serializeKey(obj), obj);
	}

}
