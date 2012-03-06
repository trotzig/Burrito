package burrito.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class BBCodeCreatorTest {

	@Test
	public void italic() {
		String bbcode = "[i]text[/i]";
		String html = BBCodeCreator.generateHTML(bbcode);
		
		assertEquals("<span style=\"font-style:italic;\">text</span>", html);
	}
	
	@Test
	public void italicBreakLine() {
		String bbcode = "[i]text \n\r qwe[/i]";
		String html = BBCodeCreator.generateHTML(bbcode);
		
		assertEquals("<span style=\"font-style:italic;\">text <br/> qwe</span>", html);
	}
	
}
