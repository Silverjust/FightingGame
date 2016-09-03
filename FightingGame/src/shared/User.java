package shared;

import game.GameBaseApp;
import processing.core.PGraphics;

public class User {
	public String ip;
	public String name;
	public String champion;
	public boolean online;
	public boolean isReady;
	public Team team=Team.LEFTSIDE;

	protected Player player;
	private GameBaseApp app;

	public User(GameBaseApp app, String ip, String name) {
		this.app = app;
		this.ip = ip;
		this.name = name;
		online = true;
	}

	public void display(PGraphics gr, int x, int y) {
		gr.fill(255);
		gr.rect(x, y, 280, 20);
		gr.fill(0);
		if (champion != null)
			gr.text(champion, x, y + app.textAscent() * app.getTextScale());
		gr.text(name, x + 70, y + app.textAscent() * app.getTextScale());
	}
}