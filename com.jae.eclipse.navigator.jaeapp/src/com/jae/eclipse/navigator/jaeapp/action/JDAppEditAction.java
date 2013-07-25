/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.action;

import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.cloudfoundry.client.CloudFoundryClientExt;
import com.jae.eclipse.cloudfoundry.exception.CloudFoundryClientRuntimeException;
import com.jae.eclipse.navigator.jaeapp.model.JDApp;
import com.jae.eclipse.navigator.jaeapp.model.User;
import com.jae.eclipse.navigator.jaeapp.util.JAEAppHelper;
import com.jae.eclipse.navigator.jaeapp.util.JDModelUtil;
import com.jae.eclipse.ui.ObjectEditor;
import com.jae.eclipse.ui.control.NumberPropertyEditor;
import com.jae.eclipse.ui.control.NumberType;
import com.jae.eclipse.ui.control.StringPropertyEditor;
import com.jae.eclipse.ui.event.IValuechangeListener;
import com.jae.eclipse.ui.event.ValueChangeEvent;
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
public class JDAppEditAction extends AbstractJDAction {

	public JDAppEditAction(ISelectionProvider provider, String text) {
		super(provider, text);
		
		this.setId("jdapp.action.edit");
		this.setImageDescriptor(ImageRepositoryManager.getImageDescriptor("jdapp.action.edit"));
		this.setMustSelect(true);
		this.setMultiable(false);
		this.setSelectType(JDApp.class);
		this.setEnabled(false);
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
	public void run() {
		final JDApp app = (JDApp) this.getStructuredSelection().getFirstElement();
		User user = JDModelUtil.getParentElement(app, User.class);
		final CloudFoundryClientExt operator = user.getCloudFoundryClient();
		CloudInfo cloudInfo = operator.getCloudInfo();
		final CloudApplication application = operator.getApplication(app.getName());
		
		CompoundControlFactory factory = new CompoundControlFactory();
		factory.getUIDescription().setWinTitle("编辑应用");
		factory.getUIDescription().setTitle("编辑应用");
		factory.getUIDescription().setDescription("编辑应用相关信息");
		
		IControlFactory codeFactory = new GroupControlFactory("代码库", new ObjectEditorControlFactory(this.createCodeObjectEditor()));
		codeFactory.setValue(app);
		factory.addControlFactory(codeFactory);
		
		IControlFactory appFactory = new GroupControlFactory("应用信息", new ObjectEditorControlFactory(this.createAppInfoObjectEditor(cloudInfo, application)));
		final Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("memory", application.getMemory());
		map.put("appCount", application.getInstances());
		appFactory.setValue(map);
		factory.addControlFactory(appFactory);
		
		final StructuredViewer viewer = (StructuredViewer) this.getSelectionProvider();
		
		final Shell shell = viewer.getControl().getShell();
		ControlFactoryDialog dialog = new ControlFactoryDialog(shell, factory);
		if(Window.OK == dialog.open()){
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
						if(application.getMemory() != map.get("memory")){
							operator.updateApplicationMemory(app.getName(), map.get("memory"));
						}
						monitor.worked(30);

						monitor.setTaskName("更新应用实例数设置");
						monitor.worked(30);
						if(application.getInstances() != map.get("appCount")){
							operator.updateApplicationInstances(app.getName(), map.get("appCount"));
							app.refresh();//实例数发生变化，需要刷新
							refreshFlag = true;
						}
					} catch (CloudFoundryClientRuntimeException e) {
						e.printStackTrace();
					} finally {
						if(refreshFlag){
							shell.getDisplay().asyncExec(new Runnable() {
								
								public void run() {
									viewer.refresh();
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
}
