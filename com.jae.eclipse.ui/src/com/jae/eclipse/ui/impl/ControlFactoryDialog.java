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

import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.UIDescription;
import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.ValidateEvent;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.factory.IControlFactory;
import com.jae.eclipse.ui.util.LayoutUtil;

/**
 * @author hongshuiqiao
 *
 */
public class ControlFactoryDialog extends TitleAreaDialog implements IValuechangeListener, IMessageCaller {
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
			if(null != uiDescription.getWinTitleImage()) newShell.setImage(uiDescription.getWinTitleImage());
			if(uiDescription.getInitWidth()>0 && uiDescription.getInitHeight()>0)
				newShell.setSize(uiDescription.getInitWidth(), uiDescription.getInitHeight());
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
		if(null != this.controlFactory){
			this.controlFactory.load();
			this.controlFactory.validate(new ValidateEvent(this.controlFactory));
		}
		return contents;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		UIDescription uiDescription = this.controlFactory.getUIDescription();
		if(null != uiDescription){
			if(null != uiDescription.getTitle()) this.setTitle(uiDescription.getTitle());
			if(null != uiDescription.getDescription()) this.setMessage(uiDescription.getDescription());
			if(null != uiDescription.getTitleImage()) this.setTitleImage(uiDescription.getTitleImage());
		}
		
		Composite composite = new Composite(parent, SWT.NONE);

//		GridLayout gridLayout = new GridLayout(columnCount, false);
		GridLayout layout = LayoutUtil.createCompactGridLayout(1);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 5;
		
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());
		// Build the separator line
		Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		this.controlFactory.addValuechangeListener(this);
		this.controlFactory.setMessageCaller(this);
		this.controlFactory.createControl(composite);
		this.controlFactory.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.controlFactory.getControl();
		
		return composite;
	}

	@Override
	protected void okPressed() {
		this.controlFactory.save();
		super.okPressed();
	}
	
	public void valuechanged(ValueChangeEvent event) {
		ValidateEvent validateEvent = new ValidateEvent(event.getSource());
		boolean validated = this.controlFactory.validate(validateEvent);
		Button okButton = getButton(IDialogConstants.OK_ID);
		if(null != okButton)
			okButton.setEnabled(validated);
	}

	public void error(String message) {
		this.hasError = true;
		this.setErrorMessage(message);
	}

	public void info(String message) {
		this.setErrorMessage(null);
		this.setMessage(message, IMessageProvider.INFORMATION);
	}

	public void warn(String message) {
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
}
