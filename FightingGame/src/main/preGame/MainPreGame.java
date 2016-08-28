package main.preGame;

import main.MainApp;
import game.ClientHandler;
import game.GameBaseApp;
import game.HUD;
import game.MainLoader;
import game.PreGameInfo;
import shared.ComHandler;
import shared.Coms;
import shared.ContentListManager;
import shared.Helper;
import shared.Mode;
import shared.Nation;
import shared.PreGame;
import shared.User;

public class MainPreGame extends PreGame {

	public PreGameDisplay display;

	private String name;

	public MainPreGame(GameBaseApp app, String name) {
		this.name = name;
		app.setContentListHandler(new ContentListManager());
		app.getContentListHandler().load();
		app.setComHandler(new ComHandler(app));
		System.out.println("MainPreGame.MainPreGame()");
	}

	public void closeBecauseServer() {
		users.clear();
		name = null;
	}

	public void setup() {
		System.out.println("MainPreGame.setup()");
		if (PreGameInfo.isSinglePlayer() && !PreGameInfo.isCampain())
			display = new PreGameSandboxDisplay();
		else if (PreGameInfo.isCampain())
			display = new PreGameCampainDisplay();
		else
			display = new PreGameNormalDisplay();
	}

	public void update() {
		if (display != null) {
			display.update();
		}
	}

	@Override
	public void addPlayer(String ip, String name) {
		if (!users.containsKey(ip)) {
			// Player p = Player.createPlayer(ip, name);
			// p.color = ref.app.color(200, 0, 0);// TODO get color setting
			User u = null;
			u = new User(app, ip, name);
			users.put(ip, u);
		}
	}

	public static void addPlayer(String name, Nation nation) {
		System.out.println("MainPreGame.addPlayer()" + GameApplet.GameBaseApp.users.size());
		GameBaseApp.getPreGameInfo().addPlayer("" + 2, name);
		GameApplet.GameBaseApp.users.get("2").nation = nation;
	}

	public void addThisPlayer(String name) {
		User u = new User(app, ClientHandler.identification, name);
		// ref.player = Player.createPlayer(u);
		// p.color = ref.app.color(0, 255, 100);// TODO get color setting
		users.put(ClientHandler.identification, u);

	}

	public void tryStart() {
		for (String key : users.keySet())
			if (users.get(key).nation == null)
				return;
		if (map == null)
			return;
		ClientHandler.send(Coms.LOAD + "");
	}

	public void setupPlayer() {
		if (!PreGameInfo.isSinglePlayer()) {
			System.out.println(PreGameInfo.isSinglePlayer());
			addThisPlayer(name);
		} else if (PreGameInfo.isCampain()) {
			addThisPlayer(name);
		} else {
			addThisPlayer(name);
			addPlayer("" + 2, "n000bBot");
			users.get("2").nation = Nation.ALIENS;
		}
	}

	@Override
	public void setMap(String string) {
		if (display != null)
			display.mapSelect.setMap(string);
	}

	@Override
	public void startLoading() {
		Nation.setNationsToPlayableNations();
		GameBaseApp.loader = new MainLoader();

		display.dispose();

		((MainApp) GameBaseApp.app).mode = Mode.LADESCREEN;
	}

	@Override
	public User getUser(String string) {
		if (string.equals(""))// returns this user
			return users.get(ClientHandler.identification);
		return super.getUser(string);
	}

	@Override
	public void dispose() {
		if (display != null) {
			display.dispose();
		}
	}

	@Override
	public void setActive(boolean b) {
		if (display != null) {
			display.setActive(b);
		}
	}

	public static class GameSettings {
		public static boolean singlePlayer;
		public static boolean sandbox;
		public static boolean campain;
		public static boolean againstAI;

		public static void setupGameSettings() {
			PreGameInfo.setSinglePlayer(false);
			PreGameInfo.setSandbox(false);
			PreGameInfo.setCampain(false);
			PreGameInfo.setAgainstAI(false);
		}
	}

	@Override
	public void write(String ip, String[] text) {
		String name = Helper.ipToName(ip, app);
		String completeText = "";
		for (int i = 2; i < text.length; i++) {// c[0] und c[1] auslassen
			completeText = completeText.concat(" ").concat(text[i]);
		}
		if (((MainApp) GameBaseApp.app).mode == Mode.PREGAME)
			display.chat.println(name, completeText);
		else if (((MainApp) GameBaseApp.app).mode == Mode.GAME)
			HUD.chat.println(name, completeText);
	}
}
