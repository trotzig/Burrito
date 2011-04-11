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

package burrito.render;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import taco.Controller;
import taco.Renderer;
import burrito.controller.RefreshSiteletController;
import burrito.services.SiteletProperties;
import burrito.sitelet.AutoRefresh;
import burrito.sitelet.Sitelet;
import burrito.util.Cache;
import burrito.util.SiteletHelper;

public class RefreshSiteletRenderer implements Renderer {

	public class CharResponseWrapper extends
	   HttpServletResponseWrapper {
	   private CharArrayWriter output;
	   public String toString() {
	      return output.toString();
	   }
	   public CharResponseWrapper(HttpServletResponse response){
	      super(response);
	      output = new CharArrayWriter();
	   }
	   public PrintWriter getWriter(){
	      return new PrintWriter(output);
	   }
	}
	
	@Override
	public void render(Object result, Controller<?> controller,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SiteletProperties props = (SiteletProperties) result;
		Sitelet sitelet = props.getAssociatedSitelet();
		CharResponseWrapper recordingResponse = new CharResponseWrapper(response);
		request.setAttribute("sitelet", sitelet);
		request.setAttribute("doRecache", true);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/sitelets/" + sitelet.getClass().getSimpleName() + "/render.jsp");
		dispatcher.forward(request, recordingResponse);
		AutoRefresh autoRefresh = sitelet.getNextAutoRefresh();
		props.setNextAutoRefresh(autoRefresh != null ? autoRefresh.getTime() : null);
		
		String newHTML = recordingResponse.toString();
		Boolean force = ((RefreshSiteletController) controller).getForce();

		if ((force != null && force) || !newHTML.equals(props.getRenderedHtml())) {
			props.setRenderedHtml(newHTML);

			Integer version = props.getRenderedVersion();
			if (version != null) version++;
			else version = 1;
			props.setRenderedVersion(version);

			props.broadcastUpdate();
			Cache.delete(SiteletHelper.CACHE_PREFIX + props.containerId);
		}

		props.update();
	}

}
