package shared;

import java.util.HashMap;

public class PreGameInfo {

	public String map;
	public HashMap<String, User> users = new HashMap<String, User>();

	public static boolean singlePlayer = true;
	public static boolean sandbox;
	public static boolean campain;
	public static boolean againstAI;
	private GameBaseApp app;

	public void addUser(User u) {
		if (users.get(u.getIp()) == null)
			users.put(u.getIp(), u);
		else
			users.get(u.getIp()).name = u.name;
	}

	public PreGameInfo(GameBaseApp app) {
		this.app = app;
		map = "maps/alpha_YangDesert/alpha_YangDesert";
	}

	@Deprecated
	public static boolean isSinglePlayer() {
		return false;
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
		User u;
		if (string.equals("")) // returns this user
			u = users.get(app.getClientHandler().getIdentification());
		else
			u = users.get(string);
		if (u == null) {
			System.err.println("PreGameInfo.getUser()" + string + " User not found");
		}
		return u;
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

}
