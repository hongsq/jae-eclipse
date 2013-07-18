/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

/**
 * @author hongshuiqiao
 *
 */
public class RemoteFileInput extends PlatformObject implements IStorageEditorInput {
	private IStorage storage;

	public RemoteFileInput(IStorage storage) {
		super();
		this.storage = storage;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return this.storage.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return this.storage.getFullPath().toString();
	}

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter) {
		if(adapter == IStorage.class)
			return this.storage;
		return super.getAdapter(adapter);
	}

	public IStorage getStorage() throws CoreException {
		return this.storage;
	}

	public int hashCode() {
		return this.storage.hashCode();
	}
}
