/**
 * 
 */
package com.jae.eclipse.ui.impl;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

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
public class ControlFactoryDialog extends TitleAreaDialog implements IValuechangeListener, IMessageCaller, ILoadable, IStore, IValidatable {
	private IControlFactory controlFactory;
	private boolean hasError;
	private boolean resizable;

	public ControlFactoryDialog(Shell parentShell, IControlFactory controlFactory) {
		super(parentShell);
		this.controlFactory = controlFactory;
	}
	
	@Override
	protected int getShellStyle() {
		int style = super.getShellStyle();
		if(resizable)
			return style|SWT.RESIZE;
		return style;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		
		UIDescription uiDescription = this.controlFactory.getUIDescription();
		if(null != uiDescription){
			if(null != uiDescription.getWinTitle()) newShell.setText(uiDescription.getWinTitle());
			if(null != uiDescription.getWinTitleImage()) newShell.setImage(uiDescription.getWinTitleImage().createImage(true));
			if(uiDescription.getInitX()>=0 && uiDescription.getInitY()>=0)
				newShell.setLocation(uiDescription.getInitX(), uiDescription.getInitY());
		}
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		this.load();
		this.validate();
		return contents;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		this.setHelpAvailable(true);
		
		UIDescription uiDescription = this.controlFactory.getUIDescription();
		if(null != uiDescription){
			if(null != uiDescription.getTitle()) this.setTitle(uiDescription.getTitle());
			if(null != uiDescription.getDescription()) this.setMessage(uiDescription.getDescription());
			if(null != uiDescription.getTitleImage()) this.setTitleImage(uiDescription.getTitleImage().createImage(true));
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
		
		this.controlFactory.addValuechangeListener(this);
		this.controlFactory.setMessageCaller(this);
		this.controlFactory.setLayoutData(layoutData);
		this.controlFactory.createControl(composite);
		
		return composite;
	}

	@Override
	protected void okPressed() {
		this.save();
		super.okPressed();
	}
	
	public void valuechanged(ValueChangeEvent event) {
		boolean validated = this.validate();
		Button okButton = getButton(IDialogConstants.OK_ID);
		if(null != okButton)
			okButton.setEnabled(validated);
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
		UIDescription uiDescription = this.controlFactory.getUIDescription();
		if(null != uiDescription){
			this.setMessage(uiDescription.getDescription(), IMessageProvider.NONE);
		}else{
			this.setMessage(null, IMessageProvider.NONE);
		}
	}

	public boolean hasError() {
		return this.hasError;
	}

	public boolean validate() {
		if(null != this.controlFactory)
			return this.controlFactory.validate();
		return true;
	}

	public void save() {
		if(null != this.controlFactory)
			this.controlFactory.save();
	}

	public void load() {
		if(null != this.controlFactory)
			this.controlFactory.load();
	}
}
