/**
 * 
 */
package com.jae.eclipse.ui.base;

import java.lang.reflect.Method;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.jae.eclipse.core.util.ObjectUtil;

/**
 * @author hongshuiqiao
 *
 */
public class ValuechageNotifier implements ISelectionChangedListener, ModifyListener, SelectionListener {
	private AbstractPropertyEditor editor;
	private Method fireMethod;

	public ValuechageNotifier(AbstractPropertyEditor editor) {
		super();
		this.editor = editor;
		try {
			this.fireMethod = ObjectUtil.getMethod(this.editor.getClass(), "fireValueChangeEvent", false);
			this.fireMethod.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void invokeFireMethod() {
		try {
			this.fireMethod.invoke(editor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void modifyText(ModifyEvent event) {
		invokeFireMethod();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		invokeFireMethod();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		invokeFireMethod();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		invokeFireMethod();
	}

}
