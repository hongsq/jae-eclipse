/**
 * 
 */
package com.jae.eclipse.ui.util;

import java.lang.reflect.Method;
import java.util.Map;

import com.jae.eclipse.ui.exception.PropertyException;

/**
 * ²Ù×÷¶ÔÏó
 * @author hongshuiqiao
 *
 */
public class ObjectUtil {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setValue(Object instance, String propertyName, Object value){
		check(instance, propertyName);
		
		if (instance instanceof Map) {
			Map map = (Map) instance;
			map.put(propertyName, value);
		}else{
			String methodName = "set"+Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
			try {
				Method setMethod = getPublicMethod(instance.getClass(), methodName, value.getClass());
				setMethod.invoke(instance, value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static Method getPublicMethod(Class<?> objectClass, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException{
		return objectClass.getMethod(methodName, parameterTypes);
	}
	
	private static void check(Object instance, String propertyName) {
		if(null == instance)
			throw new RuntimeException("instance is null.");
		
		if(StringUtil.isEmpty(propertyName))
			throw new PropertyException("propertyName is null");
	}

	@SuppressWarnings({ "rawtypes" })
	public static Object getValue(Object instance, String propertyName){
		check(instance, propertyName);
		if (instance instanceof Map) {
			Map map = (Map) instance;
			return map.get(propertyName);
		}else{
			String methodName = "get"+Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
			try {
				Method getMethod = getPublicMethod(instance.getClass(), methodName);
				return getMethod.invoke(instance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
