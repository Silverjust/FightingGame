package shared;

import processing.core.PGraphics;

public class User {
	private String ip;
	public String name;
	public String championName;
	public Team team = Team.LEFTSIDE;

	protected Player player;
	private GameBaseApp app;

	private boolean online;
	private boolean isConnectedWithGameClient;
	private boolean isReady;

	public User(GameBaseApp app, String ip, String name) {
		this.app = app;
		this.setIp(ip);
		this.name = name;
		setOnline(true);
	}

	public void display(PGraphics gr, int x, int y) {
		gr.fill(255);
		gr.rect(x, y, 280, 20);
		gr.fill(0);
		if (championName != null)
			gr.text(championName, x, y + app.textAscent() * app.getTextScale());
		gr.text(name, x + 70, y + app.textAscent() * app.getTextScale());
	}

	public boolean isConnectedWithGameClient() {
		return isConnectedWithGameClient;
	}

	public void setConnectedWithGameClient(boolean isConnectedWithGameClient) {
		this.isConnectedWithGameClient = isConnectedWithGameClient;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}