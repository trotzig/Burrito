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
