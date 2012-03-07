package burrito.services;

import java.lang.reflect.Field;
import java.util.Comparator;

import burrito.util.EntityUtil;

import siena.Model;

public class EntityComparator implements Comparator<Model> {
	
	private String sortKey ="";
	
	public EntityComparator( String sortKey){
		this.sortKey = sortKey;
	}
	
	
	@Override
	public int compare(Model o1, Model o2) {
		Field f1 = EntityUtil.getField(o1.getClass(), sortKey);
		Field f2 = EntityUtil.getField(o2.getClass(), sortKey);
		
		f1.setAccessible(true);
		f2.setAccessible(true);
		
		int ret = 0;
		try {			
			@SuppressWarnings("unchecked")
			Comparable<Object> comp1 = (Comparable<Object>)f1.get(o1);
			@SuppressWarnings("unchecked")
			Comparable<Object> comp2 = (Comparable<Object>)f2.get(o2);
			
			if (comp1 == null) {
				if (comp2 == null) {
					return 0;
				}

				return -1;
			}

			if (comp2 == null) return 1;

			ret = comp1.compareTo(comp2);
			
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Failed to sort table", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to sort table", e);
		}
		return ret;
	}

}
