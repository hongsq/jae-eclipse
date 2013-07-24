/**
 * 
 */
package com.jae.eclipse.ui.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jae.eclipse.ui.factory.IControlFactory;

/**
 * 
 * @author hongshuiqiao
 *
 */
public class CompoundControlFactory extends AbstractCompoundControlFactory {
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
	protected Control doCreateControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(this.getLayout());
		for (IControlFactory factory : factories) {
			factory.createControl(composite);
		}
		return composite;
	}

}
