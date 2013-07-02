/**
 * 
 */
package com.jae.eclipse.ui;

/**
 * @author hongshuiqiao
 *
 */
public interface IAdaptable {
	
	/**
	 * @param adapterClass
	 * @return
	 */
	public <T> T getAdapter(Class<T> adapterClass);
}
