/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.HashMap;

import com.jae.eclipse.core.ObjectOperator;

/**
 * @author hongshuiqiao
 *
 */
public class RowModel extends HashMap<String, Object> {
	private static final long serialVersionUID = 8743065007519828138L;
	private Object editObject;
	private ObjectOperator operator;
	
	public RowModel(ObjectOperator operator, Object editObject) {
		super();
		this.operator = operator;
		this.editObject = editObject;
	}
	
	public Object getEditObject() {
		return editObject;
	}

	@Override
	public Object get(Object key) {
		//先从显示对象中取，如果没有再从编辑的模型对象中取
		Object result = super.get(key);
		if(null == result)
			return this.operator.getValue(editObject, (String) key);
		return result;
	}

	@Override
	public Object put(String key, Object value) {
		// 设置属性时不保存到编辑的模型对象，只放在当前的显示对象中
		return super.put(key, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((editObject == null) ? 0 : editObject.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RowModel other = (RowModel) obj;
		if (editObject == null) {
			if (other.editObject != null)
				return false;
		} else if (!editObject.equals(other.editObject))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		return true;
	}
}
