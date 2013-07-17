/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.view;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;

import com.jae.eclipse.navigator.jaeapp.RemoteFileInput;
import com.jae.eclipse.navigator.jaeapp.RemoteFileStorage;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFile;

/**
 * @author hongshuiqiao
 *
 */
public class JAEAppView extends CommonNavigator {
	private static JAEAppView instance;

	public JAEAppView() {
		super();
		JAEAppView.instance = this;
	}

	public static JAEAppView getInstance() {
		return instance;
	}
	
	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		super.handleDoubleClick(anEvent);
		
		IStructuredSelection selection = (IStructuredSelection) anEvent.getSelection();
		Object element = selection.getFirstElement();
		if (element instanceof RemoteFile) {
			RemoteFile file = (RemoteFile) element;
			
			String editorId = "org.eclipse.ui.DefaultTextEditor";
			RemoteFileInput input = new RemoteFileInput(new RemoteFileStorage(file));
			try {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, editorId);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}
}
