package burrito.test;

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
	
}
