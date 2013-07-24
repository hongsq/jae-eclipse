/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.navigator.CommonViewer;

import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.navigator.jaeapp.view.JAEView;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.NumberPropertyEditor;
import com.jae.eclipse.ui.control.NumberType;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;
import com.jae.eclipse.ui.factory.IControlFactory;
import com.jae.eclipse.ui.factory.impl.CompoundControlFactory;
import com.jae.eclipse.ui.factory.impl.GroupControlFactory;
import com.jae.eclipse.ui.factory.impl.ObjectEditorControlFactory;
import com.jae.eclipse.ui.impl.ControlFactoryDialog;
import com.jae.eclipse.ui.validator.NotEmptyValidator;

/**
 * @author hongshuiqiao
 *
 */
public class UserAction extends AbstractJDAction {
	private boolean edit;

	public UserAction(ISelectionProvider provider, String text, boolean edit) {
		super(provider, text);
		this.edit = edit;
		
		String id = "user.action.add";
		if(edit){
			id = "user.action.edit";
			this.setEnabled(false);
		}

		this.setMustSelect(this.edit);
		this.setSelectType(User.class);
		this.setMultiable(false);
		this.setEnabled(!this.edit);
		
		this.setId(id);
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor(id));
	}
	
	private ObjectEditor createKeyObjectEditor(){
		final ObjectEditor objectEditor = new ObjectEditor();
//		objectEditor.setLayout(new GridLayout(4, false));
		
		IValidator repeatValidator = new IValidator() {
			
			public boolean validate(IMessageCaller messageCaller, Object validateObject) {
				String accessKey = (String) objectEditor.getPropertyEditor("accessKey").getValue();
				String secretKey = (String) objectEditor.getPropertyEditor("secretKey").getValue();
				
				if(!edit && (null != JAEAppHelper.getUser(accessKey, secretKey))){
					messageCaller.error("已经存在相同的用户!");
					return false;
				}
				
				return true;
			}
		};
		
		{
			StringPropertyEditor editor = new StringPropertyEditor();
			editor.setPropertyName("accessKey");
			editor.setLabel("accessKey");
			editor.addValidator(new NotEmptyValidator("accessKey"));
			editor.addValidator(repeatValidator);
			objectEditor.addPropertyEditor(editor);
		}
		{
			StringPropertyEditor editor = new StringPropertyEditor();
			editor.setPropertyName("secretKey");
			editor.setLabel("secretKey");
			editor.addValidator(new NotEmptyValidator("secretKey"));
			editor.addValidator(repeatValidator);
			objectEditor.addPropertyEditor(editor);
		}
		
		return objectEditor;
	}
	
	private ObjectEditor createLimitEditor(){
		ObjectEditor objectEditor = new ObjectEditor();
		
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("最大应用数");
			editor.setPropertyName("maxApps");
			editor.setEnable(false);
			objectEditor.addPropertyEditor(editor);
		}
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("最大服务数");
			editor.setPropertyName("maxServices");
			editor.setEnable(false);
			objectEditor.addPropertyEditor(editor);
		}
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("最大内存");
			editor.setPropertyName("maxTotalMemory");
			editor.setEnable(false);
			objectEditor.addPropertyEditor(editor);
		}
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("每个应用URI数");
			editor.setPropertyName("maxUriPerApp");
			editor.setEnable(false);
			objectEditor.addPropertyEditor(editor);
		}
		
		return objectEditor;
	}
	
	private ObjectEditor createUsageEditor(){
		ObjectEditor objectEditor = new ObjectEditor();
		
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("启动应用数");
			editor.setPropertyName("apps");
			editor.setEnable(false);
			objectEditor.addPropertyEditor(editor);
		}
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("服务数");
			editor.setPropertyName("services");
			editor.setEnable(false);
			objectEditor.addPropertyEditor(editor);
		}
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("已用内存");
			editor.setPropertyName("totalMemory");
			editor.setEnable(false);
			objectEditor.addPropertyEditor(editor);
		}
		
		return objectEditor;
	}
	
	@Override
	public void run() {
		CompoundControlFactory factory = new CompoundControlFactory();
//		factory.setLayout(LayoutUtil.createCompactGridLayout(2));
		IControlFactory keyFactory = new GroupControlFactory("KEY", new ObjectEditorControlFactory(this.createKeyObjectEditor()));
		factory.addControlFactory(keyFactory);
//		keyFactory.getLayoutData().horizontalSpan = 2;
		
		String prefix = null;
		User user = null;
		if(this.edit){
			prefix = "编辑";
			user = (User) this.getStructuredSelection().getFirstElement();
			if(user.isConnected()){
				CloudFoundryOperations operator = user.getCloudFoundryClient();
				CloudInfo info = operator.getCloudInfo();
				
				Map<String, Integer> map = new HashMap<String, Integer>();
				map.put("maxApps", info.getLimits().getMaxApps());
				map.put("maxServices", info.getLimits().getMaxServices());
				map.put("maxTotalMemory", info.getLimits().getMaxTotalMemory());
				map.put("maxUriPerApp", info.getLimits().getMaxUrisPerApp());
				map.put("apps", info.getUsage().getApps());
				map.put("services", info.getUsage().getServices());
				map.put("totalMemory", info.getUsage().getTotalMemory());
				
				IControlFactory limitFactory = new GroupControlFactory("资源权限", new ObjectEditorControlFactory(this.createLimitEditor()));
				factory.addControlFactory(limitFactory);
				limitFactory.setValue(map);
				
				IControlFactory usageFactory = new GroupControlFactory("使用情况", new ObjectEditorControlFactory(this.createUsageEditor()));
				factory.addControlFactory(usageFactory);
				usageFactory.setValue(map);
			}
		}else{
			prefix = "新增";
			user = new User("");
		}
		
		keyFactory.setValue(user);
		
		factory.getUIDescription().setWinTitle(prefix+"用户");
		factory.getUIDescription().setTitle(prefix+"用户");
		factory.getUIDescription().setDescription(prefix+"一个用户的相关信息。");
		
		CommonViewer viewer = (CommonViewer) this.getSelectionProvider();
		
		Shell shell = viewer.getTree().getShell();
		ControlFactoryDialog dialog = new ControlFactoryDialog(shell, factory);
		if(Window.OK == dialog.open()){
			try {
				user.connect();
				if(edit)
					JAEAppHelper.save();
				else{
					JAEAppHelper.regeditUser(user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			viewer.refresh();
			JAEView.getInstance().updateActionBars();
		}
	}
}
