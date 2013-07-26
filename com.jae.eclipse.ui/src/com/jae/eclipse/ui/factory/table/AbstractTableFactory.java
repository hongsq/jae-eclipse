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
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jae.eclipse.ui.IColumnValidator;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.factory.AbstractViewerFactory;
import com.jae.eclipse.ui.impl.CompoundMessageCaller;

/**
 * @author hongshuiqiao
 * 
 */
public abstract class AbstractTableFactory extends AbstractViewerFactory implements ICellModifier {
	private boolean multiSelection = false;
	private ColumnModel[] columns = new ColumnModel[0];
	private boolean headerVisible = true;
	private boolean linesVisible = true;
	private ITableValueTranslator valueTranslator = new DefaultTableValueTranslator(this);
	private List<String> columnProperties = new ArrayList<String>();
	private TableMessageCaller tableMessageCaller;
	private CompoundMessageCaller compoundMessageCaller = new CompoundMessageCaller();

	@Override
	protected ISelection computeSelection(Point point) {
		Table table = this.getViewer().getTable();
		TableItem selectedItem = table.getItem(point);
		ISelection selection = null;
		if (null == selectedItem) {
			selection = StructuredSelection.EMPTY;
		} else {
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
		if (this.multiSelection)
			style = style | SWT.MULTI;
		else
			style = style | SWT.SINGLE;

		TableViewer viewer = new TableViewer(parent, style);
		Table table = viewer.getTable();
		this.columns = this.createColumns(viewer);
		if (null != this.columns) {
			CellEditor[] editors = new CellEditor[this.columns.length];
			for (int i = 0; i < this.columns.length; i++) {
				final ColumnModel columnModel = this.columns[i];

				final TableColumn column = new TableColumn(table, SWT.NONE);
				column.setText(columnModel.getTitle());
				ImageDescriptor image = columnModel.getTitleImage();
				if (null != image)
					column.setImage(image.createImage(true));
				column.setToolTipText(columnModel.getDescription());
				column.setWidth(columnModel.getWidth());
				column.setMoveable(columnModel.isMovable());
				column.setResizable(columnModel.isResizable());
				column.setAlignment(columnModel.getAlignment());
				column.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (columnModel.isSortable()) {
							sortColumn(columnModel, column);
						}
					}
				});
				if (null != this.getObjectOperator(false)
						&& null == columnModel.getObjectOperator(false))
					columnModel
							.setObjectOperator(this.getObjectOperator(false));

				editors[i] = columnModel.getCellEditor();
				this.columnProperties.add(columnModel.getPropertyName());
			}

