/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action.provider;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import com.jae.eclipse.navigator.jaeapp.action.JDAppEditAction;
import com.jae.eclipse.navigator.jaeapp.action.JDAppImportCodeAction;
import com.jae.eclipse.navigator.util.NavigatorUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppActionProvider extends CommonActionProvider {
	private JDAppEditAction editAppAction;
	private JDAppImportCodeAction importCodeAction;

	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		StructuredViewer viewer = getActionSite().getStructuredViewer();
		
		editAppAction = new JDAppEditAction(viewer, "编辑应用");
		importCodeAction = new JDAppImportCodeAction(viewer, "导入源码");
	}

	@Override
	public void fillContextMenu(IMenuManager menuManager) {
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.editAppAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.importCodeAction);
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		IMenuManager menuManager = actionBars.getMenuManager();
		
		NavigatorUtil.insertContributionItemBefore(toolBarManager, "FRAME_ACTION_GROUP_ID", new GroupMarker("group.jdapp"));
		NavigatorUtil.insertContributionItemBefore(menuManager, "additions", new GroupMarker("group.jdapp"));
		
		NavigatorUtil.appendAction2Group(toolBarManager, "group.jdapp", this.editAppAction);
		NavigatorUtil.appendAction2Group(toolBarManager, "group.jdapp", this.importCodeAction);

		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.editAppAction);
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.importCodeAction);
	}
}
