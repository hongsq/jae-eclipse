/**
 * 
 */
package com.jae.eclipse.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.event.ILinkAction;
import com.jae.eclipse.ui.event.IValueEventContainer;


/**
 * 属性编辑器
 * @author hongshuiqiao
 *
 */
public interface IPropertyEditor extends IStore, ILoadable, IValidatable, IAdaptable, IValueEventContainer {
	
	/**
	 * 创建控件
	 * @param parent
	 */
	public void build(Composite parent);
	
	public void beforeBuild(Control parent);
	
	public void afterBuild(Control parent);
	
	/**
	 * 返回控件的显示标签名
	 * @return
	 */
	public String getLabel();
	
	/**
	 * 返回控件编辑的属性名
	 * @return
	 */
	public String getPropertyName();
	
	/**
	 * 返回控件编辑的值
	 * @return
	 */
	public Object getValue();
	
	/**
	 * 设置控件编辑的值
	 * @param value
	 */
	public void setValue(Object value);
	
	public Object getEditElement();
	
	public void setEditElement(Object editElement);
	
	public IMessageCaller getMessageCaller();
	
	public void setMessageCaller(IMessageCaller messageCaller);
	
	/**
	 * 控件是否可用
	 * @return
	 */
	public boolean isEnable();
	
	/**
	 * 设置控件可用状态
	 * @param enable
	 */
	public void setEnable(boolean enable);
	
	/**
	 * 是否使用label
	 * @return
	 */
	public boolean isUseLabel();
	
	/**
	 * 是否是link类型的label
	 * @return
	 */
	public boolean isLinkLabel();
	
	/**
	 * 设置link操作
	 * @param action
	 */
	public void setLinkAction(ILinkAction action);
	
	/**
	 * 是否是必填项
	 * @return
	 */
	public boolean isRequired();
}
