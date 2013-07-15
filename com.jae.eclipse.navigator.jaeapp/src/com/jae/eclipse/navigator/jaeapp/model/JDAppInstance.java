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
 * 应用的实例
 * @author hongshuiqiao
 *
 */
public class JDAppInstance extends AbstractJDElement {
	private int instanceIndex;

	public JDAppInstance(IJDElement parent, String name) {
		super(parent, name);
	}

	public int getInstanceIndex() {
		return instanceIndex;
	}

	public void setInstanceIndex(int instanceIndex) {
		this.instanceIndex = instanceIndex;
	}

	public RemoteResource getRemoteResource(String path){
		return RemoteResourceUtil.getRemoteResource(this, path);
	}
	
	@Override
	protected void doLoad() {
		User user = JDModelUtil.getParentElement(this, User.class);
		JDApp app = JDModelUtil.getParentElement(this, JDApp.class);
		
		CloudFoundryOperations operator = user.getCloudFoundryOperations();
		
		String rootPath = "";//根路径
		String blob = null;
		try {
			blob = operator.getFile(app.getName(), this.instanceIndex, rootPath);
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
