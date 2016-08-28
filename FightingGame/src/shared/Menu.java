package shared;

import g4p_controls.GButton;
import g4p_controls.GEvent;
import game.GameBaseApp;

public abstract class Menu {

	protected GameBaseApp app;

	public Menu(GameBaseApp app) {
		this.app = app;
	}

	public void handleButtonEvents(GButton button, GEvent event) {
	}

	public abstract void dispose();
}
