/**
 * 
 */
package com.jae.eclipse.ui.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jae.eclipse.core.util.StringUtil;
import com.jae.eclipse.ui.base.AbstractPropertyEditor;
import com.jae.eclipse.ui.base.ValuechageNotifier;
import com.jae.eclipse.ui.util.UIUtil;

/**
 * @author hongshuiqiao
 *
 */
public class StringPropertyEditor extends AbstractPropertyEditor {
	private int limit = -1;
	private boolean password;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
		if(UIUtil.isControlValid(getTextControl())){
			getTextControl().setTextLimit(limit);
		}
	}

	public boolean isPassword() {
		return password;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public Control createControl(Composite parent, int style) {
		int realStyle = style|SWT.BORDER;
		if(this.password)
			realStyle = realStyle|SWT.PASSWORD;
		
		Text text = new Text(parent, realStyle);
		if(this.limit>0)
			text.setTextLimit(limit);
		if(!StringUtil.isEmpty(this.getToolTip()))
			text.setToolTipText(this.getToolTip());
		return text;
	}

	public Text getTextControl(){
		return (Text) this.getEditControl();
	}
	
	@Override
	public void afterBuild(Control parent) {
		super.afterBuild(parent);
		
		ValuechageNotifier notifier = new ValuechageNotifier(this);
		
		Text text = this.getTextControl();
		text.addModifyListener(notifier);
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
