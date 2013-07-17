/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.internal;

import java.util.List;

/**
 * @author hongshuiqiao
 *
 */
public class JDRootSaveModel {
	private List<AppRepository> repositories;
	private String name;
	private String accessKey;
	private String secretKey;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public List<AppRepository> getRepositories() {
		return repositories;
	}
	public void setRepositories(List<AppRepository> repositories) {
		this.repositories = repositories;
	}
}
