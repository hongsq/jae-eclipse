/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractJDModel implements INameElement, IDescriptionElement, IImageElement {
	private String name;
	private String description;
	private String imageID;

	public AbstractJDModel() {
		this.setImageID(this.getClass().getName());
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageID() {
		return imageID;
	}

	public void setImageID(String imageID) {
		this.imageID = imageID;
	}
	
}
