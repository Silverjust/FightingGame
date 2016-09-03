package game;

import ddf.minim.Minim;
import preGame.ClientHandler;
import processing.core.PApplet;
import processing.core.PFont;
import server.ServerHandler;
import shared.ComHandler;
import shared.ContentListManager;
import shared.Loader;
import shared.Menu;
import shared.Mode;
import shared.Player;
import shared.Updater;

@SuppressWarnings("serial")
public class GameBaseApp extends PApplet {
	protected int appNumber;
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
	private ClientHandler clientHandler;
	private ServerHandler serverHandler;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void setAppNumber(int appNumber) {
		this.appNumber = appNumber;
	}

	public int getAppNumber() {
		return appNumber;
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

	public ContentListManager getContentListManager() {
		return contentListHandler;
	}

	public void setContentListManager(ContentListManager contentListHandler) {
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

	public void write(String text) {
		write("", text);
	}

	public void write(String name, String text) {

	}

	public ComHandler getComHandler() {
		if (comHandler == null)
			System.err.println("comHandler not created");
		return comHandler;
	}

	public void setComHandler(ComHandler comHandler) {
		this.comHandler = comHandler;
	}

	public ClientHandler getClientHandler() {
		return clientHandler;
	}

	public void setClientHandler(ClientHandler clientHandler) {
		this.clientHandler = clientHandler;
	}

	public ServerHandler getServerHandler() {
		return serverHandler;
	}

	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

}
