package game;

import java.util.HashMap;

import preGame.ClientHandler;
import shared.Coms;
import shared.User;

public class PreGameInfo {

	public String map;
	public HashMap<String, User> users = new HashMap<String, User>();

	public static boolean singlePlayer = true;
	public static boolean sandbox;
	public static boolean campain;
	public static boolean againstAI;
	private GameBaseApp app;

	public static PreGameInfo createDummyPrGaIn(GameApp app) {
		PreGameInfo pgi = new PreGameInfo(app);

		pgi.app.setClientHandler(new ClientHandler(app, ""));
		User u = new User(app, app.getClientHandler().identification, "you");
		pgi.addUser(u);
		User u2 = new User(app, "2", "enemy");
		pgi.addUser(u2);
		pgi.map = "maps/alpha_YangDesert/alpha_YangDesert";

		app.getClientHandler().send(Coms.LOAD + "");

		return pgi;
	}

	public void addUser(User u) {
		if (users.get(u.ip) == null)
			users.put(u.ip, u);
		else
			users.get(u.ip).name = u.name;
	}

	public static PreGameInfo createNewPrGaIn(GameBaseApp app) {
		PreGameInfo pgi = new PreGameInfo(app);
		return pgi;
	}

	private PreGameInfo(GameBaseApp app) {
		this.app = app;
	}

	public static boolean isSinglePlayer() {
		return singlePlayer;
	}

	public static void setSinglePlayer(boolean singlePlayer) {
		PreGameInfo.singlePlayer = singlePlayer;
	}

	public static boolean isSandbox() {
		return sandbox;
	}

	public static void setSandbox(boolean sandbox) {
		PreGameInfo.sandbox = sandbox;
	}

	public static boolean isCampain() {
		return campain;
	}

	public static void setCampain(boolean campain) {
		PreGameInfo.campain = campain;
	}

	public static boolean isAgainstAI() {
		return againstAI;
	}

	public static void setAgainstAI(boolean againstAI) {
		PreGameInfo.againstAI = againstAI;
	}

	public User getUser(String string) {
		if (string.equals(""))// returns this user
			return users.get(app.getClientHandler().identification);
		return users.get(string);
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

}
