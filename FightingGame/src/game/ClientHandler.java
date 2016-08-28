package game;

import shared.Client;

public class ClientHandler {
	public String identification;

	public Client client;

	public char startSymbol = '<';
	public char endSymbol = '>';

	private boolean reportCommunication = false;

	private GameBaseApp app;

	public ClientHandler(GameBaseApp app, String serverIp) {
		this.app = app;
		if (PreGameInfo.isSinglePlayer()) {
			System.out.println("SinglePlayer");
			identification = "1";
		} else {
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
				System.out.println("MultiPlayer");
				System.out.println(identification);
			}
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

	public void send(String s) {
		if (s != null) {
			if (PreGameInfo.isSinglePlayer()) {
				app.getComHandler().executeCom(s);
			} else {
				if (client != null && client.active()) {
					client.write(s + endSymbol);
					if (reportCommunication)
						System.out.println("out: " + s + " " + endSymbol);
				} else {
					client.stop();
				}
			}
		}
	}

	public void recieve() {
		if (client != null) {
			String in = "" + client.readStringUntil(endSymbol);
			if (in != null && !in.equals("null")) {
				if (reportCommunication)
					System.out.println("in: " + in);
				if (in.charAt(0) == startSymbol) {
					app.getComHandler().executeCom(in);
				}
			}
		}
	}

}
