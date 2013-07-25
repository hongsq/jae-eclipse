/**
 * 
 */
package com.jae.eclipse.ui.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

import com.jae.eclipse.ui.base.AbstractPropertyEditor;
import com.jae.eclipse.ui.base.ValuechageNotifier;
import com.jae.eclipse.ui.util.UIUtil;

/**
 * @author hongshuiqiao
 *
 */
public class NumberPropertyEditor extends AbstractPropertyEditor {
	private int maximum = -1;
	private int minimum = -1;
	private int pageIncrement = -1;
	private int increment = -1;
	private int digits = 0;
	private boolean readonly;
	private NumberType numberType = NumberType.INT;
	
	public NumberPropertyEditor(NumberType numberType) {
		super();
		this.numberType = numberType;
		switch (this.numberType) {
		case BYTE:
			this.setMaximum(Byte.MAX_VALUE);
			this.setMinimum(Byte.MIN_VALUE);
			this.setDigits(0);
			break;
		case SHORT:
			this.setMaximum(Short.MAX_VALUE);
			this.setMinimum(Short.MIN_VALUE);
			this.setDigits(0);
			break;
		case INT:
		case LONG:
			this.setMaximum(Integer.MAX_VALUE);
			this.setMinimum(Integer.MIN_VALUE);
			this.setDigits(0);
			break;
		case FLOAT:
		case DOUBLE:
			this.setMaximum(Integer.MAX_VALUE);
			this.setMinimum(Integer.MIN_VALUE);
			this.setDigits(2);
			break;
		}
	}

	public Control createControl(Composite parent, int style) {
		int realStyle = style|SWT.BORDER;
		if(this.readonly) realStyle = realStyle | SWT.READ_ONLY;
		Spinner spinner = new Spinner(parent, realStyle);
		spinner.setDigits(this.digits);//小数位数
		
		if(this.maximum>=0) spinner.setMaximum(this.maximum);
		if(this.minimum>=0) spinner.setMinimum(this.minimum);
		if(this.pageIncrement>=0) spinner.setPageIncrement(this.pageIncrement);//page up|down变化值 
		if(this.increment>=0) spinner.setIncrement(this.increment);
		return spinner;
	}
	
	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
		if(UIUtil.isControlValid(getSpinner()))
			getSpinner().setMaximum(maximum);
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
		if(UIUtil.isControlValid(getSpinner()))
			getSpinner().setMinimum(minimum);
	}

	public int getPageIncrement() {
		return pageIncrement;
	}

	public void setPageIncrement(int pageIncrement) {
		this.pageIncrement = pageIncrement;
		if(UIUtil.isControlValid(getSpinner()))
			getSpinner().setPageIncrement(pageIncrement);
	}

	/**
	 * 此值不包括小数点
	 * 如：1.52要点一下增或减0.1，则应该设置increment为10
	 * @return
	 */
	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int increment) {
		this.increment = increment;
		if(UIUtil.isControlValid(getSpinner()))
			getSpinner().setIncrement(increment);
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
		if(UIUtil.isControlValid(getSpinner()))
			getSpinner().setDigits(digits);
	}

	public Spinner getSpinner(){
		return (Spinner) this.getEditControl();
	}

	@Override
	protected Object doGetValue() {
		int value = this.getSpinner().getSelection();
		Double result = value/Math.pow(10, this.digits);
		switch (this.numberType) {
		case BYTE:
			return result.byteValue();
		case SHORT:
			return result.shortValue();
		case INT:
			return result.intValue();
		case LONG:
			return result.longValue();
		case FLOAT:
			return result.floatValue();
		case DOUBLE:
			return result.doubleValue();
		}
		return result;
	}

	@Override
	protected void doSetValue(Object value) {
		int selection = 0;
		switch (this.numberType) {
		case BYTE:
			selection = ((Double) (((Byte)value)/Math.pow(10, this.digits))).intValue();
			break;
		case SHORT:
			selection = ((Double) (((Short)value)/Math.pow(10, this.digits))).intValue();
			break;
		case INT:
			selection = ((Double) (((Integer)value)/Math.pow(10, this.digits))).intValue();
			break;
		case LONG:
			selection = ((Double) (((Long)value)/Math.pow(10, this.digits))).intValue();
			break;
		case FLOAT:
			selection = ((Double) (((Float)value)/Math.pow(10, this.digits))).intValue();
			break;
		case DOUBLE:
			selection = ((Double) (((Double)value)/Math.pow(10, this.digits))).intValue();
			break;
		}
		this.getSpinner().setSelection(selection);
	}
	
	@Override
	public void afterBuild(Control parent) {
		super.afterBuild(parent);
		
		ValuechageNotifier notifier = new ValuechageNotifier(this);
		
		Spinner spinner = this.getSpinner();
		spinner.addModifyListener(notifier);
	}
}
