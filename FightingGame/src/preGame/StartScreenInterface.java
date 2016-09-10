package preGame;

import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GCScheme;
import g4p_controls.GEvent;
import g4p_controls.GTextField;
import processing.core.PApplet;
import shared.Helper;
import shared.Mode;
import shared.PreGameInfo;

public class StartScreenInterface {
	PreGameApp app;

	private GTextField nameField;
	private GButton hostGameButton;
	private GTextField ipField;
	private GButton connectButton;

	private boolean isWaiting;

	public String name;

	public StartScreenInterface(PreGameApp app) {
		this.app = app;
		G4P.setGlobalColorScheme(GCScheme.BLUE_SCHEME);
		nameField = new GTextField(app, 50, 50, 100, 30, G4P.SCROLLBARS_NONE);
		nameField.setText("jojo");

		hostGameButton = new GButton(app, 50, 100, 100, 30, "host Game");
		hostGameButton.addEventHandler(this, "handleButtonEvents");

		ipField = new GTextField(app, 50, 150, 100, 30, G4P.SCROLLBARS_NONE);
		ipField.setPromptText("ip");
		// ipField.setDefaultText("ip");

		connectButton = new GButton(app, 50, 200, 100, 30, "connect");
		connectButton.addEventHandler(this, "handleButtonEvents");
		System.out.println("StartScreenInterface.StartScreenInterface()");
	}

	void update() {
		app.background(50);
		if (isWaiting) {
			app.noFill();
			app.strokeWeight(20);
			float angle = app.millis() / 100.0f;
			app.arc(200, 150, 100, 100, angle, angle + 5);
		}
	}

	public void handleButtonEvents(GButton button, GEvent event) {
		if (button == hostGameButton) {
			setWaiting(true);
			preparePregame();
			PApplet.main(new String[] { "server.ServerApp", app.getAppNumber() + "" });
		}
		if (button == connectButton) {
			if (ipField.getText().equals("") || ipField.getText().equals(" ")) {
				ipField.setFocus(true);
			} else {
				setWaiting(true);
				preparePregame();
				String ip = Helper.secureInput(ipField.getText());
				app.setClientHandler(new PreGameClientHandler(app, ip));
				setWaiting(false);
				if (app.getClientHandler().wasSuccesfull()) {
					dispose();
					app.setMode(Mode.PREGAME);
				}
			}
		}
	}

	void preparePregame() {
		PreGameInfo pgi = new PreGameInfo(app);
		app.setPreGameInfo(pgi);
		name = nameField.getText();
	}

	void setWaiting(boolean b) {
		isWaiting = b;
		nameField.setEnabled(!b);
		hostGameButton.setEnabled(!b);
		ipField.setEnabled(!b);
		connectButton.setEnabled(!b);
	}

	public void initDummy() {
		nameField.setText("dummy");
		ipField.setText("127.0.0.1");
		// handleButtonEvents((GButton) connectButton, null);
	}

	public void dispose() {
		nameField.dispose();
		hostGameButton.dispose();
		ipField.dispose();
		connectButton.dispose();
		System.out.println("StartScreenInterface.dispose()");
	}

}
