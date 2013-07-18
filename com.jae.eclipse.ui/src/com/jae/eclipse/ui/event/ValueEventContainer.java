/**
 * 
 */
package com.jae.eclipse.ui.event;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件容器
 * @author hongshuiqiao
 *
 */
public class ValueEventContainer implements IValueEventContainer {
	private List<IValuechangeListener> listeners = new ArrayList<IValuechangeListener>();
	
	public void addValuechangeListener(IValuechangeListener listener){
		this.listeners.add(listener);
	}
	
	public void removeValuechangeListener(IValuechangeListener listener){
		this.listeners.remove(listener);
	}
	
	public void clearValuechangeListeners(){
		this.listeners.clear();
	}
	
	public synchronized void fireValuechanged(ValueChangeEvent event){
		for (IValuechangeListener listener : this.listeners) {
			listener.valuechanged(event);
		}
	}

	public IValuechangeListener[] getValuechangeListeners() {
		return this.listeners.toArray(new IValuechangeListener[this.listeners.size()]);
	}
}
