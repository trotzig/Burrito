package burrito.controller;

import taco.Controller;
import burrito.services.SiteletProperties;

public class RefreshSiteletController implements Controller<SiteletProperties> {

	private Long siteletPropertiesId;
	
	@Override
	public SiteletProperties execute() {
		SiteletProperties props = SiteletProperties.get(siteletPropertiesId);
		return props;
	}

	public Long getSiteletPropertiesId() {
		return siteletPropertiesId;
	}

	public void setSiteletPropertiesId(Long siteletPropertiesId) {
		this.siteletPropertiesId = siteletPropertiesId;
	}
	
	

}
