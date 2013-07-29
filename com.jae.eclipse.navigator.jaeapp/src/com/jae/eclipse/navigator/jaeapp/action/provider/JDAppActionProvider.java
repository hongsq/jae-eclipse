/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action.provider;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import com.jae.eclipse.navigator.jaeapp.action.JDAppDeployAction;
import com.jae.eclipse.navigator.jaeapp.action.JDAppEditAction;
import com.jae.eclipse.navigator.jaeapp.action.JDAppOperatorAction;
import com.jae.eclipse.navigator.util.NavigatorUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppActionProvider extends AbstractJDActionProvider {
	private JDAppEditAction editAppAction;
	private JDAppDeployAction deployAction;
	private JDAppOperatorAction startAction;
	private JDAppOperatorAction restartAction;
	private JDAppOperatorAction stopAction;

	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		StructuredViewer viewer = getActionSite().getStructuredViewer();
		
		editAppAction = new JDAppEditAction(viewer, "编辑应用");
		deployAction = new JDAppDeployAction(viewer, "部署");
		startAction = new JDAppOperatorAction(viewer, "启动应用", JDAppOperatorAction.OP_TYPE_START);
		restartAction = new JDAppOperatorAction(viewer, "重启应用", JDAppOperatorAction.OP_TYPE_RESTART);
		stopAction = new JDAppOperatorAction(viewer, "停止应用", JDAppOperatorAction.OP_TYPE_STOP);
	}

	@Override
	public void fillContextMenu(IMenuManager menuManager) {
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.editAppAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.deployAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.startAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.restartAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.stopAction);
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
//		IToolBarManager toolBarManager = actionBars.getToolBarManager();
//		NavigatorUtil.insertContributionItemBefore(toolBarManager, "FRAME_ACTION_GROUP_ID", new GroupMarker("group.jdapp"));
//		NavigatorUtil.appendAction2Group(toolBarManager, "group.jdapp", this.editAppAction);
//		NavigatorUtil.appendAction2Group(toolBarManager, "group.jdapp", this.deployAction);
//		NavigatorUtil.appendAction2Group(toolBarManager, "group.jdapp", this.startAction);
//		NavigatorUtil.appendAction2Group(toolBarManager, "group.jdapp", this.stopAction);

//		IMenuManager menuManager = actionBars.getMenuManager();
//		NavigatorUtil.insertContributionItemBefore(menuManager, "additions", new GroupMarker("group.jdapp"));
//		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.editAppAction);
//		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.deployAction);
//		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.startAction);
//		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.stopAction);
	}
}
