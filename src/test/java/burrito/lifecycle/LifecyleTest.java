package burrito.lifecycle;

import junit.framework.Assert;

import org.junit.Test;

import burrito.test.TestBase;
import burrito.test.crud.SearchTestEntity;

public class LifecyleTest extends TestBase {

	
	@Test
	public void lifecycleSupportForMultipleEntities() {
		
		EntityInOtherPackage one = new EntityInOtherPackage();
		one.save();
		
		SearchTestEntity two = new SearchTestEntity();
		two.save();
		
		Assert.assertNotNull(one.getLastModified());
		Assert.assertNotNull(two.getLastModified());
		
		
	}
	
}
