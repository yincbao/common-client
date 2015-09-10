package org.cpw.cache.local;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.cpw.cache.exception.CacheException;
import org.cpw.cache.exception.code.ExceptionCodeEnum;

/**
 * 
 * ClassName: CacheProxyFactory
 * @description
 * @author yin_changbao
 * @Date   Sep 8, 2015
 *
 */
public class CacheProxyFactory {
	public Object getCacheProxy(Class proxyInterface, Object target) {
		try {
			Object proxy = null;
			// The naming convention for a CacheInvocationHandler is "proxyInterfaceName" + "CacheInvocationHandler" 
			// For e.g. UserDomainDAOCacheInvocationHandler
			//Note: A CacheInvocationHandler is to be defined in the same package where the proxyInterface is defined.
			String invocationHandlerName = proxyInterface.getName()
					+ "CacheInvocationHandler";
			Class invocationHandlerNameClass = Class
					.forName(invocationHandlerName);
			//Every CacheInvocationHandler defined will have a Constructor that takes LocalCacheManager as an argument
			Constructor ctor = invocationHandlerNameClass
					.getDeclaredConstructor(LocalCacheManager.class);
			ctor.setAccessible(true);
			//Create the InvocationHandler and set the target
			AbstractCacheInvocationHandler invocationHandler = (AbstractCacheInvocationHandler) ctor
					.newInstance(LocalCacheManager.getInstance());
			invocationHandler.setTarget(target);//Create the Proxy
			proxy = this.createProxy(proxyInterface, invocationHandler);
			return proxy;
		} catch (Exception e) {
			throw new CacheException(ExceptionCodeEnum.getCacheProxy, e);
		}
	}
	
	private Object createProxy(Class proxyInterface,
			InvocationHandler handler) {
		return Proxy.newProxyInstance(proxyInterface.getClassLoader(),
				new Class[] { proxyInterface }, handler);
	}	
}
