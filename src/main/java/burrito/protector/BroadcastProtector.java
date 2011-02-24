package burrito.protector;

import javax.servlet.http.HttpServletRequest;

import burrito.Configurator;

import taco.Protector;

/**
 * Protector for broadcast messages
 * 
 * @author henper
 * 
 */
public class BroadcastProtector implements Protector {

	@Override
	public boolean allow(HttpServletRequest request) {
		String secret = request.getParameter("secret");
		if (secret == null) {
			return false;
		}
		return secret.equals(Configurator.getBroadcastSettings().getSecret());
	}

}
