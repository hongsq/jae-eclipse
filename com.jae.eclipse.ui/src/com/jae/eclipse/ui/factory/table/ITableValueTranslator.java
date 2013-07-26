/**
 * 
 */
package com.jae.eclipse.ui.factory.table;


/**
 * @author hongshuiqiao
 *
 */
public interface ITableValueTranslator {

	/**
	 * 将表格上的数据保存到指定的模型对象中
	 * @param rowObjects
	 * @param model
	 */
	public void fromTable(Object[] rowObjects, Object model);
	
	/**
	 * 将模型转成表格每一行所对应的显示模型(不要将原模型直接返回，避免操作没保存也会污染原数据)
	 * @param model
	 * @return
	 */
	public Object[] toTable(Object model);
	
	/**
	 * 新建一个行对象
	 * @return
	 */
	public Object newRowObject();
}
