package com.jae.eclipse.cloudfoundry.client;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.HttpProxyConfiguration;
import org.cloudfoundry.client.lib.util.RestUtil;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author hongshuiqiao
 * 
 */
public class RestUtilExt extends RestUtil {
	private CloudCredentials credentials;
	
	public RestUtilExt(CloudCredentials credentials) {
		this.credentials = credentials;
	}

	public RestTemplate createRestTemplate(HttpProxyConfiguration httpProxyConfiguration) {
		JAERestTemplate restTemplate = new JAERestTemplate(this.credentials);
		restTemplate.setRequestFactory(createRequestFactory(httpProxyConfiguration));
		return restTemplate;
//		return super.createRestTemplate(httpProxyConfiguration);
	}
}
