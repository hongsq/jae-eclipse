/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.provider;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.jae.eclipse.navigator.jaeapp.view.JAEAppView;

/**
 * @author hongshuiqiao
 *
 */
public class Display4LoadingThread extends Thread {
	private LoadingJDElement loadingElement;
	private AtomicBoolean finished;
	private IProgressMonitor monitor;

	public Display4LoadingThread(String name, LoadingJDElement loadingElement,
			AtomicBoolean finished, IProgressMonitor monitor) {
		super(name);
		this.loadingElement = loadingElement;
		this.finished = finished;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		Display display = PlatformUI.getWorkbench().getDisplay();
		int count = 1;
		while(null != finished && !finished.get()){
			if(count>5){
				loadingElement.setName("loading . ");
				count=1;
			}else{
				loadingElement.setName(loadingElement.getName()+". ");
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {}

			if(count%2==0){
				monitor.worked(10);
			}
			
			display.asyncExec(new Runnable() {
				
				public void run() {
					JAEAppView view = JAEAppView.getInstance();
					if(null != view){
						view.getCommonViewer().refresh(loadingElement);
					}
				}
			});

			count++;
		}
	}
}
