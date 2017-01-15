package preGame;

import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GCScheme;
import g4p_controls.GEvent;
import g4p_controls.GTextField;
import gameStructure.GameObject;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Helper;
import shared.Helper.Timer;
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

	private Timer dummyInitTimer;

	private PImage krit;

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

		krit = app.loadImage("data/hud/symbols/krit.png");
	}

	void update() {
		app.background(50);

		if (isWaiting) {
			app.noFill();
			app.strokeWeight(20);
			float angle = app.millis() / 100.0f;
			app.arc(200, 150, 100, 100, angle, angle + 5);
		}

		// Helper.text(app, "§img krit§§color 255 0 0§§img krit§", 200, 200);

		app.image(krit, 200, 200);
		app.tint(255, 50, 0);
		app.image(krit, 270, 200);
		app.tint(255);

		{
			app.pushMatrix();
			// app.shearX(app.millis() / 12000.f);
			// app.shearY(app.millis() / 14000.f);

			app.translate(GameObject.xToGrid(400), GameObject.yToGrid(300));
			// app.rotate(app.millis() / 10000.f);
			app.scale(1, 0.5f);
			app.ellipse(0, 0, 20, 20);
			app.translate(app.cos(app.millis() / 5000.f) * 10, app.sin(app.millis() / 5000.f) * 5);
			app.scale(1, 0.5f);
			app.ellipse(0, 0, 10, 10);
			app.popMatrix();

			app.pushMatrix();
			// app.shearX(app.millis() / 12000.f);
			// app.shearY(app.millis() / 14000.f);

			app.translate(GameObject.xToGrid(300), GameObject.yToGrid(300));
			app.scale(1, 0.5f);
			app.rotate(app.millis() / 10000.f);

			app.rect(0, 0, 20, 50);
			app.ellipse(10, 0, 10, 10);
			app.popMatrix();
		}
		if (dummyInitTimer != null && dummyInitTimer.isNotOnCooldown()) {
			handleButtonEvents((GButton) connectButton, null);
			dummyInitTimer = null;
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
				System.out.println("StartScreenInterface.handleButtonEvents()***************************************");
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
		dummyInitTimer = new Timer(1000);
		dummyInitTimer.startCooldown();
	}

	public void dispose() {
		nameField.dispose();
		hostGameButton.dispose();
		ipField.dispose();
		connectButton.dispose();
		System.out.println("StartScreenInterface.dispose()");
	}

}
