package shared;

import ddf.minim.Minim;
import game.GameDrawer;
import processing.core.PApplet;
import processing.core.PFont;
import server.ServerApp;
import server.ServerHandler;

@SuppressWarnings("serial")
public class GameBaseApp extends PApplet {
	protected int appNumber;
	protected Menu menu;
	private ContentListManager contentListHandler;
	protected Minim minim;
	protected PFont font;
	protected float textScale;
	protected Loader loader;

	private Updater updater;
	protected Mode mode;
	/** only for client */
	protected GameDrawer gameDrawer;

	/** only for client */
	private Player player;
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
	 * @param drawer
	 *            the drawer to set
	 */
	public void setDrawer(GameDrawer drawer) {
		this.gameDrawer = drawer;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public ContentListManager getContentListManager() {
		return getContentListHandler();
	}

	public void setContentListManager(ContentListManager contentListHandler) {
		this.setContentListHandler(contentListHandler);
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
		if (this instanceof ServerApp) {
			System.out.println("GameBaseApp.getClientHandler() no client in serverApp");
		}
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

	public Updater getUpdater() {
		return updater;
	}

	public void setUpdater(Updater updater) {
		this.updater = updater;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Deprecated
	public ContentListManager getContentListHandler() {
		return contentListHandler;
	}

	@Deprecated
	public void setContentListHandler(ContentListManager contentListHandler) {
		this.contentListHandler = contentListHandler;
	}

	public boolean isServer() {
		return false;
	}

	public void exitLater() {
	delay(500);
	exit();
		
	}

}
