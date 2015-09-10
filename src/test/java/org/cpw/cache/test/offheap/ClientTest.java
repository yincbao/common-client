package org.cpw.cache.test.offheap;

import org.cpw.cache.CacheManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.Log4jConfigurer;
public class ClientTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		Log4jConfigurer.initLogging("classpath:log4j.properties");
	}
	
	@Test
	public  void testEnterant(){
		CacheManager.getRedis3ClusterCache().set("unit test", "unit testvalue");
		System.out.println(CacheManager.getRedis3ClusterCache().get("unit test"));
	}

}
