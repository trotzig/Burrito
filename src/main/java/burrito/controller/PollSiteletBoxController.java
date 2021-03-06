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
import burrito.sitelet.SiteletBoxFeedMessage;

public class PollSiteletBoxController implements Controller<SiteletBoxFeedMessage> {

	private String containerId;

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	@Override
	public SiteletBoxFeedMessage execute() {
		return SiteletProperties.getSiteletBoxFeedMessage(containerId, null, true);
	}
}
