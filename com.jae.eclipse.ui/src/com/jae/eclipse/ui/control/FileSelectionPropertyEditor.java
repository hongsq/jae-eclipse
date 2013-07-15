/**
 * 
 */
package com.jae.eclipse.ui.control;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.jae.eclipse.ui.base.AbstractSelectionPropertyEditor;

/**
 * @author hongshuiqiao
 *
 */
public class FileSelectionPropertyEditor extends AbstractSelectionPropertyEditor {
	private boolean multi;//是否允许多选
	private String winTitle;//对话框的标题
	private String fileName;//初始选择的文件名称
	private String[] filterExtensions;
	private String[] filterNames;//后缀过滤的名称，与filterExtensions一一对应，用于描述filterExtensions,如果没有，则为filterExtensions本身
	private String initFilterExtension;//初始使用的后缀名
	private String filterPath;//默认对话框所在的根目录地址
	private boolean overwrite;//当dialogStyle为SWT.SAVE时，如果overwrite为true，则选择已经存在的资源，会弹出已经存在，是否覆盖的提示。
	private String separator=";";//多选时的分隔符
	
	/**
	 * @see SWT#OPEN
	 * @see SWT#SAVE
	 */
	private int dialogStyle = SWT.OPEN;
	
	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public String getWinTitle() {
		return winTitle;
	}

	public void setWinTitle(String winTitle) {
		this.winTitle = winTitle;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getFilterExtensions() {
		return filterExtensions;
	}

	public void setFilterExtensions(String[] filterExtensions) {
		this.filterExtensions = filterExtensions;
	}

	public String getInitFilterExtension() {
		return initFilterExtension;
	}

	public void setInitFilterExtension(String initFilterExtension) {
		this.initFilterExtension = initFilterExtension;
	}

	public String[] getFilterNames() {
		return filterNames;
	}

	public void setFilterNames(String[] filterNames) {
		this.filterNames = filterNames;
	}

	public String getFilterPath() {
		return filterPath;
	}

	public void setFilterPath(String filterPath) {
		this.filterPath = filterPath;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public int getDialogStyle() {
		return dialogStyle;
	}

	public void setDialogStyle(int dialogStyle) {
		this.dialogStyle = dialogStyle;
	}

	@Override
	protected String doSelection(Shell shell, String oldValue) {
		int style = dialogStyle;
		if(multi)
			style = style | SWT.MULTI;
		
		FileDialog dialog = new FileDialog(shell, style);
		if(null != winTitle) dialog.setText(winTitle);
		if(null != fileName) dialog.setFileName(fileName);
		if(null != filterExtensions) dialog.setFilterExtensions(filterExtensions);
		if(null != initFilterExtension && null != filterExtensions){
			int index = Arrays.binarySearch(filterExtensions, initFilterExtension);
			if(index>=0) dialog.setFilterIndex(index);
		}
		
		if(null != filterNames) dialog.setFilterNames(filterNames);
		if(null != filterPath) dialog.setFilterPath(filterPath);
		dialog.setOverwrite(overwrite);
		
		String result = dialog.open();
		if(null == result)
			return oldValue;
		
		String[] fileNames = dialog.getFileNames();
		if(fileNames.length == 1)
			return result;
		
		String filePath = dialog.getFilterPath();
		String[] results = new String[fileNames.length];
		for (int i = 0; i < results.length; i++) {
			results[i] = filePath+File.separator+fileNames[i];
		}
		
		return StringUtils.join(results, separator);
	}

}
