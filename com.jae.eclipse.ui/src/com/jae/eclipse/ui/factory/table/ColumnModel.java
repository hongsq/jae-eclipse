/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;

import com.jae.eclipse.core.DefaultObjectOperator;
import com.jae.eclipse.core.ObjectOperator;
import com.jae.eclipse.ui.IColumnValidator;

/**
 * @author hongshuiqiao
 *
 */
public class ColumnModel {
	private String title;
	private String propertyName;
	private ImageDescriptor titleImage;
	private ImageDescriptor image;
	private String description;
	private int width=-1;
	private int alignment=SWT.LEFT;
	private boolean resizable;
	private boolean movable;
	private boolean editable;
	private boolean sortable;
	private CellEditor cellEditor;
	private List<IColumnValidator> validators = new ArrayList<IColumnValidator>();
	private ObjectOperator objectOperator;
	private Comparator<Object> comparator = new DefaultColumnComparator(this);

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @return
	 */
	public int getAlignment() {
		return alignment;
	}

	/**
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @param alignment
	 */
	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public CellEditor getCellEditor() {
		return cellEditor;
	}

	public void setCellEditor(CellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ImageDescriptor getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(ImageDescriptor titleImage) {
		this.titleImage = titleImage;
	}

	public ImageDescriptor getImage() {
		return image;
	}

	public void setImage(ImageDescriptor image) {
		this.image = image;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public IColumnValidator[] getValidators() {
		return this.validators.toArray(new IColumnValidator[this.validators.size()]);
	}

	public void addValidator(IColumnValidator validator){
		this.validators.add(validator);
	}
	
	public void removeValidator(IColumnValidator validator){
		this.validators.remove(validator);
	}
	
	public void clearValidators() {
		this.validators.clear();
	}

	public ObjectOperator getObjectOperator(boolean defaultIfNull) {
		if(null == this.objectOperator && defaultIfNull)
			return DefaultObjectOperator.INSTANCE;
		return objectOperator;
	}

	public void setObjectOperator(ObjectOperator objectOperator) {
		this.objectOperator = objectOperator;
	}

	public Comparator<Object> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<Object> comparator) {
		this.comparator = comparator;
	}

	public ImageDescriptor getImage(Object element, int columnIndex, int rowIndex) {
		return this.image;
	}

	public String getText(Object element, int columnIndex, int rowIndex) {
		ObjectOperator operator = this.getObjectOperator(true);
		
		Object value = operator.getValue(element, this.propertyName);
		if(null != value)
			return value.toString();
		return null;
	}
}
