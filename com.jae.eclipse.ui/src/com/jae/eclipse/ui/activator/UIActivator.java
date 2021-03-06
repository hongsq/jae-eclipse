package com.jae.eclipse.ui.activator;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.jae.eclipse.ui.util.SWTResourceUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class UIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.jae.eclipse.ui"; //$NON-NLS-1$

	// The shared instance
	private static UIActivator plugin;
	
	/**
	 * The constructor
	 */
	public UIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		SWTResourceUtil.shutdown();
		
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static UIActivator getDefault() {
		return plugin;
	}

}
