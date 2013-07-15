/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;
import com.jae.eclipse.ui.factory.IControlFactory;
import com.jae.eclipse.ui.factory.ObjectEditorControlFactory;
import com.jae.eclipse.ui.impl.ControlFactoryDialog;
import com.jae.eclipse.ui.validator.NotEmptyValidator;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppEditAction extends AbstractJDAction {

	public JDAppEditAction(ISelectionProvider provider, String text) {
		super(provider, text);
		
		this.setId("jdapp.action.edit");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("jdapp.action.edit"));
		this.setMustSelect(true);
		this.setMultiable(false);
		this.setSelectType(JDApp.class);
	}

	private ObjectEditor createObjectEditor(){
		ObjectEditor objectEditor = new ObjectEditor();
//		objectEditor.setLayout(new GridLayout(4, false));
		
		{
			StringPropertyEditor editor = new StringPropertyEditor();
			editor.setPropertyName("repositoryURL");
			editor.setLabel("代码仓库:");
			editor.addValidator(new NotEmptyValidator("代码仓库"));
			objectEditor.addPropertyEditor(editor);
		}
		
		return objectEditor;
	}
	
	@Override
	public void run() {
		IControlFactory factory = new ObjectEditorControlFactory(this.createObjectEditor());
		factory.getUIDescription().setWinTitle("编辑应用");
		factory.getUIDescription().setTitle("编辑应用");
		factory.getUIDescription().setDescription("编辑应用相关信息");
		
		JDApp app = (JDApp) this.getStructuredSelection().getFirstElement();
		
		factory.setValue(app);
		
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		
		Shell shell = viewer.getControl().getShell();
		ControlFactoryDialog dialog = new ControlFactoryDialog(shell, factory);
		if(Window.OK == dialog.open()){
			//String repositoryURL = app.getRepositoryURL();
			
			viewer.refresh(app);
		}
	}
}
