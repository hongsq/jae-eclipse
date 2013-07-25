/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.LinkedHashMap;
import java.util.Map;

import com.jae.eclipse.core.Level;
import com.jae.eclipse.ui.IMessageCaller;

/**
 * @author hongshuiqiao
 *
 */
public class TableMessageCaller implements IMessageCaller {
	private Map<Object, Map<Integer, String>> errorMap = new LinkedHashMap<Object, Map<Integer, String>>();
	private Map<Object, Map<Integer, String>> warningMap = new LinkedHashMap<Object, Map<Integer, String>>();
	private Map<Object, Map<Integer, String>> infoMap = new LinkedHashMap<Object, Map<Integer, String>>();
	//当前正在验证的对象
	private Object validateObject;
	private int validateColumn = -1;

	protected void doSetMessage(String message, Map<Object, Map<Integer, String>> messageMap) {
		if(null != validateObject){
			Map<Integer, String> map = messageMap.get(this.validateObject);
			if(null == map){
				map = new LinkedHashMap<Integer, String>();
				messageMap.put(this.validateObject, map);
			}
			if(this.validateColumn>=0)
				map.put(this.validateColumn, message);
		}
	}

	public synchronized void info(String message) {
		doSetMessage(message, this.infoMap);
	}

	public synchronized void warn(String message) {
		doSetMessage(message, this.warningMap);
	}
	
	public synchronized void error(String message) {
		doSetMessage(message, this.errorMap);
	}

	public synchronized void clear() {
		this.infoMap.clear();
		this.warningMap.clear();
		this.errorMap.clear();
	}

	public boolean hasError() {
		return !this.errorMap.isEmpty();
	}

	public Level getMessageLevel(Object element, int columnIndex, int rowIndex) {
		if(this.errorMap.containsKey(element) && this.errorMap.get(element).containsKey(columnIndex))
			return Level.ERROR;
		if(this.warningMap.containsKey(element) && this.warningMap.get(element).containsKey(columnIndex))
			return Level.WARNING;
		if(this.infoMap.containsKey(element) && this.infoMap.get(element).containsKey(columnIndex))
			return Level.INFO;
		return Level.NONE;
	}

	public Object getValidateObject() {
		return validateObject;
	}

	public synchronized void setValidateObject(Object validateObject) {
		this.validateObject = validateObject;
	}

	public int getValidateColumn() {
		return validateColumn;
	}

	public synchronized void setValidateColumn(int validateColumn) {
		this.validateColumn = validateColumn;
	}
}
