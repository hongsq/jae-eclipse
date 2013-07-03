/**
 * 
 */
package com.jae.eclipse.ui.base;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.DecoratedField;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.IControlCreator;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;

import com.jae.eclipse.ui.Constants;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IPropertyEditor;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.event.ILinkAction;
import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.LinkEvent;
import com.jae.eclipse.ui.event.ValidateEvent;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.event.ValueEventContainer;
import com.jae.eclipse.ui.impl.CompoundMessageCaller;
import com.jae.eclipse.ui.impl.PropertyMessageCaller;
import com.jae.eclipse.ui.util.LayoutUtil;
import com.jae.eclipse.ui.util.ObjectUtil;
import com.jae.eclipse.ui.util.UIUtil;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings("deprecation")
public abstract class AbstractPropertyEditor extends ValueEventContainer implements IPropertyEditor, IControlCreator {
	private boolean enable=true;
	private boolean useLabel=true;
	private boolean required=false;
	private String label;
	private String propertyName;
	private ILinkAction linkAction;
	private Control editControl;
	private Control labelControl;
	private DecoratedField decoratedField;
	// 用来在控件上显示'*'，表示必填
//	private FieldDecoration requiredFieldDecoration;
	// 用来在控件上显示警告信息
	private FieldDecoration warnFieldDecoration;
	// 用来在控件上显示错误信息
	private FieldDecoration errorFieldDecoration;
	// 用来在控件上显示代码提示信息
	private FieldDecoration assitantFieldDecoration;
	private GridData editLayoutData=new GridData(GridData.FILL_BOTH);
	private CompoundMessageCaller messageCaller = new CompoundMessageCaller();
	private PropertyMessageCaller propertyMessageCaller;
	private Object editElement;
	private List<IValidator> validators = new ArrayList<IValidator>();
	private Object oldValue;
	private boolean fireFlag = true;
	//自身的变化是否触发自己的验证（一般如果加入到一个容器中，自身的变化会触发整个容器的验证，不需要再重复验证自己）
	private boolean validateFlag = true;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Object getEditElement() {
		return editElement;
	}

	public void setEditElement(Object editElement) {
		this.editElement = editElement;
	}

	public boolean isUseLabel() {
		return useLabel;
	}

