/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;

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
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		Object[] objects = this.getStructuredSelection().toArray();
		
		User[] users = new User[objects.length];
		System.arraycopy(objects, 0, users, 0, objects.length);
		
		if(!MessageDialog.openConfirm(viewer.getControl().getShell(), "确认", "确定要删除选中的用户吗？")){
			return;
		}
		
		JAEAppHelper.unRegeditUsers(users);
		
		viewer.refresh();
	}
}
