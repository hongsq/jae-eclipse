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
	 * @param messageCaller	消息处理
	 * @param source		变化的源
	 * @param value			变化后的值
	 * @return
	 */
	public boolean validate(IMessageCaller messageCaller, Object source, Object value);
}
