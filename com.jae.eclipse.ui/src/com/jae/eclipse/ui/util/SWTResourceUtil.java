package com.jae.eclipse.ui.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
/**
 * SWT资源的统一入口，便于资源的复用及统一销毁
 * @author hongshuiqiao
 *
 */
public class SWTResourceUtil {
	private static Map<Object, Image> imageTable = new Hashtable<Object, Image>(40);
	private static Map<Object, Color> colorTable = new Hashtable<Object, Color>(7);
	private static Map<Object, Font> fontTable = new Hashtable<Object, Font>(7);

	public static final void shutdown() {
		if (imageTable != null) {
			for (Iterator<Image> i = imageTable.values().iterator(); i.hasNext();) {
				(i.next()).dispose();
			}
			imageTable = null;
		}
		if (colorTable != null) {
			for (Iterator<Color> i = colorTable.values().iterator(); i.hasNext();) {
				(i.next()).dispose();
			}
			colorTable = null;
		}
		if (fontTable != null) {
			for (Iterator<Font> i = fontTable.values().iterator(); i.hasNext();) {
				(i.next()).dispose();
			}
			fontTable = null;
		}
	}

	public static Image getImage(Object imageDescriptor){
		return imageTable.get(imageDescriptor);
	}
	
	public static void regeditImage(Object imageDescriptor, Image image){
		imageTable.put(imageDescriptor, image);
	}
	
	public static Color getColor(Object colorDescriptor){
		return colorTable.get(colorDescriptor);
	}
	
	public static void regeditColor(Object colorDescriptor, Color color){
		colorTable.put(colorDescriptor, color);
	}
	
	public static Font getFont(Object fontDescriptor){
		return fontTable.get(fontDescriptor);
	}
	
	public static void regeditFont(Object fontDescriptor, Font font){
		fontTable.put(fontDescriptor, font);
	}
}