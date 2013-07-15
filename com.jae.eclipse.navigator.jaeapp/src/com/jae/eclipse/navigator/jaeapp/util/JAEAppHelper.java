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

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.domain.CloudInfo;
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
			Object[] loadUsers = JsonHelper.toJavaArray(json);
			if(null != loadUsers){
				for (Object object : loadUsers) {
					if (object instanceof DynaBean) {
						DynaBean userBean = (DynaBean) object;
						
						User user = new User((String) userBean.get("name"));
						user.setDisplayName((String) userBean.get("displayName"));
						user.setDescription((String) userBean.get("description"));
						user.setAccessKey((String) userBean.get("accessKey"));
						user.setSecretKey((String) userBean.get("secretKey"));
						users.add(user);
						
						CloudFoundryOperations operator = user.getCloudFoundryOperations();
						CloudInfo info = operator.getCloudInfo();
						
						user.setName(info.getUser());
						user.setDisplayName(user.getName());
						
//						Object appObjects = userBean.get("apps");
//						if(null != appObjects && appObjects.getClass().isArray()){
//							for (Object appObject : ((Object[])appObjects)) {
//								if (appObject instanceof type) {
//									type new_name = (type) appObject;
//									
//								}
//							}
//						}
					}
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
				
				String name = user.getName();
				if(null != name) userMap.put("name", name);
				String displayName = user.getDisplayName();
				if(null != displayName) userMap.put("displayName", displayName);
				String description = user.getDescription();
				if(null != description) userMap.put("description", description);
				String accessKey = user.getAccessKey();
				if(null != accessKey) userMap.put("accessKey", accessKey);
				String secretKey = user.getSecretKey();
				if(null != secretKey) userMap.put("secretKey", secretKey);
				
				list.add(userMap);
			}
			
			String json = JsonHelper.getJsonArray(list);
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
