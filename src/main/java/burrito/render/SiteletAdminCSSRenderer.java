package burrito.render;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvcaur.Controller;
import mvcaur.Renderer;

public class SiteletAdminCSSRenderer implements Renderer {

	@Override
	public void render(Object obj, Controller<?> ctrl, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/css");
		resp.setCharacterEncoding("UTF-8");

		InputStream in = this.getClass()
				.getResourceAsStream("/burrito/siteletadmin.css");
		byte[] b = new byte[1024];
		int read;
		while ((read = in.read(b)) != -1) {
			resp.getOutputStream().write(b, 0, read);
		}
	}

}
