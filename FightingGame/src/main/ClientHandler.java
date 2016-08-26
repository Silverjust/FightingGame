package main;

import main.preGame.MainPreGame;
import main.preGame.MainPreGame.GameSettings;
import shared.Client;
import shared.ComHandler;
import shared.ref;

public class ClientHandler {
	public static String identification;

	public static Client client;

	public static char startSymbol = '<';
	public static char endSymbol = '>';

	private static boolean reportCommunication = false;

	public static void setup(String ip) {

		if (GameSettings.singlePlayer) {
			System.out.println("SinglePlayer");
			identification = "1";
		} else {
			try {
				client = new Client(ref.app, ip, 5204);
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
		((MainPreGame) ref.preGame).setupPlayer();
	}

	public static void update() {
		// wenn inaktiv, gameupdater pausieren
		/*
		 * if (client.available() > 0) { read(); }
		 */
	}

	public static void clientEvent(Client someClient) {
		recieve();
	}

	public static void send(String s) {
		if (s != null) {
			if (GameSettings.singlePlayer) {
				ComHandler.executeCom(s);
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

	public static void recieve() {
		if (client != null) {
			String in = "" + client.readStringUntil(endSymbol);
			if (in != null && !in.equals("null")) {
				if (reportCommunication)
					System.out.println("in: " + in);
				if (in.charAt(0) == startSymbol) {
					ComHandler.executeCom(in);
				}
			}
		}
	}

}
