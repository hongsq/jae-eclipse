/**
 * 
 */
package com.jae.eclipse.navigator.jaeapp.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongshuiqiao
 *
 */
public abstract class AbstractJDElement implements IJDElement {
	private IJDElement parent;
	private String name;
	private String description;
	private String imageID;
	private List<IJDElement> children = new ArrayList<IJDElement>();

	public AbstractJDElement(IJDElement parent, String name) {
		this.parent = parent;
		this.name = name;
		this.setImageID(this.getClass().getName());
	}
	
	@Override
	public IJDElement getParent() {
		return this.parent;
	}
	
	public String getName() {
		return name;
	}

	void setName(String name) {
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
	
	public IJDElement[] getChildren(){
		return this.children.toArray(new IJDElement[this.children.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IJDElement> T[] getChildren(Class<T> childType){
		List<T> list = new ArrayList<T>();
		for (IJDElement child : this.children) {
			if(childType.isAssignableFrom(child.getClass()))
				list.add(childType.cast(child));
		}
		
		int size = list.size();
		T[] result = (T[]) Array.newInstance(childType, size);
		System.arraycopy(list.toArray(), 0, result, 0, size);
		return result;
	}
	
	public void addChild(IJDElement child){
		this.children.add(child);
	}

	public void removeChild(IJDElement child){
		this.children.remove(child);
	}

	public void clearChildren(){
		this.children.clear();
	}
}
