/**
 * 
 */
package com.jae.eclipse.ui;

/**
 * ��֤��
 * @author hongshuiqiao
 *
 */
public interface IValidator {
	/**
	 * ��֤������֤�߼�
	 * @param messageCaller
	 * @param adaptable
	 * @return
	 */
	public boolean validate(IMessageCaller messageCaller, IAdaptable adaptable);
}
