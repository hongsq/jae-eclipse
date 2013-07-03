/**
 * 
 */
package com.jae.eclipse.ui;

import org.eclipse.swt.graphics.Image;

/**
 * @author hongshuiqiao
 *
 */
public class UIDescription {
	private String winTitle;
	private String title;
	private String description;
	private Image titleImage;
	private Image winTitleImage;
	private int initWidth=-1;
	private int initHeight=-1;
	private int initX=-1;
	private int initY=-1;

	public int getInitWidth() {
		return initWidth;
	}

	public void setInitWidth(int initWidth) {
		this.initWidth = initWidth;
	}

	public int getInitHeight() {
		return initHeight;
	}

	public void setInitHeight(int initHeight) {
		this.initHeight = initHeight;
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

	public Image getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(Image titleImage) {
		this.titleImage = titleImage;
	}

	public Image getWinTitleImage() {
		return winTitleImage;
	}

	public void setWinTitleImage(Image winTitleImage) {
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
