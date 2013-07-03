/**
 * 
 */
package com.jae.eclipse.ui.factory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.event.ValidateEvent;
import com.jae.eclipse.ui.factory.base.AbstractControlFactory;
import com.jae.eclipse.ui.util.LayoutUtil;

/**
 * 
 * @author hongshuiqiao
 *
 */
public class CompoundControlFactory extends AbstractControlFactory {
	private List<IControlFactory> factories = new ArrayList<IControlFactory>();
	
	public void addControlFactory(IControlFactory factory){
		this.factories.add(factory);
	}
	
	public void removeControlFactory(IControlFactory factory){
		this.factories.remove(factory);
	}
	
	public void clearControlFactories(){
		this.factories.clear();
	}
	
	public IControlFactory[] getControlFactories(){
		return this.factories.toArray(new IControlFactory[this.factories.size()]);
	}
	
	@Override
	public void save() {
		for (IControlFactory factory : factories) {
			factory.save();
		}
	}

	@Override
	public void load() {
		for (IControlFactory factory : factories) {
			factory.load();
		}
	}
	
	@Override
	public boolean doValidate(ValidateEvent event) {
		boolean falg = true;
		for (IControlFactory factory : factories) {
			falg = factory.validate(event)&&falg;
		}
		return falg;
	}

	@Override
	protected Control doCreateControl(Composite parent) {
		for (IControlFactory factory : factories) {
			factory.setValue(this.getValue());
			factory.addValuechangeListener(this);
			if (factory instanceof AbstractControlFactory) {
				((AbstractControlFactory) factory).setValidateFlag(false);
			}
		}
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(LayoutUtil.createCompactGridLayout(1));
		for (IControlFactory factory : factories) {
			Control control = factory.createControl(composite);
			control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		return composite;
	}

}
