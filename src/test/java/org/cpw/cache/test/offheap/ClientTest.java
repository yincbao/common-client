package org.cpw.cache.test.offheap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.cache.CacheManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.Log4jConfigurer;
public class ClientTest {
	Log logger = LogFactory.getLog(ClientTest.class);
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		Log4jConfigurer.initLogging("classpath:log4j.properties");
	}
	
	@Test
	public  void testGetSET(){
		
		
		CacheManager.getRedis3ClusterCache().set("redisCluster", "redisCluster");
		System.out.println("unit testcase: [ClientTest] outputting###########  "+CacheManager.getRedis3ClusterCache().get("redisCluster"));
		
		
		
		CacheManager.getMemCachedCache().set("memcachedTest", "memcachedTest");
		System.out.println("unit testcase: [ClientTest] outputting###########  "+CacheManager.getMemCachedCache().get("memcachedTest"));
		
		CacheManager.getLocalCache().set("locaCacheTest", "locaCacheTest");
		System.out.println("unit testcase: [ClientTest] outputting###########  "+CacheManager.getLocalCache().get("locaCacheTest"));
	}
	
	
}
