/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action.provider;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import com.jae.eclipse.navigator.jaeapp.action.RefreshAction;
import com.jae.eclipse.navigator.util.NavigatorUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JDCommonActionProvider extends AbstractJDActionProvider {
	private RefreshAction refreshAction;

	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		
		StructuredViewer viewer = getActionSite().getStructuredViewer();
		
		refreshAction = new RefreshAction(viewer, "刷新");
	}

	@Override
	public void fillContextMenu(IMenuManager menuManager) {
		NavigatorUtil.appendAction2Group(menuManager, "group.common", this.refreshAction);
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		NavigatorUtil.insertContributionItemBefore(toolBarManager, "FRAME_ACTION_GROUP_ID", new GroupMarker("group.common"));
		NavigatorUtil.appendAction2Group(toolBarManager, "group.common", this.refreshAction);
		
//		IMenuManager menuManager = actionBars.getMenuManager();
//		NavigatorUtil.insertContributionItemBefore(menuManager, "additions", new GroupMarker("group.common"));
//		NavigatorUtil.appendAction2Group(menuManager, "group.common", this.refreshAction);
	}
	
}
