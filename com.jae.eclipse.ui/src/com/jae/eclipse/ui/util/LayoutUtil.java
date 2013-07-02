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
	 * ����һ�����¼���Ϳؼ������Ϊ0��GridLayout��<BR>
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
