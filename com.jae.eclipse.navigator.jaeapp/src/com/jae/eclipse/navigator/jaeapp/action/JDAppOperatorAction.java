/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudApplication.AppState;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;
import com.jae.eclipse.cloudfoundry.exception.CloudFoundryClientRuntimeException;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;
import com.jae.eclipse.ui.dialog.DetailMessageDialog;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppOperatorAction extends AbstractJDAction {
	private boolean start;

	public JDAppOperatorAction(ISelectionProvider provider, String text, boolean start) {
		super(provider, text);
		this.start = start;
		
		String id = "jdapp.action.stop";
		if(this.start)
			id = "jdapp.action.start";
		
		this.setId(id);
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor(id));
		this.setMustSelect(true);
		this.setMultiable(true);
		this.setSelectType(JDApp.class);
		this.setEnabled(false);
	}
	
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);
		
		Object[] objects = this.getStructuredSelection().toArray();
		boolean flag = this.isEnabled();
		if(!flag)
			return;
		
		for (Object object : objects) {
			JDApp app = (JDApp) object;
			if(this.start)
				flag = !app.isStarted() && flag;
			else
				flag = app.isStarted() && flag;
		}
		
		this.setEnabled(flag);
	}

	@Override
	public void run() {
		final String name = (this.start?"启动":"关闭")+"应用";
		final Object[] objects = this.getStructuredSelection().toArray();
		final TreeViewer viewer = (TreeViewer) this.getSelectionProvider();
		final Display display = viewer.getControl().getDisplay();
		Job job = new Job(name) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					for (Object object : objects) {
						final JDApp app = (JDApp) object;

						User user = JDModelUtil.getParentElement(app, User.class);
						CloudFoundryClientExt client = user.getCloudFoundryClient();
						CloudApplication application = client.getApplication(app.getName());
						
						if(JDAppOperatorAction.this.start){
							if(application.getState() == AppState.STOPPED){
								client.startApplication(app.getName());
							}
							
							app.setStarted(true);
						}else{
							if(application.getState() != AppState.STOPPED){
								client.stopApplication(app.getName());
							}

							app.setStarted(false);
						}
						
						display.asyncExec(new Runnable() {
							
							@Override
							public void run() {
								app.refresh();
//								viewer.collapseToLevel(app, 1);
								viewer.refresh(app);
							}
						});
					}
				} catch (CloudFoundryClientRuntimeException e) {
					return Status.OK_STATUS;
				} catch (final Exception e) {
					display.asyncExec(new Runnable() {
						
						@Override
						public void run() {
							DetailMessageDialog.openError(viewer.getControl().getShell(), "提示", name+"失败。", e);
						}
					});
					return Status.OK_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		
		job.setUser(true);
		job.schedule();
	}
}
