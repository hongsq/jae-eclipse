/**
 * 
 */
package com.jae.eclipse.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;


/**
 * @author Hongsq
 * 
 */
public class JsonHelper {
	private final static JsonConfig config = new JsonConfig();
//	static{
//		config.registerJsonBeanProcessor(JsonDelegate.class, new JsonDelegateBeanProcessor());
//	}

	/**
	 * 从普通的Bean转换为字符串
	 * @param object
	 * @return
	 */
	public static String getJson(Object object) {
		if(null == object)
			return null;
		
		if (object instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) object;
			return getJsonArray(collection);
		}else if(object.getClass().isArray()){
			return getJsonArray((Object[])object);
		}
		
		JSONObject jsonObject = JSONObject.fromObject(object, config);
		return jsonObject.toString();
	}

	/**
	 * 从Java的collection转换为字符串
	 * @param collection
	 * @return
	 */
	public static String getJsonArray(Collection<?> collection) {
		JSONArray jsonArray = JSONArray.fromObject(collection, config);
		return jsonArray.toString();
	}

	/**
	 * 从Java对象数组转换为字符串
	 * @param arry
	 * @return
	 */
	public static <T> String getJsonArray(T[] arry) {
		JSONArray jsonArray = JSONArray.fromObject(arry, config);
		return jsonArray.toString();
	}

	/**
	 * 从json格式的字符串转换为Map对象
	 * @param json
	 * @return
	 */
	public static Object toJavaObject(String json) {
		return toJavaObject(json, null);
	}
	
	/**
	 * 从json格式的字符串转换为某个Bean
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T toJavaObject(String json, Class<T> cls) {
		JSONObject jsonObject = JSONObject.fromObject(json, config);
		
		Object bean = null;
		
		if(null == cls)
			bean = JSONObject.toBean(jsonObject);
		else
			bean = JSONObject.toBean(jsonObject, cls);
		
		return cls.cast(bean);
	}
	
	/**
	 * 从json格式的字符串转换为List数组
	 * @param json
	 * @return
	 */
	public static List<Object> toJavaList(String json) {
		return toJavaList(json, null);
	}
	
	public static <T> List<T> toJavaList(String json, Class<T> elementClass) {
		return new ArrayList<T>(toJavaCollection(json, elementClass));
	}
	
	public static Collection<Object> toJavaCollection(String json) {
		return toJavaCollection(json, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> toJavaCollection(String json, Class<T> elementClass) {
		JSONArray jsonArray = JSONArray.fromObject(json, config);
		if(null == elementClass)
			return JSONArray.toCollection(jsonArray);
		return JSONArray.toCollection(jsonArray, elementClass);
	}

	public static Object[] toJavaArray(String json){
		return toJavaArray(json, null);
	}
	
	/**
	 * 从json格式的字符串转换为某类对象的数组
	 * @param json
	 * @param elementClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toJavaArray(String json, Class<T> elementClass) {
		JSONArray jsonArray = JSONArray.fromObject(json, config);
		if(null == elementClass)
			return (T[]) JSONArray.toArray(jsonArray);
		return (T[]) JSONArray.toArray(jsonArray, elementClass);
	}
}
