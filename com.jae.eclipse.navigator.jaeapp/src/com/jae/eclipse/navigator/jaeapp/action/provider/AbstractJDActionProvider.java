/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action.provider;

import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractJDActionProvider extends CommonActionProvider {
	@Override
	public void updateActionBars() {
		ICommonViewerSite viewSite = getActionSite().getViewSite();
		if (viewSite instanceof ICommonViewerWorkbenchSite) {
			((ICommonViewerWorkbenchSite) viewSite).getActionBars().updateActionBars();
		}
	}
}
