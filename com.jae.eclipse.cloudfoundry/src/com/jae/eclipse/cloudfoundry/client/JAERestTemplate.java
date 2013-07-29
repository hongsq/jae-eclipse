/**
 * 
 */
package com.jae.eclipse.cloudfoundry.client;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.rest.LoggingRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import com.jae.eclipse.core.util.DataEncryptUtil;
import com.jae.eclipse.core.util.ObjectUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JAERestTemplate extends LoggingRestTemplate {
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HHmmss");
	private String version = "0.1.1";
	private String accessKey;
	private String secretKey;
	
	public JAERestTemplate(CloudCredentials credentials) {
		String token = credentials.getToken();
		String[] keys = token.split("[|]");
		this.accessKey = keys[0];
		this.secretKey = keys[1];
	}

	@Override
	protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
		ClientHttpRequest request = super.createRequest(url, method);

		String methodName = method.name().toLowerCase();
		initJAEHeaders(request, methodName);
		return request;
	}
	
	@Override
	protected <T> T doExecute(URI uri, final HttpMethod method, final RequestCallback requestCallback, final ResponseExtractor<T> responseExtractor) throws RestClientException {
		RequestCallback newRequestCallback = requestCallback;
		if(null != requestCallback){
			newRequestCallback = new RequestCallback() {
				public void doWithRequest(ClientHttpRequest request) throws IOException {
					initJAERequest(method, request, requestCallback);
				}
			};
		}
		
		ResponseExtractor<T> newResponseExtractor = responseExtractor;
		if(null != responseExtractor){
			newResponseExtractor = new ResponseExtractor<T>() {
				public T extractData(ClientHttpResponse response) throws IOException {
					T extractData = initJAEResponse(response, responseExtractor);
					return extractData;
				}
			};
		}
		
		return super.doExecute(uri, method, newRequestCallback, newResponseExtractor);
	}
	
	private void initJAERequest(HttpMethod method, ClientHttpRequest request, RequestCallback requestCallback) throws IOException {
		if(null != requestCallback)
			requestCallback.doWithRequest(request);
		
//		String methodName = method.name().toLowerCase();
//		initJAEHeaders(request, methodName);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> T initJAEResponse(ClientHttpResponse response, ResponseExtractor<T> responseExtractor) throws IOException {
		try {
			if(needUseZip(response)){
				InputStream in = new GZIPInputStream(response.getBody());
				
				Charset charset = getContentTypeCharset(response.getHeaders().getContentType());
				
				String result = IOUtils.toString(in, charset.name());
//			System.out.println(result);
				return (T)new ResponseEntity(result, response.getStatusCode());
			}else{
				return responseExtractor.extractData(response);
			}
		} finally {
//			try {
//				//已经从响应出取出结果了，则应该关闭连接
//				Field httpMethodField = ObjectUtil.getField(response.getClass(), "httpMethod", false);
//				if(null != httpMethodField){
//					httpMethodField.setAccessible(true);
//					Object object = httpMethodField.get(response);
//					if (object instanceof org.apache.commons.httpclient.HttpMethod) {
//						org.apache.commons.httpclient.HttpMethod httpMethod = (org.apache.commons.httpclient.HttpMethod) object;
//						httpMethod.abort();
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	}
	
	private boolean needUseZip(ClientHttpResponse response) throws IOException {
		InputStream in = response.getBody();
		if(null == in)
			return false;
					
		HttpHeaders headers = response.getHeaders();
		List<String> contentEncoding = headers.get("Content-Encoding");
		if(null == contentEncoding || !contentEncoding.contains("gzip"))
			return false;
		
		try {
			Field httpMethodField = ObjectUtil.getField(response.getClass(), "httpMethod", false);
			if(null == httpMethodField)
				return false;
			
			httpMethodField.setAccessible(true);
			Object object = httpMethodField.get(response);
			if (!(object instanceof org.apache.commons.httpclient.HttpMethod)) {
				return false;
			}
			org.apache.commons.httpclient.HttpMethod httpMethod = (org.apache.commons.httpclient.HttpMethod) object;
			String path = httpMethod.getPath();
			
			// path: /apps/{app}/instances/{instance}/files/{filePath}
			
			String[] pathSegments = path.split("[/]");
			if(pathSegments.length > 5 
					&& "".equals(pathSegments[0]) 
					&& "apps".equals(pathSegments[1])
					&& "instances".equals(pathSegments[3])
					&& "files".equals(pathSegments[5])
				){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private void initJAEHeaders(ClientHttpRequest request, String methodName) {
		HttpHeaders headers = request.getHeaders();
		
		List<String> contentTypes = headers.get("Content-Type");
		String contentType = null;
		if(null == contentTypes || contentTypes.isEmpty()){
			contentType = "application/json";
			headers.set("Content-Type", contentType);
		}else{
			contentType = contentTypes.get(0);
			if(contentType.contains(";"))
				contentType = contentType.substring(0, contentType.indexOf(";")).trim();
		}
//		if("post".equalsIgnoreCase(methodName))
//			contentType = "multipart/form-data; boundary=219348";
		
		String path = request.getURI().getPath();
		while(null !=path && path.startsWith("/"))
			path = path.substring(1);
		
		headers.set("Accept-Encoding", "gzip, deflate");
		headers.set("Authorization", accessKey);
		headers.set("Version", version);
		Date currentDateTime = new Date();
		String date = DATE_FORMAT.format(currentDateTime)+"T"+TIME_FORMAT.format(currentDateTime)+"Z";
//		String date = "20130716T101533Z";
//		System.out.println(date);// "20130705T091132Z"

		headers.set("Date", date);
		headers.set("Access-Key-Id", accessKey);
		headers.set("Path", path);
		headers.set("Connection", "close");//使用短连接
//		headers.set("Connection", "Keep-Alive");//使用长连接
//		headers.set("Keep-Alive", "timeout=20");
		
		String contentMD5 = "";
//		if(null != requestEntity){
//			requestEntity.toString();
//		}
		String signature = generateSignature(methodName, contentMD5, contentType, date, path);
		headers.set("Signature", signature);
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
	}
	
	private Charset getContentTypeCharset(MediaType contentType) {
	    if ((contentType != null) && (contentType.getCharSet() != null)) {
	      return contentType.getCharSet();
	    }

	    return Charset.defaultCharset();
	  }
}
