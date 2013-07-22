/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;


/**
 * @author hongshuiqiao
 *
 */
public class TableLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof RowModel) {
			RowModel row = (RowModel) element;
			
			ImageDescriptor image = row.getFactory().getColumn(columnIndex).getItemImage();
			if(null != image)
				return image.createImage(true);
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof RowModel) {
			RowModel row = (RowModel) element;
			
			Object cellValue = row.getCellValue(columnIndex);
			if(null != cellValue)
				return cellValue.toString();
		}
		return "";
	}

}
