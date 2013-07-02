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

import com.jae.eclipse.ui.IAdaptable;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IPropertyEditor;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.ComboPropertyEditor;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.impl.PropertyComposite;
import com.jae.eclipse.ui.util.StringUtil;

/**
 * @author hongshuiqiao
 *
 */
public class UIExample {
	
	protected static void createUI(Shell shell) {
		ObjectEditor objectEditor = new ObjectEditor();

		StringPropertyEditor editor = new StringPropertyEditor();
		editor.setLabel("label:");
		editor.setPropertyName("abc");
		editor.setRequired(true);
		editor.addValidator(new IValidator() {
			
			@Override
			public boolean validate(IMessageCaller messageCaller, IAdaptable adaptable) {
				IPropertyEditor editor = adaptable.getAdapter(IPropertyEditor.class);
				boolean isEmpty = StringUtil.isEmpty(editor.getValue()+"");
				if(isEmpty)
					messageCaller.error("不能为空！");
				return !isEmpty;
			}
		});
		objectEditor.addPropertyEditor(editor);
		
		editor = new StringPropertyEditor();
		editor.setLabel("label:");
		editor.setPropertyName("efg");
		editor.setRequired(true);
		editor.addValidator(new IValidator() {
			
			@Override
			public boolean validate(IMessageCaller messageCaller, IAdaptable adaptable) {
				IPropertyEditor editor = adaptable.getAdapter(IPropertyEditor.class);
				boolean isEmpty = StringUtil.isEmpty(editor.getValue()+"");
				if(isEmpty)
					messageCaller.error("不能为空！");
				return !isEmpty;
			}
		});
		objectEditor.addPropertyEditor(editor);
		
		ComboPropertyEditor comboEditor = new ComboPropertyEditor();
		comboEditor.setLabel("combo:");
		comboEditor.setPropertyName("combo");
//		comboEditor.setComboItems(new String[]{"","aa", "bb"});
		Map<String,Object> itemMap = new LinkedHashMap<>();
		itemMap.put("aa", 11);
		itemMap.put("bb", 22);
		itemMap.put("cc", 33);
		comboEditor.setComboItems(itemMap);
		
		comboEditor.addValidator(new IValidator() {
			
			@Override
			public boolean validate(IMessageCaller messageCaller, IAdaptable adaptable) {
				IPropertyEditor editor = adaptable.getAdapter(IPropertyEditor.class);
				boolean isEmpty = (null==editor.getValue());
				if(isEmpty)
					messageCaller.error("不能为空！");
				return !isEmpty;
			}
		});
		objectEditor.addPropertyEditor(comboEditor);
		
		Map map = new HashMap();
		map.put("abc", "abc");
//		map.put("efg", "efg");
		
		objectEditor.setEditElement(map);
		PropertyComposite composite = new PropertyComposite(shell, SWT.NONE, objectEditor);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		objectEditor.validate();
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

		while(!shell.isDisposed()){
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
	}

}
