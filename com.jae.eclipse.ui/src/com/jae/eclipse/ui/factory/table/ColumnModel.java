/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;

import com.jae.eclipse.ui.IValidator;

/**
 * @author hongshuiqiao
 *
 */
public class ColumnModel {
	private String columnName;
	private String propertyName;
	private ImageDescriptor columnImage;
	private ImageDescriptor itemImage;
	private String description;
	private int width=-1;
	private int alignment=SWT.CENTER;
	private boolean resizable;
	private boolean movable;
	private boolean editable;
	private CellEditor cellEditor;
	private IValidator validator;

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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public ImageDescriptor getColumnImage() {
		return columnImage;
	}

	public void setColumnImage(ImageDescriptor columnImage) {
		this.columnImage = columnImage;
	}

	public ImageDescriptor getItemImage() {
		return itemImage;
	}

	public void setItemImage(ImageDescriptor itemImage) {
		this.itemImage = itemImage;
	}

	public IValidator getValidator() {
		return validator;
	}

	public void setValidator(IValidator validator) {
		this.validator = validator;
	}
}
