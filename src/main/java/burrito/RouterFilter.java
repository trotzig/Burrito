package burrito;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * An extended version of the default taco filter. Basically, just adds support
 * for signaling that a user is admin
 * 
 * @author henper
 * 
 */
public class RouterFilter extends taco.RouterFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		boolean admin = Configurator.getAdminProtector().allow((HttpServletRequest) req);
		
		req.setAttribute("burrito_userIsAdmin", admin);

		//for backwards compability...:
		req.setAttribute("taco_userIsAdmin", admin);
		
		super.doFilter(req, resp, chain);
	}
	
}
