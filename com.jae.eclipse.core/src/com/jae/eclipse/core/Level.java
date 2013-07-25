/**
 * 
 */
package com.jae.eclipse.core;

/**
 * @author hongshuiqiao
 *
 */
public enum Level {
	ERROR("error"),
	WARNING("warning"),
	INFO("info"),
	NONE("none");
	
	private String name;

	private Level(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public String getName() {
		return name;
	}
}
