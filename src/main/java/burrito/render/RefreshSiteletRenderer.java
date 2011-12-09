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
import java.util.logging.Logger;

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
import burrito.util.SiteletHelper;

public class RefreshSiteletRenderer implements Renderer {

	private static Logger log = Logger.getLogger(RefreshSiteletRenderer.class.getName());
	
	public static class CharResponseWrapper extends
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
		refreshSitelet((SiteletProperties) result, ((RefreshSiteletController) controller).getForce(), request, response);
	}

	public static void refreshSitelet(SiteletProperties props, Boolean force,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Sitelet sitelet = props.getAssociatedSitelet();
		
		String newHTML = renderAndRecordJspOutput("render.jsp", request, response, sitelet);
		String updateJsFunction = null;
		try {
			updateJsFunction = renderAndRecordJspOutput("update.jsp", request, response, sitelet).trim();
		} catch (Exception e) {
			log.info("No update.jsp found for " + sitelet.getClass().getSimpleName());
		}
		
		AutoRefresh autoRefresh = sitelet.getNextAutoRefresh();
		props.setNextAutoRefresh(autoRefresh != null ? autoRefresh.getTime() : null);
		
		if ((force != null && force) || !newHTML.equals(props.getRenderedHtml())) {
			props.setRenderedHtml(newHTML);
			props.setRenderedUpdateFunction(updateJsFunction);

			Integer version = props.getRenderedVersion();
			if (version != null) version++;
			else version = 1;
			props.setRenderedVersion(version);

			props.broadcastUpdate();
			SiteletHelper.clearSiteletContainerCache(props.containerId);
		}

		props.setRetired(false);
		props.update();
	}
	
	public static String renderAndRecordJspOutput(String jspFileName, HttpServletRequest request,
			HttpServletResponse response, Sitelet sitelet) throws ServletException, IOException {
		String path = "/sitelets/" + sitelet.getClass().getSimpleName();
		CharResponseWrapper recordingResponse = new CharResponseWrapper(response);
		request.setAttribute("sitelet", sitelet);
		request.setAttribute("doRecache", true);
		RequestDispatcher dispatcher = request.getRequestDispatcher(path + "/" + jspFileName);
		dispatcher.include(request, recordingResponse);
		
		String newHTML = recordingResponse.toString();
		return newHTML;
	}

}
