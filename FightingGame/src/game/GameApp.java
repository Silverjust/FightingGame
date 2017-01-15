package game;

import javax.swing.JFrame;

import ddf.minim.Minim;
import g4p_controls.G4P;
import main.Listener;
import main.appdata.SettingHandler;
import main.preGame.PreGameMenu;
import processing.core.PConstants;
import processing.core.PFont;
import shared.Client;
import shared.Coms;
import shared.ContentListManager;
import shared.GameBaseApp;
import shared.Global;
import shared.Mode;
import shared.PreGameInfo;
import shared.Updater.Time;
import shared.VersionControle;

@SuppressWarnings("serial")
public class GameApp extends GameBaseApp {

	/*
	 * public static void main(String args[]) { boolean fullscreen = false;
	 * fullscreen = true; if (fullscreen) { PApplet.main(new String[] {
	 * "--present", "game.GameApp" }); } else { PApplet.main(new String[] {
	 * "game.GameApp" }); } }
	 */

	PFont font;
	SettingHandler settingHandler;

	public void setup() {
		System.out.println("GameApp.setup() creating PApplet");
		size(displayWidth, displayHeight, P2D);
		System.out.println("GameApp.setup() setup the rest");
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setResizable(true);
		frame.setTitle("FigthingGame");
		frame.addWindowListener(new Listener(this));
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// frame.setVisible(true);
		Global.addApp(this);

		// font = createFont("BrowalliaNew-Bold", 40);
		font = createFont("DejaVuSans-Bold", 40);
		setTextScale(0.5F);// so ungef‰r
		setFont(font);
		System.out.println(font.ascent());
		textFont(font);

		noSmooth();
		G4P.messagesEnabled(false);

		setMinim(new Minim(this));

		settingHandler = new SettingHandler(this);
		VersionControle.setup(this);
		VersionControle.versionControle();

		Time.setup(this);
		setContentListManager(new ContentListManager(this));
		getContentListManager().load();
		setComHandler(new GameComHandler(this));

		setPreGameInfo(new PreGameInfo(this));
		setClientHandler(new GameClientHandler(this, args[0]));
		System.out.println("GameApp.setup()" + getPreGameInfo());
		setMode(Mode.PREGAME);
	}

	public void draw() {
		switch (mode) {
		case PREGAME:
			break;
		case LADESCREEN:
			loader.update();
			break;
		case GAME:
			// if (frameCount % 100 == 0)// DEBUG
			// CommandHandler.executeCommands("/fps");
			// HUD.chatPrintln("fps", ""+frameRate);
			getUpdater().update();
			getDrawer().update();

			break;

		default:
			break;
		}
		// text("ccccccccccccccc", 100, 100);// habs gefunden, war zum test

	}

	@Override
	public void write(String name, String text) {
		/*
		 * if (((MainApp) ref.app).mode == Mode.PREGAME)
		 * display.chat.println(name, completeText); else
		 */
		if (mode == Mode.GAME) {
			getDrawer().getHud().chat.println(name, text);
		}
	}

	@Override
	public void keyPressed() {
		if (mode != Mode.GAME && key == settingHandler.getSetting().togglePause) {
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
		getClientHandler().clientEvent(someClient);
	}

	@Override
	public void dispose() {// Player in schlieﬂen
		try {
			if (getClientHandler().client != null)
				getUpdater().send(Coms.PAUSE + " true");
			// ProfileHandler.dispose();
			getDrawer().getHud().dispose();
			// TODO close all ingame sounds
			if (minim != null)
				minim.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setVisible(false);
		super.dispose();
	}

	@Override
	public boolean isServer() {
		// TODO Auto-generated method stub
		return false;
	}
}
