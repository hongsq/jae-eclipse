/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hongshuiqiao
 *
 */
public class JDRootSaveModel {
	private List<AppRepository> repositories = new ArrayList<AppRepository>();
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
	public void addAppRepository(AppRepository appRepository){
		this.repositories.add(appRepository);
	}
	public AppRepository[] getRepositories() {
		return this.repositories.toArray(new AppRepository[this.repositories.size()]);
	}
	public void setRepositories(AppRepository[] repositories) {
		if(null != repositories){
			this.repositories.addAll(Arrays.asList(repositories));
		}
	}
}
