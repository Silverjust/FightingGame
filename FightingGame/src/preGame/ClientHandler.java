package preGame;

import game.GameBaseApp;
import shared.Client;

public class ClientHandler {
	public String identification;

	public Client client;

	public char startSymbol = '<';
	public char endSymbol = '>';

	private boolean reportCommunication = true;

	private GameBaseApp app;

	public ClientHandler(GameBaseApp app, String serverIp) {
		this.app = app;
		app.setClientHandler(this);
		app.setComHandler(new PreGameComHandler(app));
		try {
			client = new Client(app, serverIp, 5204);
			identification = client.myIp();
			// identification = socket.getLocalAddress().toString()
			// .substring(1);
			// socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (client == null || !client.active()) {
			System.out.println("no connection");
			client = null;
			return;
		} else {
			System.out.println("client connected to server: " + identification);
		}
	}

	public void update() {
		// wenn inaktiv, gameupdater pausieren
		/*
		 * if (client.available() > 0) { read(); }
		 */
	}

	public void clientEvent(Client someClient) {
		recieve();
	}

	public void send(String output) {
		if (output != null) {
			{
				if (client != null && client.active()) {
					client.write(output + endSymbol);
					if (reportCommunication)
						System.out.println("c out: " + output + endSymbol);
				} else {
					client.stop();
				}
			}
		}
	}

	public void recieve() {
		if (client != null) {
			String input = "" + client.readStringUntil(endSymbol);
			if (input != null && !input.equals("null")) {
				if (reportCommunication)
					System.out.println("c  in: " + input);
				if (input.charAt(0) == startSymbol) {
					app.getComHandler().executeCom(input);
				}
			}
		}
	}

	public boolean wasSuccesfull() {
		return client != null && client.active();
	}

}
