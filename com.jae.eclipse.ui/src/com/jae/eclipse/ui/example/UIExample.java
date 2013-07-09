/**
 * 
 */
package com.jae.eclipse.ui.example;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.ComboPropertyEditor;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.event.ValidateEvent;
import com.jae.eclipse.ui.factory.ObjectEditorControlFactory;
import com.jae.eclipse.ui.impl.ControlFactoryDialog;
import com.jae.eclipse.ui.impl.PropertyComposite;
import com.jae.eclipse.ui.util.StringUtil;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UIExample {
	
	protected static void createUI(Shell shell) {
		ObjectEditor objectEditor = createObjectEditor();
		PropertyComposite composite = new PropertyComposite(shell, SWT.NONE, objectEditor);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		objectEditor.load();
		objectEditor.validate(new ValidateEvent(objectEditor));
	}

	private static ObjectEditor createObjectEditor() {
		ObjectEditor objectEditor = new ObjectEditor();

		{
			final StringPropertyEditor editor = new StringPropertyEditor();
			editor.setLabel("label:");
			editor.setPropertyName("abc");
			editor.setRequired(true);
			editor.addValidator(new IValidator() {
				@Override
				public boolean validate(IMessageCaller messageCaller, Object validateObject) {
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
			editor.setLabel("label:");
			editor.setPropertyName("efg");
			editor.setRequired(true);
			editor.addValidator(new IValidator() {
				
				@Override
				public boolean validate(IMessageCaller messageCaller, Object validateObject) {
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
			comboEditor.setLabel("combo:");
			comboEditor.setPropertyName("combo");
//			comboEditor.setComboItems(new String[]{"","aa", "bb"});
			Map<String,Object> itemMap = new LinkedHashMap<>();
			itemMap.put("aa", 11);
			itemMap.put("bb", 22);
			itemMap.put("cc", 33);
			comboEditor.setComboItems(itemMap);
			
			comboEditor.addValidator(new IValidator() {
				
				@Override
				public boolean validate(IMessageCaller messageCaller, Object validateObject) {
					boolean isEmpty = (null==comboEditor.getValue());
					if(isEmpty)
						messageCaller.error("不能为空！");
					return !isEmpty;
				}
			});
			objectEditor.addPropertyEditor(comboEditor);
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
		createUI(shell);
		
		
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
		Map map = new HashMap();
		map.put("abc", "abc");
		map.put("efg", "efg");
		map.put("combo", 11);
		ObjectEditorControlFactory factory = new ObjectEditorControlFactory(createObjectEditor());
		factory.setValue(map);
		
		factory.getUIDescription().setWinTitle("winTitle");
		factory.getUIDescription().setTitle("title");
		factory.getUIDescription().setDescription("description");
		factory.getUIDescription().setInitHeight(500);
		factory.getUIDescription().setInitWidth(500);
		
		ControlFactoryDialog dialog = new ControlFactoryDialog(shell, factory);
		
		dialog.setResizable(false);
		
		dialog.open();
		
		Map result = (Map) factory.getValue();
		System.out.println(result.get("combo"));
	}

}
