package burrito.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import burrito.util.StringUtils;

public class StringUtilsTest {

	@Test
	public void testSubstringBefore() {
		String res = StringUtils.substringBefore("asdasd=qweqwe", "=");
		assertEquals("asdasd", res);
	}
	
	@Test
	public void testSubstringAfter() throws Exception {
		String res = StringUtils.substringAfter("asdasd=qweqwe", "=");
		assertEquals("qweqwe", res);
	}
	
	@Test
	public void testSubstringAfterAll() throws Exception {
		String res = StringUtils.substringAfter("asdasd=qweqwe", "asdasd=");
		assertEquals("qweqwe", res);
	}
	
	@Test
	public void testStripBBCode() {
		String test = "[b]Bold text[/b], [url=http://www.google.com]link[/url], no styling";
		assertEquals("Bold text, link, no styling", StringUtils.stripBBCode(test));
	}
	
}
