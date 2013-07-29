/**
 * 
 */
package com.jae.eclipse.ui.factory;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractViewerFactory extends AbstractControlFactory implements ISelectionChangedListener {
	private IContentProvider contentProvider;
	private IBaseLabelProvider labelProvider;
	private String title;
	private StructuredViewer viewer;
	private MenuManager menuManager;
	private ToolBarManager toolBarManager;
	
	public AbstractViewerFactory() {
		super();
		this.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	@Override
	protected Control doCreateControl(Composite parent) {
		ViewForm viewForm = new ViewForm(parent, SWT.FLAT|SWT.BORDER);
		
		viewer = createViewer(viewForm);
		Control viewControl = viewer.getControl();
		viewForm.setContent(viewControl);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.setInput(createInput());
		
		init(viewer);
		
		menuManager = new MenuManager("#PopupMenu");//创建菜单管理器
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				//刷新菜单
				fillContextMenu(manager);
			}

		});
		fillContextMenu(menuManager);
		viewControl.setMenu(menuManager.createContextMenu(viewControl));
		
		ToolBar toolBar = new ToolBar(viewForm, SWT.FLAT); // 创建一个ToolBar容器
		viewForm.setTopRight(toolBar); // 顶端右边缘：工具栏
		toolBarManager = new ToolBarManager(toolBar); // 创建一个toolBar的管理器
		fillActionToolBars(toolBarManager); // 将Action通过toolBarManager注入ToolBar中

		if(null != title){
			Label titleLabel = new Label(viewForm, SWT.NONE);
			titleLabel.setText(title);
			viewForm.setTopLeft(titleLabel);//顶端左侧：标题
		}
		
		Listener listener = new Listener(){
			public void handleEvent(Event event) {
				//添加mouseup监听器是因为现有的右键点击空白区域时，不能触发空的selection，这样常会导致菜单等的状态不正确
				viewer.setSelection(computeSelection(new Point(event.x, event.y)), true);
			}
		};
		viewControl.addListener (SWT.MouseUp,listener);
		viewer.addSelectionChangedListener(this);

		this.menuManager.update(true);
		this.toolBarManager.update(true);
		
		return viewForm;
	}
	
	protected void init(StructuredViewer viewer) {
		// nothing to do.
	}

	public void selectionChanged(SelectionChangedEvent event) {
		this.menuManager.update(true);
		this.toolBarManager.update(true);
	}

	protected abstract Object createInput();

	protected abstract void fillActionToolBars(IToolBarManager manager);

	protected abstract void fillContextMenu(IMenuManager manager);
	
	protected abstract ISelection computeSelection(Point point);

	protected abstract StructuredViewer createViewer(Composite parent);

	public IContentProvider getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(IContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public IBaseLabelProvider getLabelProvider() {
		return labelProvider;
	}

	public void setLabelProvider(IBaseLabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public StructuredViewer getViewer() {
		return viewer;
	}
	
	public void setValue(Object value) {
		super.setValue(value);
		
		if(null != this.viewer)
			this.viewer.setInput(this.createInput());
	}
}
