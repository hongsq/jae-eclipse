/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.viewers.TableViewer;

import com.jae.eclipse.ui.factory.table.AbstractTableFactory;

/**
 * @author hongshuiqiao
 *
 */
public class AddURIAction extends AbstractJDAction {
	private AbstractTableFactory factory;
	
	public AddURIAction(AbstractTableFactory factory, String text) {
		super(factory.getViewer(), text);
		this.factory = factory;
	}

	@Override
	public TableViewer getSelectionProvider() {
		return (TableViewer) super.getSelectionProvider();
	}
	
	@Override
	public void run() {
//		this.factory.get
	}
}
