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

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return this.storage.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return this.storage.getFullPath().toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == IStorage.class)
			return this.storage;
		return super.getAdapter(adapter);
	}

	@Override
	public IStorage getStorage() throws CoreException {
		return this.storage;
	}

	public int hashCode() {
		return this.storage.hashCode();
	}
}
