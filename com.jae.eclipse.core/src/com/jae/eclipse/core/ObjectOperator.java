/**
 * 
 */
package com.jae.eclipse.core;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings("rawtypes")
public interface ObjectOperator {

	public Object newInstance(Class instanceClass);
	
	public void setValue(Object instance, String property, Object value);
	
	public Object getValue(Object instance, String property);
}
