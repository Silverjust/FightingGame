package game;

import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PFont;
import shared.ComHandler;
import shared.ContentListManager;
import shared.Helper;
import shared.Loader;
import shared.Menu;
import shared.Mode;
import shared.Player;
import shared.Updater;

@SuppressWarnings("serial")
public class GameBaseApp extends PApplet {

	protected Menu menu;
	protected ContentListManager contentListHandler;
	protected Minim minim;
	protected PFont font;
	protected float textScale;
	protected Loader loader;

	protected Updater updater;
	protected Mode mode;
	/** only for client */
	protected GameDrawer gameDrawer;

	/** only for client */
	protected Player player;
	protected PreGameInfo preGameInfo;
	private ComHandler comHandler;
	public ClientHandler clientHandler;

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
		if (gameDrawer == null)
			System.err.println("GameDrawer not created");
		return gameDrawer;
	}

	public PFont getPFont() {
		return font;
	}

	public float getTextScale() {
		return textScale;
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

	public PreGameInfo getPreGameInfo() {
		return preGameInfo;
	}

	public void setPreGameInfo(PreGameInfo preGame) {
		this.preGameInfo = preGame;
	}

	public void write(String ip, String string) {
		write(ip, new String[] { string });
	}

	public void write(String ip, String[] text) {
		String name = Helper.ipToName(ip, this);
		String completeText = "";
		for (int i = 2; i < text.length; i++) {// c[0] und c[1] auslassen
			completeText = completeText.concat(" ").concat(text[i]);
		}
		/*
		 * if (((MainApp) ref.app).mode == Mode.PREGAME)
		 * display.chat.println(name, completeText); else
		 */
		if (mode == Mode.GAME)
			getDrawer().getHud().chat.println(name, completeText);
	}

	public ComHandler getComHandler() {
		if (comHandler == null)
			System.err.println("comHandler not created");
		return comHandler;
	}

	public void setComHandler(ComHandler comHandler) {
		this.comHandler = comHandler;
	}

}
