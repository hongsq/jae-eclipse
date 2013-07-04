/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;


/**
 * @author hongshuiqiao
 *
 */
public class User extends AbstractJDElement {
	private String password;
	
	public User(String name) {
		super(null, name);
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
