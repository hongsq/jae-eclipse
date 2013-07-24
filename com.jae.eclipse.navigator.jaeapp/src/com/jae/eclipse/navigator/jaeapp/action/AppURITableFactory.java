/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.TableViewer;

import com.jae.eclipse.navigator.util.NavigatorUtil;
import com.jae.eclipse.ui.factory.table.AbstractTableFactory;
import com.jae.eclipse.ui.factory.table.ColumnModel;

/**
 * @author hongshuiqiao
 *
 */
public class AppURITableFactory extends AbstractTableFactory {
	private IAction addURIAction;
	private IAction deleteURIAction;

	@Override
	protected ColumnModel[] createColumns(TableViewer viewer) {
		List<ColumnModel> list = new ArrayList<ColumnModel>();
		
		{
			ColumnModel column = new ColumnModel();
			column.setColumnName("域名");
			column.setPropertyName("uri");
			
			list.add(column);
		}
		
		return list.toArray(new ColumnModel[list.size()]);
	}

	@Override
	protected void fillActionToolBars(IToolBarManager manager) {
		NavigatorUtil.addAction(manager, this.addURIAction);
		NavigatorUtil.addAction(manager, this.deleteURIAction);
	}

	@Override
	protected void fillContextMenu(IMenuManager manager) {
		NavigatorUtil.addAction(manager, this.addURIAction);
		NavigatorUtil.addAction(manager, this.deleteURIAction);
	}

}
