/**
 * 
 */
package com.jae.eclipse.ui.factory.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

import com.jae.eclipse.ui.factory.AbstractControlFactory;
import com.jae.eclipse.ui.factory.IControlFactory;
import com.jae.eclipse.ui.util.LayoutUtil;

/**
 * @author hongshuiqiao
 *
 */
public class GroupControlFactory extends AbstractControlFactory {
	private String groupName;
	private IControlFactory factory;

	public GroupControlFactory(String groupName, IControlFactory factory) {
		super();
		this.groupName = groupName;
		this.factory = factory;
	}

	public String getGroupName() {
		return groupName;
	}

	public IControlFactory getFactory() {
		return factory;
	}

	public void save() {
		this.factory.save();
	}

	public void load() {
		this.factory.load();
	}

	@Override
	protected boolean doValidate() {
		return this.factory.validate();
	}

	@Override
	protected void beforeCreateControl() {
		super.beforeCreateControl();
		
		if(null != this.getValue()) this.factory.setValue(this.getValue());
		this.factory.addValuechangeListener(this);
		this.factory.setMessageCaller(this.getMessageCaller());
		if (factory instanceof AbstractControlFactory) {
			((AbstractControlFactory) this.factory).setValidateFlag(false);
		}
	}
	
	@Override
	protected Control doCreateControl(Composite parent) {
		Group group = new Group(parent, SWT.SHADOW_NONE);
		group.setText(groupName);
		group.setLayoutData(this.getLayoutData());
		group.setLayout(LayoutUtil.createCompactGridLayout(1));
		
		factory.createControl(group);
		return group;
	}

}
