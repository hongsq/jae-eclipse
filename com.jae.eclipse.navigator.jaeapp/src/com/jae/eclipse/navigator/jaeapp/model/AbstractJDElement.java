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
	private String displayName;
	private String description;
	private String imageID;
	private List<IJDElement> children = new ArrayList<IJDElement>();
	private LoadState loadeState = LoadState.NONE;
	private Object loadLock = new Object();

	public AbstractJDElement(IJDElement parent, String name) {
		this.parent = parent;
		this.name = name;
		this.displayName = this.name;//默认显示名称与名称一样
		this.setImageID(this.getClass().getName());
	}
	
	public IJDElement getParent() {
		return this.parent;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
		load();
		
		return this.children.toArray(new IJDElement[this.children.size()]);
	}
	
	public LoadState getLoadState() {
		return this.loadeState;
	}
	
	protected void load(){
		if(this.loadeState != LoadState.LOADED){
			//只要不是加载成功，就都需要在锁中等待
			synchronized (loadLock) {
				if(this.loadeState == LoadState.NONE){
					try {
						this.loadeState = LoadState.LOADING;
						doLoad();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						this.loadeState = LoadState.LOADED;
					}
				}
			}
		}
	}
	
	protected abstract void doLoad();

	public void refresh(){
		synchronized (loadLock) {
			this.loadeState = LoadState.NONE;
			this.children.clear();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IJDElement> T[] getChildren(Class<T> childType){
		load();
		
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
