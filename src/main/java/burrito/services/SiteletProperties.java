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

package burrito.services;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import burrito.Configurator;
import burrito.sitelet.Sitelet;
import burrito.sitelet.SiteletBoxFeedMessage;
import burrito.sitelet.SiteletBoxMemberMessage;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.repackaged.com.google.common.base.CharEscapers;
import com.google.gson.Gson;

public class SiteletProperties extends Model implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(Generator.AUTO_INCREMENT)
	public Long id;

	public Integer order;

	public String containerId;

	public String entityTypeClassName;

	public Long entityId;

	public String renderedHtml;
	
	public String renderedUpdateFunction;

	public Integer renderedVersion;

	public Date nextAutoRefresh;

	public static Query<SiteletProperties> all() {
		return Model.all(SiteletProperties.class);
	}

	public static SiteletProperties get(Long id) {
		return all().filter("id", id).get();
	}

	public static List<SiteletProperties> getByContainerId(String containerId) {
		return all().filter("containerId", containerId).order("order").fetch();
	}

	/**
	 * Gets the key to use when caching content within this sitelet.
	 * 
	 * @return
	 */
	public String getCacheKey() {
		return containerId + id;
	}

	public static SiteletProperties getByEntityId(Long entityId) {
		return all().filter("entityId", entityId).get();
	}

	@SuppressWarnings("unchecked")
	public Sitelet getAssociatedSitelet() {
		try {
			return (Sitelet) Model
					.all((Class<? extends Model>) Class
							.forName(this.entityTypeClassName))
					.filter("id", entityId).get();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unknown class: " + entityTypeClassName,
					e);
		}
	}

	/**
	 * Gets all sitelets that need to be refreshed. I.e. those who have a
	 * nextAutoRefresh date in the past.
	 */
	public static List<SiteletProperties> getSiteletsNeedingRefresh() {
		return all().filter("nextAutoRefresh<", new Date()).fetch();
	}

	public Long getId() {
		return id;
	}

	/**
	 * Sets the pre-rendered html output from this sitelet instance
	 * 
	 * @param renderedHtml
	 */
	public void setRenderedHtml(String renderedHtml) {
		this.renderedHtml = renderedHtml;
	}

	public String getRenderedHtml() {
		return renderedHtml;
	}

	public void setRenderedVersion(Integer renderedVersion) {
		this.renderedVersion = renderedVersion;
	}

	public Integer getRenderedVersion() {
		return renderedVersion;
	}

	public void setNextAutoRefresh(Date nextAutoRefresh) {
		this.nextAutoRefresh = nextAutoRefresh;
	}

	public Date getNextAutoRefresh() {
		return nextAutoRefresh;
	}

	
	public String getRenderedUpdateFunction() {
		return renderedUpdateFunction;
	}

	public void setRenderedUpdateFunction(String renderedUpdateFunction) {
		this.renderedUpdateFunction = renderedUpdateFunction;
	}

	/**
	 * Triggers a job that will rerender the html for this sitelet. Please note
	 * that this refresh is done in the background. You can't be absolutely sure
	 * when the sitelet is done refreshing.
	 */
	public void triggerRefreshAsync() {
		if (id == null) {
			throw new IllegalStateException("The sitelet has no id. Must call insert() first");
		}
		Queue queue = QueueFactory.getQueue("burrito-sitelets");
		queue.add(withUrl("/burrito/sitelets/refresh/sitelet").param("siteletPropertiesId", String.valueOf(id)));
	}

	public static SiteletBoxFeedMessage getSiteletBoxFeedMessage(String containerId, SiteletProperties updatedSitelet, boolean includeAllHtml) {
		List<SiteletBoxMemberMessage> messages = new ArrayList<SiteletBoxMemberMessage>();
		List<SiteletProperties> props = getByContainerId(containerId);
		for (SiteletProperties prop : props) {
			SiteletBoxMemberMessage msg = new SiteletBoxMemberMessage(prop.getId());
			if (updatedSitelet != null && updatedSitelet.getId().longValue() == prop.getId().longValue()) {
				msg.setVersion(updatedSitelet.getRenderedVersion());
				msg.setHtml(updatedSitelet.getRenderedHtml());
				msg.setJs(updatedSitelet.getRenderedUpdateFunction());
			}
			else {
				msg.setVersion(prop.getRenderedVersion());
				if (includeAllHtml) msg.setHtml(prop.getRenderedHtml());
			}
			messages.add(msg);
		}
		SiteletBoxFeedMessage result = new SiteletBoxFeedMessage();
		result.setSitelets(messages);
		return result;
	}

	public void broadcastUpdate() {
		broadcast(this.containerId, this);
	}

	/**
	 * Broadcast a change to this sitelet box
	 * @param siteletProperties
	 */
	public static void broadcast(String containerId, SiteletProperties siteletProperties) {
		String json = new Gson().toJson(getSiteletBoxFeedMessage(containerId, siteletProperties, false));
		new Broadcaster(Configurator.getBroadcastSettings()).broadcast(json, 
				"burrito:sitelet-box:" + CharEscapers.uriEscaper(false).escape(Configurator.getSiteIdentifier()) + 
				"|" + CharEscapers.uriEscaper(false).escape(containerId), null);
	}

	/**
	 * Returns a description of the content of 
	 * @return
	 */
	public String describe() {
		return getAssociatedSitelet().describe();
	}
	
	
	

}
