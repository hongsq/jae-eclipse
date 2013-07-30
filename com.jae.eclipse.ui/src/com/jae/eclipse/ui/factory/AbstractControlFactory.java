/**
 * 
 */
package com.jae.eclipse.ui.factory;

import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

import com.jae.eclipse.core.DefaultObjectOperator;
import com.jae.eclipse.core.ObjectOperator;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.UIDescription;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.event.ValueEventContainer;

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
	private GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
	private ObjectOperator objectOperator;
	private String helpContextID;

	public final void createControl(Composite parent) {
		this.beforeCreateControl();
		this.control = this.doCreateControl(parent);
		this.control.setLayoutData(this.layoutData);
		if(null != this.helpContextID){
			this.control.addHelpListener(new HelpListener() {
				
				public void helpRequested(HelpEvent e) {
					PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpContextID);
				}
			});
		}
		this.afterCreateControl();
	}

	protected void beforeCreateControl() {
		// nothing to do.
	}
	
	protected void afterCreateControl() {
		// nothing to do.
	}

	public IMessageCaller getMessageCaller() {
		return messageCaller;
	}

	public void setMessageCaller(IMessageCaller messageCaller) {
		this.messageCaller = messageCaller;
	}

	public GridData getLayoutData() {
		return layoutData;
	}

	public void setLayoutData(GridData layoutData) {
		this.layoutData = layoutData;
	}
	
	public String getHelpContextID() {
		return helpContextID;
	}

	public void setHelpContextID(String helpContextID) {
		this.helpContextID = helpContextID;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public ObjectOperator getObjectOperator(boolean defaultIfNull) {
		if(null == this.objectOperator && defaultIfNull)
			return DefaultObjectOperator.INSTANCE;
		return objectOperator;
	}

	public void setObjectOperator(ObjectOperator objectOperator) {
		this.objectOperator = objectOperator;
	}

	public boolean isValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(boolean validateFlag) {
		this.validateFlag = validateFlag;
	}

	public void valuechanged(ValueChangeEvent event) {
		if(validateFlag){
			validate();
		}
		this.fireValuechanged(event);
	}
	
	public boolean validate() {
		if(null != this.messageCaller)
			this.messageCaller.clear();
		return doValidate();
	}
	
	public UIDescription getUIDescription() {
		return this.uiDescription;
	}
	
	public void setUIDescription(UIDescription uiDescription) {
		this.uiDescription = uiDescription;
	}

	public Control getControl() {
		return this.control;
	}
	
	protected abstract boolean doValidate();

	protected abstract Control doCreateControl(Composite parent);
}
