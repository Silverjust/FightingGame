package shared;

import g4p_controls.GButton;
import g4p_controls.GEvent;
import game.GameApplet;

public abstract class Menu {

	protected GameApplet app;

	public Menu(GameApplet app) {
		this.app = app;
	}

	public void handleButtonEvents(GButton button, GEvent event) {
	}

	public abstract void dispose();
}
