package shared;

public abstract class ClientHandler {

	protected String identification;
	protected String serverIp;
	public Client client;
	public char startSymbol = '<';
	public char endSymbol = '>';
	private boolean reportCommunication = true;
	protected GameBaseApp app;

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
					app.getComHandler().executeCom(input, false);
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

}
