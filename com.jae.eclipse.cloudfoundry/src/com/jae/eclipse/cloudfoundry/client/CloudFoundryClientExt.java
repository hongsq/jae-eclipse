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
import org.springframework.util.Assert;

/**
 * @author hongshuiqiao
 * 
 */
public class CloudFoundryClientExt implements CloudFoundryOperations {
	private CloudControllerClient cc;
	private CloudInfo info;

	public CloudFoundryClientExt(URL cloudControllerUrl) {
		this(null, cloudControllerUrl, null, null);
	}

	public CloudFoundryClientExt(CloudCredentials credentials,
			URL cloudControllerUrl) {
		this(credentials, cloudControllerUrl, null, null);
	}

	public CloudFoundryClientExt(CloudCredentials credentials,
			URL cloudControllerUrl, CloudSpace sessionSpace) {
		this(credentials, cloudControllerUrl, null, sessionSpace);
	}

	public CloudFoundryClientExt(URL cloudControllerUrl,
			HttpProxyConfiguration httpProxyConfiguration) {
		this(null, cloudControllerUrl, httpProxyConfiguration, null);
	}

	public CloudFoundryClientExt(CloudCredentials credentials,
			URL cloudControllerUrl,
			HttpProxyConfiguration httpProxyConfiguration) {
		this(credentials, cloudControllerUrl, httpProxyConfiguration, null);
	}

	public CloudFoundryClientExt(CloudCredentials credentials,
			URL cloudControllerUrl,
			HttpProxyConfiguration httpProxyConfiguration,
			CloudSpace sessionSpace) {
		Assert.notNull(cloudControllerUrl,
				"URL for cloud controller cannot be null");
		RestUtilExt restUtilExt = new RestUtilExt();
		CloudControllerClientFactory cloudControllerClientFactory = new CloudControllerClientFactory(
				restUtilExt, httpProxyConfiguration);

		this.cc = cloudControllerClientFactory.newCloudController(
				cloudControllerUrl, credentials, sessionSpace);
	}

	public URL getCloudControllerUrl() {
		return this.cc.getCloudControllerUrl();
	}

	public CloudInfo getCloudInfo() {
		if (this.info == null) {
			this.info = this.cc.getInfo();
		}
		return this.info;
	}

	public boolean supportsSpaces() {
		return this.cc.supportsSpaces();
	}

	public List<CloudSpace> getSpaces() {
		return this.cc.getSpaces();
	}

	public List<String> getApplicationPlans() {
		return this.cc.getApplicationPlans();
	}

	public void register(String email, String password) {
		this.cc.register(email, password);
	}

	public void updatePassword(String newPassword) {
		this.cc.updatePassword(newPassword);
	}

	public void updatePassword(CloudCredentials credentials, String newPassword) {
		this.cc.updatePassword(credentials, newPassword);
	}

	public void unregister() {
		this.cc.unregister();
	}

	public String login() {
		
		CloudInfo info = this.cc.getInfo();
		
//		return this.cc.login();
		return info.getUser();
	}

	public void logout() {
		this.cc.logout();
	}

	public List<CloudApplication> getApplications() {
		return this.cc.getApplications();
	}

	public CloudApplication getApplication(String appName) {
		return this.cc.getApplication(appName);
	}

	public ApplicationStats getApplicationStats(String appName) {
		return this.cc.getApplicationStats(appName);
	}

	public int[] getApplicationMemoryChoices() {
		return this.cc.getApplicationMemoryChoices();
	}

	public int getDefaultApplicationMemory(String framework) {
		return this.cc.getDefaultApplicationMemory(framework);
	}

	public void createApplication(String appName, Staging staging, int memory,
			List<String> uris, List<String> serviceNames) {
		this.cc.createApplication(appName, staging, memory, uris, serviceNames,
				false);
	}

	public void createApplication(String appName, Staging staging, int memory,
			List<String> uris, List<String> serviceNames, boolean checkExists) {
		this.cc.createApplication(appName, staging, memory, uris, serviceNames,
				checkExists);
	}

	public void createApplication(String appName, Staging staging, int memory,
			List<String> uris, List<String> serviceNames, String applicationPlan) {
		this.cc.createApplication(appName, staging, memory, uris, serviceNames,
				applicationPlan, false);
	}

	public void createApplication(String appName, Staging staging, int memory,
			List<String> uris, List<String> serviceNames,
			String applicationPlan, boolean checkExists) {
		this.cc.createApplication(appName, staging, memory, uris, serviceNames,
				applicationPlan, checkExists);
	}

	public void createApplication(String appName, String framework, int memory,
			List<String> uris, List<String> serviceNames) {
		this.cc.createApplication(appName, new Staging(framework), memory,
				uris, serviceNames, false);
	}

	public void createApplication(String appName, String framework, int memory,
			List<String> uris, List<String> serviceNames, boolean checkExists) {
		this.cc.createApplication(appName, new Staging(framework), memory,
				uris, serviceNames, checkExists);
	}

	public void createService(CloudService service) {
		this.cc.createService(service);
	}

	public void uploadApplication(String appName, String file)
			throws IOException {
		this.cc.uploadApplication(appName, new File(file), null);
	}

	public void uploadApplication(String appName, File file) throws IOException {
		this.cc.uploadApplication(appName, file, null);
	}

	public void uploadApplication(String appName, File file,
			UploadStatusCallback callback) throws IOException {
		this.cc.uploadApplication(appName, file, callback);
	}

	public void uploadApplication(String appName, ApplicationArchive archive)
			throws IOException {
		this.cc.uploadApplication(appName, archive, null);
	}

