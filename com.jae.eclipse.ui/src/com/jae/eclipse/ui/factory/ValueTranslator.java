/**
 * 
 */
package com.jae.eclipse.ui.factory;

/**
 * @author hongshuiqiao
 *
 */
public interface ValueTranslator<SOURCE, TARGET> {
	
	/**
	 * 根据source构造target
	 * @param source
	 * @return
	 */
	public TARGET from(SOURCE source);

	/**
	 * 根据target构造source
	 * @param target
	 * @return
	 */
	public SOURCE to(TARGET target);
}
