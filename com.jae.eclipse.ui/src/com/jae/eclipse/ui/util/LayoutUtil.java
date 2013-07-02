/**
 * 
 */
package com.jae.eclipse.ui.util;

import org.eclipse.swt.layout.GridLayout;

/**
 * @author hongshuiqiao
 *
 */
public class LayoutUtil {

	/**
	 * 创建一个上下间隔和控件间隔都为0的GridLayout。<BR>
	 * 
	 * @param columnCount
	 * @return
	 */
	public static GridLayout createCompactGridLayout(int columnCount) {
		GridLayout gridLayout = new GridLayout(columnCount, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;

		return gridLayout;
	}
}
