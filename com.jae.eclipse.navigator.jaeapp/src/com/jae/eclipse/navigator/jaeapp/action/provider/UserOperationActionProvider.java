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

import com.jae.eclipse.navigator.jaeapp.action.ConnectAction;
import com.jae.eclipse.navigator.jaeapp.action.DeleteUserAction;
import com.jae.eclipse.navigator.jaeapp.action.UserAction;
import com.jae.eclipse.navigator.util.NavigatorUtil;

/**
 * @author hongshuiqiao
 *
 */
public class UserOperationActionProvider extends AbstractJDActionProvider {
	private UserAction addUserAction;
	private UserAction editUserAction;
	private DeleteUserAction deleteUserAction;
	private ConnectAction connectAction;
	private ConnectAction disConnectAction;

	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		
		StructuredViewer viewer = getActionSite().getStructuredViewer();
		
		addUserAction = new UserAction(viewer, "新增用户", false);
		editUserAction = new UserAction(viewer, "编辑用户", true);
		deleteUserAction = new DeleteUserAction(viewer, "删除用户");
		connectAction = new ConnectAction(viewer, "连接", true);
		disConnectAction = new ConnectAction(viewer, "断开", false);
	}

	@Override
	public void fillContextMenu(IMenuManager menuManager) {
		NavigatorUtil.appendAction2Group(menuManager, "group.user", this.addUserAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.user", this.editUserAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.user", this.deleteUserAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.user", this.connectAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.user", this.disConnectAction);
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		NavigatorUtil.insertContributionItemBefore(toolBarManager, "FRAME_ACTION_GROUP_ID", new GroupMarker("group.user"));
		NavigatorUtil.appendAction2Group(toolBarManager, "group.user", this.addUserAction);
		NavigatorUtil.appendAction2Group(toolBarManager, "group.user", this.editUserAction);
		NavigatorUtil.appendAction2Group(toolBarManager, "group.user", this.deleteUserAction);
		NavigatorUtil.appendAction2Group(toolBarManager, "group.user", this.connectAction);
		NavigatorUtil.appendAction2Group(toolBarManager, "group.user", this.disConnectAction);
		
//		IMenuManager menuManager = actionBars.getMenuManager();
//		NavigatorUtil.insertContributionItemBefore(menuManager, "additions", new GroupMarker("group.user"));
//		NavigatorUtil.appendAction2Group(menuManager, "group.user", this.addUserAction);
//		NavigatorUtil.appendAction2Group(menuManager, "group.user", this.editUserAction);
//		NavigatorUtil.appendAction2Group(menuManager, "group.user", this.deleteUserAction);
	}
}
