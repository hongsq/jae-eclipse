/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;
import com.jae.eclipse.navigator.jaeapp.model.AbstractJDElement;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public class DeleteAction extends AbstractJDAction {
	private List<User> users = new ArrayList<User>();
	private List<JDApp> apps = new ArrayList<JDApp>();

	public DeleteAction(ISelectionProvider provider, String text) {
		super(provider, text);
		
		this.setId("common.action.delete");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("delete"));
		this.setMustSelect(true);
		this.setMultiable(true);
		this.setSelectType(AbstractJDElement.class);
		this.setEnabled(false);
	}

	@Override
	public void selectionChanged(IStructuredSelection selection) {
		users.clear();
		apps.clear();
		
		super.selectionChanged(selection);
		if (this.isEnabled()) {
			for (Object element : selection.toArray()) {
				if (element instanceof User) {
					users.add((User) element);
				} else if (element instanceof JDApp) {
					apps.add((JDApp) element);
				}else{
					this.setEnabled(false);
					return;
				}
			}
			
			if(users.isEmpty() && apps.isEmpty()){
				this.setEnabled(false);
				return;
			}
		}
	}
	
	@Override
	public void run() {
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		StringBuilder userMessage = new StringBuilder();
		for (int i = 0; i < this.users.size(); i++) {
			if(i>0)
				userMessage.append(",");
			
			User user = this.users.get(i);
			userMessage.append(user.getName());
		}
		StringBuilder appMessage = new StringBuilder();
		for (int i = 0; i < this.apps.size(); i++) {
			if(i>0)
				appMessage.append(",");
			
			JDApp app = this.apps.get(i);
			appMessage.append(app.getName());
		}
		
		StringBuilder message = new StringBuilder();
		message.append("确定要删除选中的");
		if(userMessage.length()>0){
			message.append("用户\"");
			message.append(userMessage.toString());
			message.append("\"");
			
			if(appMessage.length()>0)
				message.append("及");
		}
		
		if(appMessage.length()>0){
			message.append("应用\"");
			message.append(appMessage.toString());
			message.append("\"");
		}
		message.append("吗?");
		if(!MessageDialog.openConfirm(viewer.getControl().getShell(), "确认", message.toString())){
			return;
		}
		
		if(!this.apps.isEmpty()){
			Set<User> refreshUser = new HashSet<User>();
			for (JDApp app : this.apps) {
				User user = JDModelUtil.getParentElement(app, User.class);
				CloudFoundryClientExt client = user.getCloudFoundryClient();
				client.deleteApplication(app.getName());
				refreshUser.add(user);
			}
			for (User user : refreshUser) {
				user.refresh();
			}
		}
		
		if(!this.users.isEmpty()){
			JAEAppHelper.unRegeditUsers(users.toArray(new User[users.size()]));
		}
		
		viewer.refresh();
	}
}
