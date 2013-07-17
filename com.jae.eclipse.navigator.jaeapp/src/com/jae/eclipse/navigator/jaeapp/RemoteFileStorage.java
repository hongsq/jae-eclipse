/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.PlatformObject;

import com.jae.eclipse.navigator.jaeapp.model.RemoteFile;

/**
 * @author hongshuiqiao
 *
 */
public class RemoteFileStorage extends PlatformObject implements IStorage {
	private RemoteFile file;

	public RemoteFileStorage(RemoteFile file) {
		super();
		this.file = file;
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == RemoteFile.class)
			return file;
		return super.getAdapter(adapter);
	}

	@Override
	public InputStream getContents() throws CoreException {
		try {
			return new ByteArrayInputStream(this.file.getContent().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public IPath getFullPath() {
		return new Path(this.file.getPath());
	}

	@Override
	public String getName() {
		return this.file.getName();
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

}
