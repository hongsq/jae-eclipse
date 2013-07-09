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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.Platform;

import com.jae.eclipse.core.util.JsonHelper;
import com.jae.eclipse.navigator.jaeapp.model.User;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class JAEAppHelper {
	private static List<User> users = new ArrayList<User>();
	
	static{
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load() throws IOException {
		URL url = Platform.getConfigurationLocation().getDataArea("jdconfig/user.xml");
		if(null == url)
			return;
		
		try {
			String json = IOUtils.toString(url.openStream(), "UTF-8");
			Map[] loadUsers = JsonHelper.toJavaArray(json);
			if(null != loadUsers){
				for (Map userMap : loadUsers) {
					User user = new User((String) userMap.get("name"));
					user.setDisplayName((String) userMap.get("displayName"));
					user.setDescription((String) userMap.get("description"));
					user.setAccessKey((String) userMap.get("accessKey"));
					user.setSecretKey((String) userMap.get("secretKey"));
					users.add(user);
				}
			}
		} catch(FileNotFoundException e){
			//nothing to do.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void save() throws IOException {
		URL url = Platform.getConfigurationLocation().getDataArea("jdconfig/user.xml");
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
			List<Map<?, ?>> list = new ArrayList<>();
			for (User user : users) {
				Map userMap = new LinkedHashMap<>();
				userMap.put("name", user.getName());
				userMap.put("displayName", user.getDisplayName());
				userMap.put("description", user.getDescription());
				userMap.put("accessKey", user.getAccessKey());
				userMap.put("secretKey", user.getSecretKey());
				list.add(userMap);
			}
			
			String json = JsonHelper.getJsonArray(users);
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
