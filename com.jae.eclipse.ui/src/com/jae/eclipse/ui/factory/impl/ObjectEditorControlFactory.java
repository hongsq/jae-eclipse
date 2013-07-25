/**
 * 
 */
package com.jae.eclipse.ui.factory.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.factory.AbstractControlFactory;
import com.jae.eclipse.ui.impl.PropertyComposite;

/**
 * @author hongshuiqiao
 *
 */
public class ObjectEditorControlFactory extends AbstractControlFactory {
	private ObjectEditor objectEditor;
	
	public ObjectEditorControlFactory(ObjectEditor objectEditor) {
		super();
		this.objectEditor = objectEditor;
	}

	public ObjectEditor getObjectEditor() {
		return objectEditor;
	}

	public void save() {
		this.objectEditor.save();
	}
	
	public void load() {
		this.objectEditor.load();
	}

	@Override
	public boolean doValidate() {
		return this.objectEditor.validate();
	}

	@Override
	protected Control doCreateControl(Composite parent) {
		this.objectEditor.setValue(this.getValue());
		this.objectEditor.addValuechangeListener(this);
		this.objectEditor.setValidateFlag(false);
		this.objectEditor.setMessageCaller(this.getMessageCaller());
		if((null != this.getObjectOperator(false)) && (null == this.objectEditor.getObjectOperator(false)))
			this.objectEditor.setObjectOperator(this.getObjectOperator(false));
		
		PropertyComposite composite = new PropertyComposite(parent, SWT.NONE, this.objectEditor);
		
		return composite;
	}

}
