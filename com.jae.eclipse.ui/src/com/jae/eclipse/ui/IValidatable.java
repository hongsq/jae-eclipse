/**
 * 
 */
package com.jae.eclipse.ui;



/**
 * @author hongshuiqiao
 *
 */
public interface IValidatable {
	/**
	 * 值变化时触发自身验证
	 * @return
	 */
	public boolean validate();
}
