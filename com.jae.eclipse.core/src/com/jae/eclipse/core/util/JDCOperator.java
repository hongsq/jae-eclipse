/**
 * 
 */
package com.jae.eclipse.core.util;

import java.io.IOException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * @author hongshuiqiao
 * 
 */
public class JDCOperator {
	private String url = "http://api.jd-app.com";
	private String accessKey;
	private String secretKey;
	private String version = "0.1.1";
	
	public JDCOperator(String accessKey, String secretKey) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public JDCOperator(String url, String accessKey, String secretKey) {
		super();
		this.url = url;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public static void main(String[] args) {
		String accessKey = "9f7ce80405f240938743875e332d9aa4";
		String secretKey = "65428cb05e764b4f90d95f3b8f3d53e10UFtzrPf";
		JDCOperator operator = new JDCOperator(accessKey, secretKey);
		System.out.println(operator.handle("get", "apps"));
	}

	public String handle(String httpMethodName, String path) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpUriRequest httpMethod = new HttpGet(url+"/"+path);
			if("POST".equalsIgnoreCase(httpMethodName)){
				httpMethod = new HttpPost(url);
			}else if("PUT".equalsIgnoreCase(httpMethodName)){
				httpMethod = new HttpPut(url);
			}else if("DELETE".equalsIgnoreCase(httpMethodName)){
				httpMethod = new HttpDelete(url);
			}

			String contentType = "application/json";
			httpMethod.setHeader("Accept", contentType);
			httpMethod.setHeader("Accept-Encoding", "gzip, deflate");
			httpMethod.setHeader("Authorization", accessKey);
			httpMethod.setHeader("Version", version);
			String date = "20130705T094918Z";// new SimpleDateFormat("yyyyMMdd z").format(new Date());
			System.out.println(date);// "20130705T091132Z"

			httpMethod.setHeader("Date", date);
			httpMethod.setHeader("Access-Key-Id", accessKey);
			httpMethod.setHeader("Path", path);
			httpMethod.setHeader("Content-Type", contentType);
			
			String contentMD5 = "";
			String signature = generateSignature(httpMethodName, contentMD5, contentType, date, path);
			httpMethod.setHeader("Signature", signature);

//			httpMethod.setHeader("User-Agent", "Ruby");

			HttpResponse response = httpclient.execute(httpMethod);
			HttpEntity entity = response.getEntity();

			System.out.println("get: " + response.getStatusLine());
			String entityString = EntityUtils.toString(entity, Consts.UTF_8);
			
//			System.out.println("cookies:");
//			List<Cookie> cookies = httpclient.getCookieStore().getCookies();
//			if (cookies.isEmpty()) {
//				System.out.println("None");
//			} else {
//				for (int i = 0; i < cookies.size(); i++) {
//					System.out.println("- " + cookies.get(i).toString());
//				}
//			}
			EntityUtils.consume(entity);
			
			return entityString;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return null;
	}

	/**
	 * 
	 * @param httpMethod	表示 http 的请求动作(不能为空);
	 * @param contentMD5	表示请求内容数据的 MD5 值(可以为空);
	 * @param contentType	表示请求内容的类型(可以为空);
	 * @param date			表示了此次操作的时间(不能为空);
	 * @param path			表示 http 请求的路径(可以为空)
	 * @return
	 */
	private String generateSignature(String httpMethod, String contentMD5, String contentType,
			String date, String path) {

		String stringToSign = httpMethod + "\n" + // http请求动作
				contentMD5 + "\n" + // http请求的md5值（暂时没用，目前都是空值）
				contentType + "\n" + // http请求的类型
				date + "\n" + // http请求的时间
				path; // jdc请求的路径

		//Signature = Base64(HMAC-SHA1(UTF-8-Encoding-Of(SecretKey, StringToSign ) ) );
		try {
			byte[] hmac = DataEncryptUtil.encryptHMAC(DataEncryptUtil.KEY_MAC_SHA1, stringToSign.getBytes("UTF-8"), secretKey.getBytes("UTF-8"));
			return DataEncryptUtil.encryptBASE64(hmac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
//		return "yUNUZB/mTA7fMlbpuQckjxWzOBo=";
	}

}
