package org.cpw.cache.local;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
 
/**
 * 
 * ClassName: AbstractCacheInvocationHandler
 * @description
 * @author yin_changbao
 * @Date   Sep 8, 2015
 *
 */
public abstract class AbstractCacheInvocationHandler implements
		java.lang.reflect.InvocationHandler {
	private Object target = null;
	
	public AbstractCacheInvocationHandler(LocalCacheManager LocalCacheManager) {
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public final Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			Object retObject = null;
			if (this.canInterceptMethod(method, args) == false) {
				retObject = method.invoke(target, args);
			} else {
				retObject = this.getFromCache(method, args);
				if (retObject == null) {
					retObject = method.invoke(target, args);
					if(retObject!=null) {
						this.addObjectToCache(method, args, retObject);
					}
				}
			}
			return retObject;
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Checks if the method passed in the parameter is to be intercepted
	 * by the Proxy.
	 * @param method
	 * @param args
	 * @return
	 */
	protected abstract boolean canInterceptMethod(Method method,
			Object[] args);

	/**
	 * This method get the value that is returned by the "Method" parameter
	 * from the Cache. Returns null if the value does not exist in the cache.
	 * @param method
	 * @param args
	 * @return
	 */
	protected abstract Object getFromCache(Method method,
			Object[] args);

	/**
	 * This method adds an object to a cache  
	 * @param method
	 * @param args
	 * @param cacheObject
	 * @return
	 * @throws Throwable
	 */
	protected abstract void addObjectToCache(Method method,
			Object[] args, Object cacheObject);
}
