package game;

import ddf.minim.AudioPlayer;
import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GCScheme;
import processing.core.PImage;
import shared.Menu;

public class HUD {

	static PImage keritImg, paxImg, arcImg, prunImg;
	static PImage overlay;
	public int height = 200;

	public AudioPlayer sound;

	public Menu menue;
	public Chat chat;
	public PlayerInterface playerInterface;
	Minimap minimap;
	private GameBaseApp app;
	private GameDrawer gameDrawer;
	private SoundHandler soundHandler;

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		if (app.player != null) {
			overlay = imageHandler.load("hud/", "overlay");
			// sound = ref.minim
			// .loadFile(ref.player.getNation().toString() + "/" +
			// ref.player.getNation().toString() + ".mp3");
			/** soundfiles are only tests and will be removed */
		}
	}

	public HUD(GameBaseApp app, GameDrawer gameDrawer) {
		this.app = app;
		this.gameDrawer = gameDrawer;
		gameDrawer.setHud(this);
		setupG4P();
		playerInterface = new PlayerInterface(app, this);
		chat = new Chat(app);
		minimap = new Minimap(app);
		soundHandler = new SoundHandler(app);
		boolean b = false;
		if (b) {
			sound.play();
			sound.setGain(-25);
			// sound.loop();
		}
	}

	private void setupG4P() {
		new GButton(app, 0, 0, 1, 1).dispose();//init g4p
		G4P.setGlobalColorScheme(8);
		G4P.changeCursor(false);
		GCScheme.setScheme(8, 0, app.color(0));
		GCScheme.setScheme(8, 6, app.color(0, 100));
		GCScheme.setScheme(8, 7, app.color(0, 50));
		GCScheme.setScheme(8, 12, app.color(255));
	}

	public void update() {
		soundHandler.update();
		app.fill(app.color(255));
		gameDrawer.imageHandler.drawImage(app, overlay, 0, app.height - height, app.width, height);
		// SelectionDisplay.update();
		minimap.update();
		playerInterface.update();
		// GroupHandler.update();
	}

	public String[] buttonImageFilename() {
		return new String[] { "hud/button_normal.png", "hud/button_mouseover.png", "hud/button_clicked.png" };
	}

	public void dispose() {
		try {
			if (sound != null)
				sound.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
