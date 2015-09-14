package org.cpw.cache.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cpw.annotation.KeyExtractable;

public class AnnotationProcessor {
	
	private static final Log logger = LogFactory.getLog(AnnotationProcessor.class);

	/**
	 * process Annotation KeyExtractable(attrs={},operations={})
	 * @param instance
	 * @return
	 */
	public static <T> String processSerializable(Object instance) {
		if(instance==null)
			return null;
		Class<?> clazz = instance.getClass();
		if (!clazz.isAnnotationPresent(org.cpw.annotation.KeyExtractable.class))
			return null;
		KeyExtractable keyExtractable = clazz.getAnnotation(org.cpw.annotation.KeyExtractable.class);
		String[] attrs = keyExtractable.attrs();
		StringBuffer sb = new StringBuffer();
		List<String> skipMethods = new ArrayList<String>();
		for(String attr:attrs){
			sb.append(String.valueOf(getFieldValue(attr, instance,skipMethods)));
		}
		String[] operations = keyExtractable.operations();
		for(String operation:operations){
			if(!skipMethods.contains(operation))
				try{
					sb.append(StringUtil.nullToEmpty(String.valueOf(instance.getClass().getMethod(operation, new Class[] {}).invoke(instance, new Class[] {}))));
				}catch(Exception e){
					logger.error(e.getMessage(),e);
				}
				
		}
		return sb.toString();
	}

	/**
	 * get field value of an instance
	 * @param fieldName
	 * @param instance
	 * @param methods
	 * @return
	 */
	private static Object getFieldValue(String fieldName, Object instance,List<String> methods) {
		String firstLetter = fieldName.substring(0, 1).toUpperCase();
		String getter = "get" + firstLetter + fieldName.substring(1);
		Method method;
			
			try {
				method = instance.getClass().getMethod(getter, new Class[] {});
				return method.invoke(instance, new Object[] {});
			} catch (Exception  e) {
				logger.error("");
				return "";
			}
	}
	
}
