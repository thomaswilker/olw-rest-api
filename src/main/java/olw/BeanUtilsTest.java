package olw;

import java.lang.reflect.Field;

import org.springframework.core.GenericCollectionTypeResolver;

import olw.model.AbstractEntity;
import olw.model.Material;
import olw.model.annotations.IndexedBy;

public class BeanUtilsTest {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		
		System.out.println(Material.class.getAnnotation(IndexedBy.class).value());
		
		Field field = Material.class.getDeclaredField("collections");
		Class c = GenericCollectionTypeResolver.getCollectionFieldType(field);
		System.out.println(c.isAnnotationPresent(IndexedBy.class));
		System.out.println(AbstractEntity.class.isAssignableFrom(c));
			  
	}

}
