package shared;

import java.util.ArrayList;

import entity.MainBuilding;
import game.GameBaseApp;
import gameStructure.Champion;
import gameStructure.GameObject;
import processing.core.PApplet;
import shared.Updater.GameState;

public class Player {
	public int color;

	public ArrayList<GameObject> visibleEntities = new ArrayList<GameObject>();
	private User user;
	public MainBuilding mainBuilding;
	public GameState gameState;// win, loose
	public Champion champion;
	public GameBaseApp app;

	public static Player createNeutralPlayer(GameBaseApp app) {
		Player p = new Player(app);
		p.setUser(new User(app, "", "neutral"));
		p.getUser().player = p;
		p.getUser().online = true;
		p.color = app.color(150);
		p.setNation(Nation.NEUTRAL);
		return p;
	}

	public static Player createPlayer(GameBaseApp app, User user) {
		Player p = new Player(app);
		p.setUser(user);
		p.getUser().player = p;
		p.gameState = GameState.PLAY;
		return p;
	}

	private Player(GameBaseApp app) {
		this.app = app;
	}

	@Override
	public String toString() {
		if (getUser().online) {
			return "[" + getUser().name + "]";
		}
		return "offline:[" + getUser().name + "]";
	}

	@Deprecated
	public Nation getNation() {
		return getUser().nation;
	}

	@Deprecated

	public void setNation(Nation nation) {
		getUser().nation = nation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		System.out.println("Player.setUser()");
		this.user = user;
	}

}
