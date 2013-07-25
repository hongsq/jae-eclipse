/**
 * 
 */
package com.jae.eclipse.ui.factory.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;

/**
 * @author hongshuiqiao
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ListTableDataProvider implements ITableDataProvider {
	private List list = new ArrayList();

	public ListTableDataProvider() {
		super();
	}

	public void dispose() {
	}

	public synchronized void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		list.clear();
		if(null == newInput)
			return;
		if(newInput.getClass().isArray()){
			list.addAll(Arrays.asList((Object[])newInput));
		}else if(newInput instanceof Collection){
			list.addAll((Collection) newInput);
		}
	}

	public Object[] getElements(Object inputElement) {
		return list.toArray();
	}

	public int indexOf(Object element) {
		return this.list.indexOf(element);
	}

	public List<?> getInput() {
		return this.list;
	}

	public synchronized void sort(Comparator comparator, boolean isASC) {
		if (isASC) {
			Collections.sort(this.list, comparator);
		}
		else {
			Collections.sort(this.list, new ReverseComparator(comparator));
		}
	}

	public int size() {
		return this.list.size();
	}

	public synchronized void add(Object element) {
		this.list.add(element);
	}

	public synchronized void insert(int index, Object element) {
		this.list.add(index, element);
	}

	public synchronized void replace(int index, Object element) {
		this.list.set(index, element);
	}

	public synchronized void remove(Object element) {
		this.list.remove(element);
	}

	public synchronized void remove(int index) {
		this.list.remove(index);
	}

	public Object get(int index) {
		return this.list.get(index);
	}

	public synchronized void clear() {
		list.clear();
	}

}
