package burrito.util;

import org.junit.Assert;
import org.junit.Test;

import siena.Model;
import burrito.EntityValidationException;
import burrito.annotations.Required;
import burrito.test.TestBase;
import burrito.util.ValidationUtil;

public class ValidationUtilTest extends TestBase {

	@SuppressWarnings("unused")
	private class DummyModel extends Model {
		public String optionalField;

		@Required
		public String requiredField;
	}

	@Test
	public void testAssertNoMissingRequiredFields() {
		DummyModel entity = new DummyModel();

		try {
			ValidationUtil.assertNoMissingRequiredFields(entity);
			Assert.fail("Should fail on missing field");
		}
		catch (EntityValidationException e) {
			Assert.assertNotNull(e.getMessage());
		}

		entity.optionalField = "Hej";

		try {
			ValidationUtil.assertNoMissingRequiredFields(entity);
			Assert.fail("Should fail on missing field");
		}
		catch (EntityValidationException e) {
			Assert.assertNotNull(e.getMessage());
		}

		entity.requiredField = "världen";

		try {
			ValidationUtil.assertNoMissingRequiredFields(entity);
		}
		catch (EntityValidationException e) {
			Assert.fail();
		}

		entity.optionalField = null;

		try {
			ValidationUtil.assertNoMissingRequiredFields(entity);
		}
		catch (EntityValidationException e) {
			Assert.fail();
		}
	}
}
