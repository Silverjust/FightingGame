package shared;

import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PFont;
import game.GameDrawer;

public class ref {
	public static PApplet app;
	public static Updater updater;
	public static PreGame preGame;
	public static Loader loader;
	public static float textScale;
	public static PFont font;

	public static GameDrawer drawer;
	public static Player player;
	public static Minim minim;

	/**
	 * @param app
	 *            the app to set
	 */
	public static void setApp(PApplet app) {
		ref.app = app;
	}

	/**
	 * @param updater
	 *            the updater to set
	 */
	public static void setUpdater(Updater updater) {
		ref.updater = updater;
	}

	/**
	 * @param drawer
	 *            the drawer to set
	 */
	public static void setDrawer(GameDrawer drawer) {
		ref.drawer = drawer;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public static void setPlayer(Player player) {
		ref.player = player;
	}

	/**
	 * @param preGame
	 *            the preGame to set
	 */
	public static void setPreGame(PreGame preGame) {
		ref.preGame = preGame;
	}

	/**
	 * @param textScale
	 *            the textScale to set
	 */
	public static void setTextScale(float textScale) {
		ref.textScale = textScale;
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public static void setFont(PFont font) {
		ref.font = font;
	}

	/**
	 * @param minim the minim to set
	 */
	public static void setMinim(Minim minim) {
		ref.minim = minim;
	}

}
