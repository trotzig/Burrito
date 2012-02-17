package burrito.test;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import siena.Model;
import siena.PersistenceManager;
import siena.PersistenceManagerFactory;
import siena.core.PersistenceManagerLifeCycleWrapper;
import siena.gae.GaePersistenceManager;
import burrito.client.crud.CrudGenericException;
import burrito.client.crud.CrudService;
import burrito.client.crud.FieldValueNotUniqueException;
import burrito.client.crud.generic.CrudEntityDescription;
import burrito.client.crud.generic.CrudField;
import burrito.client.crud.generic.fields.AdminLinkMethodField;
import burrito.client.crud.generic.fields.DisplayableMethodField;
import burrito.services.CrudServiceImpl;
import burrito.test.crud.ChildEntity;
import burrito.test.crud.GrandParentEntity;
import burrito.test.crud.ParentEntity;

public class CrudTest extends TestBase {

	@Before
	public void enableLifeTimeSupport() {
		PersistenceManager pm = new GaePersistenceManager();
		pm = new PersistenceManagerLifeCycleWrapper(pm);
		pm.init(null);
		PersistenceManagerFactory.install(pm, GrandParentEntity.class);
		PersistenceManagerFactory.install(pm, ParentEntity.class);
		PersistenceManagerFactory.install(pm, ChildEntity.class);
		
	}
	
	@Test
	public void testInheritance() throws FieldValueNotUniqueException, CrudGenericException {
		
		CrudService service = new CrudServiceImpl();
		CrudEntityDescription desc = service.describe(ChildEntity.class.getName(), -1l, null);
		CrudEntityDescription headers = service.getEntityHeaders(ChildEntity.class.getName());
		
		Assert.assertNotNull(desc.getField("childProperty"));
		Assert.assertNotNull(headers.getField("childProperty"));
		Assert.assertNotNull(desc.getField("parentProperty"));
		Assert.assertNull(headers.getField("parentProperty"));
		Assert.assertNotNull(desc.getField("grandParentProperty"));
		Assert.assertNotNull(headers.getField("grandParentProperty"));
		Assert.assertNull(desc.getField("childMethod"));
		Assert.assertNull(desc.getField("parentMethod"));
		Assert.assertNull(desc.getField("grandParentMethod"));
		Assert.assertNotNull(desc.getField("displayableChildMethod"));
		Assert.assertNotNull(headers.getField("displayableChildMethod"));
		Assert.assertNotNull(desc.getField("displayableParentMethod"));
		Assert.assertNotNull(headers.getField("displayableParentMethod"));
		Assert.assertNotNull(desc.getField("displayableGrandParentMethod"));
		Assert.assertNotNull(headers.getField("displayableGrandParentMethod"));
		
		Assert.assertNotNull(desc.getField("adminLinkChildMethod"));
		Assert.assertNotNull(desc.getField("adminLinkParentMethod"));
		Assert.assertNotNull(desc.getField("adminLinkGrandParentMethod"));
		Assert.assertNotNull(headers.getField("adminLinkChildMethod"));
		Assert.assertNotNull(headers.getField("adminLinkParentMethod"));
		Assert.assertNotNull(headers.getField("adminLinkGrandParentMethod"));
		
		cleanDescriptionFromMethods(desc);
		
		service.save(desc, null);
		
		//Verify pre-inserts
		ChildEntity entity = Model.all(ChildEntity.class).get();
		Assert.assertEquals("automatic", entity.getParentProperty());
		Assert.assertEquals(Long.valueOf(123), entity.getGrandParentProperty());
		
		CrudEntityDescription copy = service.describe(ChildEntity.class.getName(), -1l, entity.getId());
		cleanDescriptionFromMethods(copy);
		service.save(copy, entity.getId());
		
		Assert.assertEquals(2, Model.all(ChildEntity.class).fetch().size());
		
		
	}
	
	

	private void cleanDescriptionFromMethods(CrudEntityDescription desc) {
		ArrayList<CrudField> cleaned = new ArrayList<CrudField>();
		for (CrudField field : desc.getFields()) {
			if (field instanceof DisplayableMethodField || field instanceof AdminLinkMethodField) {
				//skip methods
				continue;
			}
			cleaned.add(field);
		}
		desc.setFields(cleaned);
	}
	
	
}
