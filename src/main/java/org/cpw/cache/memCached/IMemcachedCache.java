package org.cpw.cache.memCached;

import org.cpw.cache.ICache;

public interface IMemcachedCache extends ICache{
	
	 public long incr(Object key, long value);
	 
	 public long incr(Object key, long value, long defaultValue);
	 
	 public long decr(Object key, long value);
	 
	 public long keyVersion(Object key);
	 
	 public boolean cas(Object key, Object value,long exptime);
	 
	 public boolean cas(Object key, Object value) ;
	 
	 
	 
}
