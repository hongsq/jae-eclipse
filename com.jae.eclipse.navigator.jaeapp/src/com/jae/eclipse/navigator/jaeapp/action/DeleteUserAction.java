/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.navigator.CommonViewer;

import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public class DeleteUserAction extends AbstractJDAction {

	public DeleteUserAction(ISelectionProvider provider, String text) {
		super(provider, text);
		
		this.setId("user.action.remove");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("user.action.remove"));
		
		this.setMustSelect(true);
		this.setMultiable(true);
		this.setSelectType(User.class);
		this.setEnabled(false);
	}

	@Override
	public void run() {
		Object[] objects = this.getStructuredSelection().toArray();
		
		User[] users = new User[objects.length];
		System.arraycopy(objects, 0, users, 0, objects.length);
		
		JAEAppHelper.unRegeditUsers(users);
		
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		viewer.refresh();
	}
}
