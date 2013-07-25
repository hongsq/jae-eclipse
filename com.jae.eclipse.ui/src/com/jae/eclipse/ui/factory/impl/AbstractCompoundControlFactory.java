/**
 * 
 */
package com.jae.eclipse.ui.factory.impl;

import org.eclipse.swt.layout.GridLayout;

import com.jae.eclipse.ui.ILayoutContainer;
import com.jae.eclipse.ui.factory.AbstractControlFactory;
import com.jae.eclipse.ui.factory.IControlFactory;
import com.jae.eclipse.ui.util.LayoutUtil;

/**
 * 
 * @author hongshuiqiao
 *
 */
public abstract class AbstractCompoundControlFactory extends AbstractControlFactory implements ILayoutContainer {
	private GridLayout layout = LayoutUtil.createCompactGridLayout(1);
	
	public abstract IControlFactory[] getControlFactories();
	
	public abstract void clearControlFactories();
	
	public void save() {
		IControlFactory[] factories = this.getControlFactories();
		for (IControlFactory factory : factories) {
			factory.save();
		}
	}

	public void load() {
		IControlFactory[] factories = this.getControlFactories();
		for (IControlFactory factory : factories) {
			factory.load();
		}
	}
	
	@Override
	public boolean doValidate() {
		boolean falg = true;
		IControlFactory[] factories = this.getControlFactories();
		for (IControlFactory factory : factories) {
			falg = factory.validate()&&falg;
		}
		return falg;
	}

	public GridLayout getLayout() {
		return layout;
	}

	public void setLayout(GridLayout layout) {
		this.layout = layout;
	}

	@Override
	protected void beforeCreateControl() {
		super.beforeCreateControl();
		
		IControlFactory[] factories = this.getControlFactories();
		for (IControlFactory factory : factories) {
			if(null != this.getValue()) factory.setValue(this.getValue());
			factory.addValuechangeListener(this);
			factory.setMessageCaller(this.getMessageCaller());
			if (factory instanceof AbstractControlFactory) {
				AbstractControlFactory controlFactory = (AbstractControlFactory) factory;
				controlFactory.setValidateFlag(false);
				if((null != this.getObjectOperator(false)) && (null == controlFactory.getObjectOperator(false)))
					controlFactory.setObjectOperator(this.getObjectOperator(false));
			}
		}
	}
}
