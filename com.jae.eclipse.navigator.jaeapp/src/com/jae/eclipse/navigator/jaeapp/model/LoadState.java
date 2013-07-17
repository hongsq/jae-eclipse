/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

/**
 * @author hongshuiqiao
 *
 */
public enum LoadState {
	NONE("none"),
	LOADING("Loading"),
	LOADED("Loaded");
	
	private String name;

	private LoadState(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
