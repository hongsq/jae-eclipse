package com.jae.eclipse.navigator.jaeapp.model;
/**
 * @author hongshuiqiao
 *
 * @param <E>
 */
public interface IJDElement extends INameElement, IDescriptionElement, IImageElement {
	
	public IJDElement getParent();
	
	public IJDElement[] getChildren();
	
	public <T extends IJDElement> T[] getChildren(Class<T> childType);
	
	public void addChild(IJDElement child);
	
	public void removeChild(IJDElement child);
	
	public void clearChildren();
}