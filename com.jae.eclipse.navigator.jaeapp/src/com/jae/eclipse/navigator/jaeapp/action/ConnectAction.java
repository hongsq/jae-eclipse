/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public class ConnectAction extends AbstractJDAction {
	private boolean connect;

	public ConnectAction(ISelectionProvider provider, String text, boolean connect) {
		super(provider, text);
		this.connect = connect;
		
		this.setMustSelect(true);
		this.setSelectType(User.class);
		this.setMultiable(false);
		
		this.setEnabled(false);
		
		String id = "action.user.connect";
		if(!this.connect) id = "action.user.disconnect";
		this.setId(id);
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor(id));
	}
	
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);
		
		Object[] objects = this.getStructuredSelection().toArray();
		boolean flag = this.isEnabled();
		if(!flag)
			return;
		
		for (Object object : objects) {
			User user = (User) object;
			if(this.connect)
				flag = !user.isConnected() && flag;
			else
				flag = user.isConnected() && flag;
		}
		
		this.setEnabled(flag);
	}

	@Override
	public void run() {
		TreeViewer viewer = (TreeViewer) this.getSelectionProvider();
		Object[] objects = this.getStructuredSelection().toArray();
		for (Object object : objects) {
			User user = (User) object;
			if(this.connect){
				user.connect();
			}else{
				user.disConnect();
			}
			
			viewer.refresh();
		}
	}
}
