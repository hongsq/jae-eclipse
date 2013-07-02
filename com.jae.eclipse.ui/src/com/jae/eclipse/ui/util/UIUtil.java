/**
 * 
 */
package com.jae.eclipse.ui.util;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * @author hongshuiqiao
 *
 */
public class UIUtil {
	
	public static boolean isControlValid(Control control){
		return null != control && !control.isDisposed();
	}

	public void runInUI(Runnable runnable, boolean sync){
		Display display = Display.getCurrent();
		if(null == display)
			display = PlatformUI.getWorkbench().getDisplay();
		
		if(Thread.currentThread() == display.getThread()){
			runnable.run();
		}else{
			if(sync){
				display.syncExec(runnable);
			}else{
				display.asyncExec(runnable);
			}
		}
	}
}
