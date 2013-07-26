/**
 * 
 */
package com.jae.eclipse.ui.factory.table.action;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.actions.SelectionProviderAction;

import com.jae.eclipse.ui.extension.ImageRepositoryManager;
import com.jae.eclipse.ui.factory.table.AbstractTableFactory;

/**
 * @author hongshuiqiao
 *
 */
public class TableAddAction extends SelectionProviderAction {
	private AbstractTableFactory factory;
	
	public TableAddAction(AbstractTableFactory factory, String text) {
		super(factory.getViewer(), text);
		this.factory = factory;
		this.setId("add");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("add"));
//		this.setEnabled(true);
	}

	@Override
	public TableViewer getSelectionProvider() {
		return (TableViewer) super.getSelectionProvider();
	}
	
	public AbstractTableFactory getFactory() {
		return factory;
	}
	
	@Override
	public void run() {
		Object newRowObject = this.factory.getValueTranslator().newRowObject();
		this.factory.getContentProvider().add(newRowObject);
		this.getSelectionProvider().refresh();
		this.factory.validate();
		this.getSelectionProvider().editElement(newRowObject, 0);
	}
}
