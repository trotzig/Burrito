package burrito.controller;

import taco.Controller;
import burrito.services.SiteletProperties;

public class RefreshSiteletController implements Controller<SiteletProperties> {

	private Long siteletPropertiesId;

	private Boolean force;

	public Boolean getForce() {
		return force;
	}

	public void setForce(Boolean force) {
		this.force = force;
	}

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
