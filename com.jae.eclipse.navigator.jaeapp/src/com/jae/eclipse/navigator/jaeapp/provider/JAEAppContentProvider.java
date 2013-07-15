/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.provider;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.jae.eclipse.navigator.jaeapp.model.JDAppInstance;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFile;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFolder;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JAEAppContentProvider implements ITreeContentProvider {
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return this.getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IWorkspaceRoot) {
			return JAEAppHelper.getUsers();
		}
		
		IWorkbenchAdapter adapter = JAEAppUtil.getWorkbenchAdapter(parentElement);
		return adapter.getChildren(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		IWorkbenchAdapter adapter = JAEAppUtil.getWorkbenchAdapter(element);
		return adapter.getParent(element);
	}

	@Override
	public boolean hasChildren(Object element) {
		if ((element instanceof IWorkspaceRoot)
				|| (element instanceof JDAppInstance)
				|| (element instanceof RemoteFolder)) {
			return true;
		}
		
		if((element instanceof RemoteFile))
			return false;
		
		IWorkbenchAdapter adapter = JAEAppUtil.getWorkbenchAdapter(element);
		return adapter.getChildren(element).length>0;
	}

}
