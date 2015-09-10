package org.cpw.cache;

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

	public void set(String key, String value);

	public void set(Object key, Object value);

	public void set(String key, String value, long second);

	public void set(Object key, Object value, long second);

	public boolean delete(Object key);

	public <T> T get(String key, Class<T> objectType);

	public <T> T get(Object key, Class<T> objectType);

	public String get(String key);

}