	public void setUseLabel(boolean useLabel) {
		this.useLabel = useLabel;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public ILinkAction getLinkAction() {
		return linkAction;
	}

	public void setLinkAction(ILinkAction linkAction) {
		this.linkAction = linkAction;
	}
	
	@Override
	public boolean isLinkLabel() {
		return null != this.linkAction;
	}
	
	protected void fireValueChangeEvent(){
		if(!fireFlag)
			return;
		
		Object newValue = getValue();
		ValueChangeEvent valueChangeEvent = new ValueChangeEvent(this, this.oldValue, newValue);
		fireValuechanged(valueChangeEvent);
		this.oldValue = newValue;
	}
	
	public void addValidator(IValidator validator){
		this.validators.add(validator);
	}
	
	public void removeValidator(IValidator validator){
		this.validators.remove(validator);
	}
	
	public void clearValidators(){
		this.validators.clear();
	}
	
	@Override
	public <T> T getAdapter(Class<T> adapterClass) {
		if(adapterClass == IPropertyEditor.class)
			return adapterClass.cast(this);
		if(adapterClass == ILinkAction.class)
			return adapterClass.cast(this.linkAction);
		if(adapterClass == IMessageCaller.class)
			return adapterClass.cast(this.messageCaller);
		
		return null;
	}
	
	@Override
	public void save() {
		if(!UIUtil.isControlValid(this.editControl))
			return;
		
		Object value = this.getValue();
		Object oldValue = ObjectUtil.getValue(this.editElement, this.propertyName);
		if(null != value && value.equals(oldValue))
			return;
		
		if(null == value && null == oldValue)
			return;
		
		ObjectUtil.setValue(this.editElement, this.propertyName, value);
	}
	
	@Override
	public void load() {
		Object value = ObjectUtil.getValue(this.editElement, this.propertyName);
		this.setValue(value);
	}
	
	@Override
	public void setValue(Object value) {
		Object oldValue = this.getValue();
		if(null != value && value.equals(oldValue))
			return;
		
		if(null == value && null == oldValue)
			return;
		
		//UI是单线程 的，不需要sync
		fireFlag = false;
		doSetValue(value);
		fireFlag = true;
		fireValueChangeEvent();
	}
	
	@Override
	public Object getValue() {
		Object value = this.doGetValue();
		
		return value;
	}
	
	protected abstract Object doGetValue();
	
	protected abstract void doSetValue(Object value);
	
	@Override
	public void beforeBuild(Control parent) {
	}
	
	@Override
	public void afterBuild(Control parent) {
		if(this.isLinkLabel()){
			((Hyperlink)this.labelControl).addHyperlinkListener(new HyperlinkAdapter(){
				@Override
				public void linkActivated(HyperlinkEvent e) {
					AbstractPropertyEditor editor = AbstractPropertyEditor.this;
					editor.linkAction.linkActivated(new LinkEvent(editor));
				}
			});
		}
		
		this.addValuechangeListener(new IValuechangeListener() {
			
			@Override
			public void valuechanged(ValueChangeEvent event) {
				if(!validateFlag)
					return;
				
				ValidateEvent validateEvent = new ValidateEvent(event.getSource());
				validate(validateEvent);
			}
		});
//		
//		this.editControl.addFocusListener(new FocusListener() {
//			@Override
//			public void focusLost(FocusEvent e) {
//				validate();
//			}
//			
//			@Override
//			public void focusGained(FocusEvent e) {
//				validate();
//			}
//		});
	}
	
	public boolean isValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(boolean validateFlag) {
		this.validateFlag = validateFlag;
	}

	@Override
	public boolean validate(ValidateEvent event) {
		if(null != this.propertyMessageCaller)
			this.propertyMessageCaller.clear();
		
		for (IValidator validator : this.validators) {
			if(!validator.validate(this.messageCaller, this))
				return false;
		}
		
		return true;
	}
	
	public Control getEditControl() {
		return editControl;
	}

	public Control getLabelControl() {
		return labelControl;
	}

	public DecoratedField getDecoratedField() {
		return decoratedField;
	}
	
	public FieldDecoration getAssitantFieldDecoration() {
		return assitantFieldDecoration;
	}
	
	public FieldDecoration getWarnFieldDecoration() {
		return warnFieldDecoration;
	}
	
	public FieldDecoration getErrorFieldDecoration() {
		return errorFieldDecoration;
	}
	
	@Override
	public void build(Composite parent) {
		this.labelControl = buildLabelControl(parent);
		if(null != this.labelControl){
			this.labelControl.setLayoutData(new GridData());
		}
		
		Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		topComposite.setLayout(LayoutUtil.createCompactGridLayout(1));
		
		this.decoratedField = new DecoratedField(topComposite, SWT.NONE, this);
		this.decoratedField.getLayoutControl().setLayoutData(this.editLayoutData);
		this.editControl = this.decoratedField.getControl();
		
		this.buildFieldDecoration();
		
		this.propertyMessageCaller = new PropertyMessageCaller(this);
		this.messageCaller.addMessageCaller(this.propertyMessageCaller);
	}

	protected void buildFieldDecoration() {
		FieldDecorationRegistry fieldDecorationRegistry = FieldDecorationRegistry.getDefault();

//		// 加这个内容是为了占位
//		this.requiredFieldDecoration = fieldDecorationRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED);
//		this.decoratedField.addFieldDecoration(this.requiredFieldDecoration, SWT.CENTER | SWT.LEFT, false);
//		if(this.required)
//			this.decoratedField.showDecoration(this.requiredFieldDecoration);
//		else
//			this.decoratedField.hideDecoration(this.requiredFieldDecoration);

		Image warnImage = fieldDecorationRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
		this.warnFieldDecoration = new FieldDecoration(warnImage, "");

		Image errorImage = fieldDecorationRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		this.errorFieldDecoration = new FieldDecoration(errorImage, "");

		Image assitantImage = fieldDecorationRegistry.getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage();
		String description = JFaceResources.getString("FieldDecorationRegistry.contentAssistMessage");
		description = description + "(Alt+/)";
		this.assitantFieldDecoration = new FieldDecoration(assitantImage, description);
	}
	
	public GridData getEditLayoutData() {
		return editLayoutData;
	}

	public void setEditLayoutData(GridData editLayoutData) {
		this.editLayoutData = editLayoutData;
	}
	
	public IMessageCaller getMessageCaller() {
		return messageCaller;
	}
	
	public void setMessageCaller(IMessageCaller messageCaller) {
		this.messageCaller.clearMessageCallers();
		if(null != messageCaller)
			this.messageCaller.addMessageCaller(messageCaller);
	}

	/**
	 * 标签控件可能是普通的Label也可能是超链接
	 * @param parent
	 * @return
	 */
	protected Control buildLabelControl(Composite parent) {
		if(!this.isUseLabel())
			return null;
		
		String labelText = this.label;
		if(this.isRequired()){
			labelText = this.label + Constants.REQUIRED_MARK;
		}
		
		if(this.isLinkLabel()){
			Hyperlink link = new Hyperlink(parent, SWT.NONE);
			link.setText(labelText);
			
			return link;
		}else{
			Label labelControl = new Label(parent, SWT.NONE);
			labelControl.setText(labelText);
//			labelControl.setAlignment(SWT.RIGHT);
			
			return labelControl;
		}
	}
}
