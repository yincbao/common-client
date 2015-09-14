package org.cpw.cache.util;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.exception.ConfigException;
import org.cpw.exception.code.ExceptionCodeEnum;


public class DstCacheConfig{
	
  private static Properties dstCacheProperties = null;
  private static final String PROPS_FILE_NAME = "dst-caches.properties";

  private static Log logger = LogFactory.getLog( DstCacheConfig.class.getName() );

  static
  {
    loadConfig();
  }
  
  private DstCacheConfig(){}

  public static void loadConfig() {
     try {
	   logger.debug("loadConfig() starts.");
	   InputStream is = DstCacheConfig.class.getClassLoader().getResourceAsStream(DstCacheConfig.PROPS_FILE_NAME);

	   if (is == null) {
		   throw new IOException("Properties file: "+ DstCacheConfig.PROPS_FILE_NAME + " is not found!");
	   }

	   dstCacheProperties = new Properties();
	   dstCacheProperties.load(is);
       is.close();
     }
     catch (Exception e) {
       logger.error(e.getMessage(), e);
     }
     finally {
	   logger.debug("loadConfig() ends.");
	 }
  }

  public static String getProperty(String key) {
     if (dstCacheProperties != null) {
	     return dstCacheProperties.getProperty(key).trim();
	 }
	 else {
	     throw new ConfigException(ExceptionCodeEnum.configFileNotFound);
     }
  }
  public static String getProperty(String property, String def) {
		String retVal = null;
		if (dstCacheProperties != null){
			retVal = dstCacheProperties.getProperty(property, def).trim();
		}else{
			retVal = def;
		}
		return retVal;
	}
  
  public static int getIntProperty(String property) {
		int retVal = 0;
		if (dstCacheProperties != null){
			try{
				retVal = Integer.parseInt(dstCacheProperties.getProperty(property).trim());
			}catch(NumberFormatException e){
				throw new ConfigException(ExceptionCodeEnum.numberFormatException,e);
			}
			
		}
		return retVal;
	}
  
  public static int getIntProperty(String property,int def) {
		int retVal = 0;
		if (dstCacheProperties != null){
			try{
				retVal = Integer.parseInt(dstCacheProperties.getProperty(property).trim());
			}catch(NumberFormatException e){
				retVal = def;
				logger.warn("no property :"+property+" found, return default value: "+def);
			}
			
		}
		return retVal;
	}
  
  public static long getLongProperty(String property) {
		long retVal = 0;
		if (dstCacheProperties != null){
			try{
				retVal = Long.parseLong(dstCacheProperties.getProperty(property).trim());
			}catch(NumberFormatException e){
				throw new ConfigException(ExceptionCodeEnum.numberFormatException,e);
			}
			
		}
		return retVal;
	}

  public static long getLongProperty(String property,long def) {
		long retVal = 0;
		if (dstCacheProperties != null){
			try{
				retVal = Integer.parseInt(dstCacheProperties.getProperty(property).trim());
			}catch(NumberFormatException e){
				retVal = def;
				logger.warn("no property :"+property+" found, return default value: "+def);
			}
			
		}
		return retVal;
	}
  
  public static double getDoubleProperty(String property) {
	  double retVal = 0;
		if (dstCacheProperties != null){
			try{
				retVal = Double.parseDouble(dstCacheProperties.getProperty(property).trim());
			}catch(NumberFormatException e){
				throw new ConfigException(ExceptionCodeEnum.numberFormatException,e);
			}
			
		}
		return retVal;
	}
  
  public static double getDoubleProperty(String property,double def) {
	  double retVal = 0;
		if (dstCacheProperties != null){
			try{
				retVal = Double.parseDouble(dstCacheProperties.getProperty(property).trim());
			}catch(NumberFormatException e){
				retVal = def;
				logger.warn("no property :"+property+" found, return default value: "+def);
			}
		}
		return retVal;
	}
}
