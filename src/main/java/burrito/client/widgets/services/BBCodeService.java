package burrito.client.widgets.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bbCodeService")
public interface BBCodeService extends RemoteService {

	String generateBBCodePreview(String bbcode);
	
}
