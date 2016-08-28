package game;

import java.util.HashMap;

import shared.Coms;
import shared.User;

public class PreGameInfo {

	public String map;
	public HashMap<String, User> users = new HashMap<String, User>();

	public static boolean singlePlayer = true;
	public static boolean sandbox;
	public static boolean campain;
	public static boolean againstAI;
	private GameApp app;

	public PreGameInfo(GameApp app) {
		this.app = app;
		app.clientHandler = new ClientHandler(app, "");
		User u = new User(app, app.clientHandler.identification, "you");
		users.put(app.clientHandler.identification, u);
		User u2 = new User(app, "2", "enemy");
		users.put("2", u2);

		map = "maps/alpha_YangDesert/alpha_YangDesert";

		app.clientHandler.send(Coms.LOAD + "");

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
			return users.get(app.clientHandler.identification);
		return users.get(string);
	}

}
