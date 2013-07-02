/**
 * 
 */
package com.jae.eclipse.ui;

/**
 * �����������message
 * @author hongshuiqiao
 *
 */
public interface IMessageCaller {
	/**
	 * ���������Ϣ
	 * @param message
	 */
	public void error(String message);
	
	/**
	 * �����ʾ��Ϣ
	 * @param message
	 */
	public void info(String message);
	
	/**
	 * ���������Ϣ
	 * @param message
	 */
	public void warn(String message);
	
	/**
	 * ����ɵ���Ϣ
	 */
	public void clear();
	
	/**
	 * �Ƿ��������
	 * @return
	 */
	public boolean hasError();
}
