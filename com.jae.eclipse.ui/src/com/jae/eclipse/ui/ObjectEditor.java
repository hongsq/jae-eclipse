/**
 * 
 */
package com.jae.eclipse.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jae.eclipse.ui.base.AbstractPropertyEditor;
import com.jae.eclipse.ui.event.IValueEventContainer;
import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.ValidateEvent;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.event.ValueEventContainer;
import com.jae.eclipse.ui.util.LayoutUtil;

/**
 * 对象编辑器
 * @author hongshuiqiao
 *
 */
public class ObjectEditor extends ValueEventContainer implements IUIDescElement, IStore, ILoadable, IValueEventContainer, IValidatable, IValuechangeListener {
	private UIDescription uiDescription = new UIDescription();
	private List<IPropertyEditor> editors = new ArrayList<IPropertyEditor>();
	private GridLayout layout = LayoutUtil.createCompactGridLayout(2);
	private Object value;
	private IMessageCaller messageCaller;
	//自身的变化是否触发自己的验证（一般如果加入到一个容器中，自身的变化会触发整个容器的验证，不需要再重复验证自己）
	private boolean validateFlag = true;
	
	public void addPropertyEditor(IPropertyEditor editor){
		editors.add(editor);
		editor.addValuechangeListener(this);
	}
	
	public void removePropertyEditor(IPropertyEditor editor){
		editors.remove(editor);
		editor.removeValuechangeListener(this);
	}
	
	public void clearPropertyEditors(){
		editors.clear();
		for (IPropertyEditor editor : this.editors) {
			editor.removeValuechangeListener(this);
		}
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

	public GridLayout getLayout() {
		return layout;
	}

	public void setLayout(GridLayout layout) {
		this.layout = layout;
	}

	protected void beforeBuild(Composite parent) {
	}
	
	protected void afterBuild(Composite parent) {
	}
	
	/**
	 * 创建控件
	 * @param parent
	 */
	public void build(Composite parent){
		beforeBuild(parent);
		
		for (IPropertyEditor editor : this.editors) {
			editor.setEditElement(this.value);
			editor.setMessageCaller(this.messageCaller);
			if (editor instanceof AbstractPropertyEditor) {
				((AbstractPropertyEditor) editor).setValidateFlag(false);
			}
			
			editor.beforeBuild(parent);
		}
		
		for (IPropertyEditor editor : this.editors) {
			editor.build(parent);
		}
		
		for (IPropertyEditor editor : this.editors) {
			editor.afterBuild(parent);
		}
		
		afterBuild(parent);
	}

	@Override
	public boolean validate(ValidateEvent event) {
		boolean flag = true;
		if(null != this.messageCaller)
			this.messageCaller.clear();
		for (IPropertyEditor editor : this.editors) {
			flag = editor.validate(event) && flag;
		}
		return flag;
	}

	@Override
	public void load() {
		for (IPropertyEditor editor : this.editors) {
			editor.load();
		}
	}

	@Override
	public void save() {
		for (IPropertyEditor editor : this.editors) {
			editor.save();
		}
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
	public UIDescription getUIDescription() {
		return this.uiDescription;
	}

	@Override
	public void setUIDescription(UIDescription uiDescription) {
		this.uiDescription = uiDescription;
	}
}
