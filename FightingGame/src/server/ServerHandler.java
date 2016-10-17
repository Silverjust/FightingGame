package server;

import java.net.BindException;

import shared.Client;
import shared.ComHandler;
import shared.Server;

public class ServerHandler {
	ServerApp app;

	Server server;

	String input;

	char endSymbol = '>';

	public boolean doProtocol = false;
	public static boolean reportCommunication = true;

	private ComHandler comHandler;

	ServerHandler(ServerApp app) {
		this.app = app;
		app.setServerHandler(this);
		comHandler = new ServerComHandler(app);
		app.setComHandler(comHandler);
		try {
			server = new Server(app, 5204);
			if (app.getAdminApp() != null)
				app.getAdminApp().connectToServer(Server.ip());
		} catch (Exception e) {
			e.printStackTrace();
			app.getAdminApp().releaseConnectionAttempt(app);
			if (e instanceof RuntimeException && e.getCause() instanceof BindException) {
				app.write("ERROR: address already in use ");
			}
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
					if (input != null && !input.equals("") && !input.equals("null")) {
						if (app.serverInterface.displayCommands.isSelected())
							app.write("in  " + input);
						if (reportCommunication)
							System.out.println("s  in: " + input);

						// if (doProtocol)
						// Protocol.filterComs("< ", input);

						if (input.charAt(0) == '<')
							comHandler.executeCom(input, false, client.ip());

					}
				}
			}
		}
	}

	public void serverEvent(Server server, Client someClient) {
		app.serverPreGameManager.serverEvent(server, someClient);
	}

	/**
	 * only use in updater
	 * <p>
	 * use updater.send() instead
	 */
	public void sendDirect(String out) {
		if (app.serverInterface != null && app.serverInterface.displayCommands.isSelected())
			app.write("out " + out + endSymbol);
		if (doProtocol)
			Protocol.filterComs("> ", out);
		server.write(out + endSymbol);
		if (reportCommunication)
			System.out.println("s out: " + out + endSymbol);
	}

	public void disconnectEvent(Client client) {
		app.serverPreGameManager.disconnectEvent(client);

	}

	boolean isWorking() {
		return server != null && server.active();
	}

	void dispose() {
		// Protocol.createFile();
		// Protocol.dispose();
	}
}
