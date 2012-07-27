package burrito.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import siena.Model;

public class EntityUtil {

	public static List<Method> getMethods(Class<?> clazz) {
		List<Method> methods = new ArrayList<Method>();
		while(clazz != Model.class) {
			methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
			clazz = clazz.getSuperclass();
			if (clazz == null) return methods;
		}
		return methods;
	}

	public static List<Field> getFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		while(clazz != Model.class) {
			fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
			clazz = clazz.getSuperclass();
			if (clazz == null) return fields;
		}
		return fields;
	}

	public static Field getField(Class<?> clazz, String name) {
		while(clazz != Model.class) {
			try {
				return clazz.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				//expected
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	public static List<Class<?>> getInterfaces(Class<?> clazz) {
		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		while(clazz != Model.class) {
			interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
			clazz = clazz.getSuperclass();
			if (clazz == null) return interfaces;
		}
		return interfaces;
	}
	
	public static List<Class<?>> getParentClasses(Class<?> clazz) {
		List<Class<?>> parentClasses = new ArrayList<Class<?>>();
		while(clazz != Model.class) {
			parentClasses.add(clazz.getSuperclass());
			clazz = clazz.getSuperclass();
			if (clazz == null) return parentClasses;
		}
		return parentClasses;
	}
	
	@SuppressWarnings("unchecked")
	public static Class<? extends Model> getClazz(String entityName) {
		// Attempts to get the class from the entity name.
		Class<? extends Model> clazz;
		try {
			clazz = (Class<? extends Model>) Class.forName(entityName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class not found: " + entityName, e);
		}
		if (!classIsSubclassOfModel(clazz)) {
		
			throw new RuntimeException(
					"Class must be subclass of siena.Model. The specified class is not: "
							+ clazz.getName());
		}
		return clazz;
	}
	
	private static boolean classIsSubclassOfModel(Class<?> clazz) {
		if (clazz.equals(Model.class)) {
			return true;
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null) {
			return false;
		}
		return classIsSubclassOfModel(superClass);
		
	}
}
