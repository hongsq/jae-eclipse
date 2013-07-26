/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.jae.eclipse.navigator.util.NavigatorUtil;
import com.jae.eclipse.ui.factory.table.AbstractTableFactory;
import com.jae.eclipse.ui.factory.table.ColumnModel;
import com.jae.eclipse.ui.factory.table.DefaultTableValueTranslator;
import com.jae.eclipse.ui.factory.table.ITableValueTranslator;
import com.jae.eclipse.ui.factory.table.action.TableAddAction;
import com.jae.eclipse.ui.factory.table.action.TableDeleteAction;
import com.jae.eclipse.ui.validator.ColumnValidator;
import com.jae.eclipse.ui.validator.NotEmptyValidator;
import com.jae.eclipse.ui.validator.TableRepeatValidator;

/**
 * @author hongshuiqiao
 *
 */
public class AppEnvTableFactory extends AbstractTableFactory {
	private ITableValueTranslator valueTranslator = new DefaultTableValueTranslator(this){
		protected Object doNewRowObject() {
			Map<String, String> map = new HashMap<String, String>();
			return map;
		}
	};
	private IAction addAction;
	private IAction deleteAction;
	
	public AppEnvTableFactory() {
		this.setValueTranslator(this.valueTranslator);
	}

	@Override
	protected void init(StructuredViewer viewer) {
		super.init(viewer);
		addAction = new TableAddAction(this, "新增");
		deleteAction = new TableDeleteAction(this, "删除");
	}
	
	@Override
	protected ColumnModel[] createColumns(TableViewer viewer) {
		List<ColumnModel> list = new ArrayList<ColumnModel>();
		
		{
			ColumnModel column = new ColumnModel();
			column.setTitle("变量名");
			column.setPropertyName("name");
			column.setWidth(100);
			column.setResizable(true);
			column.setEditable(true);
			column.addValidator(new ColumnValidator(new NotEmptyValidator("变量名")));
			column.addValidator(new TableRepeatValidator("变量名"));
			column.setCellEditor(new TextCellEditor(viewer.getTable()));
			
			list.add(column);
		}
		{
			ColumnModel column = new ColumnModel();
			column.setTitle("变量值");
			column.setPropertyName("value");
			column.setWidth(100);
			column.setResizable(true);
			column.setEditable(true);
			column.addValidator(new ColumnValidator(new NotEmptyValidator("变量值")));
			column.addValidator(new TableRepeatValidator("变量值"));
			column.setCellEditor(new TextCellEditor(viewer.getTable()));
			
			list.add(column);
		}
		
		return list.toArray(new ColumnModel[list.size()]);
	}
	
	@Override
	protected void fillActionToolBars(IToolBarManager manager) {
		NavigatorUtil.addAction(manager, this.addAction);
		NavigatorUtil.addAction(manager, this.deleteAction);
	}

	@Override
	protected void fillContextMenu(IMenuManager manager) {
		NavigatorUtil.addAction(manager, this.addAction);
		NavigatorUtil.addAction(manager, this.deleteAction);
	}
}