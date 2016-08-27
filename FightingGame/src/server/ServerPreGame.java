package server;

import game.GameApplet;
import main.preGame.PreGameNormalDisplay;
import shared.ContentListManager;
import shared.Helper;
import shared.Mode;
import shared.PreGame;
import shared.User;

public class ServerPreGame extends PreGame {

	public ServerPreGame() {
		ContentListManager.load();
		int i = ContentListManager.getModeMaps().size();

		@SuppressWarnings("unchecked")
		String[] intNames = (String[]) ContentListManager.getModeMaps().keys().toArray(new String[i]);
		int startMapNumber =PreGameNormalDisplay.getStartMap();// new PreGameNormalDisplay().startMap;
		map = ContentListManager.getModeMaps().getString(intNames[startMapNumber]);
		// PApplet.printArray(intNames);
		// System.out.println(map);
	}

	@Override
	public void update() {

	}

	@Override
	public void startLoading() {
		GameApplet.loader = new MultiplayerLoader();
		((ServerApp) GameApplet.app).mode = Mode.LADESCREEN;
	}

	@Override
	public void addPlayer(String ip, String name) {
		if (!users.containsKey(ip)) {
			User u = new User(app, ip, name);
			users.put(ip, u);
		} else if (users.get(ip).name.equals(ip)) {
			users.get(ip).name = name;
		}
	}

	@Override
	public void setMap(String string) {
		if (ContentListManager.getModeMaps().keys().contains(string))
			map = ContentListManager.getModeMaps().getString(string);
		((ServerApp) GameApplet.app).gui.addChatText("map set to " + string);

	}

	@Override
	public void write(String ip, String[] text) {
		String name = Helper.ipToName(ip);
		String completeText = "";
		for (int i = 2; i < text.length; i++) {// c[0] und c[1] auslassen
			completeText = completeText.concat(" ").concat(text[i]);
		}
		((ServerApp) GameApplet.app).gui.addChatText(name + ">>" + completeText);
		System.out.println(" " + name + ">>" + completeText);
	}
}
