package preGame;

import g4p_controls.GButton;
import g4p_controls.GEvent;

public class PreGameInterface {

	private PreGameApp app;
	private GButton startButton;

	public PreGameInterface(PreGameApp app) {
		this.app = app;

		startButton = new GButton(app, 850, 550, 100, 30, "start Game");
		startButton.addEventHandler(this, "handleButtonEvents");
	}

	void update() {
		app.background(150);
	}

	public void handleButtonEvents(GButton button, GEvent event) {
		if (button == startButton) {

		}
	}
}
