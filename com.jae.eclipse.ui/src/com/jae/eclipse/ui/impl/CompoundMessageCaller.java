/**
 * 
 */
package com.jae.eclipse.ui.impl;

import java.util.ArrayList;
import java.util.List;

import com.jae.eclipse.ui.IMessageCaller;

/**
 * @author hongshuiqiao
 *
 */
public class CompoundMessageCaller implements IMessageCaller {
	private List<IMessageCaller> list = new ArrayList<IMessageCaller>();

	public void addMessageCaller(IMessageCaller messageCaller){
		this.list.add(messageCaller);
	}
	
	public void removeMessageCaller(IMessageCaller messageCaller){
		this.list.remove(messageCaller);
	}
	
	public void clearMessageCallers(){
		this.list.clear();
	}
	
	public IMessageCaller[] getMessageCallers(){
		return this.list.toArray(new IMessageCaller[this.list.size()]);
	}
	
	@Override
	public void error(String message) {
		for (IMessageCaller messageCaller : this.list) {
			messageCaller.error(message);
		}
	}

	@Override
	public void info(String message) {
		for (IMessageCaller messageCaller : this.list) {
			messageCaller.info(message);
		}
	}

	@Override
	public void warn(String message) {
		for (IMessageCaller messageCaller : this.list) {
			messageCaller.warn(message);
		}
	}

	@Override
	public void clear() {
		for (IMessageCaller messageCaller : this.list) {
			messageCaller.clear();
		}
	}

	@Override
	public boolean hasError() {
		for (IMessageCaller messageCaller : this.list) {
			if(messageCaller.hasError())
				return true;
		}
		return false;
	}

}
