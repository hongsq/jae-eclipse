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
	public final static int OP_TYPE_START = 1;
	public final static int OP_TYPE_RESTART = 2;
	public final static int OP_TYPE_STOP = 3;
	private int opType;

	/**
	 * @param provider
	 * @param text
	 * @param opType
	 */
	public JDAppOperatorAction(ISelectionProvider provider, String text, int opType) {
		super(provider, text);
		this.opType = opType;
		
		String id = "jdapp.action.stop";
		String imageID = "stop";
		if(OP_TYPE_START == opType){
			id = "jdapp.action.start";
			imageID = "start";
		}else if(OP_TYPE_RESTART == opType){
			id = "jdapp.action.restart";
			imageID = "restart";
		}
		
		this.setId(id);
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor(imageID));
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
			if(this.opType == OP_TYPE_START)
				flag = !app.isStarted() && flag;
			else
				flag = app.isStarted() && flag;
		}
		
		this.setEnabled(flag);
	}

	@Override
	public void run() {
		final Object[] objects = this.getStructuredSelection().toArray();
		final TreeViewer viewer = (TreeViewer) this.getSelectionProvider();
		final Display display = viewer.getControl().getDisplay();
		final String name = getText();
		Job job = new Job(name) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask(name, objects.length*100);
					for (Object object : objects) {
						final JDApp app = (JDApp) object;
						monitor.setTaskName(name+"\""+app.getName()+"\"");
						monitor.worked(50);

						User user = JDModelUtil.getParentElement(app, User.class);
						CloudFoundryClientExt client = user.getCloudFoundryClient();
						CloudApplication application = client.getApplication(app.getName());
						
						if(JDAppOperatorAction.this.opType == JDAppOperatorAction.OP_TYPE_START){
							if(application.getState() == AppState.STOPPED){
								client.startApplication(app.getName());
							}
							app.setStarted(true);
						}else if(JDAppOperatorAction.this.opType == JDAppOperatorAction.OP_TYPE_RESTART){
							if(application.getState() == AppState.STARTED){
								client.restartApplication(app.getName());
							}
							app.setStarted(true);
						}else if(JDAppOperatorAction.this.opType == JDAppOperatorAction.OP_TYPE_STOP){
							if(application.getState() != AppState.STOPPED){
								client.stopApplication(app.getName());
							}
							app.setStarted(false);
						}
						monitor.worked(50);
						display.asyncExec(new Runnable() {
							
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
						
						public void run() {
							DetailMessageDialog.openError(viewer.getControl().getShell(), "提示", name+"失败。", e);
						}
					});
					return Status.OK_STATUS;
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		
		job.setUser(true);
		job.schedule();
	}
}
