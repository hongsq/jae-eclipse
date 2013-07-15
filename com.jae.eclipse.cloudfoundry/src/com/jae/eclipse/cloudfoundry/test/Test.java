/**
 * 
 */
package com.jae.eclipse.cloudfoundry.test;

import java.net.URL;
import java.util.List;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.domain.CloudApplication;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;

/**
 * @author hongshuiqiao
 *
 */
public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://api.jd-app.com");
//		String email = "hongshuiqiao@jd.com";
//		String password = "hongshuiqiao";
//		String version = "0.1.1";
		String accessKey = "9f7ce80405f240938743875e332d9aa4";
		String secretKey = "65428cb05e764b4f90d95f3b8f3d53e10UFtzrPf";
		CloudCredentials credentials = new CloudCredentials(accessKey+"|"+secretKey);
		CloudFoundryClientExt client = new CloudFoundryClientExt(credentials, url);

//		File warFile = new File("C:/Users/Administrator/git/jae_hongsq/hello-java-1.0.war");
//		File warFile = new File("C:/Users/Administrator/git/jae_hongsq/hello-java-1.0-war");
//		client.uploadApplication("hongsq", warFile);
		
//		String result = client.getFile("hongsq", 0, "logs/stderr.log");
		String result = client.getFile("hongsq_python_django", 0, "app");
		System.out.println(result);
		
		
//		getApps(client);
//		ApplicationStats stats = client.getApplicationStats("hongsq");
//		System.out.println(stats);
	}

	private static void getApps(CloudFoundryClientExt client) {
		List<CloudApplication> apps = client.getApplications();
		for (CloudApplication app : apps) {
			System.out.println(app.getName());
		}
	}

}
