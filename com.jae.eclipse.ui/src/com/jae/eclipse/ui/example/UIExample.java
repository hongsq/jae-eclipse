/**
 * 
 */
package com.jae.eclipse.ui.example;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.core.util.StringUtil;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IPropertyEditor;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.BooleanPropertyEditor;
import com.jae.eclipse.ui.control.ComboPropertyEditor;
import com.jae.eclipse.ui.control.DirectorySelectionPropertyEditor;
import com.jae.eclipse.ui.control.FileSelectionPropertyEditor;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.dialog.DetailMessageDialog;
import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.impl.PropertyComposite;
import com.jae.eclipse.ui.validator.NotEmptyValidator;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UIExample {
	
	protected static Control createUI(Composite parent) {
		ObjectEditor objectEditor = createObjectEditor();
		PropertyComposite composite = new PropertyComposite(parent, SWT.NONE, objectEditor);
		
		objectEditor.load();
		objectEditor.validate();
		
		return composite;
	}

	private static ObjectEditor createObjectEditor() {
		final ObjectEditor objectEditor = new ObjectEditor();
		objectEditor.setLayout(new GridLayout(4, false));

		{
			final StringPropertyEditor editor = new StringPropertyEditor();
			editor.setLabel("label");
			editor.setPropertyName("abc");
			editor.setRequired(true);
			editor.addValidator(new IValidator() {
				public boolean validate(IMessageCaller messageCaller, Object source, Object value) {
					boolean isEmpty = StringUtil.isEmpty(editor.getValue()+"");
					if(isEmpty)
						messageCaller.error("不能为空！");
					
					if("a".equals(editor.getValue()))
						messageCaller.warn("test warning");
					
					if("ab".equals(editor.getValue()))
						messageCaller.info("test info");
					
					return !isEmpty;
				}
			});
			objectEditor.addPropertyEditor(editor);
		}
		
		{
			final StringPropertyEditor editor = new StringPropertyEditor();
			editor.setLabel("label");
			editor.setPropertyName("efg");
			editor.setRequired(true);
			editor.addValidator(new IValidator() {
				
				public boolean validate(IMessageCaller messageCaller, Object source, Object value) {
					boolean isEmpty = StringUtil.isEmpty(editor.getValue()+"");
					if(isEmpty)
						messageCaller.error("不能为空！");
					return !isEmpty;
				}
			});
			objectEditor.addPropertyEditor(editor);
		}
		
		{
			final ComboPropertyEditor comboEditor = new ComboPropertyEditor();
			comboEditor.setLabel("combo");
			comboEditor.setPropertyName("combo");
//			comboEditor.setComboItems(new String[]{"","aa", "bb"});
			Map<String,Object> itemMap = new LinkedHashMap<String,Object>();
			itemMap.put("aa", 11);
			itemMap.put("bb", 22);
			itemMap.put("cc", 33);
			comboEditor.setComboItems(itemMap);
			
			comboEditor.addValidator(new IValidator() {
				
				public boolean validate(IMessageCaller messageCaller, Object source, Object value) {
					boolean isEmpty = (null==comboEditor.getValue());
					if(isEmpty)
						messageCaller.error("不能为空！");
					return !isEmpty;
				}
			});
			objectEditor.addPropertyEditor(comboEditor);
		}
		
		{
			final BooleanPropertyEditor editor = new BooleanPropertyEditor();
			editor.setLabel("boolean");
			editor.setPropertyName("boolean");
//			editor.setReverse(true);
			editor.addValuechangeListener(new IValuechangeListener() {
				
				public void valuechanged(ValueChangeEvent event) {
					IPropertyEditor abcEditor = objectEditor.getPropertyEditor("file");
					
					if(Boolean.TRUE.equals(editor.getValue()))
						abcEditor.setEnable(true);
					else
						abcEditor.setEnable(false);
					
				}
			});
			objectEditor.addPropertyEditor(editor);
		}
		
		{
			FileSelectionPropertyEditor editor = new FileSelectionPropertyEditor();
			editor.getLayoutData().horizontalSpan = 3;
			editor.setLabel("file");
			editor.setPropertyName("file");
			editor.addValidator(new NotEmptyValidator("file"));
			
			editor.setWinTitle("title");
			editor.setDialogStyle(SWT.OPEN);
			editor.setFileName("fileName");
			editor.setFilterExtensions(new String[]{"*.war,*.zip", "*.war;*.zip", "*.*"});
//			editor.setFilterNames(new String[]{"name1","name2"});
//			editor.setInitFilterExtension("*.2");
			editor.setFilterPath("D:/");
			editor.setOverwrite(true);
			editor.setMulti(true);
//			editor.setReadOnly(true);
			
			objectEditor.addPropertyEditor(editor);
		}
		
		{
			DirectorySelectionPropertyEditor editor = new DirectorySelectionPropertyEditor();
			editor.getLayoutData().horizontalSpan = 3;
			editor.setLabel("directory");
			editor.setPropertyName("directory");
			editor.addValidator(new NotEmptyValidator("directory"));
			
			editor.setWinTitle("directory");
			editor.setMessage("message");
			editor.setFilterPath("D:/");
			editor.setReadOnly(true);
			
			objectEditor.addPropertyEditor(editor);
		}
		
		Map map = new HashMap();
		map.put("abc", "abc");
//		map.put("efg", "efg");
		
		objectEditor.setValue(map);
		return objectEditor;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		
		Shell shell = new Shell(display);
		
		shell.setText("UI Example");
		shell.setSize(500, 500);
		shell.setLocation(300, 200);
		
		shell.setLayout(new GridLayout(2, false));
		
//		Control control = createUI(shell);
//		control.setLayoutData(new GridData(GridData.FILL_BOTH));

		ViewForm viewForm = new ViewForm(shell, SWT.NONE);
		viewForm.setTopCenterSeparate(false);
		viewForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Control control = createUI(viewForm);
		viewForm.setContent(control);
		
//		Label label = new Label(viewForm, SWT.NONE);
//		label.setText("TopCenter");
//		viewForm.setTopCenter(label);
		
		Label label = new Label(viewForm, SWT.NONE);
		label.setText("TopLeft");
		viewForm.setTopLeft(label);
		
		label = new Label(viewForm, SWT.NONE);
		label.setText("TopRight");
		viewForm.setTopRight(label);
		
//		IMenuManager menuManager = new MenuManager();
		
//		viewForm.setMenu(menuManager.);
		
//		Label label = new Label(shell, SWT.NONE);
//		label.setText("test");
		
//		shell.pack();
		shell.open();

		createDialog(shell);
		
		while(!shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
	}

	private static void createDialog(Shell shell) {
//		Map map = new HashMap();
//		map.put("abc", "abc");
//		map.put("efg", "efg");
//		map.put("combo", 11);
//		ObjectEditorControlFactory factory = new ObjectEditorControlFactory(createObjectEditor());
//		factory.setValue(map);
//		
//		factory.getUIDescription().setWinTitle("winTitle");
//		factory.getUIDescription().setTitle("title");
//		factory.getUIDescription().setDescription("description");
//		factory.getUIDescription().setInitHeight(500);
//		factory.getUIDescription().setInitWidth(500);
//		
//		ControlFactoryDialog dialog = new ControlFactoryDialog(shell, factory);
//		
//		dialog.setResizable(false);
//		
//		dialog.open();
//		
//		Map result = (Map) factory.getValue();
//		System.out.println(result.get("combo"));
		
		DetailMessageDialog.openError(shell, "title", "message", "detail");
		
	}

}