	public void uploadApplication(String appName, ApplicationArchive archive,
			UploadStatusCallback callback) throws IOException {
		this.cc.uploadApplication(appName, archive, callback);
	}

	public void startApplication(String appName) {
		this.cc.startApplication(appName);
	}
//	@Override
//	public StartingInfo startApplication(String appName) {
//		return this.cc.startApplication(appName);
//	}

	public void debugApplication(String appName, CloudApplication.DebugMode mode) {
		this.cc.debugApplication(appName, mode);
	}

	public void stopApplication(String appName) {
		this.cc.stopApplication(appName);
	}

	public void restartApplication(String appName) {
		this.cc.restartApplication(appName);
	}

	public void deleteApplication(String appName) {
		this.cc.deleteApplication(appName);
	}

	public void deleteAllApplications() {
		this.cc.deleteAllApplications();
	}

	public void deleteAllServices() {
		this.cc.deleteAllServices();
	}

	public void updateApplicationMemory(String appName, int memory) {
		this.cc.updateApplicationMemory(appName, memory);
	}

	public void updateApplicationInstances(String appName, int instances) {
		this.cc.updateApplicationInstances(appName, instances);
	}

	public void updateApplicationServices(String appName, List<String> services) {
		this.cc.updateApplicationServices(appName, services);
	}

	public void updateApplicationStaging(String appName, Staging staging) {
		this.cc.updateApplicationStaging(appName, staging);
	}

	public void updateApplicationUris(String appName, List<String> uris) {
		this.cc.updateApplicationUris(appName, uris);
	}

	public void updateApplicationEnv(String appName, Map<String, String> env) {
		this.cc.updateApplicationEnv(appName, env);
	}

	public void updateApplicationEnv(String appName, List<String> env) {
		this.cc.updateApplicationEnv(appName, env);
	}

	public void updateApplicationPlan(String appName, String applicationPlan) {
		this.cc.updateApplicationPlan(appName, applicationPlan);
	}

	public Map<String, String> getLogs(String appName) {
		return this.cc.getLogs(appName);
	}

	public Map<String, String> getCrashLogs(String appName) {
		return this.cc.getCrashLogs(appName);
	}

	public String getFile(String appName, int instanceIndex, String filePath) {
		return this.cc.getFile(appName, instanceIndex, filePath, 0, -1);
	}

	public String getFile(String appName, int instanceIndex, String filePath,
			int startPosition) {
		Assert.isTrue(
				startPosition >= 0,
				startPosition
						+ " is not a valid value for start position, it should be 0 or greater.");

		return this.cc.getFile(appName, instanceIndex, filePath, startPosition,
				-1);
	}

	public String getFile(String appName, int instanceIndex, String filePath,
			int startPosition, int endPosition) {
		Assert.isTrue(
				startPosition >= 0,
				startPosition
						+ " is not a valid value for start position, it should be 0 or greater.");

		Assert.isTrue(
				endPosition > startPosition,
				endPosition
						+ " is not a valid value for end position, it should be greater than startPosition "
						+ "which is " + startPosition + ".");

		return this.cc.getFile(appName, instanceIndex, filePath, startPosition,
				endPosition - 1);
	}

	public String getFileTail(String appName, int instanceIndex,
			String filePath, int length) {
		Assert.isTrue(
				length > 0,
				length
						+ " is not a valid value for length, it should be 1 or greater.");
		return this.cc.getFile(appName, instanceIndex, filePath, -1, length);
	}

	public List<CloudService> getServices() {
		return this.cc.getServices();
	}

	public CloudService getService(String service) {
		return this.cc.getService(service);
	}

	public void deleteService(String service) {
		this.cc.deleteService(service);
	}

	public List<ServiceConfiguration> getServiceConfigurations() {
		return this.cc.getServiceConfigurations();
	}

	public void bindService(String appName, String serviceName) {
		this.cc.bindService(appName, serviceName);
	}

	public void unbindService(String appName, String serviceName) {
		this.cc.unbindService(appName, serviceName);
	}

	public InstancesInfo getApplicationInstances(String appName) {
		return this.cc.getApplicationInstances(appName);
	}

	public CrashesInfo getCrashes(String appName) {
		return this.cc.getCrashes(appName);
	}

	public void rename(String appName, String newName) {
		this.cc.rename(appName, newName);
	}

	public List<CloudDomain> getDomainsForOrg() {
		return this.cc.getDomainsForOrg();
	}

	public List<CloudDomain> getDomains() {
		return this.cc.getDomains();
	}

	public void addDomain(String domainName) {
		this.cc.addDomain(domainName);
	}

	public void deleteDomain(String domainName) {
		this.cc.deleteDomain(domainName);
	}

	public void removeDomain(String domainName) {
		this.cc.removeDomain(domainName);
	}

	public List<CloudRoute> getRoutes(String domainName) {
		return this.cc.getRoutes(domainName);
	}

	public void addRoute(String host, String domainName) {
		this.cc.addRoute(host, domainName);
	}

	public void deleteRoute(String host, String domainName) {
		this.cc.deleteRoute(host, domainName);
	}

	public void updateHttpProxyConfiguration(
			HttpProxyConfiguration httpProxyConfiguration) {
		this.cc.updateHttpProxyConfiguration(httpProxyConfiguration);
	}

	public void registerRestLogListener(RestLogCallback callBack) {
		this.cc.registerRestLogListener(callBack);
	}

	public void unRegisterRestLogListener(RestLogCallback callBack) {
		this.cc.unRegisterRestLogListener(callBack);
	}

}
