package burrito.services;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.EnumIndexedListField;
import burrito.test.TestBase;
import burrito.test.crud.IndexedByEnumEntity;
import burrito.util.EntityUtil;

public class CrudServiceImplTest extends TestBase{
	
	@Test
	public void getIndexedByEnumFieldFromCrudEntity() throws Exception {
		
		IndexedByEnumEntity entity = new IndexedByEnumEntity();
		Field field = EntityUtil.getField(IndexedByEnumEntity.class, "indexString");
		field.setAccessible(true);
		
		CrudServiceImpl crudServiceImpl = new CrudServiceImpl();
		CrudField crudField = crudServiceImpl.processStandardCrud(field, entity);
		
		Assert.assertTrue(crudField instanceof EnumIndexedListField);
		
		EnumIndexedListField enumIndexed = (EnumIndexedListField) crudField;
		Assert.assertEquals("java.lang.String", enumIndexed.getListClassName());
	}

}
