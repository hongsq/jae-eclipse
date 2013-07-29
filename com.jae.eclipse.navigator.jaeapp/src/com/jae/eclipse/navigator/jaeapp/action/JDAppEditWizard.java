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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.PlatformUI;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;
import com.jae.eclipse.cloudfoundry.exception.CloudFoundryClientRuntimeException;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;
import com.jae.eclipse.navigator.jaeapp.view.JAEView;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.NumberPropertyEditor;
import com.jae.eclipse.ui.control.NumberType;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.ValueChangeEvent;
import com.jae.eclipse.ui.factory.IControlFactory;
import com.jae.eclipse.ui.factory.impl.CompoundControlFactory;
import com.jae.eclipse.ui.factory.impl.GroupControlFactory;
import com.jae.eclipse.ui.factory.impl.ObjectEditorControlFactory;
import com.jae.eclipse.ui.impl.AbstractWizard;
import com.jae.eclipse.ui.impl.ControlFactoryWizardPage;
import com.jae.eclipse.ui.validator.NotEmptyValidator;

/**
 * @author hongshuiqiao
 *
 */
public class JDAppEditWizard extends AbstractWizard {
	private JDApp app;
	private User user;
	private CloudFoundryClientExt operator;
	private CloudInfo cloudInfo;
	private CloudApplication application;
	private Map<String, Integer> appInfo = new HashMap<String, Integer>();
	private List<Map<String, String>> uriList = new ArrayList<Map<String,String>>();
	private List<Map<String, String>> envList = new ArrayList<Map<String,String>>();
	
	public JDAppEditWizard(JDApp app) {
		super();
		this.app = app;
		this.user = JDModelUtil.getParentElement(app, User.class);
		this.operator = this.user.getCloudFoundryClient();
		this.cloudInfo = this.operator.getCloudInfo();
		this.application = this.operator.getApplication(app.getName());
		this.appInfo.put("memory", this.application.getMemory());
		this.appInfo.put("appCount", this.application.getInstances());
		for (String uri : this.application.getUris()) {
			Map<String, String> uriMap = new HashMap<String, String>();
			uriMap.put("uri", uri);
			this.uriList.add(uriMap);
		}
		
		Map<String, String> map = this.application.getEnvAsMap();
		for (String envKey : map.keySet()) {
			String value = map.get(envKey);
			Map<String, String> envMap = new HashMap<String, String>();
			envMap.put("name", envKey);
			envMap.put("value", value);
			this.envList.add(envMap);
		}
	}

	private ObjectEditor createCodeObjectEditor(){
		final ObjectEditor objectEditor = new ObjectEditor();
		{
			StringPropertyEditor editor = new StringPropertyEditor();
			editor.setPropertyName("repositoryURL");
			editor.setLabel("代码仓库:");
			editor.addValidator(new NotEmptyValidator("代码仓库"));
			objectEditor.addPropertyEditor(editor);
		}
		return objectEditor;
	}
	
