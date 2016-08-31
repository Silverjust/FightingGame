package server;

import java.util.HashMap;

import shared.Client;
import shared.Coms;
import shared.Mode;
import shared.Server;
import shared.User;

public class PreGameManager {
	private ServerApp app;
	private HashMap<String, User> users = new HashMap<String, User>();

	public PreGameManager(ServerApp app) {
		this.app = app;
	}

	public void serverEvent(Server server, Client someClient) {
		if (app.getMode() == Mode.GAME && getUsers().containsKey(someClient.ip())) {
			User u = getUsers().get(someClient.ip());
			app.gui.addChatText(u.name + " has reconnected");
			app.serverHandler.send("<identify reconnect");
			app.serverHandler.send("<setNation " + u.ip + " " + u.toString());
			app.serverHandler.send("<setMap " + u.ip + " " /* +app.map */);
		} else {
			//app.gui.addChatText("We have a new client: " + someClient.ip());
			addUser(someClient.ip(), someClient.ip());
			app.serverHandler.send("<identify server");
		}
	}

	private void addUser(String ip, String ip2) {
		// TODO Auto-generated method stub

	}

	public void disconnectEvent(Client client) {
		app.gui.addChatText(getUsers().get(client.ip()) + " disconnected");
		app.serverHandler.send(Coms.PAUSE + " true");
	}

	public HashMap<String, User> getUsers() {
		return users;
	}

}
