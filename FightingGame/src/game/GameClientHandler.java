package game;

import shared.Client;
import shared.ClientHandler;

public class GameClientHandler extends ClientHandler {

	public GameClientHandler(GameApp app, String serverIp) {
		super(app, serverIp);
		app.setClientHandler(this);
		app.setComHandler(new GameComHandler(app));
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
			System.out.println("game-client connected to server: " + getIdentification());
		}
	}

}
