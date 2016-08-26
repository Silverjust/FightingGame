package game;

import javax.swing.JFrame;

import ddf.minim.Minim;
import g4p_controls.G4P;
import main.ClientHandler;
import main.Listener;
import main.appdata.ProfileHandler;
import main.appdata.SettingHandler;
import main.preGame.PreGameMenu;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import shared.Client;
import shared.Coms;
import shared.Mode;
import shared.VersionControle;

@SuppressWarnings("serial")
public class GameApp extends GameApplet {
	public static void main(String args[]) {
		boolean fullscreen = false;
		fullscreen = true;
		if (fullscreen) {
			PApplet.main(new String[] { "--present", "game.GameApp" });
		} else {
			PApplet.main(new String[] { "game.GameApp" });
		}
	}

	PFont font;

	public void setup() {
		size(displayWidth, displayHeight, P2D);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setResizable(true);
		frame.setTitle("FigthingGame");
		frame.addWindowListener(new Listener());
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// frame.setVisible(true);
		frameRate(60);
		font = createFont("Aharoni Fett", 40);
		setTextScale(0.5F);// so ungef‰r
		setFont(font);
		// System.out.println(font.ascent());
		textFont(font);
		noSmooth();
		G4P.messagesEnabled(false);

		setMinim(new Minim(this));

		SettingHandler.setup();
		VersionControle.versionControle();
	}

	public void draw() {
		switch (mode) {
		case HAUPTLADESCREEN:
			break;
		case PREGAME:
			break;
		case LADESCREEN:
			loader.update();
			break;
		case GAME:
			// if (frameCount % 100 == 0)// DEBUG
			// CommandHandler.executeCommands("/fps");
			// HUD.chatPrintln("fps", ""+frameRate);
			updater.update();
			getDrawer().update();

			break;

		default:
			break;
		}
		// text("ccccccccccccccc", 100, 100);// habs gefunden, war zum test

	}

	@Override
	public void keyPressed() {
		if (mode != Mode.GAME && key == SettingHandler.setting.togglePause) {
			if (menu == null) {
				menu = new PreGameMenu();
			} else {
				menu.dispose();
				menu = null;
			}
		}
		if (mode != Mode.GAME && key == PConstants.ESC) {
			key = 0;
		}
	}

	public void clientEvent(Client someClient) {
		ClientHandler.clientEvent(someClient);
	}

	@Override
	public void dispose() {// Player in schlieﬂen
		try {
			if (ClientHandler.client != null)
				updater.send(Coms.PAUSE + " true");
			ProfileHandler.dispose();
			HUD.dispose();
			// TODO close all ingame sounds
			if (minim != null)
				minim.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setVisible(false);
		super.dispose();
	}

}
