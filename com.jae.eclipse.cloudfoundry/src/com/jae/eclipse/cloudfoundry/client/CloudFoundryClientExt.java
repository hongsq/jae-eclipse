/**
 * 
 */
package com.jae.eclipse.cloudfoundry.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryOperations;
import org.cloudfoundry.client.lib.HttpProxyConfiguration;
import org.cloudfoundry.client.lib.RestLogCallback;
import org.cloudfoundry.client.lib.UploadStatusCallback;
import org.cloudfoundry.client.lib.archive.ApplicationArchive;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudDomain;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.CloudRoute;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.domain.CrashesInfo;
import org.cloudfoundry.client.lib.domain.InstancesInfo;
import org.cloudfoundry.client.lib.domain.ServiceConfiguration;
import org.cloudfoundry.client.lib.domain.Staging;
import org.cloudfoundry.client.lib.rest.CloudControllerClient;
import org.cloudfoundry.client.lib.rest.CloudControllerClientFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.springframework.util.Assert;

import com.jae.eclipse.cloudfoundry.exception.CloudFoundryClientRuntimeException;
import com.jae.eclipse.ui.dialog.DetailMessageDialog;

/**
 * @author hongshuiqiao
 * 
 */
public class CloudFoundryClientExt implements CloudFoundryOperations {
	private CloudControllerClient cc;
	private CloudInfo info;
	private RestUtilExt restUtil;
	private URL cloudControllerUrl;
//	private RestTemplate restTemplate;

	public CloudFoundryClientExt(URL cloudControllerUrl) {
		this(null, cloudControllerUrl, null, null);
	}

	public CloudFoundryClientExt(CloudCredentials credentials, URL cloudControllerUrl) {
		this(credentials, cloudControllerUrl, null, null);
	}

	public CloudFoundryClientExt(CloudCredentials credentials, URL cloudControllerUrl, CloudSpace sessionSpace) {
		this(credentials, cloudControllerUrl, null, sessionSpace);
	}

	public CloudFoundryClientExt(URL cloudControllerUrl, HttpProxyConfiguration httpProxyConfiguration) {
		this(null, cloudControllerUrl, httpProxyConfiguration, null);
	}

	public CloudFoundryClientExt(CloudCredentials credentials, URL cloudControllerUrl, HttpProxyConfiguration httpProxyConfiguration) {
		this(credentials, cloudControllerUrl, httpProxyConfiguration, null);
	}

