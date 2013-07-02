/**
 * 
 */
package com.jae.eclipse.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.event.ILinkAction;
import com.jae.eclipse.ui.event.IValueEventContainer;


/**
 * ���Ա༭��
 * @author hongshuiqiao
 *
 */
public interface IPropertyEditor extends IStore, ILoadable, IValidatable, IAdaptable, IValueEventContainer {
	
	/**
	 * �����ؼ�
	 * @param parent
	 */
	public void build(Composite parent);
	
	public void beforeBuild(Control parent);
	
	public void afterBuild(Control parent);
	
	/**
	 * ���ؿؼ�����ʾ��ǩ��
	 * @return
	 */
	public String getLabel();
	
	/**
	 * ���ؿؼ��༭��������
	 * @return
	 */
	public String getPropertyName();
	
	/**
	 * ���ؿؼ��༭��ֵ
	 * @return
	 */
	public Object getValue();
	
	/**
	 * ���ÿؼ��༭��ֵ
	 * @param value
	 */
	public void setValue(Object value);
	
	public Object getEditElement();
	
	public void setEditElement(Object editElement);
	
	public IMessageCaller getMessageCaller();
	
	public void setMessageCaller(IMessageCaller messageCaller);
	
	/**
	 * �ؼ��Ƿ����
	 * @return
	 */
	public boolean isEnable();
	
	/**
	 * ���ÿؼ�����״̬
	 * @param enable
	 */
	public void setEnable(boolean enable);
	
	/**
	 * �Ƿ�ʹ��label
	 * @return
	 */
	public boolean isUseLabel();
	
	/**
	 * �Ƿ���link���͵�label
	 * @return
	 */
	public boolean isLinkLabel();
	
	/**
	 * ����link����
	 * @param action
	 */
	public void setLinkAction(ILinkAction action);
	
	/**
	 * �Ƿ��Ǳ�����
	 * @return
	 */
	public boolean isRequired();
}
