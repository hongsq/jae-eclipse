/**
 * 
 */
package com.jae.eclipse.ui.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.base.AbstractPropertyEditor;

/**
 * @author hongshuiqiao
 *
 */
public class ComboPropertyEditor extends AbstractPropertyEditor {
	private boolean readOnly=true;
	private int limit=-1;
	private List<String> items = new ArrayList<String>();
	private Map<String, Object> itemMap = new LinkedHashMap<String, Object>();

	@Override
	public Control createControl(Composite parent, int style) {
		int newStyle = style;
		if(readOnly)
			newStyle = newStyle|SWT.READ_ONLY;
		Combo combo = new Combo(parent, newStyle);
		
		if(!items.isEmpty())
			combo.setItems(items.toArray(new String[items.size()]));
		
		if(limit>0)
			combo.setTextLimit(limit);
		
		return combo;
	}
	
	public void setComboItems(String[] items){
		this.items.clear();
		this.itemMap.clear();
		
		this.items.addAll(Arrays.asList(items));
	}
	
	public void setComboItems(Map<String, Object> items){
		this.items.clear();
		this.itemMap.clear();
		
		this.items.addAll(items.keySet());
		this.itemMap.putAll(items);
	}
	
	public Combo getCombo(){
		return (Combo) this.getEditControl();
	}

	@Override
	public void afterBuild(Control parent) {
		super.afterBuild(parent);
		Combo combo = getCombo();
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				fireValueChangeEvent();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		if(!readOnly){
			combo.addModifyListener(new ModifyListener() {
				
				@Override
				public void modifyText(ModifyEvent e) {
					fireValueChangeEvent();
				}
			});
		}

	}
	
	@Override
	protected Object doGetValue() {
		Combo combo = getCombo();
		String item = combo.getText();
		if(itemMap.isEmpty()) {
			return item;
		} else{
			if(null != item)
				return itemMap.get(item);
		}
		
		return null;
	}

	@Override
	protected void doSetValue(Object value) {
		if(null == value)
			return;
		
		Combo combo = getCombo();
		
		String item = null;
		if(!this.itemMap.isEmpty()){
			for (String key : this.items) {
				if(value.equals(this.itemMap.get(key))){
					item = key;
					break;
				}
			}
		}else{
			item = value.toString();
		}
		
		if(null != item)
			combo.setText(item);
	}

}
