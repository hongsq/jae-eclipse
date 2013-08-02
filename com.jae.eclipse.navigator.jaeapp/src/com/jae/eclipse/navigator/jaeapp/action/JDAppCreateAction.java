/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.Staging;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;
import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.ui.HelpContextConstants;
import com.jae.eclipse.ui.IMessageCaller;
import com.jae.eclipse.ui.IValidator;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.ComboPropertyEditor;
import com.jae.eclipse.ui.control.NumberPropertyEditor;
import com.jae.eclipse.ui.control.NumberType;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.extension.ImageRepositoryManager;
import com.jae.eclipse.ui.factory.IControlFactory;
import com.jae.eclipse.ui.factory.impl.CompoundControlFactory;
import com.jae.eclipse.ui.factory.impl.GroupControlFactory;
import com.jae.eclipse.ui.factory.impl.ObjectEditorControlFactory;
import com.jae.eclipse.ui.impl.ControlFactoryDialog;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppCreateAction extends AbstractJDAction {

	public JDAppCreateAction(ISelectionProvider provider, String text) {
		super(provider, text);
		
		this.setId("jdapp.action.add");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("jdapp.action.add"));
		this.setMustSelect(true);
		this.setMultiable(false);
		this.setSelectType(User.class);
		this.setEnabled(false);
	}

	@Override
	public void run() {
		User user = (User) this.getStructuredSelection().getFirstElement();
		CloudFoundryClientExt client = user.getCloudFoundryClient();
		
		CloudInfo cloudInfo = client.getCloudInfo();
		List<String> appNames = new ArrayList<String>();
		List<CloudApplication> applications = client.getApplications();
		for (CloudApplication application : applications) {
			appNames.add(application.getName());
		}
		
		CompoundControlFactory factory = new CompoundControlFactory();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("memory", 128);
		map.put("staging", JAEAppHelper.getStaging("Java 1.6.0"));
		IControlFactory infoFactory = new GroupControlFactory("基本信息", new ObjectEditorControlFactory(createObjectEditor(cloudInfo, appNames)));
		infoFactory.setValue(map);
		factory.addControlFactory(infoFactory);
		
		List<Map<String, String>> uriList = new ArrayList<Map<String,String>>();
		AppURITableFactory uriFactory = new AppURITableFactory(null);
		uriFactory.setTitle("域名配置");
		uriFactory.setNotEmpty(true);
		uriFactory.setValue(uriList);
		factory.addControlFactory(uriFactory);
		
		factory.getUIDescription().setWinTitle("新建应用");
		factory.getUIDescription().setTitle("新建应用");
		factory.getUIDescription().setDescription("请选择合适的应用框架。");
		factory.getUIDescription().setMinHeight(250);
		factory.getUIDescription().setMinWidth(200);
		factory.setHelpContextID(HelpContextConstants.CONTEXT_JAE_APP);
		
		StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		ControlFactoryDialog dialog = new ControlFactoryDialog(viewer.getControl().getShell(), factory);
		if(Window.OK == dialog.open()){
			String appName = (String) map.get("appName");
			Staging staging = (Staging) map.get("staging");
			int memory = (Integer) map.get("memory");
			
			List<String> uris = new ArrayList<String>();
			for (Map<String, String> uriMap : uriList) {
				String uri = uriMap.get("uri");
				if(null != uri)
					uris.add(uri);
			}
			
			client.createApplication(appName, staging, memory, uris, null, null);
			user.refresh();
			viewer.refresh();
		}
	}
	
	private ObjectEditor createObjectEditor(CloudInfo cloudInfo, final List<String> appNames){
		ObjectEditor objectEditor = new ObjectEditor();
		{
			StringPropertyEditor editor = new StringPropertyEditor();
			editor.setPropertyName("appName");
			editor.setLabel("应用名");
			editor.setRequired(true);
			editor.addValidator(new IValidator() {
				
				public boolean validate(IMessageCaller messageCaller, Object source, Object value) {
					if(null != value && appNames.contains(value)){
						messageCaller.error("该应用名已经存在，请修改应用名。");
						return false;
					}
					return true;
				}
			});
			
			objectEditor.addPropertyEditor(editor);
		}
		{
			ComboPropertyEditor editor = new ComboPropertyEditor();
			editor.setPropertyName("staging");
			editor.setLabel("框架");
			editor.setReadOnly(true);
			editor.setRequired(true);
			editor.setComboItems(JAEAppHelper.getSupportStagings());
			
			objectEditor.addPropertyEditor(editor);
		}
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("内存(M)");
			editor.setPropertyName("memory");
			editor.setIncrement(10);
			editor.setMinimum(128);
			int maxMemory = (cloudInfo.getLimits().getMaxTotalMemory()-cloudInfo.getUsage().getTotalMemory());
			editor.setMaximum(maxMemory);
			objectEditor.addPropertyEditor(editor);
		}
		return objectEditor;
	}
}
