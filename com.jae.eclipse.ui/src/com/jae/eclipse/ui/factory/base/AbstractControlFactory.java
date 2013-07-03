/**
 * 
 */
package com.jae.eclipse.ui.factory.base;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.UIDescription;
import com.jae.eclipse.ui.event.ValidateEvent;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.event.ValueEventContainer;
import com.jae.eclipse.ui.factory.IControlFactory;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractControlFactory extends ValueEventContainer implements IControlFactory {
	private Control control;
	private UIDescription uiDescription = new UIDescription();
	private Object value;
	//自身的变化是否触发自己的验证（一般如果加入到一个容器中，自身的变化会触发整个容器的验证，不需要再重复验证自己）
	private boolean validateFlag = true;
	private IMessageCaller messageCaller;

	@Override
	public Control createControl(Composite parent) {
		this.control = this.doCreateControl(parent);
		return this.control;
	}

	public IMessageCaller getMessageCaller() {
		return messageCaller;
	}

	public void setMessageCaller(IMessageCaller messageCaller) {
		this.messageCaller = messageCaller;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public boolean isValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(boolean validateFlag) {
		this.validateFlag = validateFlag;
	}

	@Override
	public void valuechanged(ValueChangeEvent event) {
		if(validateFlag){
			ValidateEvent validateEvent = new ValidateEvent(event.getSource());
			validate(validateEvent);
		}
		this.fireValuechanged(event);
	}
	
	@Override
	public boolean validate(ValidateEvent event) {
		if(null != this.messageCaller)
			this.messageCaller.clear();
		return doValidate(event);
	}
	
	@Override
	public UIDescription getUIDescription() {
		return this.uiDescription;
	}
	
	@Override
	public void setUIDescription(UIDescription uiDescription) {
		this.uiDescription = uiDescription;
	}

	protected abstract boolean doValidate(ValidateEvent event);

	protected abstract Control doCreateControl(Composite parent);
}
