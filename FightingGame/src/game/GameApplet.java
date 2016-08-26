package game;

import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PFont;
import shared.ContentListManager;
import shared.Loader;
import shared.Menu;
import shared.Mode;
import shared.Player;
import shared.PreGame;
import shared.Updater;

@SuppressWarnings("serial")
public class GameApplet extends PApplet {

	protected Menu menu;
	protected ContentListManager contentListHandler;
	public Minim minim;
	public PFont font;
	public float textScale;
	public Loader loader;
	@Deprecated
	public PreGame preGame;
	public Updater updater;
	public Mode mode;
	/** only for client */
	public GameDrawer gameDrawer;

	/** only for client */
	public Player player;
	public PreGameInfo preGameInfo;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	/**
	 * @param minim
	 *            the minim to set
	 */
	public void setMinim(Minim minim) {
		this.minim = minim;
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(PFont font) {
		this.font = font;
	}

	/**
	 * @param textScale
	 *            the textScale to set
	 */
	public void setTextScale(float textScale) {
		this.textScale = textScale;
	}

	/**
	 * @param preGame
	 *            the preGame to set
	 */
	public void setPreGame(PreGame preGame) {
		this.preGame = preGame;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @param drawer
	 *            the drawer to set
	 */
	public void setDrawer(GameDrawer drawer) {
		this.gameDrawer = drawer;
	}

	/**
	 * @param updater
	 *            the updater to set
	 */
	public void setUpdater(Updater updater) {
		this.updater = updater;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public ContentListManager getContentListHandler() {
		return contentListHandler;
	}

	public void setContentListHandler(ContentListManager contentListHandler) {
		this.contentListHandler = contentListHandler;
	}

	public Loader getLoader() {
		return loader;
	}

	public void setLoader(Loader loader) {
		this.loader = loader;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Minim getMinim() {
		return minim;
	}

	public Player getPlayer() {
		return player;
	}

	public GameDrawer getDrawer() {
		return gameDrawer;
	}

	public PFont getPFont() {
		return font;
	}

	public float getTextScale() {
		return textScale;
	}

	public PreGame getPreGame() {
		return preGame;
	}

	public Updater getUpdater() {
		return updater;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	public float getxCenter() {
		return (float) (width / 2.0);
	}

	public float getyCenter() {
		return (float) (height / 2.0);
	}

	public float getScale() {
		return ((width / 1600.0F) < (height / 900.0F)) ? (width / 1600.0F) : (height / 900.0F);
	}

}
