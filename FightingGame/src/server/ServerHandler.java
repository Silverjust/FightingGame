package server;

import game.GameApplet;
import shared.Client;
import shared.Server;
import shared.ComHandler;
import shared.Coms;
import shared.Mode;
import shared.Player;

public class ServerHandler {
	ServerApp app;

	Server server;

	String input;

	char endSymbol = '>';

	public boolean doProtocol = false;

	ServerHandler() {
		app = (ServerApp) GameApplet.app;

		try {
			server = new Server(GameApplet.app, 5204);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void update() {
		if (doProtocol && GameApplet.app.frameCount % 100 == 0) 
			 Protocol.collectInfos();
		
		if (server != null) {
			while (server.available() != null) {
				Client client = server.available();
				if (client != null) {
					input = "" + client.readStringUntil(endSymbol);
					if (input != null) {

						if (app.gui.displayCommands.isSelected())
							app.gui.addChatText("in  " + input);

						if (doProtocol)
							Protocol.filterComs("< ", input);

						if (input.charAt(0) == '<')
							ComHandler.executeCom(input);

						send(input.replace(endSymbol + "", ""));
					}
				}
			}
		}
	}

	public void serverEvent(Server server, Client someClient) {
		if (app.mode == Mode.GAME
				&& GameApplet.updater.players.containsKey(someClient.ip())) {
			Player p = GameApplet.updater.players.get(someClient.ip());
			app.gui.addChatText(p.getUser().name + " has reconnected");
			send("<identify reconnect");
			send("<setNation " + p.getUser().ip + " " + p.getNation().toString());
			send("<setMap " + p.getUser().ip + " " + GameApplet.preGame.map);
		} else {
			app.gui.addChatText("We have a new client: " + someClient.ip());
			GameApplet.preGame.addPlayer(someClient.ip(), someClient.ip());
			send("<identify server");
		}
	}

	public void send(String out) {
		if (app.gui.displayCommands.isSelected())
			app.gui.addChatText("out " + out + endSymbol);
		if (doProtocol)
			Protocol.filterComs("> ", out);
		server.write(out + endSymbol);
	}

	public void disconnectEvent(Client client) {
		app.gui.addChatText(GameApplet.updater.players.get(client.ip())
				+ " disconnected");
		send(Coms.PAUSE+" true");
	}
	
		void dispose(){
			Protocol.createFile();
			Protocol.dispose();
		}
}
