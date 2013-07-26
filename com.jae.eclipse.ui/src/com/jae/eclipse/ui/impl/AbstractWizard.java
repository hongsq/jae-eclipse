/**
 * 
 */
package com.jae.eclipse.ui.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.jae.eclipse.ui.IStore;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractWizard extends Wizard {

	public AbstractWizard() {
		super();
	}

	@Override
	public boolean performFinish() {
		IWizardPage[] pages = this.getPages();
		for (IWizardPage page : pages) {
			if (page instanceof IStore) {
				IStore store = (IStore) page;
				store.save();
			}
		}
		
		IRunnableWithProgress operation = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				doFinish(monitor);
			}
		};
		
		try {
			this.getContainer().run(false, false, operation);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	protected abstract void doFinish(IProgressMonitor monitor);

}
