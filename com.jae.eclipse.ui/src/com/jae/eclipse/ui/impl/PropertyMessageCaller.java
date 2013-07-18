/**
 * 
 */
package com.jae.eclipse.ui.impl;

import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.core.util.StringUtil;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.base.AbstractPropertyEditor;

/**
 * 属性编辑器的messagecaller
 * @author hongshuiqiao
 *
 */
@SuppressWarnings("deprecation")
public class PropertyMessageCaller implements IMessageCaller {
	private AbstractPropertyEditor propertyEditor;
	private boolean hasError=false;
	
	public PropertyMessageCaller(AbstractPropertyEditor propertyEditor) {
		super();
		this.propertyEditor = propertyEditor;
		build();
	}
	
	protected void build(){
		final FieldDecoration errorFieldDecoration = this.propertyEditor.getErrorFieldDecoration();
		this.propertyEditor.getDecoratedField().addFieldDecoration(errorFieldDecoration, SWT.TOP | SWT.LEFT, false);
		this.propertyEditor.getDecoratedField().hideDecoration(errorFieldDecoration);
		
		FieldDecoration warnFieldDecoration = this.propertyEditor.getWarnFieldDecoration();
		this.propertyEditor.getDecoratedField().addFieldDecoration(warnFieldDecoration, SWT.BOTTOM | SWT.LEFT, false);
		this.propertyEditor.getDecoratedField().hideDecoration(warnFieldDecoration);

		if (null != this.propertyEditor.getEditControl()) {
			this.propertyEditor.getEditControl().addFocusListener(
					new FocusAdapter() {
						public void focusGained(FocusEvent event) {
							AbstractPropertyEditor propertyEditor = PropertyMessageCaller.this.propertyEditor;

							if (propertyEditor.getEditControl().isFocusControl()) {
								if (!StringUtil.isEmpty(errorFieldDecoration.getDescription())) {
									propertyEditor.getDecoratedField().showHoverText(errorFieldDecoration.getDescription());
								}
							}
						}

						public void focusLost(FocusEvent r_Event) {
							PropertyMessageCaller.this.propertyEditor.getDecoratedField().hideHover();
						}

					});

			this.propertyEditor.getEditControl().addMouseTrackListener(
					new MouseTrackAdapter() {
						public void mouseExit(MouseEvent event) {
							PropertyMessageCaller.this.propertyEditor.getDecoratedField().hideHover();
						}

						@Override
						public void mouseHover(MouseEvent e) {
							AbstractPropertyEditor propertyEditor = PropertyMessageCaller.this.propertyEditor;

							if (!StringUtil.isEmpty(errorFieldDecoration.getDescription())) {
								propertyEditor.getDecoratedField().showHoverText(errorFieldDecoration.getDescription());
							}
						}
					});
		}
	}

	public void error(String message) {
		FieldDecoration fieldDecoration = this.propertyEditor.getErrorFieldDecoration();
		fieldDecoration.setDescription(message);
		this.propertyEditor.getDecoratedField().showDecoration(fieldDecoration);
		if(this.propertyEditor.getEditControl().isFocusControl())
			this.propertyEditor.getDecoratedField().showHoverText(message);
		else
			this.propertyEditor.getDecoratedField().hideHover();

		this.hasError = true;
	}

	public void info(String message) {
		if (null != message) {
			Control editorControl = this.propertyEditor.getEditControl();
			if (null != editorControl) {
				editorControl.setToolTipText(message);
			}
		}
	}

	public void warn(String message) {
		FieldDecoration fieldDecoration = this.propertyEditor.getWarnFieldDecoration();
		fieldDecoration.setDescription(message);
		this.propertyEditor.getDecoratedField().showDecoration(fieldDecoration);
		
		if(this.propertyEditor.getEditControl().isFocusControl())
			this.propertyEditor.getDecoratedField().showHoverText(message);
		else
			this.propertyEditor.getDecoratedField().hideHover();
	}

	public void clear() {
		this.hasError = false;
		this.propertyEditor.getDecoratedField().hideDecoration(this.propertyEditor.getWarnFieldDecoration());
		this.propertyEditor.getDecoratedField().hideDecoration(this.propertyEditor.getErrorFieldDecoration());
		this.propertyEditor.getErrorFieldDecoration().setDescription(null);
		this.propertyEditor.getWarnFieldDecoration().setDescription(null);
		Control editorControl = this.propertyEditor.getEditControl();
		if (null != editorControl) editorControl.setToolTipText(null);
		
		String toolTip = this.propertyEditor.getToolTip();
		if(!StringUtil.isEmpty(toolTip))
			this.propertyEditor.setToolTip(toolTip);
		
		// this.propertyEditor.getDecoratedField().hideDecoration(this.propertyEditor.getAssitantFieldDecoration());
		this.propertyEditor.getDecoratedField().hideHover();
	}

	public boolean hasError() {
		return this.hasError;
	}

}
