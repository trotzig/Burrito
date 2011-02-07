package burrito.client.crud.widgets;

import com.google.gwt.i18n.client.Messages;

public interface LinkedEntityJsonMessages extends Messages {

	String createJsonForType(String linkText, String typeValue, Long typeId);

	String createJsonForAbsoluteLink(String linkText, String typeAbsoluteUrl,
			String strUrl);

	
	
}