	private ObjectEditor createAppInfoObjectEditor(final CloudInfo cloudInfo, final CloudApplication application){
		final ObjectEditor objectEditor = new ObjectEditor();
//		objectEditor.setLayout(new GridLayout(4, false));
		
		final int maxTotalMemory = cloudInfo.getLimits().getMaxTotalMemory();
		final int totalMemory = cloudInfo.getUsage().getTotalMemory();
		
		IValuechangeListener listener = new IValuechangeListener() {
			
			public void valuechanged(ValueChangeEvent event) {
				if (!(event.getSource() instanceof NumberPropertyEditor)) {
					return;
				}
				
				NumberPropertyEditor editor = (NumberPropertyEditor) event.getSource();
				if("memory".equals(editor.getPropertyName())){
					NumberPropertyEditor appCountEditor = (NumberPropertyEditor) objectEditor.getPropertyEditor("appCount");
					int memory = (Integer) editor.getValue();
					int count = (Integer) appCountEditor.getValue();
					
					int maxAppCount = (maxTotalMemory-totalMemory+application.getRunningInstances()*application.getMemory())/memory;
					if(maxAppCount<count)
						appCountEditor.setValue(maxAppCount);
				}else if("appCount".equals(editor.getPropertyName())){
					NumberPropertyEditor memoryEditor = (NumberPropertyEditor) objectEditor.getPropertyEditor("memory");
					int count = (Integer) editor.getValue();
					int memory = (Integer) memoryEditor.getValue();
					
					if(count <=0 ) count = 1;
					int maxMemory = (maxTotalMemory-totalMemory+application.getRunningInstances()*application.getMemory())/count;
					if(maxMemory<memory)
						memoryEditor.setValue(maxMemory);
				}
			}
		};
		
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("内存(M)");
			editor.setPropertyName("memory");
			editor.setIncrement(10);
			editor.setMinimum(128);
			int maxMemory = (maxTotalMemory-totalMemory+application.getRunningInstances()*application.getMemory());
			editor.setMaximum(maxMemory);
			editor.addValuechangeListener(listener);
			objectEditor.addPropertyEditor(editor);
		}
		{
			NumberPropertyEditor editor = new NumberPropertyEditor(NumberType.INT);
			editor.setLabel("实例数(个)");
			editor.setPropertyName("appCount");
			editor.setMinimum(1);
			int maxAppCount = (maxTotalMemory-totalMemory+application.getRunningInstances()*application.getMemory())/128;
			editor.setMaximum(maxAppCount);
			editor.addValuechangeListener(listener);
			objectEditor.addPropertyEditor(editor);
		}
		return objectEditor;
	}
	
	@Override
	public void addPages() {
		IControlFactory appInfoFactory = createAppInfoFactory();
		appInfoFactory.getUIDescription().setWinTitle("编辑应用");
		appInfoFactory.getUIDescription().setTitle("编辑应用");
		appInfoFactory.getUIDescription().setDescription("编辑应用的基本信息,如果代码库地址不是默认\"https://code.jd.com/{username}/jae_{appname}.git\",则需要主动设置。");
		appInfoFactory.getUIDescription().setWidth(150);
		this.addPage(new ControlFactoryWizardPage("appInfo", appInfoFactory));
		
		IControlFactory envFactory = new AppEnvTableFactory();
		envFactory.setValue(this.envList);
		envFactory.getUIDescription().setWinTitle("设置环境变量");
		envFactory.getUIDescription().setTitle("设置环境变量");
		envFactory.getUIDescription().setDescription("根据应用需要设置环境变量。");
		envFactory.getUIDescription().setWidth(150);
		this.addPage(new ControlFactoryWizardPage("envInfo", envFactory));
	}

	private CompoundControlFactory createAppInfoFactory() {
		CompoundControlFactory factory = new CompoundControlFactory();
		
		IControlFactory codeFactory = new GroupControlFactory("代码库", new ObjectEditorControlFactory(this.createCodeObjectEditor()));
		codeFactory.setValue(app);
		factory.addControlFactory(codeFactory);
		
		IControlFactory appFactory = new GroupControlFactory("应用信息", new ObjectEditorControlFactory(this.createAppInfoObjectEditor(cloudInfo, application)));
		appFactory.setValue(appInfo);
		factory.addControlFactory(appFactory);
		
		IControlFactory uriFactory = new AppURITableFactory(app);
		uriFactory.setValue(uriList);
		factory.addControlFactory(uriFactory);
		return factory;
	}
	
	@Override
	protected void doFinish(IProgressMonitor monitor) {
		//默认仓库不记录
		if(!JAEAppHelper.getDefaultAppRepository(app).equals(app.getRepositoryURL()))
			JAEAppHelper.regeditAppRepository(app);
		
		Job job = new Job("编辑应用") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				boolean refreshFlag = false;
				try {
					monitor.beginTask("更新应用设置", 100);
					monitor.worked(20);
					
					monitor.setTaskName("更新应用内存设置");
					String appName = app.getName();
					if(application.getMemory() != appInfo.get("memory")){
						operator.updateApplicationMemory(appName, appInfo.get("memory"));
					}
					monitor.worked(30);

					monitor.setTaskName("更新应用实例数设置");
					monitor.worked(30);
					if(application.getInstances() != appInfo.get("appCount")){
						operator.updateApplicationInstances(appName, appInfo.get("appCount"));
						app.refresh();//实例数发生变化，需要刷新
						refreshFlag = true;
					}
					
					List<String> oldUris = new ArrayList<String>(application.getUris());
					Map<String, String> oldEnv = new HashMap<String, String>(application.getEnvAsMap());
					
					List<String> newUris = new ArrayList<String>();
					for (Map<String, String> uriMap : uriList) {
						String uri = uriMap.get("uri");
						if(null != uri)
							newUris.add(uri);
					}
					
					Map<String, String> newEnv = new HashMap<String, String>();
					for (Map<String, String> envMap : envList) {
						String name = envMap.get("name");
						String value = envMap.get("value");
						
						if(null != name && null != value)
							newEnv.put(name, value);
					}
					
					if(!newUris.equals(oldUris))
						operator.updateApplicationUris(appName, newUris);
					
					if(!newEnv.equals(oldEnv))
						operator.updateApplicationEnv(appName, newEnv);
				} catch (CloudFoundryClientRuntimeException e) {
					e.printStackTrace();
				} finally {
					if(refreshFlag){
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
							
							public void run() {
								JAEView.getInstance().getCommonViewer().refresh();
							}
						});
					}
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		
		job.setUser(true);
		job.schedule();
	}

}
