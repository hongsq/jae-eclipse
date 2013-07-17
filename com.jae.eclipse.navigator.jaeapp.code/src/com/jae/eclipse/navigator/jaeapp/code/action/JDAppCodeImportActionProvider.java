/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.code.action;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;

import com.jae.eclipse.navigator.util.NavigatorUtil;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppCodeImportActionProvider extends CommonActionProvider {
	private JDappCodeImportAction importCodeAction;

	@Override
	public void init(ICommonActionExtensionSite aSite) {
		super.init(aSite);
		StructuredViewer viewer = getActionSite().getStructuredViewer();
		
		importCodeAction = new JDappCodeImportAction(viewer, "导入源码");
	}

	@Override
	public void fillContextMenu(IMenuManager menuManager) {
		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.importCodeAction);
	}
	
	@Override
	public void fillActionBars(IActionBars actionBars) {
//		IToolBarManager toolBarManager = actionBars.getToolBarManager();
//		NavigatorUtil.insertContributionItemBefore(toolBarManager, "FRAME_ACTION_GROUP_ID", new GroupMarker("group.jdapp"));
//		NavigatorUtil.appendAction2Group(toolBarManager, "group.jdapp", this.importCodeAction);

//		IMenuManager menuManager = actionBars.getMenuManager();
//		NavigatorUtil.insertContributionItemBefore(menuManager, "additions", new GroupMarker("group.jdapp"));
//		NavigatorUtil.appendAction2Group(menuManager, "group.jdapp", this.importCodeAction);
	}
}
