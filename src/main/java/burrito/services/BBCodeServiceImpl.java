package burrito.services;

import burrito.client.widgets.services.BBCodeService;
import burrito.util.Cache;
import burrito.util.StringUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BBCodeServiceImpl extends RemoteServiceServlet implements BBCodeService {

	@Override
	public String generateBBCodePreview(String bbcode) {
		String key = "bbcode:" + StringUtils.generateRandomString(10);
		
		Cache.put(key, bbcode);
		
		return key;
	}

}
