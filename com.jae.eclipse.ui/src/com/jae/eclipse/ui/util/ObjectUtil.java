/**
 * 
 */
package com.jae.eclipse.ui.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jae.eclipse.ui.exception.PropertyException;

/**
 * 操作对象
 * @author hongshuiqiao
 *
 */
public class ObjectUtil {

	/**
	 * 返回父类所带的泛型的具体类型
	 * @param instance
	 * @param gecSuperClass
	 * @return
	 */
	public static Class<?>[] getGenericType(Object instance, Class<?> gecSuperClass){
		Class<?> superClass = instance.getClass().getSuperclass();
		while(null != superClass 
				&& (superClass != Object.class)){
			
			if(superClass.getSuperclass() == gecSuperClass)
				break;
			
			superClass = superClass.getSuperclass();
		}
		
		ParameterizedType type = (ParameterizedType) superClass.getGenericSuperclass();
		Type[] argTypes = type.getActualTypeArguments();
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (Type argType : argTypes) {
			if (argType instanceof Class<?>) {
				result.add((Class<?>) argType);
			}
		}
		
		return result.toArray(new Class<?>[result.size()]);
	}
	
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
