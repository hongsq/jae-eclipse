/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.view;

import java.lang.reflect.Method;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

import com.jae.eclipse.core.util.ObjectUtil;
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
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		
		final CommonViewer commonViewer = this.getCommonViewer();
		
		commonViewer.getTree().addListener(SWT.MouseUp, new Listener() {
			
			public void handleEvent(Event event) {
				try {
					Point point = new Point(event.x, event.y);
					TreeItem item = commonViewer.getTree().getItem(point);
					ISelection selection = StructuredSelection.EMPTY;
					if(null != item){
						selection = commonViewer.getSelection();
					}
					
					Method method = ObjectUtil.getMethod(commonViewer.getClass(), "updateSelection", false, ISelection.class);
					if(null != method){
						method.setAccessible(true);
						method.invoke(commonViewer, selection);
					}
				} catch (Exception e) {}
			}
		});
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
