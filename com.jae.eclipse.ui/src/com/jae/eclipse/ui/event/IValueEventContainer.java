package com.jae.eclipse.ui.event;

public interface IValueEventContainer {

	public void addValuechangeListener(IValuechangeListener listener);

	public void removeValuechangeListener(IValuechangeListener listener);

	public void clearValuechangeListeners();

	public void fireValuechanged(ValueChangeEvent event);

	public IValuechangeListener[] getValuechangeListeners();
}