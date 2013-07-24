/**
 * 
 */
package com.jae.eclipse.ui.control;

/**
 * @author hongshuiqiao
 *
 */
public enum NumberType {
	BYTE("byte"),
	SHORT("short"),
	INT("int"),
	LONG("long"),
	FLOAT("float"),
	DOUBLE("double");
	
	private String name;

	private NumberType(String name) {
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