	public CloudFoundryClientExt(CloudCredentials credentials, URL cloudControllerUrl, HttpProxyConfiguration httpProxyConfiguration, CloudSpace sessionSpace) {
		Assert.notNull(cloudControllerUrl, "URL for cloud controller cannot be null");
		this.restUtil = new RestUtilExt(credentials);
		CloudControllerClientFactory cloudControllerClientFactory = new CloudControllerClientFactory(this.restUtil, httpProxyConfiguration);

		try {
			this.cc = cloudControllerClientFactory.newCloudController(cloudControllerUrl, credentials, sessionSpace);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
		this.cloudControllerUrl = cloudControllerUrl;
//		this.restTemplate = this.restUtil.createRestTemplate(httpProxyConfiguration);
	}
	
	public RestUtilExt getRestUtil() {
		return restUtil;
	}

	protected String getUrl(String path) {
		return this.cloudControllerUrl
				+ (path.startsWith("/") ? path : new StringBuilder()
						.append("/").append(path).toString());
	}
	
	public URL getCloudControllerUrl() {
		try {
			return this.cc.getCloudControllerUrl();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public CloudInfo getCloudInfo() {
		try {
			if (this.info == null) {
				this.info = this.cc.getInfo();
			}
			return this.info;
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public boolean supportsSpaces() {
		try {
			return this.cc.supportsSpaces();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public List<CloudSpace> getSpaces() {
		try {
			return this.cc.getSpaces();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public List<String> getApplicationPlans() {
		try {
			return this.cc.getApplicationPlans();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void register(String email, String password) {
		try {
			this.cc.register(email, password);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updatePassword(String newPassword) {
		try {
			this.cc.updatePassword(newPassword);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updatePassword(CloudCredentials credentials, String newPassword) {
		try {
			this.cc.updatePassword(credentials, newPassword);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void unregister() {
		try {
			this.cc.unregister();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public String login() {
		try {
			CloudInfo info = this.cc.getInfo();
			
//		return this.cc.login();
			return info.getUser();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void logout() {
		try {
			this.cc.logout();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

//	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CloudApplication> getApplications() {
//		List<Map> appsAsMap = null;
		
		try {
			return this.cc.getApplications();
//			appsAsMap = (List<Map>)this.restTemplate.getForObject(getUrl("/apps"), List.class, new Object[0]);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}

//		List apps = new ArrayList();
//		final StringBuilder builder = new StringBuilder();
//		for (Map appAsMap : appsAsMap) {
//			try {
//				apps.add(new CloudApplication(appAsMap));
//			} catch (Exception e) {
//				e.printStackTrace();
//				builder.append(appAsMap.get("name"));
//				builder.append(",");
//			}
//		}
//
//		if(builder.length()>0){
//			builder.deleteCharAt(builder.length()-1);
//			UIUtil.runInUI(new Runnable() {
//				
//				public void run() {
//					Shell shell = Display.getCurrent().getActiveShell();
//					MessageDialog.openWarning(shell, "警告", "加载应用\""+builder.toString()+"\"失败。");
//				}
//			}, false);
//		}
//	    return apps;
	}

	private void openError(final Exception e) {
		final Display display = PlatformUI.getWorkbench().getDisplay();
		if(Thread.currentThread() == display.getThread()){
			doOpenError(e, display);
		}else{
			display.asyncExec(new Runnable() {
				
				public void run() {
					doOpenError(e, display);					
				}
			});
		}
	}

	private void doOpenError(Exception e, Display display) {
		Shell shell = display.getActiveShell();
		DetailMessageDialog.openError(shell, "错误", "远程操作云擎时出错，请稍后再试", e);
	}

	public CloudApplication getApplication(String appName) {
		try {
			return this.cc.getApplication(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public ApplicationStats getApplicationStats(String appName) {
		try {
			return this.cc.getApplicationStats(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public int[] getApplicationMemoryChoices() {
		try {
			return this.cc.getApplicationMemoryChoices();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public int getDefaultApplicationMemory(String framework) {
		try {
			return this.cc.getDefaultApplicationMemory(framework);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void createApplication(String appName, Staging staging, int memory, List<String> uris, List<String> serviceNames) {
		try {
			this.cc.createApplication(appName, staging, memory, uris, serviceNames, false);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void createApplication(String appName, Staging staging, int memory, List<String> uris, List<String> serviceNames, boolean checkExists) {
		try {
			this.cc.createApplication(appName, staging, memory, uris, serviceNames, checkExists);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void createApplication(String appName, Staging staging, int memory, List<String> uris, List<String> serviceNames, String applicationPlan) {
		try {
			this.cc.createApplication(appName, staging, memory, uris, serviceNames, applicationPlan, false);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void createApplication(String appName, Staging staging, int memory, List<String> uris, List<String> serviceNames, String applicationPlan, boolean checkExists) {
		try {
			this.cc.createApplication(appName, staging, memory, uris, serviceNames, applicationPlan, checkExists);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void createApplication(String appName, String framework, int memory, List<String> uris, List<String> serviceNames) {
		try {
			this.cc.createApplication(appName, new Staging(framework), memory, uris, serviceNames, false);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void createApplication(String appName, String framework, int memory, List<String> uris, List<String> serviceNames, boolean checkExists) {
		try {
			this.cc.createApplication(appName, new Staging(framework), memory, uris, serviceNames, checkExists);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void createService(CloudService service) {
		try {
			this.cc.createService(service);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void uploadApplication(String appName, String file) throws IOException {
		try {
			this.cc.uploadApplication(appName, new File(file), null);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void uploadApplication(String appName, File file) throws IOException {
		try {
			this.cc.uploadApplication(appName, file, null);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void uploadApplication(String appName, File file, UploadStatusCallback callback) throws IOException {
		try {
			this.cc.uploadApplication(appName, file, callback);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void uploadApplication(String appName, ApplicationArchive archive) throws IOException {
		try {
			this.cc.uploadApplication(appName, archive, null);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void uploadApplication(String appName, ApplicationArchive archive, UploadStatusCallback callback) throws IOException {
		try {
			this.cc.uploadApplication(appName, archive, callback);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void startApplication(String appName) {
		try {
			this.cc.startApplication(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}
//	@Override
//	public StartingInfo startApplication(String appName) {
//		return this.cc.startApplication(appName);
//	}

	public void debugApplication(String appName, CloudApplication.DebugMode mode) {
		try {
			this.cc.debugApplication(appName, mode);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void stopApplication(String appName) {
		try {
			this.cc.stopApplication(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void restartApplication(String appName) {
		try {
			this.cc.restartApplication(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void deleteApplication(String appName) {
		try {
			this.cc.deleteApplication(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void deleteAllApplications() {
		try {
			this.cc.deleteAllApplications();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void deleteAllServices() {
		try {
			this.cc.deleteAllServices();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateApplicationMemory(String appName, int memory) {
		try {
			this.cc.updateApplicationMemory(appName, memory);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateApplicationInstances(String appName, int instances) {
		try {
			this.cc.updateApplicationInstances(appName, instances);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateApplicationServices(String appName, List<String> services) {
		try {
			this.cc.updateApplicationServices(appName, services);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateApplicationStaging(String appName, Staging staging) {
		try {
			this.cc.updateApplicationStaging(appName, staging);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateApplicationUris(String appName, List<String> uris) {
		try {
			this.cc.updateApplicationUris(appName, uris);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateApplicationEnv(String appName, Map<String, String> env) {
		try {
			this.cc.updateApplicationEnv(appName, env);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateApplicationEnv(String appName, List<String> env) {
		try {
			this.cc.updateApplicationEnv(appName, env);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateApplicationPlan(String appName, String applicationPlan) {
		try {
			this.cc.updateApplicationPlan(appName, applicationPlan);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public Map<String, String> getLogs(String appName) {
		try {
			return this.cc.getLogs(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public Map<String, String> getCrashLogs(String appName) {
		try {
			return this.cc.getCrashLogs(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public String getFile(String appName, int instanceIndex, String filePath) {
		try {
			return this.cc.getFile(appName, instanceIndex, filePath, 0, -1);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public String getFile(String appName, int instanceIndex, String filePath, int startPosition) {
		Assert.isTrue(startPosition >= 0, startPosition + " is not a valid value for start position, it should be 0 or greater.");

		try {
			return this.cc.getFile(appName, instanceIndex, filePath, startPosition, -1);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public String getFile(String appName, int instanceIndex, String filePath, int startPosition, int endPosition) {
		Assert.isTrue(startPosition >= 0, startPosition + " is not a valid value for start position, it should be 0 or greater.");

		Assert.isTrue(endPosition > startPosition, endPosition + " is not a valid value for end position, it should be greater than startPosition " + "which is " + startPosition + ".");

		try {
			return this.cc.getFile(appName, instanceIndex, filePath, startPosition, endPosition - 1);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public String getFileTail(String appName, int instanceIndex, String filePath, int length) {
		Assert.isTrue(length > 0, length + " is not a valid value for length, it should be 1 or greater.");
		try {
			return this.cc.getFile(appName, instanceIndex, filePath, -1, length);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public List<CloudService> getServices() {
		try {
			return this.cc.getServices();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public CloudService getService(String service) {
		try {
			return this.cc.getService(service);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void deleteService(String service) {
		try {
			this.cc.deleteService(service);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public List<ServiceConfiguration> getServiceConfigurations() {
		try {
			return this.cc.getServiceConfigurations();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void bindService(String appName, String serviceName) {
		try {
			this.cc.bindService(appName, serviceName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void unbindService(String appName, String serviceName) {
		try {
			this.cc.unbindService(appName, serviceName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public InstancesInfo getApplicationInstances(String appName) {
		try {
			return this.cc.getApplicationInstances(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public CrashesInfo getCrashes(String appName) {
		try {
			return this.cc.getCrashes(appName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void rename(String appName, String newName) {
		try {
			this.cc.rename(appName, newName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public List<CloudDomain> getDomainsForOrg() {
		try {
			return this.cc.getDomainsForOrg();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public List<CloudDomain> getDomains() {
		try {
			return this.cc.getDomains();
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void addDomain(String domainName) {
		try {
			this.cc.addDomain(domainName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void deleteDomain(String domainName) {
		try {
			this.cc.deleteDomain(domainName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void removeDomain(String domainName) {
		try {
			this.cc.removeDomain(domainName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public List<CloudRoute> getRoutes(String domainName) {
		try {
			return this.cc.getRoutes(domainName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void addRoute(String host, String domainName) {
		try {
			this.cc.addRoute(host, domainName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void deleteRoute(String host, String domainName) {
		try {
			this.cc.deleteRoute(host, domainName);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void updateHttpProxyConfiguration(HttpProxyConfiguration httpProxyConfiguration) {
		try {
			this.cc.updateHttpProxyConfiguration(httpProxyConfiguration);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void registerRestLogListener(RestLogCallback callBack) {
		try {
			this.cc.registerRestLogListener(callBack);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

	public void unRegisterRestLogListener(RestLogCallback callBack) {
		try {
			this.cc.unRegisterRestLogListener(callBack);
		} catch (Exception e) {
			openError(e);
			throw new CloudFoundryClientRuntimeException(e);
		}
	}

}
