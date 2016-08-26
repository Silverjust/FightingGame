package game;

import ddf.minim.AudioPlayer;
import ddf.minim.Controller;
import g4p_controls.G4P;
import g4p_controls.GCScheme;
import processing.core.PImage;
import shared.Menu;
//import shared.Menu;
import shared.ref;

public class HUD {

	static PImage keritImg, paxImg, arcImg, prunImg;
	static PImage overlay;
	public static int height = 200;

	public static AudioPlayer sound;

	public static Menu menue;
	public static Chat chat;
	public static PlayerInterface playerInterface;
	static Minimap minimap;

	public static void loadImages() {
		if (ref.player != null) {
			overlay = ImageHandler.load("hud/", "overlay");
			// sound = ref.minim
			// .loadFile(ref.player.getNation().toString() + "/" +
			// ref.player.getNation().toString() + ".mp3");
			/** soundfiles are only tests and will be removed */
		}
	}

	public static void setup() {
		setupG4P();
		playerInterface = new PlayerInterface();
		chat = new Chat();
		minimap = new Minimap();
		boolean b = false;
		if (b) {
			sound.play();
			sound.setGain(-25);
			// sound.loop();
		}
	}

	static void setupG4P() {
		G4P.setGlobalColorScheme(8);
		G4P.changeCursor(false);
		GCScheme.setScheme(8, 0, ref.app.color(0));
		GCScheme.setScheme(8, 6, ref.app.color(0, 100));
		GCScheme.setScheme(8, 7, ref.app.color(0, 50));
		GCScheme.setScheme(8, 12, ref.app.color(255));
	}

	public static void update() {
		SoundHandler.update();
		ref.app.fill(ref.app.color(255));
		ImageHandler.drawImage(ref.app, overlay, 0, ref.app.height - height, ref.app.width, height);
		// SelectionDisplay.update();
		minimap.update();
		playerInterface.update();
		// GroupHandler.update();
	}

	public static String[] buttonImageFilename() {
		return new String[] { "hud/button_normal.png", "hud/button_mouseover.png", "hud/button_clicked.png" };
	}

	public static void dispose() {
		try {

			GroupHandler.dispose();
			ImageHandler.dispose();
			if (sound != null)
				sound.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
