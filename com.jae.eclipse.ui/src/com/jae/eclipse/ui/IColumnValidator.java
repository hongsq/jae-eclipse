/**
 * 
 */
package com.jae.eclipse.ui;

import org.eclipse.jface.viewers.ColumnViewer;

/**
 * @author hongshuiqiao
 *
 */
public interface IColumnValidator {
	
	/**
	 * 验证器的验证逻辑
	 * @param messageCaller	消息处理
	 * @param viewer		
	 * @param rowData		单元格对应的模型
	 * @param columnIndex	单元格所在列号
	 * @param rowIndex		单元格所在行号
	 * @param value			单元格的值
	 * @return
	 */
	public boolean validate(IMessageCaller messageCaller, ColumnViewer viewer, Object rowData, int columnIndex, int rowIndex, Object value);
}
