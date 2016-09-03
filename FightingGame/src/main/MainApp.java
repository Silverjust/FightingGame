package main;

import g4p_controls.G4P;
import game.ClientHandler;
import game.GameBaseApp;
import game.GameDrawer;
import game.HUD;

import javax.swing.JFrame;

import main.appdata.ProfileHandler;
import main.appdata.SettingHandler;
import ddf.minim.Minim;
import main.preGame.PreGameMenu;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import shared.Client;
import shared.Coms;
import shared.ContentListManager;
import shared.Menu;
import shared.Mode;
import shared.VersionControle;

@SuppressWarnings("serial")
public class MainApp extends PApplet {
	public static void main(String args[]) {
		boolean fullscreen = false;
		fullscreen = true;
		if (fullscreen) {
			PApplet.main(new String[] { "--present", "main.MainApp" });
		} else {
			PApplet.main(new String[] { "main.MainApp" });
		}
	}

	PFont font;

	public Mode mode;

	public StartPage startPage;

	public Menu menu;

	public ContentListManager contentListHandler;

	public void setup() {
		size(displayWidth, displayHeight, P2D);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setResizable(true);
		frame.setTitle("Battle of Orion");
		frame.addWindowListener(new Listener());
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// frame.setVisible(true);
		frameRate(60);
		font = createFont("Aharoni Fett", 40);
		GameBaseApp.setTextScale(0.5F);// so ungef‰r
		GameBaseApp.setFont(font);
		// System.out.println(font.ascent());
		textFont(font);
		noSmooth();
		mode = Mode.STARTSCREEN;
		G4P.messagesEnabled(false);

		GameBaseApp.setApp(this);
		FrameInfo.setup();
		GameBaseApp.setMinim(new Minim(this));

		SettingHandler.setup();
		startPage = new StartPage();
		VersionControle.versionControle();
	}

	public void draw() {
		switch (mode) {
		case HAUPTLADESCREEN:
			break;
		case STARTSCREEN:
			startPage.update();
			break;
		case PREGAME:
			GameBaseApp.getPreGameInfo().update();
			break;
		case LADESCREEN:
			GameBaseApp.loader.update();
			break;
		case GAME:
			// if (frameCount % 100 == 0)// DEBUG
			// CommandHandler.executeCommands("/fps");
			// HUD.chatPrintln("fps", ""+frameRate);
			GameBaseApp.updater.update();
			GameDrawer.update();

			break;

		default:
			break;
		}
		// text("ccccccccccccccc", 100, 100);// habs gefunden, war zum test

	}

	@Override
	public void keyPressed() {
		if (mode != Mode.GAME && key == SettingHandler.getSetting().togglePause) {
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
				GameBaseApp.updater.sendDirect(Coms.PAUSE + " true");
			if (startPage != null)
				startPage.dispose();
			if (GameBaseApp.getPreGameInfo() != null)
				GameBaseApp.getPreGameInfo().dispose();
			ProfileHandler.dispose();
			HUD.dispose();
			// TODO close all ingame sounds
			if (GameBaseApp.minim != null)
				GameBaseApp.minim.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setVisible(false);
		super.dispose();
	}

}
