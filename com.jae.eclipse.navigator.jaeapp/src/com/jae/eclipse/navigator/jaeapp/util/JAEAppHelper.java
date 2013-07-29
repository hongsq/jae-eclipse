/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.lib.domain.Staging;
import org.eclipse.core.runtime.Platform;

import com.jae.eclipse.core.util.JsonHelper;
import com.jae.eclipse.navigator.jaeapp.internal.AppRepository;
import com.jae.eclipse.navigator.jaeapp.internal.JDRootSaveModel;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.User;

/**
 * @author hongshuiqiao
 *
 */
public class JAEAppHelper {
	private static final String JDCONFIG_USER_CONFIG = "jdconfig/user.config";
	private static Map<String, User> users = new LinkedHashMap<String, User>();
	private static Map<String, String> srcRepositoryMap = new HashMap<String, String>();
	private static Map<String, Staging> supportStaging = new HashMap<String, Staging>();
	
	static{
		supportStaging.put("Java 1.6.0", new Staging("java", "java_web"));
		supportStaging.put("Java Spring 3.0.0", new Staging("java", "spring"));
		supportStaging.put("Node 0.8", new Staging("node08", "node"));
		supportStaging.put("Ruby Sinatra 1.3.2", new Staging("ruby19", "sinatra"));
		supportStaging.put("Ruby on Rails 3.2.9", new Staging("ruby19", "rails3"));
		supportStaging.put("PHP 5.3.10", new Staging("php", "php"));
		supportStaging.put("Python WSGI 2.7.3", new Staging("python2", "wsgi"));
		supportStaging.put("Python Django 1.5.1", new Staging("python2", "django"));
		
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized static void load() throws IOException {
		URL url = Platform.getConfigurationLocation().getDataArea(JDCONFIG_USER_CONFIG);
		if(null == url)
			return;
		
		try {
			String json = IOUtils.toString(url.openStream(), "UTF-8");
			if(null == json || "".equals(json.trim()))
				return;
			
			JDRootSaveModel[] rootElements = JsonHelper.toJavaArray(json, JDRootSaveModel.class);
			if(null != rootElements){
				for (JDRootSaveModel jaeRoot : rootElements) {
					User user = new User(jaeRoot.getName());
					user.setAccessKey(jaeRoot.getAccessKey());
					user.setSecretKey(jaeRoot.getSecretKey());
					
					users.put(createKey(user.getAccessKey(), user.getSecretKey()), user);
					
					AppRepository[] apps = jaeRoot.getRepositories();
					if(null == apps || apps.length <= 0)
						continue;
					
					for (AppRepository app : apps) {
						String key = createKey(user)+"|"+app.getName();
						srcRepositoryMap.put(key, app.getUrl());
					}
				}
			}
		} catch(FileNotFoundException e){
			//nothing to do.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static void save() throws IOException {
		URL url = Platform.getConfigurationLocation().getDataArea(JDCONFIG_USER_CONFIG);
		if(null == url)
			return;
		
		FileOutputStream out = null;
		File file = new File(url.getFile());
		if(!file.exists()){
			File parent = file.getParentFile();
			if(null != parent && !parent.exists())
				parent.mkdirs();
			
			file.createNewFile();
		}
		
		try {
			out = new FileOutputStream(file);
			Map<String, JDRootSaveModel> rootElements = new HashMap<String, JDRootSaveModel>();
			for (User user : users.values()) {
				JDRootSaveModel root = new JDRootSaveModel();
				
				String accessKey = user.getAccessKey();
				String secretKey = user.getSecretKey();
				root.setSecretKey(secretKey);
				root.setAccessKey(accessKey);
				root.setName(user.getName());
				
				rootElements.put(createKey(accessKey, secretKey), root);
			}
			
			Set<String> keySet = srcRepositoryMap.keySet();
			for (String key : keySet) {
				String srcRepository = srcRepositoryMap.get(key);
				String[] keyArray = key.split("[|]");
				if(keyArray.length != 3)
					continue;
				
				String accessKey = keyArray[0];
				String secretKey = keyArray[1];
				
				JDRootSaveModel root = rootElements.get(createKey(accessKey, secretKey));
				if(null == root)
					continue;
				
				String name = keyArray[2];
				
				AppRepository repository = new AppRepository();
				repository.setName(name);
				repository.setUrl(srcRepository);
				root.addAppRepository(repository);
			}
			
			String json = JsonHelper.getJsonArray(rootElements.values());
			out.write(json.getBytes("UTF-8"));
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	private static String createKey(String accessKey, String secretKey) {
		return accessKey+"|"+secretKey;
	}
	
	private static String createKey(User user) {
		return user.getAccessKey()+"|"+user.getSecretKey();
	}

	public static User getUser(String accessKey, String secretKey){
		return users.get(createKey(accessKey, secretKey));
	}
	
	/**
	 * 返回所有的JAE用户
	 * @return
	 */
	public static User[] getUsers(){
		return users.values().toArray(new User[users.size()]);
	}

	public static void regeditUsers(User[] users){
		for (User user : users) {
			JAEAppHelper.users.put(createKey(user), user);
		}
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void regeditUser(User user){
		JAEAppHelper.users.put(createKey(user), user);
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unRegeditUsers(User[] users){
		for (User user : users) {
			JAEAppHelper.users.remove(createKey(user));
		}
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unRegeditUser(User user){
		JAEAppHelper.users.remove(createKey(user));
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearUsers(){
		users.clear();
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void regeditAppRepository(JDApp app){
		User user = JDModelUtil.getParentElement(app, User.class);
		srcRepositoryMap.put(createKey(user)+"|"+app.getName(), app.getRepositoryURL());
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unRegeditAppRepository(JDApp app){
		User user = JDModelUtil.getParentElement(app, User.class);
		srcRepositoryMap.remove(createKey(user)+"|"+app.getName());
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void regeditAppRepositories(JDApp[] apps){
		for (JDApp app : apps) {
			User user = JDModelUtil.getParentElement(app, User.class);
			srcRepositoryMap.put(createKey(user)+"|"+app.getName(), app.getRepositoryURL());
		}
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unRegeditAppRepositories(JDApp[] apps){
		for (JDApp app : apps) {
			User user = JDModelUtil.getParentElement(app, User.class);
			srcRepositoryMap.remove(createKey(user)+"|"+app.getName());
		}
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getAppRepository(JDApp app){
		User user = JDModelUtil.getParentElement(app, User.class);
		return srcRepositoryMap.get(createKey(user)+"|"+app.getName());
	}
	
	public static String getDefaultAppRepository(JDApp app){
		User user = JDModelUtil.getParentElement(app, User.class);
		return MessageFormat.format("https://code.jd.com/{0}/jae_{1}.git", user.getName(), app.getName());
	}
	
	public static Staging getStaging(String stagingName){
		return supportStaging.get(stagingName);
	}
	
	public static String[] getSupportStagingNames(){
		return supportStaging.keySet().toArray(new String[supportStaging.size()]);
	}
	
	public static Map<String, Staging> getSupportStagings(){
		return Collections.unmodifiableMap(supportStaging);
	}
}
