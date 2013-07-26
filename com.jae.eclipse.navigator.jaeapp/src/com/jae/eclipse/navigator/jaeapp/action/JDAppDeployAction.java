/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;
import com.jae.eclipse.cloudfoundry.exception.CloudFoundryClientRuntimeException;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.FileSelectionPropertyEditor;
import com.jae.eclipse.ui.dialog.DetailMessageDialog;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;
import com.jae.eclipse.ui.factory.impl.ObjectEditorControlFactory;
import com.jae.eclipse.ui.impl.ControlFactoryDialog;
import com.jae.eclipse.ui.validator.NotEmptyValidator;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppDeployAction extends AbstractJDAction {

	public JDAppDeployAction(ISelectionProvider provider, String text) {
		super(provider, text);

		this.setId("jdapp.action.deploy");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("deploy"));
		this.setMustSelect(true);
		this.setMultiable(false);
		this.setSelectType(JDApp.class);
		this.setEnabled(false);
	}

	@Override
	public void run() {
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		final Shell shell = viewer.getControl().getShell();
		
		ObjectEditorControlFactory factory = new ObjectEditorControlFactory(createObjectEditor());
		factory.getUIDescription().setWinTitle("部署");
		factory.getUIDescription().setTitle("应用部署");
		factory.getUIDescription().setDescription("选择要部署的资源。");
		
		Map<String, String> map = new HashMap<String, String>();
		factory.setValue(map);
		
		ControlFactoryDialog dialog = new ControlFactoryDialog(shell, factory);
		if(Window.OK == dialog.open()){
			String path = map.get("path");
			final File file = new File(path);
			
			String errorMessage = null;
			if(!file.exists()){
				errorMessage = "文件\""+path + "\"不存在";
			}else if(!file.isFile()){
				errorMessage = "文件\""+path + "\"不是文件";
			}
			
			if(null != errorMessage){
				DetailMessageDialog.openError(shell, "提示", errorMessage);
				return;
			}
			
			final Display display = PlatformUI.getWorkbench().getDisplay();
			Job job = new Job("deploy") {
				
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					JDApp app = (JDApp) JDAppDeployAction.this.getStructuredSelection().getFirstElement();
					monitor.beginTask("部署资源到应用\""+app.getName()+"\"", 100);
					User user = JDModelUtil.getParentElement(app, User.class);
					CloudFoundryClientExt client = user.getCloudFoundryClient();
					monitor.worked(30);
					try {
						client.uploadApplication(app.getName(), file);
					} catch (final IOException e) {
						display.asyncExec(new Runnable() {
							
							public void run() {
								DetailMessageDialog.openError(shell, "错误", "部署失败。", e);								
							}
						});
						return Status.OK_STATUS;
					} catch (CloudFoundryClientRuntimeException e) {
						return Status.OK_STATUS;
					} catch (final Exception e) {
						display.asyncExec(new Runnable() {
							
							public void run() {
								DetailMessageDialog.openError(shell, "提示", "部署失败。", e);
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
	
	protected ObjectEditor createObjectEditor(){
		ObjectEditor objectEditor = new ObjectEditor();
		
		FileSelectionPropertyEditor editor = new FileSelectionPropertyEditor();
		editor.setPropertyName("path");
		editor.setLabel("路径");
		editor.setFilterExtensions(new String[]{"*.war;*.zip","*.*"});
		editor.setButtonText("浏览");
		editor.setRequired(true);
		editor.addValidator(new NotEmptyValidator("路径"));
		objectEditor.addPropertyEditor(editor);
		
		return objectEditor;
	}
}
