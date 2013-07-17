/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;

import com.jae.eclipse.navigator.jaeapp.model.AbstractJDElement;
import com.jae.eclipse.navigator.jaeapp.model.LoadState;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;

/**
 * @author hongshuiqiao
 *
 */
public class RefreshAction extends AbstractJDAction {

	public RefreshAction(ISelectionProvider provider, String text) {
		super(provider, text);
		
		this.setId("common.action.refresh");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("common.action.refresh"));
		this.setMustSelect(true);
		this.setMultiable(true);
		this.setSelectType(AbstractJDElement.class);
		this.setEnabled(false);
	}

	@Override
	public void run() {
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		Object[] elements = this.getStructuredSelection().toArray();
		for (Object element : elements) {
			AbstractJDElement jdElement = (AbstractJDElement) element;
			
			//正在加载的就没有必要再刷新了
			if(jdElement.getLoadState() != LoadState.LOADING){
				jdElement.refresh();
//				((TreeViewer)viewer).collapseToLevel(element, 1);
				viewer.refresh(element);
			}
		}
	}
}
