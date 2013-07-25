/**
 * 
 */
package com.jae.eclipse.core;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jae.eclipse.core.exception.JAERuntimeException;
import com.jae.eclipse.core.util.ObjectUtil;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings("rawtypes")
public class DefaultObjectOperator implements ObjectOperator {
	public static final DefaultObjectOperator INSTANCE = new DefaultObjectOperator();

	public Object newInstance(Class instanceClass) {
		if(instanceClass.isInterface()){
			if(Map.class.isAssignableFrom(instanceClass))
				return new HashMap();
			
			if(List.class.isAssignableFrom(instanceClass))
				return new ArrayList();
			
			throw new JAERuntimeException("\""+instanceClass.getName()+"\"无法实例化");
		}
		
		try {
			return instanceClass.newInstance();
		} catch (Exception e) {
			throw new JAERuntimeException("\""+instanceClass.getName()+"\"无法实例化", e);
		}
	}

	public void setValue(Object instance, String property, Object value) {
		ObjectUtil.setValue(instance, property, value);
	}

	public Object getValue(Object instance, String property) {
		return ObjectUtil.getValue(instance, property);
	}

}
