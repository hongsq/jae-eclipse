/**
 * 
 */
package com.jae.eclipse.ui.factory.base;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.event.ValidateEvent;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractViewerFactory extends AbstractControlFactory {

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean doValidate(ValidateEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Control doCreateControl(Composite parent) {
		/**
		 * @see SWT#BORDER
		 * @see SWT#FLAT
		 */
		ViewForm viewForm = new ViewForm(parent, SWT.FLAT);
		
		Viewer viewer = createViewer(parent);
		
		viewForm.setContent(viewer.getControl());
		
		return null;
	}

	protected abstract Viewer createViewer(Composite parent);

}
