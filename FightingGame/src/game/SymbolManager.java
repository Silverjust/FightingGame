package game;

import java.util.HashMap;

import processing.core.PImage;
import shared.GameBaseApp;

public class SymbolManager {

	private static HashMap<String, PImage> images = new HashMap<String, PImage>();

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		String path = "hud/symbols/";
		images.put("armor", imageHandler.load(path, "armor"));
		images.put("mr", imageHandler.load(path, "magic-resist"));
		images.put("ms", imageHandler.load(path, "movement-speed"));
		images.put("ad", imageHandler.load(path, "attackdamage"));
		images.put("ap", imageHandler.load(path, "abilitypower"));
		images.put("krit", imageHandler.load(path, "krit"));
	}

	public SymbolManager(GameBaseApp app) {
	}

	public PImage getSymbol(String string) {
		return images.get(string);
	}

}
