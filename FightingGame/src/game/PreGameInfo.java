package game;

import java.util.HashMap;

import main.preGame.MainPreGame.GameSettings;
import shared.Coms;
import shared.User;

public class PreGameInfo {

	public String map;
	public HashMap<String, User> users;

	public PreGameInfo(GameApp app) {
		app.clientHandler = new ClientHandler(app, "");
		User u = new User(app, app.clientHandler.identification, "you");
		users.put(app.clientHandler.identification, u);
		User u2 = new User(app, "2", "enemy");
		users.put("2", u2);

		map = "maps/alpha_YangDesert/alpha_YangDesert";

		app.clientHandler.send(Coms.LOAD + "");

	}

	public static boolean isSinglePlayer() {
		return GameSettings.singlePlayer;
	}

	public static void setSinglePlayer(boolean singlePlayer) {
		GameSettings.singlePlayer = singlePlayer;
	}

	public static boolean isSandbox() {
		return GameSettings.sandbox;
	}

	public static void setSandbox(boolean sandbox) {
		GameSettings.sandbox = sandbox;
	}

	public static boolean isCampain() {
		return GameSettings.campain;
	}

	public static void setCampain(boolean campain) {
		GameSettings.campain = campain;
	}

	public static boolean isAgainstAI() {
		return GameSettings.againstAI;
	}

	public static void setAgainstAI(boolean againstAI) {
		GameSettings.againstAI = againstAI;
	}

}
