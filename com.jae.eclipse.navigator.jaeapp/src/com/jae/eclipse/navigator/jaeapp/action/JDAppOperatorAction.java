/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import com.jae.eclipse.core.util.JDCOperator;
import com.jae.eclipse.core.util.JsonHelper;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppOperatorAction extends AbstractJDAction {
	private boolean start;

	public JDAppOperatorAction(ISelectionProvider provider, String text, boolean start) {
		super(provider, text);
		this.start = start;
		
		String id = "jdapp.action.stop";
		if(this.start)
			id = "jdapp.action.start";
		
		this.setId(id);
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor(id));
		this.setMustSelect(true);
		this.setMultiable(true);
		this.setSelectType(JDApp.class);
	}
	
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);
		
		Object[] objects = this.getStructuredSelection().toArray();
		boolean flag = this.isEnabled();
		if(!flag)
			return;
		
		for (Object object : objects) {
			JDApp app = (JDApp) object;
			if(this.start)
				flag = !app.isStarted() && flag;
			else
				flag = app.isStarted() && flag;
		}
		
		this.setEnabled(flag);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run() {
		Object[] objects = this.getStructuredSelection().toArray();
		for (Object object : objects) {
			JDApp app = (JDApp) object;
			User user = (User) app.getParent();
			JDCOperator operator = new JDCOperator(user.getAccessKey(), user.getSecretKey());
			
			String path = "apps/"+app.getName();
			String getResult = operator.handle("get", path);
			Map map = JsonHelper.toJavaObject(getResult, HashMap.class);
			
			if("STARTED".equalsIgnoreCase((String) map.get("state")) && !this.start){
				map.put("state", "STOPPED");
				String result = operator.handleWithString("put", path, JsonHelper.getJson(map));
				System.out.println(result);
			}else if("STOPPED".equalsIgnoreCase((String) map.get("state")) && this.start){
				map.put("state", "STARTED");
				String result = operator.handleWithString("put", path, JsonHelper.getJson(map));
				System.out.println(result);
			}
			
			StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
			viewer.refresh(app);
		}
	}
}
