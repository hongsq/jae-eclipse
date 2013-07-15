/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.util;

import com.jae.eclipse.navigator.jaeapp.model.IJDElement;

/**
 * @author hongshuiqiao
 *
 */
public class JDModelUtil {
	
	/**
	 * 根据指定的父类型获取指定元素的父节点
	 * @param element
	 * @param parentType
	 * @return
	 */
	public static <T> T getParentElement(IJDElement element, Class<T> parentType){
		if(null == element || null == parentType)
			return null;
		
		if(element.getClass() == parentType)
			return parentType.cast(element);
		
		return getParentElement(element.getParent(), parentType);
	}

}
