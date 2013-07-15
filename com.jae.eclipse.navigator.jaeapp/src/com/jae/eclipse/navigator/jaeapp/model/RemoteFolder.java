/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;
import com.jae.eclipse.navigator.jaeapp.util.RemoteResourceUtil;

/**
 * @author hongshuiqiao
 *
 */
public class RemoteFolder extends RemoteResource {

	public RemoteFolder(IJDElement parent, String name) {
		super(parent, name);
		this.setImageID("folder");
	}

	public RemoteResource getRemoteResource(String path){
		return RemoteResourceUtil.getRemoteResource(this, path);
	}

	public RemoteResource[] getSubResources(){
		return this.getChildren(RemoteResource.class);
	}
	
	@Override
	protected void doLoad() {
		User user = JDModelUtil.getParentElement(this, User.class);
		JDApp app = JDModelUtil.getParentElement(this, JDApp.class);
		JDAppInstance instance = JDModelUtil.getParentElement(this, JDAppInstance.class);
		
		CloudFoundryOperations operator = user.getCloudFoundryOperations();
		
		String blob = null;
		try {
			blob = operator.getFile(app.getName(), instance.getInstanceIndex(), this.getPath());
		} catch (Exception e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "错误", "获取远程资源时出错，请稍后再试");
			throw e;
		}
		
		String[] files = blob.split("\n");
		for (int i = 0; i < files.length; i++) {
			String[] content = files[i].split("\\s+");
			String name = content[0];
			if (name.trim().length() > 0) {
				RemoteResource resource = null;
				if (name.endsWith("/")) {
					name = name.substring(0, name.length() - 1);
					resource = new RemoteFolder(this, name);
				}else{
					resource = new RemoteFile(this, name);
				}
				
				this.addChild(resource);
			}
		}
	
	}
}
