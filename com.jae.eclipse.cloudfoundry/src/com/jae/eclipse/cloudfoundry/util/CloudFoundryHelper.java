/**
 * 
 */
package com.jae.eclipse.cloudfoundry.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.client.lib.CloudCredentials;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;

/**
 * @author hongshuiqiao
 *
 */
public class CloudFoundryHelper {
	private static Map<String, CloudFoundryClientExt> clientMap = new HashMap<String, CloudFoundryClientExt>();
	private static URL url;
	static{
		try {
			url = new URL("http://api.jd-app.com");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static CloudFoundryClientExt getCloudFoundryClient(String accessKey, String secretKey){
		String token = accessKey+"|"+secretKey;
		CloudFoundryClientExt cloudFoundryClient = clientMap.get(token);
		if(null == cloudFoundryClient){
			CloudCredentials credentials = new CloudCredentials(token);
			cloudFoundryClient = new CloudFoundryClientExt(credentials, url);
			clientMap.put(token, cloudFoundryClient);
		}
		return cloudFoundryClient;
	}
}
