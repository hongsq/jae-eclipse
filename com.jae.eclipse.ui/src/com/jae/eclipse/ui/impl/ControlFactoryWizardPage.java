/**
 * 
 */
package com.jae.eclipse.ui.impl;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jae.eclipse.ui.ILoadable;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IStore;
import com.jae.eclipse.ui.IValidatable;
import com.jae.eclipse.ui.UIDescription;
import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.factory.IControlFactory;

/**
 * @author hongshuiqiao
 *
 */
public class ControlFactoryWizardPage extends WizardPage implements IValuechangeListener, IStore, ILoadable, IValidatable, IMessageCaller {
	private IControlFactory factory;
	private boolean hasError;
	
	public ControlFactoryWizardPage(String pageName, IControlFactory factory) {
		super(pageName);
		this.factory = factory;
	}

	public ControlFactoryWizardPage(String pageName, String title, ImageDescriptor titleImage, IControlFactory factory) {
		super(pageName, title, titleImage);
		this.factory = factory;
	}

	public void createControl(Composite parent) {
		UIDescription uiDescription = this.factory.getUIDescription();
		if(null != uiDescription){
			if(null != uiDescription.getTitle()) this.setTitle(uiDescription.getTitle());
			if(null != uiDescription.getDescription()) this.setMessage(uiDescription.getDescription());
			if(null != uiDescription.getTitleImage()) this.setImageDescriptor(uiDescription.getTitleImage());
			IWizard wizard = this.getWizard();
			if(null != uiDescription.getWinTitle()) {
				if (wizard instanceof Wizard) {
					((Wizard) wizard).setWindowTitle(uiDescription.getWinTitle());
				}
			}
			ImageDescriptor winTitleImage = uiDescription.getWinTitleImage();
			if(null != winTitleImage) wizard.getContainer().getShell().setImage(winTitleImage.createImage(true));
		}
		
		Composite composite = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 5;
		
		composite.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		if(null != uiDescription){
			if(uiDescription.getWidth()>0) layoutData.widthHint = uiDescription.getWidth();
			if(uiDescription.getHeight()>0) layoutData.heightHint = uiDescription.getHeight();
			if(uiDescription.getMinWidth()>0) layoutData.minimumWidth = uiDescription.getMinWidth();
			if(uiDescription.getMinHeight()>0) layoutData.minimumHeight = uiDescription.getMinHeight();
		}
		composite.setLayoutData(layoutData);
		composite.setFont(parent.getFont());
		// Build the separator line
		Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		this.factory.addValuechangeListener(this);
		this.factory.setMessageCaller(this);
		this.factory.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.factory.createControl(composite);
		
		this.setControl(composite);
		//创建完后加载数据
		this.load();
		this.validate();
	}

	public void error(String message) {
		this.hasError = true;
		this.setErrorMessage(message);
	}

	public void info(String message) {
		if(this.hasError)
			return;
		this.setErrorMessage(null);
		this.setMessage(message, IMessageProvider.INFORMATION);
	}

	public void warn(String message) {
		if(this.hasError)
			return;
		this.setErrorMessage(null);
		this.setMessage(message, IMessageProvider.WARNING);
	}

	public void clear() {
		this.hasError = false;
		this.setErrorMessage(null);
		UIDescription uiDescription = this.factory.getUIDescription();
		if(null != uiDescription){
			this.setMessage(uiDescription.getDescription(), IMessageProvider.NONE);
		}else{
			this.setMessage(null, IMessageProvider.NONE);
		}
	}

	public boolean hasError() {
		return this.hasError;
	}

	public void valuechanged(ValueChangeEvent event) {
		boolean validated = this.validate();
		setPageComplete(validated);
	}

	public void load() {
		if(null != this.factory)
			this.factory.load();
	}

	public void save() {
		if(null != this.factory)
			this.factory.save();
	}

	public boolean validate() {
		if(null != this.factory)
			return this.factory.validate();
		return true;
	}

}
