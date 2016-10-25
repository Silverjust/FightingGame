package shared;

import server.ServerHandler;

public abstract class ClientHandler {

	protected String identification;
	protected String serverIp;
	public Client client;
	public char startSymbol = '<';
	public char endSymbol = '>';
	protected GameBaseApp app;
	protected String clientConsoleName = "c";

	public ClientHandler(GameBaseApp app, String serverIp) {
		this.app = app;
		this.serverIp = serverIp;
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
					if (isReportCommunication())
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
				if (isReportCommunication())
					System.out.println(clientConsoleName + "  in: " + input);
				if (input.charAt(0) == startSymbol) {
					app.getComHandler().executeCom(input, false, null);
				}
			}
		}
	}

	public boolean wasSuccesfull() {
		return client != null && client.active();
	}

	public String getIdentification() {
		return identification;
	}

	public String getServerIp() {
		return serverIp;
	}

	public boolean isReportCommunication() {
		return ServerHandler.reportCommunication;
	}

}
