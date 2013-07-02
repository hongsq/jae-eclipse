/**
 * 
 */
package com.jae.eclipse.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.impl.CompoundMessageCaller;
import com.jae.eclipse.ui.util.LayoutUtil;

/**
 * 对象编辑器
 * @author hongshuiqiao
 *
 */
public class ObjectEditor implements IStore, ILoadable, IValidatable, IAdaptable, IValuechangeListener {
	private List<IPropertyEditor> editors = new ArrayList<IPropertyEditor>();
	private GridLayout layout = LayoutUtil.createCompactGridLayout(2);
	private Object editElement;
	private CompoundMessageCaller messageCaller = new CompoundMessageCaller();
	
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
	
	public Object getEditElement() {
		return editElement;
	}

	public void setEditElement(Object editElement) {
		this.editElement = editElement;
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
		for (IPropertyEditor editor : this.editors) {
			editor.load();
		}
	}
	
	/**
	 * 创建控件
	 * @param parent
	 */
	public void build(Composite parent){
		beforeBuild(parent);
		
		for (IPropertyEditor editor : this.editors) {
			editor.setEditElement(this.editElement);
			editor.setMessageCaller(this.messageCaller);
			
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
	public boolean validate() {
		boolean flag = true;
		for (IPropertyEditor editor : this.editors) {
			flag = editor.validate() && flag;
		}
		return flag;
	}

	@Override
	public <T> T getAdapter(Class<T> adapterClass) {
		if(adapterClass == ObjectEditor.class)
			return adapterClass.cast(this);
		if(adapterClass == IMessageCaller.class)
			return adapterClass.cast(this.messageCaller);
		
		return null;
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

	@Override
	public void valuechanged(ValueChangeEvent event) {
		validate();
	}
}
