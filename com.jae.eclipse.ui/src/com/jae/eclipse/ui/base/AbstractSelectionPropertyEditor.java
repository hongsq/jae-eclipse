/**
 * 
 */
package com.jae.eclipse.ui.base;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jae.eclipse.ui.util.LayoutUtil;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractSelectionPropertyEditor extends AbstractPropertyEditor implements SelectionListener {
	private Text text;
	private Button button;
	private String buttonText="打开";
	private boolean readOnly;
	
	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Text getText() {
		return text;
	}

	public Button getButton() {
		return button;
	}

	public Control createControl(Composite parent, int style) {
		Composite composite = new Composite(parent, style);
		composite.setLayout(LayoutUtil.createCompactGridLayout(2));
		
		int textStyle = SWT.BORDER;
		if(readOnly) textStyle = textStyle | SWT.READ_ONLY;
		text = new Text(composite, textStyle);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		button = new Button(composite, SWT.PUSH);
		button.setText(buttonText);
		button.setLayoutData(new GridData());
		return composite;
	}

	@Override
	protected Object doGetValue() {
		return text.getText();
	}

	@Override
	protected void doSetValue(Object value) {
		if(null == value)
			text.setText("");
		else
			text.setText(value.toString());
	}

	@Override
	public void afterBuild(Control parent) {
		super.afterBuild(parent);
		ValuechageNotifier notifier = new ValuechageNotifier(this);
		this.text.addModifyListener(notifier);
		
		this.button.addSelectionListener(this);
	}

	public void widgetSelected(SelectionEvent event) {
		Shell shell = event.widget.getDisplay().getActiveShell();
		String selectionValue = doSelection(shell, this.text.getText());
		
		this.setValue(selectionValue);
	}

	protected abstract String doSelection(Shell shell, String oldValue);

	public void widgetDefaultSelected(SelectionEvent event) {
		widgetSelected(event);
	}
	
	@Override
	public void setEnable(boolean enabled) {
		this.text.setEnabled(enabled);
		this.button.setEnabled(enabled);
	}
	
	@Override
	public boolean isEnable() {
		return this.text.isEnabled() || this.button.isEnabled();
	}
}
