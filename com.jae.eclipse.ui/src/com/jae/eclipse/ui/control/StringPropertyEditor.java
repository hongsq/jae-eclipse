/**
 * 
 */
package com.jae.eclipse.ui.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jae.eclipse.ui.IPropertyEditor;
import com.jae.eclipse.ui.base.AbstractPropertyEditor;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.util.StringUtil;

/**
 * @author hongshuiqiao
 *
 */
public class StringPropertyEditor extends AbstractPropertyEditor {
	private String toolTip;
	private int limit = -1;

	@Override
	public Control createControl(Composite parent, int style) {
		Text text = new Text(parent, style|SWT.BORDER);
		if(this.limit>0)
			text.setTextLimit(limit);
		if(!StringUtil.isEmpty(toolTip))
			text.setToolTipText(toolTip);
		return text;
	}

	public Text getTextControl(){
		return (Text) this.getEditControl();
	}
	
	@Override
	public void afterBuild(Control parent) {
		super.afterBuild(parent);
		
		Text text = this.getTextControl();
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				IPropertyEditor editor = StringPropertyEditor.this;
				fireValuechanged(new ValueChangeEvent(editor, null, null));
//				validate();
			}
		});
	}
	
	@Override
	protected Object doGetValue() {
		return getTextControl().getText();
	}

	@Override
	protected void doSetValue(Object value) {
		if(null == value)
			this.getTextControl().setText("");
		else
			this.getTextControl().setText(String.valueOf(value));
	}

}
