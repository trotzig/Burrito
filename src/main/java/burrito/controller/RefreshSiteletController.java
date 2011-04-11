/**
 * Copyright 2011 Henric Persson (henric.persson@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
