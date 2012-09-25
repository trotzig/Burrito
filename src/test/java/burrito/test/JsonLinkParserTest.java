package burrito.test;

import junit.framework.Assert;

import org.junit.Test;

import burrito.links.JsonLinkParser;
import burrito.links.Link;
import burrito.test.crud.LinkableEntity;

public class JsonLinkParserTest extends TestBase {

	@Test
	public void testUrl() {
		String value = "/test";
		Link link = new JsonLinkParser().parse(value);

		Assert.assertNotNull(link);
		Assert.assertEquals("/test", link.getUrl());
		Assert.assertEquals("/test", link.getText());
		Assert.assertEquals(Long.valueOf(-1L), link.getId());
		Assert.assertEquals("link_absolute", link.getTypeName());
	}

	@Test
	public void testUrlLink() {
		String value = "{\"typeClassName\": \"link_absolute\", \"typeId\": -1, \"absoluteUrl\": \"/test\", \"linkText\": \"Test\"}";
		Link link = new JsonLinkParser().parse(value);

		Assert.assertNotNull(link);
		Assert.assertEquals("/test", link.getUrl());
		Assert.assertEquals("Test", link.getText());
		Assert.assertEquals(Long.valueOf(-1L), link.getId());
		Assert.assertEquals("link_absolute", link.getTypeName());
	}

	@Test
	public void testEntityLink() {
		LinkableEntity entity = new LinkableEntity();
		entity.setUrl("/test");
		entity.insert();

		String value = "{\"typeClassName\": \"" + LinkableEntity.class.getName() + "\", \"typeId\": " + entity.getId() + ", \"absoluteUrl\": null, \"linkText\": \"Test\"}";
		Link link = new JsonLinkParser().parse(value);

		Assert.assertNotNull(link);
		Assert.assertEquals("/test", link.getUrl());
		Assert.assertEquals("Test", link.getText());
		Assert.assertEquals(entity.getId(), link.getId());
		Assert.assertEquals(LinkableEntity.class.getName(), link.getTypeName());
	}

	@Test
	public void testNull() {
		String value = null;
		Link link = new JsonLinkParser().parse(value);

		Assert.assertNull(link);
	}

	@Test
	public void testMissingClass() {
		String value = "{\"typeClassName\": \"burrito.some.non.Existent.class\", \"typeId\": 0, \"absoluteUrl\": null, \"linkText\": \"Test\"}";
		Link link = new JsonLinkParser().parse(value);

		Assert.assertNull(link);
	}

	@Test
	public void testMissingEntityLink() {
		String value = "{\"typeClassName\": \"" + LinkableEntity.class.getName() + "\", \"typeId\": 123, \"absoluteUrl\": null, \"linkText\": \"Test\"}";
		Link link = new JsonLinkParser().parse(value);

		Assert.assertNull(link);
	}
}
