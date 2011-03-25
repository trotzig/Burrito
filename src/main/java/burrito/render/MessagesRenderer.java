package burrito.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import taco.Controller;
import taco.Renderer;

import com.google.gson.Gson;

public class MessagesRenderer implements Renderer {

	@Override
	public void render(Object obj, Controller<?> ctrl,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Gson gson = new Gson();
		resp.setContentType("text/javascript");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		writer.println("var BurritoMessages = {");
		
		@SuppressWarnings("unchecked")
		Map<String, String> res = (Map<String, String>) obj;
		for (Entry<String, String> entry : res.entrySet()) {
			writer.println("\"" + entry.getKey() + "\" : " + gson.toJson(entry.getValue()) +  ",");
		}
		writer.println("\"_dummy\" : \"dummy\""); //prevents syntax error
		writer.println("};");
	}

}
