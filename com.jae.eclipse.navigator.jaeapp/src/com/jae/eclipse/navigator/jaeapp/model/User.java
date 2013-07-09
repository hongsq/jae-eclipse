/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import java.text.MessageFormat;
import java.util.Map;

import com.jae.eclipse.core.util.JDCOperator;
import com.jae.eclipse.core.util.JsonHelper;


/**
 * @author hongshuiqiao
 *
 */
public class User extends AbstractJDElement {
	private String accessKey;
	private String secretKey;
	
	public User(String name) {
		super(null, name);
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

	@Override
	protected void doLoadChildren() {
		JDCOperator operator = new JDCOperator(accessKey, secretKey);
		String result = operator.handle("get", "apps");
		
		Map<?, ?>[] maps = JsonHelper.toJavaArray(result);
		for (Map<?, ?> map : maps) {
			String appName = (String) map.get("name");
			if(null != appName){
				JDApp app = new JDApp(this, appName);
				String repositoryURL = MessageFormat.format("https://code.jd.com/{0}/jae_{1}.git", this.getName(), appName);
				app.setRepositoryURL(repositoryURL);
				app.setDisplayName(appName+" - "+repositoryURL);
				this.addChild(app);
			}
		}
	}
}
