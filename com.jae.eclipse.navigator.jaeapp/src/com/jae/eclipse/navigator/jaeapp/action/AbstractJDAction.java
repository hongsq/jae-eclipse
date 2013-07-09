/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionProviderAction;

/**
 * 抽象的Action，默认处理的选择事件
 * @author hongshuiqiao
 *
 */
public abstract class AbstractJDAction extends SelectionProviderAction {
	private boolean mustSelect = false;
	private Class<?> selectType = null;
	private boolean multiable=true;

	public AbstractJDAction(ISelectionProvider provider, String text) {
		super(provider, text);
	}
	
	public boolean isMustSelect() {
		return mustSelect;
	}

	public void setMustSelect(boolean mustSelect) {
		this.mustSelect = mustSelect;
	}

	public Class<?> getSelectType() {
		return selectType;
	}

	public void setSelectType(Class<?> selectType) {
		this.selectType = selectType;
	}

	public boolean isMultiable() {
		return multiable;
	}

	public void setMultiable(boolean multiable) {
		this.multiable = multiable;
	}

	@Override
	public void selectionChanged(IStructuredSelection selection) {
		Object[] selectedObjects = selection.toArray();
		if(selectedObjects.length<=0){
			if(mustSelect)
				this.setEnabled(false);
			else
				this.setEnabled(true);
			
			return;
		}
		
		if(selectedObjects.length > 1 && !multiable){
			this.setEnabled(false);
			return;
		}
		
		boolean flag = true;
		for (Object object : selectedObjects) {
			if(null==selectType || !selectType.isAssignableFrom(object.getClass())){
				flag = flag && false;
			}
		}
		
		this.setEnabled(flag);
	}

}
