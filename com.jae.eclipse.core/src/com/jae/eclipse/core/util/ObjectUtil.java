/**
 * 
 */
package com.jae.eclipse.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jae.eclipse.core.exception.PropertyException;

/**
 * 操作对象
 * @author hongshuiqiao
 *
 */
public class ObjectUtil {
	
	public static void main(String[] args) {
		System.out.println(Object.class.getSuperclass());
	}

	public static Field getField(Class<?> instanceClass, String fieldName, boolean onlyPublic) throws NoSuchFieldException, SecurityException{
		if(onlyPublic)
			return instanceClass.getField(fieldName);
		
		Field field = doGetField(instanceClass, fieldName);
		if(null == field)
			throw new NoSuchFieldException(fieldName);
		
		return field;
	}
	
	private static Field doGetField(Class<?> instanceClass, String fieldName){
		Field[] fields = instanceClass.getDeclaredFields();
		for (Field field : fields) {
			if(fieldName.equals(field.getName()))
				return field;
		}
		
		try {
			Field field = instanceClass.getDeclaredField(fieldName);
			if(null != field)
				return field;
		} catch (Exception e) {}
		
		Class<?> superClass = instanceClass.getSuperclass();
		if(null != superClass)
			return doGetField(superClass, fieldName);
			
		return null;
	}
	
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
			String setMethodName = "set"+Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
			String getMethodName = "get"+Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
			try {
				Method getMethod = getMethod(instance.getClass(), getMethodName, true);
				Method setMethod = getMethod(instance.getClass(), setMethodName, true, getMethod.getReturnType());
				setMethod.invoke(instance, value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static Method getMethod(Class<?> objectClass, String methodName, boolean onlyPublic, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException{
		if(onlyPublic)
			return objectClass.getMethod(methodName, parameterTypes);
			
		Method method = doGetMethod(objectClass, methodName, parameterTypes);
		if(null == method)
			throw new NoSuchMethodException(objectClass.getName() + "." + methodName + argumentTypesToString(parameterTypes));
		
		return method;
	}
	
	private static Method doGetMethod(Class<?> objectClass, String methodName, Class<?>... parameterTypes){
		try {
			Method method = objectClass.getDeclaredMethod(methodName, parameterTypes);
			if(null != method)
				return method;
		} catch (Exception e) {}
		
		Class<?> superClass = objectClass.getSuperclass();
		if(null != superClass && !superClass.isInterface())
			return doGetMethod(superClass, methodName, parameterTypes);
			
		return null;
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
				Method getMethod = getMethod(instance.getClass(), methodName, true);
				return getMethod.invoke(instance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static String argumentTypesToString(Class<?>[] argTypes) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        if (argTypes != null) {
            for (int i = 0; i < argTypes.length; i++) {
                if (i > 0) {
                    buf.append(", ");
                }
                Class<?> c = argTypes[i];
                buf.append((c == null) ? "null" : c.getName());
            }
        }
        buf.append(")");
        return buf.toString();
    }
	
	public static void copyProperties(Object source, Object target, String[] properties){
		for (String propertyName : properties) {
			Object value = getValue(source, propertyName);
			setValue(target, propertyName, value);
		}
	}
}
