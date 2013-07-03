/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

/**
 * @author hongshuiqiao
 *
 */
public class JDApp implements INameElement {
	private String name;
	private String repositoryURL;

	public JDApp(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}
}
