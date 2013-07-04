/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongshuiqiao
 *
 */
public class User extends AbstractJDModel {
	private String password;
	private List<JDApp> apps = new ArrayList<JDApp>();
	
	public User(String name) {
		super();
		this.setName(name);
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void addJDApp(JDApp app){
		this.apps.add(app);
	}
	
	public void removeJDApp(JDApp app){
		this.apps.remove(app);
	}
	
	public JDApp[] getJDApps(){
		return this.apps.toArray(new JDApp[this.apps.size()]);
	}
	
	public void clearJDApps(){
		this.apps.clear();
	}
}
