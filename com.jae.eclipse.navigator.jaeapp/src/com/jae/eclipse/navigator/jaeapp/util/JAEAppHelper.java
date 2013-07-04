/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.util;

import java.util.ArrayList;
import java.util.List;

import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFile;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFolder;
import com.jae.eclipse.navigator.jaeapp.model.User;

/**
 * @author hongshuiqiao
 *
 */
public class JAEAppHelper {
	private static List<User> users = new ArrayList<User>();
	
	static{
		//TODO 测试数据
		for (int i = 0; i < 3; i++) {
			User user = new User("user"+i);
			user.setPassword("password"+i);
			user.setDescription("description-user"+i);
			
			for (int j = 0; j < 2; j++) {
				JDApp app = new JDApp(user, "app"+i+j);
				app.setRepositoryURL("repositoryURL"+i+j);
				app.setDescription(app.getRepositoryURL());
				
				for (int k = 0; k < 5; k++) {
					if(k%2==0){
						RemoteFolder folder = new RemoteFolder(app, null, "folder"+k);
						
						for (int l = 0; l < 5; l++) {
							RemoteFile file = new RemoteFile(app, folder, "file"+k+l);
							folder.addChild(file);
						}
						
						app.addChild(folder);
					}else{
						RemoteFile file = new RemoteFile(app, null, "file"+k);
						app.addChild(file);
					}
				}
				
				
				user.addChild(app);
			}
			
			users.add(user);
		}
	}

	/**
	 * 返回所有的JAE用户
	 * @return
	 */
	public static User[] getUsers(){
		return users.toArray(new User[users.size()]);
	}

}
