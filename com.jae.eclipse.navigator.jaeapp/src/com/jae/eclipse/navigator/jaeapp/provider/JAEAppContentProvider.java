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
import com.jae.eclipse.navigator.jaeapp.view.JAEAppView;

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
							adapter.getChildren(jdElement);
							
							display.asyncExec(new Runnable() {
								
								@Override
								public void run() {
									JAEAppView view = JAEAppView.getInstance();
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
						}
						return Status.OK_STATUS;
					}
				};
				
				Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						int count = 1;
						while(!finished.get()){
							if(count>5){
								loadingElement.setName("loading . ");
								count=1;
							}else{
								loadingElement.setName(loadingElement.getName()+". ");
							}
							
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {}

							count++;
							
							display.asyncExec(new Runnable() {
								
								public void run() {
									JAEAppView view = JAEAppView.getInstance();
									if(null != view){
										view.getCommonViewer().refresh(loadingElement);
									}
								}
							});
						}
					}
				}, "loading - "+jdElement.getName());
				
				job.schedule();
				thread.start();
				
				return new Object[]{loadingElement};
			}
		}
		
		return adapter.getChildren(jdElement);
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
		
		return getChildren(element).length>0;
	}

}

class LoadingJDElement extends AbstractJDElement{

	public LoadingJDElement(IJDElement parent, String name) {
		super(parent, name);
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}
	
	@Override
	protected void doLoad() {
		// nothing to do.
	}
	
	@Override
	public LoadState getLoadState() {
		return LoadState.LOADED;
	}
}