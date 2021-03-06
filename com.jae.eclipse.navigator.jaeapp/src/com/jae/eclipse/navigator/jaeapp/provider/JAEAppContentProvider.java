/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.provider;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.IWorkbenchAdapter;

import com.jae.eclipse.navigator.jaeapp.model.AbstractJDElement;
import com.jae.eclipse.navigator.jaeapp.model.IJDElement;
import com.jae.eclipse.navigator.jaeapp.model.JDAppInstance;
import com.jae.eclipse.navigator.jaeapp.model.LoadState;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFile;
import com.jae.eclipse.navigator.jaeapp.model.RemoteFolder;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppUtil;
import com.jae.eclipse.navigator.jaeapp.view.JAEView;

/**
 * @author hongshuiqiao
 *
 */
public class JAEAppContentProvider implements ITreeContentProvider {
	
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getElements(Object inputElement) {
		return this.getChildren(inputElement);
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IWorkspaceRoot) {
			return JAEAppHelper.getUsers();
		}
		
		if (parentElement instanceof IJDElement) {
			IJDElement jdElement = (IJDElement) parentElement;
			return doGetJDElementChildren(jdElement);
		}
		
		IWorkbenchAdapter adapter = JAEAppUtil.getWorkbenchAdapter(parentElement);
		return adapter.getChildren(parentElement);
	}

	private Object[] doGetJDElementChildren(final IJDElement jdElement) {
		final IWorkbenchAdapter adapter = JAEAppUtil.getWorkbenchAdapter(jdElement);
		
		if (jdElement instanceof AbstractJDElement) {
			if(LoadState.NONE == ((AbstractJDElement) jdElement).getLoadState()){
				
				final LoadingJDElement loadingElement = new LoadingJDElement(jdElement, "loading . ");
				final AtomicBoolean finished = new AtomicBoolean(false);
				final Display display = PlatformUI.getWorkbench().getDisplay();
				
				Job job = new Job("loading - "+jdElement.getName()) {
					
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						try {
							monitor.beginTask("load children from remote.", 100);
							monitor.worked(10);
							
							Display4LoadingThread thread = new Display4LoadingThread("loading - "+jdElement.getName(), loadingElement, finished, monitor);
							thread.start();
							
							adapter.getChildren(jdElement);
							
							display.asyncExec(new Runnable() {
								
								public void run() {
									JAEView view = JAEView.getInstance();
									if(null != view){
										LoadState state = ((AbstractJDElement) jdElement).getLoadState();
										if(LoadState.LOADED == state){
											//加载结束
											jdElement.removeChild(loadingElement);
										}
										
										view.getCommonViewer().refresh(jdElement);
									}
								}
							});
						} finally {
							finished.set(true);
							monitor.done();
						}
						return Status.OK_STATUS;
					}
				};
				
				job.schedule();
				
				return new Object[]{loadingElement};
			}
		}
		
		return adapter.getChildren(jdElement);
	}

	public Object getParent(Object element) {
		IWorkbenchAdapter adapter = JAEAppUtil.getWorkbenchAdapter(element);
		return adapter.getParent(element);
	}

	public boolean hasChildren(Object element) {
		if ((element instanceof IWorkspaceRoot)
				|| (element instanceof JDAppInstance)
				|| (element instanceof RemoteFolder)) {
			return true;
		}
		
		if((element instanceof RemoteFile))
			return false;
		
		return getChildren(element).length>0;
	}
}
