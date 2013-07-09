/**
 * 
 */
package com.jae.eclipse.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author hongshuiqiao
 * 
 */
public class CopyOfJDCOperator {
	private String url = "http://api.jd-app.com/info";
	private String accessKey = "9f7ce80405f240938743875e332d9aa4";
	private String secretKey = "65428cb05e764b4f90d95f3b8f3d53e10UFtzrPf";
//	private String version = "0.1.1";
	
	public static void main(String[] args) {
		CopyOfJDCOperator operator = new CopyOfJDCOperator();
		System.out.println(operator.handle("get", "apps"));
	}

	public boolean handle(String httpMethodName, String path) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpUriRequest httpMethod = new HttpGet(url);
			if("POST".equalsIgnoreCase(httpMethodName)){
				httpMethod = new HttpPost(url);
			}else if("PUT".equalsIgnoreCase(httpMethodName)){
				httpMethod = new HttpPut(url);
			}else if("DELETE".equalsIgnoreCase(httpMethodName)){
				httpMethod = new HttpDelete(url);
			}

			String contentType = "application/json";
//			httpMethod.setHeader("Accept", contentType);
//			httpMethod.setHeader("Accept-Encoding", "gzip, deflate");
//			httpMethod.setHeader("Authorization", accessKey);
//			httpMethod.setHeader("Version", version);
			String date = "20130705T094918Z";// new SimpleDateFormat("yyyyMMdd z").format(new Date());
//			System.out.println(date);// "20130705T091132Z"

//			httpMethod.setHeader("Date", date);
//			httpMethod.setHeader("Access-Key-Id", accessKey);
//			httpMethod.setHeader("Path", path);
//			httpMethod.setHeader("Content-Type", contentType);
			
			String contentMD5 = "";
			String stringToSign = buildStringToSign(httpMethodName, contentMD5, contentType, date, path);
			String signature = generateSignature(stringToSign);
//			httpMethod.setHeader("Signature", signature);

//			httpMethod.setHeader("User-Agent", "Ruby");
			
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("serviceType", "jss"));
			nvps.add(new BasicNameValuePair("accessKey", accessKey));
			nvps.add(new BasicNameValuePair("signature", signature));
			nvps.add(new BasicNameValuePair("stringToSign", stringToSign));
			
			if (httpMethod instanceof HttpEntityEnclosingRequest) {
				HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) httpMethod;
				request.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
			}

			HttpResponse response = httpclient.execute(httpMethod);
			HttpEntity entity = response.getEntity();

			System.out.println("get: " + response.getStatusLine());

			if(null != entity)
				System.out.println("entity:"+EntityUtils.toString(entity, Consts.UTF_8));
			System.out.println("cookies:");
			List<Cookie> cookies = httpclient.getCookieStore().getCookies();
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					System.out.println("- " + cookies.get(i).toString());
				}
			}
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return false;
	}

	/**
	 * @param stringToSign
	 * @return
	 */
	private String generateSignature(String stringToSign) {
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

	/**
	 * 
	 * @param httpMethod	表示 http 的请求动作(不能为空);
	 * @param contentMD5	表示请求内容数据的 MD5 值(可以为空);
	 * @param contentType	表示请求内容的类型(可以为空);
	 * @param date			表示了此次操作的时间(不能为空);
	 * @param path			表示 http 请求的路径(可以为空)
	 * @return
	 */
	private String buildStringToSign(String httpMethod, String contentMD5,
			String contentType, String date, String path) {
		String stringToSign = httpMethod + "\n" + // http请求动作
				contentMD5 + "\n" + // http请求的md5值（暂时没用，目前都是空值）
				contentType + "\n" + // http请求的类型
				date + "\n" + // http请求的时间
				path; // jdc请求的路径
		return stringToSign;
	}

}
