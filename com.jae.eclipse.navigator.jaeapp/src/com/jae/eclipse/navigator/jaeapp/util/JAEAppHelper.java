/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.Platform;

import com.jae.eclipse.core.util.JsonHelper;
import com.jae.eclipse.navigator.jaeapp.internal.AppRepository;
import com.jae.eclipse.navigator.jaeapp.internal.JDRootSaveModel;
import com.jae.eclipse.navigator.jaeapp.model.User;

/**
 * @author hongshuiqiao
 *
 */
public class JAEAppHelper {
	private static final String JDCONFIG_USER_CONFIG = "jdconfig/user.config";
	private static List<User> users = new ArrayList<User>();
	private static Map<String, String> srcRepositoryMap = new HashMap<String, String>();
	
	static{
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load() throws IOException {
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
					User user = new User("");
					user.setDisplayName(jaeRoot.getName());
					user.setAccessKey(jaeRoot.getAccessKey());
					user.setSecretKey(jaeRoot.getSecretKey());
					
					users.add(user);
				}
			}
		} catch(FileNotFoundException e){
			//nothing to do.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void save() throws IOException {
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
			for (User user : users) {
				JDRootSaveModel root = new JDRootSaveModel();
				
				String accessKey = user.getAccessKey();
				String secretKey = user.getSecretKey();
				root.setSecretKey(secretKey);
				root.setAccessKey(accessKey);
				root.setName(user.getDisplayName());
				
				rootElements.put(accessKey+"|"+secretKey, root);
			}
			
			Set<String> keySet = srcRepositoryMap.keySet();
			for (String key : keySet) {
				String srcRepository = srcRepositoryMap.get(key);
				String[] keyArray = key.split("[|]");
				if(keyArray.length != 3)
					continue;
				
				String accessKey = keyArray[0];
				String secretKey = keyArray[1];
				
				JDRootSaveModel root = rootElements.get(accessKey+"|"+secretKey);
				if(null == root)
					continue;
				
				String name = keyArray[2];
				
				AppRepository repository = new AppRepository();
				repository.setName(name);
				repository.setUrl(srcRepository);
				List<AppRepository> list = root.getRepositories();
				if(null == list){
					list = new ArrayList<AppRepository>();
					root.setRepositories(list);
				}
				list.add(repository);
			}
			
			String json = JsonHelper.getJsonArray(rootElements.values());
			out.write(json.getBytes("UTF-8"));
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 返回所有的JAE用户
	 * @return
	 */
	public static User[] getUsers(){
		return users.toArray(new User[users.size()]);
	}

	public static void regeditUsers(User[] users){
		JAEAppHelper.users.addAll(Arrays.asList(users));
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void regeditUser(User user){
		users.add(user);
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unRegeditUsers(User[] users){
		JAEAppHelper.users.removeAll(Arrays.asList(users));
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unRegeditUser(User user){
		users.remove(user);
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearUsers(){
		users.clear();
	}
}
