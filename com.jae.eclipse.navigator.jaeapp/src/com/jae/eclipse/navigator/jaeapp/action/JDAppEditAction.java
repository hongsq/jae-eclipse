/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppEditAction extends AbstractJDAction {

	public JDAppEditAction(ISelectionProvider provider, String text) {
		super(provider, text);
		
		this.setId("jdapp.action.edit");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("jdapp.action.edit"));
		this.setMustSelect(true);
		this.setMultiable(false);
		this.setSelectType(JDApp.class);
		this.setEnabled(false);
	}

	@Override
	public void run() {
		JDApp app = (JDApp) this.getStructuredSelection().getFirstElement();
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		Shell shell = viewer.getControl().getShell();
		
		WizardDialog dialog = new WizardDialog(shell, new JDAppEditWizard(app));
		if(Window.OK == dialog.open()){
			
		}
	}
}
