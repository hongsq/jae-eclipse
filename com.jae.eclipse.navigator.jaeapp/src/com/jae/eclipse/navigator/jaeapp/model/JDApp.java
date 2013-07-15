/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.jae.eclipse.core.util.JDCOperator;
import com.jae.eclipse.core.util.JsonHelper;
import com.jae.eclipse.navigator.jaeapp.util.RemoteResourceUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JDApp extends AbstractJDElement {
	private String repositoryURL;
	private String model;
	private boolean started = false;

	public JDApp(User user, String name) {
		super(user, name);
	}

	public String getRepositoryURL() {
		return repositoryURL;
	}

	public void setRepositoryURL(String repositoryURL) {
		this.repositoryURL = repositoryURL;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public RemoteResource getRemoteResource(String path){
		return RemoteResourceUtil.getRemoteResource(this, null, path);
	}

	@Override
	protected void doLoad() {
		User user = (User) this.getParent();
		JDCOperator operator = new JDCOperator(user.getAccessKey(), user.getSecretKey());
		
		String result = operator.handle("get", "apps/"+this.getName());
		if(null == result){
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			MessageDialog.openConfirm(shell, "操作失败", "获取应用\""+this.getName()+"\"信息失败！");
			return;
		}
		
		Map<?, ?> map = JsonHelper.toJavaObject(result, HashMap.class);
		
		Object staging = map.get("staging");
		if (staging instanceof DynaBean) {
			this.setModel((String)((DynaBean) staging).get("model"));
		}else if(staging instanceof Map){
			this.setModel((String)((Map<?, ?>) staging).get("model"));
		}
		
		this.setImageID("jdapp.model."+this.getModel());
		String state = (String) map.get("state");
		if("STARTED".equalsIgnoreCase(state))
			this.setStarted(true);
		else
			this.setStarted(false);
		
	}
}
