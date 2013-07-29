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
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
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
//		String accessKey = "9c379f079214447fad2959c4621cd6feVb797oH1";
//		String secretKey = "5e998dbbafb44ca783099afcdead40fa7A3Vf7Fh";
		CloudCredentials credentials = new CloudCredentials(accessKey+"|"+secretKey);
		CloudFoundryClientExt client = new CloudFoundryClientExt(credentials, url);
		
		client.deleteApplication("spring");
		System.out.println("done");
		
//		File warFile = new File("C:/Users/Administrator/git/jae_hongsq/hello-java-1.0.war");
//		File warFile = new File("C:/Users/Administrator/git/jae_hongsq/hello-java-1.0-war");
//		client.uploadApplication("hongsq", warFile);
		
//		String result = client.getFile("hongsq", 0, "logs/stderr.log");
//		String result = client.getFile("hongsq_python_django", 0, "app");
//		System.out.println(result);
		
		
//		getApps(client);
//		ApplicationStats stats = client.getApplicationStats("hongsq");
//		System.out.println(stats);
		
		

//		RestTemplate restTemplate = client.getRestUtil().createRestTemplate(null);
//		
//		HttpHeaders headers = new HttpHeaders();
//		
//	    
////	    if (supportsRanges) {
////	      headers.set("Range", range);
////	    }
//	    HttpEntity requestEntity = new HttpEntity(headers);
//	    String urlPath = url+"/apps/{app}/instances/{instance}/files/{filePath}";
//	    String app = "hongsq";
//	    String instance = "0";
//	    String filePath = "logs";
//	    ResponseEntity responseEntity = restTemplate.exchange(urlPath, HttpMethod.GET, requestEntity, String.class, new Object[] { app, instance, filePath });
//
//	    
//	    
//		System.out.println(responseEntity.getBody());
		
//		restTemplate.
		
	}

	private static void getApps(CloudFoundryClientExt client) {
		List<CloudApplication> apps = client.getApplications();
		for (CloudApplication app : apps) {
			System.out.println(app.getName());
		}
	}

}
