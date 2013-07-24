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
//	private final ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());
	
	public Image getColumnImage(Object element, int columnIndex) {
		if (element instanceof RowModel) {
			RowModel row = (RowModel) element;
			
			ImageDescriptor image = row.getFactory().getColumn(columnIndex).getItemImage();
			if(null != image){
				return decorateImage(image, element, columnIndex);
			}
		}
		return null;
	}

	protected Image decorateImage(ImageDescriptor base, Object element, int columnIndex) {
//		ImageDescriptor overlay = null;
//		DecorationOverlayIcon decorated = new DecorationOverlayIcon(base.createImage(false), overlay, IDecoration.BOTTOM_LEFT);
//		
//		return (Image) this.resourceManager.get(decorated);
		return base.createImage(true);
	}

	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof RowModel) {
			RowModel row = (RowModel) element;
			
			Object cellValue = row.getCellValue(columnIndex);
			if(null != cellValue){
				return decorateText(cellValue.toString(), element, columnIndex);
			}
		}
		return "";
	}

	protected String decorateText(String base, Object element, int columnIndex) {
		return base;
	}

}
