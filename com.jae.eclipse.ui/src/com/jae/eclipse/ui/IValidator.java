/**
 * 
 */
package com.jae.eclipse.ui;


/**
 * 验证器
 * @author hongshuiqiao
 *
 */
public interface IValidator {
	/**
	 * 验证器的验证逻辑
	 * @param messageCaller
	 * @param validateObject
	 * @return
	 */
	public boolean validate(IMessageCaller messageCaller, Object validateObject);
}
