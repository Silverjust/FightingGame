package server;

import game.GameBaseApp;
import game.PreGameInfo;
import shared.Client;
import shared.Coms;
import shared.Mode;
import shared.Server;
import shared.User;

public class ServerPreGameManager {
	private ServerApp app;
	private PreGameInfo info;

	public ServerPreGameManager(ServerApp app) {
		this.app = app;
		info = PreGameInfo.createNewPrGaIn((GameBaseApp)app);
		app.setPreGameInfo(info);
	}

	public void serverEvent(Server server, Client someClient) {
		String ip = someClient.ip();
		if (app.getMode() == Mode.GAME && info.getUsers().containsKey(ip)) {
			/*User u = info.getUsers().get(someClient.ip());
			app.write(u.name + " has reconnected");
			app.serverHandler.send("<identify reconnect");*/
		} else {
			addUser(ip, ip);
			app.getServerHandler().sendDirect(Coms.IDENTIFY + " " + ip);
		}
	}

	private void addUser(String ip, String name) {
		User u=new User(app, ip, name);
		info.addUser(u);
	}

	public void disconnectEvent(Client client) {
		app.write(info.getUsers().get(client.ip()) + " disconnected");
		app.getServerHandler().sendDirect(Coms.PAUSE + " true");
	}

	public void sendPregameInfo() {

	}

}
