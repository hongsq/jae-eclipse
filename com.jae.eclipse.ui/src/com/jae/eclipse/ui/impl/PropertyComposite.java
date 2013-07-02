/**
 * 
 */
package com.jae.eclipse.ui.impl;

import org.eclipse.swt.widgets.Composite;

import com.jae.eclipse.ui.ObjectEditor;

/**
 * @author hongshuiqiao
 *
 */
public class PropertyComposite extends Composite {
	private ObjectEditor objectEditor;

	/**
	 * @param parent
	 * @param style
	 * @param objectEditor
	 */
	public PropertyComposite(Composite parent, int style, ObjectEditor objectEditor) {
		super(parent, style);
		this.objectEditor = objectEditor;
		
		this.setLayout(this.objectEditor.getLayout());
		this.objectEditor.build(this);
	}

	public ObjectEditor getObjectEditor() {
		return objectEditor;
	}
}
