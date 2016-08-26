package shared;

import java.util.ArrayList;

import gameStructure.Champion;
import gameStructure.GameObject;
import gameStructure.MainBuilding;
import processing.core.PApplet;
import shared.Updater.GameState;

public class Player {
	public int color;

	public ArrayList<GameObject> visibleEntities = new ArrayList<GameObject>();
	private User user;
	public MainBuilding mainBuilding;
	public GameState gameState;// win, loose
	public Champion champion;
	public PApplet app;

	public static Player createNeutralPlayer() {
		Player p = new Player();
		p.setUser(new User("", "neutral"));
		p.getUser().player = p;
		p.getUser().online = true;
		p.color = ref.app.color(150);
		p.setNation(Nation.NEUTRAL);
		return p;
	}

	public static Player createPlayer(User user) {
		Player p = new Player();
		p.setUser(user);
		p.getUser().player = p;
		p.gameState = GameState.PLAY;
		return p;
	}

	private Player() {
		app = ref.app;
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
		System.out.println("Player.setUser()");		this.user = user;
	}

}
