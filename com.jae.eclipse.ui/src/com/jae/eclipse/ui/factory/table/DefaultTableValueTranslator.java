/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.jae.eclipse.core.util.ObjectUtil;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DefaultTableValueTranslator implements TableValueTranslator {
	private AbstractTableFactory factory;

	public DefaultTableValueTranslator(AbstractTableFactory factory) {
		super();
		this.factory = factory;
	}

	public void fromTable(Object[] rowObjects, Object model) {
		List modelList = new ArrayList();
		String[] properties = (String[]) this.factory.getViewer().getColumnProperties();
		for (Object rowElement : rowObjects) {
			RowModel rowModel = (RowModel) rowElement;
			
			//将显示模型上的数据保存到编辑模型上
			Object editObject = rowModel.getEditObject();
			ObjectUtil.copyProperties(rowModel, editObject, properties);
			modelList.add(editObject);
		}
		
		if(model.getClass().isArray()){
			model = modelList.toArray();
		}else if(model instanceof Collection){
			Collection collection = (Collection) model;
			collection.clear();
			collection.addAll(modelList);
		}
	}

	public Object[] toTable(Object model) {
		List list = new ArrayList();

		if(null != model){
			List modelList = new ArrayList();
			if(model.getClass().isArray()){
				modelList.addAll(Arrays.asList((Object[])model));
			}else if(model instanceof Collection){
				modelList.addAll((Collection) model);
			}
			
			for (Object editModel : modelList) {
				list.add(new RowModel(factory.getObjectOperator(true), editModel));
			}
		}
		
		return list.toArray();
	}

	public Object newRowObject() {
		return new RowModel(factory.getObjectOperator(true), doNewRowObject());
	}

	protected Object doNewRowObject() {
		return null;
	}
}
