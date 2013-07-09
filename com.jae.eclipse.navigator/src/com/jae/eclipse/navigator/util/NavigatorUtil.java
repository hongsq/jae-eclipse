/**
 * 
 */
package com.jae.eclipse.navigator.util;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;

/**
 * @author hongshuiqiao
 *
 */
public class NavigatorUtil {

	public static boolean containContributionItem(IContributionManager manager, String id){
		IContributionItem[] items = manager.getItems();
		
		for (IContributionItem item : items) {
			if(id.equalsIgnoreCase(item.getId())){
				return true;
			}
		}
		
		return false;
	}
	
	public static void addContributionItem(IContributionManager manager, IContributionItem item){
		if(null == manager.find(item.getId())){
			manager.add(item);
		}
	}
	
	public static void addAction(IContributionManager manager, IAction action){
		if(null == manager.find(action.getId())){
			manager.add(action);
		}
	}
	
	public static void appendContributionItem2Group(IContributionManager manager, String groupName, IContributionItem item){
		if(null == manager.find(groupName)){
			manager.add(new GroupMarker(groupName));
		}
		
		if(null == manager.find(item.getId())){
			manager.appendToGroup(groupName, item);
		}
	}
	
	public static void appendAction2Group(IContributionManager manager, String groupName, IAction action){
		if(null == manager.find(groupName)){
			manager.add(new GroupMarker(groupName));
		}
		
		if(null == manager.find(action.getId())){
			manager.appendToGroup(groupName, action);
		}
	}
	
	public static void prependContributionItem2Group(IContributionManager manager, String groupName, IContributionItem item){
		if(null == manager.find(groupName)){
			manager.add(new GroupMarker(groupName));
		}
		
		if(null == manager.find(item.getId())){
			manager.prependToGroup(groupName, item);
		}
	}
	
	public static void preppendAction2Group(IContributionManager manager, String groupName, IAction action){
		if(null == manager.find(groupName)){
			manager.add(new GroupMarker(groupName));
		}
		
		if(null == manager.find(action.getId())){
			manager.prependToGroup(groupName, action);
		}
	}
	
	public static void insertContributionItemBefore(IContributionManager manager, String beforeID, IContributionItem item){
		boolean insert = true;
		if(null == manager.find(beforeID)){
			insert = false;
		}
		
		if(null == manager.find(item.getId())){
			if(insert)
				manager.insertBefore(beforeID, item);
			else
				manager.add(item);
		}
	}
	
	public static void insertActionBefore(IContributionManager manager, String beforeID, IAction action){
		if(null == manager.find(beforeID)){
			manager.add(new GroupMarker(beforeID));
		}
		
		if(null == manager.find(action.getId())){
			manager.insertBefore(beforeID, action);
		}
	}
	
	public static void insertContributionItemAfter(IContributionManager manager, String beforeID, IContributionItem item){
		boolean insert = true;
		if(null == manager.find(beforeID)){
			insert = false;
		}
		
		if(null == manager.find(item.getId())){
			if(insert)
				manager.insertAfter(beforeID, item);
			else
				manager.add(item);
		}
	}
	
	public static void insertActionAfter(IContributionManager manager, String beforeID, IAction action){
		if(null == manager.find(beforeID)){
			manager.add(new GroupMarker(beforeID));
		}
		
		if(null == manager.find(action.getId())){
			manager.insertAfter(beforeID, action);
		}
	}
}
