/**
 * 
 */
package com.jae.eclipse.ui.factory.table.action;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.actions.SelectionProviderAction;

import com.jae.eclipse.ui.extension.ImageRepositoryManager;
import com.jae.eclipse.ui.factory.table.AbstractTableFactory;

/**
 * @author hongshuiqiao
 *
 */
public class TableDeleteAction extends SelectionProviderAction {
	private AbstractTableFactory factory;
	
	public TableDeleteAction(AbstractTableFactory factory, String text) {
		super(factory.getViewer(), text);
		this.factory = factory;
		this.setId("remove");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("remove"));
		this.setEnabled(false);
	}

	@Override
	public TableViewer getSelectionProvider() {
		return (TableViewer) super.getSelectionProvider();
	}
	
	public AbstractTableFactory getFactory() {
		return factory;
	}
	
	@Override
	public void selectionChanged(IStructuredSelection selection) {
		super.selectionChanged(selection);
		Object[] selectedObjects = selection.toArray();
		if(selectedObjects.length>0){
			this.setEnabled(true);
		}else{
			this.setEnabled(false);
		}
	}
	
	@Override
	public void run() {
		Object[] selectedObjects = this.getStructuredSelection().toArray();
		for (Object selectedObject : selectedObjects) {
			this.factory.getContentProvider().remove(selectedObject);
		}
		this.getSelectionProvider().refresh();
		this.factory.validate();
	}
}
