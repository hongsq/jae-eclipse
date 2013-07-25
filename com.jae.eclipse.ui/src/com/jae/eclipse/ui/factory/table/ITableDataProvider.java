/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

/**
 * @author hongshuiqiao
 *
 */
public interface ITableDataProvider extends IStructuredContentProvider {

	public List<?> getInput();
	
	public int indexOf(Object element);
	
	public void sort(Comparator<?> comparator, boolean isASC);

	public int size();
	
	public void add(Object element);
	
	public void insert(int index, Object element);
	
	public void replace(int index, Object element);
	
	public void remove(Object element);
	
	public void remove(int index);
	
	public Object get(int index);

	public void clear();
}
