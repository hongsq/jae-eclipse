/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.viewers.ISelectionProvider;

import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppDeployAction extends AbstractJDAction {

	public JDAppDeployAction(ISelectionProvider provider, String text) {
		super(provider, text);

		this.setId("jdapp.action.deploy");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("jdapp.action.deploy"));
		this.setMustSelect(true);
		this.setMultiable(false);
		this.setSelectType(JDApp.class);
	}

	@Override
	public void run() {
		
	}
}
