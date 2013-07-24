/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.event.ValidateEvent;
import com.jae.eclipse.ui.factory.AbstractViewerFactory;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractTableFactory extends AbstractViewerFactory implements ICellModifier {
	private boolean multiSelection=false;
	private List<IValidator> validators = new ArrayList<IValidator>();
	private ColumnModel[] columns = new ColumnModel[0];
	private TableValueTranslator valueTranslator;
	private boolean headerVisible=true;
	private boolean linesVisible=true;

	@Override
	protected ISelection computeSelection(Point point) {
		Table table = this.getViewer().getTable();
		TableItem selectedItem = table.getItem(point);
		ISelection selection = null;
		if(null == selectedItem){
			selection = StructuredSelection.EMPTY;
		}else{
			TableItem[] items = table.getSelection();
			List<Object> list = new ArrayList<Object>(items.length);
			for (TableItem tableItem : items) {
				list.add(tableItem.getData());
			}
			
			selection = new StructuredSelection(list);
		}
		return selection;
	}

	@Override
	protected TableViewer createViewer(Composite parent) {
		int style = SWT.FULL_SELECTION;
		if(this.multiSelection)
			style = style | SWT.MULTI;
		else
			style = style | SWT.SINGLE;
		
		TableViewer viewer = new TableViewer(parent, style);
		Table table = viewer.getTable();
		columns = this.createColumns(viewer);
		if(null != columns){
			String[] columnProperties = new String[columns.length];
			CellEditor[] editors = new CellEditor[columns.length];
			for (int i = 0; i < columns.length; i++) {
				ColumnModel columnModel = columns[i];
				
				TableColumn column = new TableColumn(table, SWT.CENTER);
				column.setText(columnModel.getColumnName());
				ImageDescriptor image = columnModel.getColumnImage();
				if(null != image)	column.setImage(image.createImage(true));
				IValidator validator = columnModel.getValidator();
				if(null != validator)	this.validators.add(validator);
				column.setToolTipText(columnModel.getDescription());
				column.setWidth(columnModel.getWidth());
				column.setMoveable(columnModel.isMovable());
				column.setResizable(columnModel.isResizable());
				column.setAlignment(columnModel.getAlignment());
				
				editors[i] = columnModel.getCellEditor();
				columnProperties[i] = columnModel.getPropertyName();
			}
			
			viewer.setColumnProperties(columnProperties);
			viewer.setCellEditors(editors);
			viewer.setCellModifier(this);
		}else{
			columns = new ColumnModel[0];
		}
		
		if(null == this.getContentProvider())
			this.setContentProvider(new CommonContentProvider());
		
		if(null == this.getLabelProvider())
			this.setLabelProvider(new TableLabelProvider());
		
		table.setHeaderVisible(headerVisible);
		table.setLinesVisible(linesVisible);
		
		return viewer;
	}

	public TableValueTranslator getValueTranslator() {
		return valueTranslator;
	}

	public void setValueTranslator(TableValueTranslator valueTranslator) {
		this.valueTranslator = valueTranslator;
	}

	public boolean isHeaderVisible() {
		return headerVisible;
	}

	public void setHeaderVisible(boolean headerVisible) {
		this.headerVisible = headerVisible;
	}

	public boolean isLinesVisible() {
		return linesVisible;
	}

	public void setLinesVisible(boolean linesVisible) {
		this.linesVisible = linesVisible;
	}

	@Override
	public TableViewer getViewer() {
		return (TableViewer) super.getViewer();
	}
	
	public ColumnModel[] getColumns() {
		return columns;
	}
	
	@Override
	protected RowModel[] createInput() {
		return this.getValueTranslator().to(this.getValue());
	}
	
	public void load() {
		this.getViewer().refresh();
	}

	public void save() {
		this.getValueTranslator().from((RowModel[])this.getViewer().getInput());
	}

	public boolean canModify(Object element, String property) {
		RowModel row = null;
		if (element instanceof TableItem) {
			row = (RowModel) ((TableItem) element).getData();
		} else if (element instanceof RowModel) {
			row = (RowModel)element;
		}
		ColumnModel column = row.getFactory().getColumn(property);
		return null != column && null != column.getCellEditor() && column.isEditable();
	}

	public Object getValue(Object element, String property) {
		RowModel row = null;
		if (element instanceof TableItem) {
			row = (RowModel) ((TableItem) element).getData();
		} else if (element instanceof RowModel) {
			row = (RowModel)element;
		}
		return row.getCellValue(property);
	}

	public void modify(Object element, String property, Object value) {
		RowModel row = null;
		if (element instanceof TableItem) {
			row = (RowModel) ((TableItem) element).getData();
		} else if (element instanceof RowModel) {
			row = (RowModel)element;
		}
		row.setCellValue(property, value);
		
		this.getViewer().refresh();
	}

	@Override
	protected boolean doValidate(ValidateEvent event) {
		boolean flag = true;
		IMessageCaller messageCaller = this.getMessageCaller();
		if(null != messageCaller)
			messageCaller.clear();

		for (IValidator validator : this.validators) {
			flag = validator.validate(messageCaller, this) && flag;
		}
		return flag;
	}
	
	public ColumnModel getColumn(String property){
		for (ColumnModel column : this.columns) {
			if(column.getPropertyName().equals(property))
				return column;
		}
		return null;
	}
	
	public ColumnModel getColumn(int columnIndex){
		if(columnIndex<0 || columnIndex>=this.columns.length)
			return null;
		return this.columns[columnIndex];
	}
	
	protected abstract ColumnModel[] createColumns(TableViewer viewer);
}
