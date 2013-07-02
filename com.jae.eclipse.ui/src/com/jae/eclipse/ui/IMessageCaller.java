/**
 * 
 */
package com.jae.eclipse.ui;

/**
 * 用于输出各种message
 * @author hongshuiqiao
 *
 */
public interface IMessageCaller {
	/**
	 * 输出错误信息
	 * @param message
	 */
	public void error(String message);
	
	/**
	 * 输出提示信息
	 * @param message
	 */
	public void info(String message);
	
	/**
	 * 输出警告信息
	 * @param message
	 */
	public void warn(String message);
	
	/**
	 * 清除旧的信息
	 */
	public void clear();
	
	/**
	 * 是否包含错误
	 * @return
	 */
	public boolean hasError();
}
