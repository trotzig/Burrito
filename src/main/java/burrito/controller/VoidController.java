package burrito.controller;

import mvcaur.Controller;

public class VoidController implements Controller<Void> {

	@Override
	public Void execute() {
		return null;
	}

}
