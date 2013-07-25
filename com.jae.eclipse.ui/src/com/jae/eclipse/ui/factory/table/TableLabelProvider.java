/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.jae.eclipse.core.Level;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;
import com.jae.eclipse.ui.resource.PoolImageDescriptor;


/**
 * @author hongshuiqiao
 *
 */
public class TableLabelProvider extends BaseLabelProvider implements ITableLabelProvider {
	private AbstractTableFactory tableFactory;
	private static ImageDescriptor errorOvr = ImageRepositoryManager.getImageDescriptor("error_ovr");
	private static ImageDescriptor warningOvr = ImageRepositoryManager.getImageDescriptor("warning_ovr");

	static{
		FieldDecorationRegistry fieldDecorationRegistry = FieldDecorationRegistry.getDefault();
		
		FieldDecoration warningFieldDecoration = fieldDecorationRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING);
		warningOvr = new PoolImageDescriptor(ImageDescriptor.createFromImage(warningFieldDecoration.getImage()));

		FieldDecoration errorFieldDecoration = fieldDecorationRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		errorOvr = new PoolImageDescriptor(ImageDescriptor.createFromImage(errorFieldDecoration.getImage()));
	}
	
	public TableLabelProvider(AbstractTableFactory tableFactory) {
		super();
		this.tableFactory = tableFactory;
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		ITableDataProvider dataProvider = this.tableFactory.getContentProvider();
		int rowIndex = dataProvider.indexOf(element);
		ImageDescriptor image = this.tableFactory.getColumn(columnIndex).getImage(element, columnIndex, rowIndex);
		return decorateImage(image, element, columnIndex, rowIndex);
	}

	protected Image decorateImage(ImageDescriptor base, Object element, int columnIndex, int rowIndex) {
		TableMessageCaller messageCaller = this.tableFactory.getTableMessageCaller();
		Level messageLevel = Level.NONE;
		if(null != messageCaller)
			messageLevel = messageCaller.getMessageLevel(element, columnIndex, rowIndex);
		
		ImageDescriptor overlay = null;
		switch (messageLevel) {
		case ERROR:
			overlay = errorOvr;
			break;
		case WARNING:
			overlay = warningOvr;
			break;
		default:
			break;
		}
		
		ImageDescriptor image = null;
		if(null == base){
			image = overlay;
		}else{
			DecorationOverlayIcon decorated = new DecorationOverlayIcon(base.createImage(true), overlay, IDecoration.BOTTOM_LEFT);
			image = new PoolImageDescriptor(decorated);
		}
		
		if(null == image)
			return null;
		
		return image.createImage(true);
	}

	public String getColumnText(Object element, int columnIndex) {
		ITableDataProvider dataProvider = this.tableFactory.getContentProvider();
		int rowIndex = dataProvider.indexOf(element);
		String baseText = this.tableFactory.getColumn(columnIndex).getText(element, columnIndex, rowIndex);
		return decorateText(baseText, element, columnIndex, rowIndex);
	}

	protected String decorateText(String base, Object element, int columnIndex, int rowIndex) {
		if(null == base)
			return "";
		return base;
	}

}
