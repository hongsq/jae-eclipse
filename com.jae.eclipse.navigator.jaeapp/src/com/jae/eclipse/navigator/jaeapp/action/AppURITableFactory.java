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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import com.jae.eclipse.core.util.StringUtil;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.util.NavigatorUtil;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.factory.table.AbstractTableFactory;
import com.jae.eclipse.ui.factory.table.ColumnModel;
import com.jae.eclipse.ui.factory.table.DefaultTableValueTranslator;
import com.jae.eclipse.ui.factory.table.ITableValueTranslator;
import com.jae.eclipse.ui.factory.table.RowModel;
import com.jae.eclipse.ui.factory.table.action.TableAddAction;
import com.jae.eclipse.ui.factory.table.action.TableDeleteAction;
import com.jae.eclipse.ui.validator.ColumnValidator;
import com.jae.eclipse.ui.validator.TableRepeatValidator;

/**
 * @author hongshuiqiao
 *
 */
public class AppURITableFactory extends AbstractTableFactory {
	private IAction addURIAction;
	private IAction deleteURIAction;
	private JDApp app;
	private ITableValueTranslator valueTranslator = new DefaultTableValueTranslator(this){
		protected Object doNewRowObject() {
			Map<String, String> map = new HashMap<String, String>();
			return map;
		}
	};
	
	public AppURITableFactory(JDApp app) {
		super();
		this.app = app;
		this.setValueTranslator(valueTranslator);
	}

	@Override
	protected void init(StructuredViewer viewer) {
		super.init(viewer);
		addURIAction = new TableAddAction(this, "新增"){
			@Override
			public void selectionChanged(IStructuredSelection selection) {
				if(this.getSelectionProvider().getTable().getItemCount()>=2){
					this.setEnabled(false);
				}else{
					this.setEnabled(true);
				}
			}
		};
		deleteURIAction = new TableDeleteAction(this, "删除"){
			@Override
			public void selectionChanged(IStructuredSelection selection) {
				super.selectionChanged(selection);
				if(this.isEnabled() && null != AppURITableFactory.this.app){
					Object[] objects = selection.toArray();
					for (Object object : objects) {
						String uri = (String) ((RowModel)object).get("uri");
						if(null != uri &&
								uri.toLowerCase().startsWith(AppURITableFactory.this.app.getName().toLowerCase()+".")){
							this.setEnabled(false);
							break;
						}
					}
					
				}
			}
		};
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		boolean canModify = super.canModify(element, property);
		if(canModify && null != this.app){
			Object value = this.getValue(element, property);
			if(this.app.getName().equals(value)){
				return false;
			}
		}
		return canModify;
	}
	
	@Override
	public Object getValue(Object element, String property) {
		String uri = (String) super.getValue(element, property);
		if(null != uri && uri.contains("."))
			return uri.substring(0, uri.indexOf("."));
		return uri;
	}
	
	@Override
	public void modify(Object element, String property, Object value) {
		super.modify(element, property, value+".jd-app.com");
	}
	
	@Override
	protected ColumnModel[] createColumns(TableViewer viewer) {
		List<ColumnModel> list = new ArrayList<ColumnModel>();
		
		{
			ColumnModel column = new ColumnModel();
			column.setTitle("域名");
			column.setPropertyName("uri");
			column.setWidth(300);
			column.setSortable(true);
			column.setResizable(true);
			column.setEditable(true);
			column.addValidator(new ColumnValidator(new IValidator() {
				
				public boolean validate(IMessageCaller messageCaller, Object source, Object value) {
					if(null == value || StringUtil.isEmpty(value.toString())){
						messageCaller.error("域名不能为空。");
						return false;
					}
					
					if(!value.toString().endsWith(".jd-app.com")){
						messageCaller.error("域名必须以\".jd-app.com\"结束。");
						return false;
					}
					
					String prefix = value.toString().substring(0, value.toString().length()-".jd-app.com".length());
					if(StringUtil.isEmpty(prefix)){
						messageCaller.error("域名不能为空。");
						return false;
					}else if(prefix.contains(".")){
						messageCaller.error("域名不能带\".\"。");
						return false;
					}
					
					return true;
				}
			}));
			column.addValidator(new TableRepeatValidator("域名"));
			column.setCellEditor(new TextCellEditor(viewer.getTable()));
			
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
