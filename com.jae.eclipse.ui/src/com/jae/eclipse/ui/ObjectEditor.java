/**
 * 
 */
package com.jae.eclipse.ui;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jae.eclipse.core.DefaultObjectOperator;
import com.jae.eclipse.core.ObjectOperator;
import com.jae.eclipse.ui.base.AbstractPropertyEditor;
import com.jae.eclipse.ui.event.IValueEventContainer;
import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.event.ValueEventContainer;
import com.jae.eclipse.ui.util.LayoutUtil;

/**
 * 对象编辑器
 * @author hongshuiqiao
 *
 */
public class ObjectEditor extends ValueEventContainer implements ILayoutContainer, ILayoutElement, IUIDescElement, IStore, ILoadable, IValueEventContainer, IValidatable, IValuechangeListener {
	private UIDescription uiDescription = new UIDescription();
	private Map<String, IPropertyEditor> editors = new LinkedHashMap<String, IPropertyEditor>();
	private GridLayout layout = LayoutUtil.createCompactGridLayout(2);
	private Object value;
	private IMessageCaller messageCaller;
	//自身的变化是否触发自己的验证（一般如果加入到一个容器中，自身的变化会触发整个容器的验证，不需要再重复验证自己）
	private boolean validateFlag = true;
	private GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
	private ObjectOperator objectOperator;
	
	public void addPropertyEditor(IPropertyEditor editor){
		editors.put(editor.getPropertyName(), editor);
		editor.addValuechangeListener(this);
	}
	
	public void removePropertyEditor(IPropertyEditor editor){
		editors.remove(editor.getPropertyName());
		editor.removeValuechangeListener(this);
	}
	
	public void clearPropertyEditors(){
		editors.clear();
		Collection<IPropertyEditor> propertyEditors = this.editors.values();
		for (IPropertyEditor editor : propertyEditors) {
			editor.removeValuechangeListener(this);
		}
	}
	
	public IPropertyEditor[] getPropertyEditors(){
		return editors.values().toArray(new IPropertyEditor[editors.size()]);
	}
	
	public IPropertyEditor getPropertyEditor(String propertyName){
		return editors.get(propertyName);
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

	public ObjectOperator getObjectOperator(boolean defaultIfNull) {
		if(null == this.objectOperator && defaultIfNull)
			return DefaultObjectOperator.INSTANCE;
		return objectOperator;
	}

	public void setObjectOperator(ObjectOperator objectOperator) {
		this.objectOperator = objectOperator;
	}

	public GridLayout getLayout() {
		return layout;
	}

	public void setLayout(GridLayout layout) {
		this.layout = layout;
	}

	public GridData getLayoutData() {
		return layoutData;
	}

	public void setLayoutData(GridData layoutData) {
		this.layoutData = layoutData;
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
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(this.getLayout());
		if(null != uiDescription){
			if(uiDescription.getWidth()>0 && this.layoutData.widthHint<0)
				this.layoutData.widthHint = uiDescription.getWidth();
			if(uiDescription.getHeight()>0 && this.layoutData.heightHint<0)
				this.layoutData.heightHint = uiDescription.getHeight();
			if(uiDescription.getMinWidth()>0 && this.layoutData.minimumWidth<0)
				this.layoutData.minimumWidth = uiDescription.getMinWidth();
			if(uiDescription.getMinHeight()>0 && this.layoutData.minimumHeight<0)
				this.layoutData.minimumHeight = uiDescription.getMinHeight();
		}
		composite.setLayoutData(this.layoutData);
		
		beforeBuild(composite);
		
		Collection<IPropertyEditor> propertyEditors = this.editors.values();
		for (IPropertyEditor editor : propertyEditors) {
			editor.setEditElement(this.value);
			editor.setMessageCaller(this.messageCaller);
			if (editor instanceof AbstractPropertyEditor) {
				AbstractPropertyEditor propertyEditor = (AbstractPropertyEditor) editor;
				propertyEditor.setValidateFlag(false);
				
				if((null != this.objectOperator) && (null == propertyEditor.getObjectOperator(false)))
					propertyEditor.setObjectOperator(this.objectOperator);
			}
			
			editor.beforeBuild(composite);
		}
		
		for (IPropertyEditor editor : propertyEditors) {
			editor.build(composite);
		}
		
		for (IPropertyEditor editor : propertyEditors) {
			editor.afterBuild(composite);
		}
		
		afterBuild(composite);
	}

	public boolean validate() {
		boolean flag = true;
		if(null != this.messageCaller)
			this.messageCaller.clear();

		Collection<IPropertyEditor> propertyEditors = this.editors.values();
		for (IPropertyEditor editor : propertyEditors) {
			flag = editor.validate() && flag;
		}
		return flag;
	}

	public void load() {
		Collection<IPropertyEditor> propertyEditors = this.editors.values();
		for (IPropertyEditor editor : propertyEditors) {
			editor.load();
		}
	}

	public void save() {
		Collection<IPropertyEditor> propertyEditors = this.editors.values();
		for (IPropertyEditor editor : propertyEditors) {
			editor.save();
		}
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

	public UIDescription getUIDescription() {
		return this.uiDescription;
	}

	public void setUIDescription(UIDescription uiDescription) {
		this.uiDescription = uiDescription;
	}
}
