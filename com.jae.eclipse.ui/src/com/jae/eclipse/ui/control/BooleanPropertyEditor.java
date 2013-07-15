/**
 * 
 */
package com.jae.eclipse.ui.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.jae.eclipse.ui.base.AbstractPropertyEditor;
import com.jae.eclipse.ui.base.ValuechageNotifier;

/**
 * @author hongshuiqiao
 *
 */
public class BooleanPropertyEditor extends AbstractPropertyEditor {
	private boolean reverse;
	
	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	@Override
	protected Control buildLabelControl(Composite parent) {
		if(this.reverse){
			if(!this.isUseLabel())
				return null;
			
			return new Label(parent, SWT.NONE);
		}else{
			return super.buildLabelControl(parent);
		}
	}

	@Override
	public Control createControl(Composite parent, int style) {
		Button button = new Button(parent, style|SWT.CHECK);
		if(this.reverse)
			button.setText(this.getLabel());
		return button;
	}

	public Button getButton(){
		return (Button) this.getEditControl();
	}
	
	@Override
	public void afterBuild(Control parent) {
		super.afterBuild(parent);
		Button button = this.getButton();
		
		ValuechageNotifier notifier = new ValuechageNotifier(this);
		button.addSelectionListener(notifier);
	}
	
	@Override
	protected Object doGetValue() {
		Button button = this.getButton();
		
		return button.getSelection();
	}

	@Override
	protected void doSetValue(Object value) {
		Button button = this.getButton();
		if(Boolean.TRUE.equals(value))
			button.setSelection(true);
		else
			button.setSelection(false);
	}
}
