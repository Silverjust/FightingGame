package server;

import shared.Client;
import shared.ComHandler;
import shared.Server;

public class ServerHandler {
	ServerApp app;

	Server server;

	String input;

	char endSymbol = '>';

	public boolean doProtocol = false;

	private ComHandler comHandler;

	ServerHandler(ServerApp app) {
		this.app = app;
		app.serverHandler = this;
		comHandler = new ComHandler(app);
		app.setComHandler(comHandler);
		try {
			server = new Server(app, 5204);
			if (app.adminApp != null)
			app.adminApp.connectToServer(Server.ip());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void update() {
		// if (doProtocol && app.frameCount % 100 == 0)
		// Protocol.collectInfos();

		if (server != null) {
			while (server.available() != null) {
				Client client = server.available();
				if (client != null) {
					input = "" + client.readStringUntil(endSymbol);
					if (input != null) {

						if (app.gui.displayCommands.isSelected())
							app.gui.addChatText("in  " + input);

						// if (doProtocol)
						// Protocol.filterComs("< ", input);

						if (input.charAt(0) == '<')
							comHandler.executeCom(input);

						send(input.replace(endSymbol + "", ""));
					}
				}
			}
		}
	}

	public void serverEvent(Server server, Client someClient) {
		app.preGame.serverEvent(server, someClient);
	}

	public void send(String out) {
		/*if (app.gui.displayCommands.isSelected())
			app.gui.addChatText("out " + out + endSymbol);*/
		if (doProtocol)
			Protocol.filterComs("> ", out);
		server.write(out + endSymbol);
	}

	public void disconnectEvent(Client client) {
		app.preGame.disconnectEvent(client);

	}

	void dispose() {
		Protocol.createFile();
		Protocol.dispose();
	}
}
