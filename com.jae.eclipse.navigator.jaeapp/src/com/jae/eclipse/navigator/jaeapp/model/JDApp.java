/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

/**
 * @author hongshuiqiao
 *
 */
public class JDApp extends AbstractJDModel {
	private User user;
	private String repositoryURL;

	public JDApp(String name) {
		super();
		this.setName(name);
	}

	public JDApp(User user, String name) {
		super();
		this.user = user;
		this.setName(name);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}
}