			viewer.setColumnProperties(this.getColumnProperties());
			viewer.setCellEditors(editors);
			viewer.setCellModifier(this);
		} else {
			this.columns = new ColumnModel[0];
		}

		if (null == this.getContentProvider())
			this.setContentProvider(new ListTableDataProvider(this));

		if (null == this.getLabelProvider())
			this.setLabelProvider(new TableLabelProvider(this));

		table.setHeaderVisible(this.headerVisible);
		table.setLinesVisible(this.linesVisible);

		return viewer;
	}

	@Override
	protected void afterCreateControl() {
		super.afterCreateControl();
		this.tableMessageCaller = new TableMessageCaller();
		this.compoundMessageCaller.addMessageCaller(this.tableMessageCaller);

		if (null != this.getMessageCaller())
			this.compoundMessageCaller
					.addMessageCaller(this.getMessageCaller());
	}

	protected void sortColumn(ColumnModel columnModel, TableColumn column) {
		boolean isASC = true;// 正序(一般是从小到大)

		Table table = this.getViewer().getTable();
		if (column.equals(table.getSortColumn())) {
			// 再次点击同一个排序列
			int direction = table.getSortDirection();
			if (direction == SWT.DOWN)// 上次是从反序大到小，则此次使用正序从小到大
				isASC = true;
			else
				isASC = false;
		}

		this.getContentProvider().sort(columnModel.getComparator(), isASC);

		if (isASC)
			table.setSortDirection(SWT.UP);
		else
			table.setSortDirection(SWT.DOWN);
		
		table.setSortColumn(column);
		this.getViewer().refresh();
	}

	public ITableValueTranslator getValueTranslator() {
		return this.valueTranslator;
	}

	public void setValueTranslator(ITableValueTranslator valueTranslator) {
		this.valueTranslator = valueTranslator;
	}

	public TableMessageCaller getTableMessageCaller() {
		return tableMessageCaller;
	}

	@Override
	public ITableDataProvider getContentProvider() {
		return (ITableDataProvider) super.getContentProvider();
	}

	@Override
	public ITableLabelProvider getLabelProvider() {
		return (ITableLabelProvider) super.getLabelProvider();
	}

	public boolean isHeaderVisible() {
		return this.headerVisible;
	}

	public void setHeaderVisible(boolean headerVisible) {
		this.headerVisible = headerVisible;
	}

	public boolean isLinesVisible() {
		return this.linesVisible;
	}

	public void setLinesVisible(boolean linesVisible) {
		this.linesVisible = linesVisible;
	}

	@Override
	public TableViewer getViewer() {
		return (TableViewer) super.getViewer();
	}

	public void load() {
		this.getViewer().refresh();
	}

	public void save() {
		this.getValueTranslator().fromTable(this.getContentProvider().getInput().toArray(), this.getValue());
	}

	@Override
	protected Object createInput() {
		return this.getValueTranslator().toTable(this.getValue());
	}

	public boolean canModify(Object element, String property) {
		// element为模型
		ColumnModel column = this.getColumn(property);
		return null != column && null != column.getCellEditor() && column.isEditable();
	}

	public Object getValue(Object element, String property) {
		// element为模型
		Object instance = element;
		if (element instanceof TableItem) {
			instance = ((TableItem) element).getData();
		}

		Object value = this.getColumn(property).getObjectOperator(true).getValue(instance, property);
		if(null == value)
			return "";
		return value;
	}

	public void modify(Object element, String property, Object value) {
		// element为TableItem
		Object instance = element;
		if (element instanceof TableItem) {
			instance = ((TableItem) element).getData();
		}
		
		Object oldValue = this.getColumn(property).getObjectOperator(true).getValue(instance, property);
		if((null != oldValue && !oldValue.equals(value))
				|| (null == oldValue && null != value)){
			this.getColumn(property).getObjectOperator(true).setValue(instance, property, value);
			this.fireValuechanged(new ValueChangeEvent(instance, oldValue, value));
			this.validate();
			this.getViewer().refresh();
		}
		
	}

	@Override
	protected boolean doValidate() {
		if (null != this.tableMessageCaller)
			this.tableMessageCaller.clear();

		boolean flag = true;
		TableItem[] items = this.getViewer().getTable().getItems();
		for (int rowIndex = 0; rowIndex < items.length; rowIndex++) {
			TableItem tableItem = items[rowIndex];
			RowModel rowModel = (RowModel) tableItem.getData();
			this.tableMessageCaller.setValidateObject(rowModel);
			for (int columnIndex = 0; columnIndex < this.columns.length; columnIndex++) {
				this.tableMessageCaller.setValidateColumn(columnIndex);
				ColumnModel column = this.columns[columnIndex];
				IColumnValidator[] validators = column.getValidators();
				for (IColumnValidator validator : validators) {
					flag = validator.validate(this.compoundMessageCaller,
							this.getViewer(), rowModel, columnIndex, rowIndex,
							rowModel.get(column.getPropertyName())) && flag;
				}
			}
		}

		return flag;
	}

	public String[] getColumnProperties() {
		return this.columnProperties.toArray(new String[this.columnProperties.size()]);
	}

	public ColumnModel getColumn(String property) {
		for (ColumnModel column : this.columns) {
			if (column.getPropertyName().equals(property))
				return column;
		}
		return null;
	}

	public ColumnModel getColumn(int columnIndex) {
		if (columnIndex < 0 || columnIndex >= this.columns.length)
			return null;
		return this.columns[columnIndex];
	}

	protected abstract ColumnModel[] createColumns(TableViewer viewer);
}
