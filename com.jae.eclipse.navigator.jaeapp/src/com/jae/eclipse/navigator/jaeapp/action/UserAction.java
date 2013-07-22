/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.io.IOException;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.navigator.CommonViewer;

import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;
import com.jae.eclipse.ui.factory.IControlFactory;
import com.jae.eclipse.ui.factory.impl.ObjectEditorControlFactory;
import com.jae.eclipse.ui.impl.ControlFactoryDialog;
import com.jae.eclipse.ui.validator.NotEmptyValidator;

/**
 * @author hongshuiqiao
 *
 */
public class UserAction extends AbstractJDAction {
	private boolean edit;

	public UserAction(ISelectionProvider provider, String text, boolean edit) {
		super(provider, text);
		this.edit = edit;
		
		String id = "user.action.add";
		if(edit){
			id = "user.action.edit";
			this.setEnabled(false);
		}

		this.setMustSelect(this.edit);
		this.setSelectType(User.class);
		this.setMultiable(false);
		this.setEnabled(!this.edit);
		
		this.setId(id);
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor(id));
	}
	
	private ObjectEditor createObjectEditor(){
		ObjectEditor objectEditor = new ObjectEditor();
//		objectEditor.setLayout(new GridLayout(4, false));
		
		{
			StringPropertyEditor editor = new StringPropertyEditor();
			editor.setPropertyName("displayName");
			editor.setLabel("名称");
			editor.addValidator(new NotEmptyValidator("名称"));
			objectEditor.addPropertyEditor(editor);
		}
		{
			StringPropertyEditor editor = new StringPropertyEditor();
			editor.setPropertyName("accessKey");
			editor.setLabel("accessKey");
			editor.addValidator(new NotEmptyValidator("accessKey"));
			objectEditor.addPropertyEditor(editor);
		}
		
		{
			StringPropertyEditor editor = new StringPropertyEditor();
			editor.setPropertyName("secretKey");
			editor.setLabel("secretKey");
			editor.addValidator(new NotEmptyValidator("secretKey"));
			objectEditor.addPropertyEditor(editor);
		}
		
		return objectEditor;
	}
	
	@Override
	public void run() {
		IControlFactory factory = new ObjectEditorControlFactory(this.createObjectEditor());
		String prefix = null;
		User user = null;
		if(this.edit){
			prefix = "编辑";
			user = (User) this.getStructuredSelection().getFirstElement();
		}else{
			prefix = "新增";
			user = new User("");
		}
		
		factory.getUIDescription().setWinTitle(prefix+"用户");
		factory.getUIDescription().setTitle(prefix+"用户");
		factory.getUIDescription().setDescription(prefix+"一个用户的相关信息。");
		
		factory.setValue(user);
		
		CommonViewer viewer = (CommonViewer) this.getSelectionProvider();
		
		Shell shell = viewer.getTree().getShell();
		ControlFactoryDialog dialog = new ControlFactoryDialog(shell, factory);
		if(Window.OK == dialog.open()){
			try {
				if(edit)
					JAEAppHelper.save();
				else{
					JAEAppHelper.regeditUser(user);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			viewer.refresh();
		}
	}
}
