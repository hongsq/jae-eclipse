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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.base.AbstractPropertyEditor;
import com.jae.eclipse.ui.base.ValuechageNotifier;
import com.jae.eclipse.ui.util.UIUtil;

/**
 * @author hongshuiqiao
 *
 */
public class ComboPropertyEditor extends AbstractPropertyEditor {
	private boolean readOnly=true;
	private int limit=-1;
	private List<String> items = new ArrayList<String>();
	private Map<String, Object> itemMap = new LinkedHashMap<String, Object>();

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
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
		if(UIUtil.isControlValid(getCombo()))
			getCombo().setTextLimit(limit);
	}

	public void setComboItems(String[] items){
		this.items.clear();
		this.itemMap.clear();
		
		this.items.addAll(Arrays.asList(items));
	}
	
	public void setComboItems(Map<String, ?> items){
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
		
		ValuechageNotifier notifier = new ValuechageNotifier(this);
		combo.addSelectionListener(notifier);
		
		if(!readOnly){
			combo.addModifyListener(notifier);
		}
	}
	
	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
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
