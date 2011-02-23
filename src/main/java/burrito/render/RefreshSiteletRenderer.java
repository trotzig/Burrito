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
		RequestDispatcher dispatcher = request.getRequestDispatcher("/sitelets/" + sitelet.getClass().getSimpleName() + "/render.jsp");
		dispatcher.forward(request, recordingResponse);
		AutoRefresh autoRefresh = sitelet.getNextAutoRefresh();
		props.setNextAutoRefresh(autoRefresh.getTime());
		
		String newHTML = recordingResponse.toString();
		if (!newHTML.equals(props.getRenderedHtml())) {
			props.setRenderedHtml(newHTML);
			props.broadcastUpdate();
			Cache.delete(SiteletHelper.CACHE_PREFIX + props.containerId);
		}
		props.update();
	}

}
