/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.jae.eclipse.cloudfoundry.exception.CloudFoundryClientRuntimeException;
import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.view.JAEView;
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
		final TreeViewer viewer = (TreeViewer) this.getSelectionProvider();
		final Display display = viewer.getControl().getDisplay();
		Job job = new Job(this.getText()) {
			
			protected IStatus run(final IProgressMonitor monitor) {
				try {
					Object[] objects = ConnectAction.this.getStructuredSelection().toArray();
					monitor.beginTask(getText()+"远程服务器", objects.length * 100);
					
					for (Object object : objects) {
						final User user = (User) object;
						
						monitor.setTaskName(getText()+user.getDisplayName());
						monitor.worked(50);
						
						try {
							if(ConnectAction.this.connect){
								user.connect();
							}else{
								user.disConnect();
							}
						} catch (CloudFoundryClientRuntimeException e) {
							return Status.OK_STATUS;
						}

						monitor.worked(50);
						display.asyncExec(new Runnable() {
							
							public void run() {
								viewer.refresh();
								JAEView.getInstance().updateActionBars();
							}
						});
					}
					return Status.OK_STATUS;
				} finally {
					monitor.done();
				}
			}
		};
		
		job.setUser(true);
		job.schedule();
	}
}
