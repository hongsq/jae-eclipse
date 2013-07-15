/**
 * 
 */
package com.jae.eclipse.ui.control;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.ui.base.AbstractSelectionPropertyEditor;

/**
 * @author hongshuiqiao
 *
 */
public class DirectorySelectionPropertyEditor extends AbstractSelectionPropertyEditor {
	private String filterPath;//默认的根目录
	private String message;//提示的描述信息
	private String winTitle;//窗口标题

	public String getFilterPath() {
		return filterPath;
	}

	public void setFilterPath(String filterPath) {
		this.filterPath = filterPath;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getWinTitle() {
		return winTitle;
	}

	public void setWinTitle(String winTitle) {
		this.winTitle = winTitle;
	}

	@Override
	protected String doSelection(Shell shell, String oldValue) {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		if(null != filterPath) dialog.setFilterPath(filterPath);
		if(null != message) dialog.setMessage(message);
		if(null != winTitle) dialog.setText(winTitle);
		String result = dialog.open();
		if(null == result)
			return oldValue;
		
		return result;
	}

}
