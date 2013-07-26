/**
 * 
 */
package com.jae.eclipse.ui;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author hongshuiqiao
 *
 */
public class UIDescription {
	private String winTitle;
	private String title;
	private String description;
	private ImageDescriptor titleImage;
	private ImageDescriptor winTitleImage;
	private int width=-1;
	private int height=-1;
	private int minWidth=-1;
	private int minHeight=-1;
	private int initX=-1;
	private int initY=-1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	public String getWinTitle() {
		return winTitle;
	}

	public void setWinTitle(String winTitle) {
		this.winTitle = winTitle;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ImageDescriptor getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(ImageDescriptor titleImage) {
		this.titleImage = titleImage;
	}

	public ImageDescriptor getWinTitleImage() {
		return winTitleImage;
	}

	public void setWinTitleImage(ImageDescriptor winTitleImage) {
		this.winTitleImage = winTitleImage;
	}

	public int getInitX() {
		return initX;
	}

	public void setInitX(int initX) {
		this.initX = initX;
	}

	public int getInitY() {
		return initY;
	}

	public void setInitY(int initY) {
		this.initY = initY;
	}
}
