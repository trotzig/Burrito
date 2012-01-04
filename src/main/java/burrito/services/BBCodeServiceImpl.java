package burrito.services;

import burrito.client.widgets.services.BBCodeService;
import burrito.util.BBCodeCreator;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class BBCodeServiceImpl extends RemoteServiceServlet implements BBCodeService {

	@Override
	public String generateBBCodePreview(String bbcode) {
		String html = BBCodeCreator.generateHTML(bbcode);
		return html;
	}

}
