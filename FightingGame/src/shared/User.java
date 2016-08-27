package shared;

import game.GameApplet;
import processing.core.PGraphics;

public class User {
	public String ip;
	public String name;
	public boolean online;
	public boolean isReady;
	@Deprecated
	public Nation nation;

	protected Player player;
	private GameApplet app;

	public User(GameApplet app, String ip, String name) {
		this.app = app;
		this.ip = ip;
		this.name = name;
		online = true;
	}

	public void display(PGraphics gr, int x, int y) {
		gr.fill(255);
		gr.rect(x, y, 280, 20);
		gr.fill(0);
		if (nation != null)
			gr.text(nation.officialName(), x, y + app.textAscent() * app.getTextScale());
		gr.text(name, x + 70, y + app.textAscent() * app.getTextScale());
	}
}