package shared;

import java.util.ArrayList;
import java.util.HashMap;

import gameStructure.Champion;
import gameStructure.GameObject;
import shared.Updater.GameState;

public class Player {
	public int color;

	public ArrayList<GameObject> visibleGObjects = new ArrayList<GameObject>();
	private User user;
	public GameState gameState;// win, loose
	private Champion champion;
	public GameBaseApp app;

	public static Player createNeutralPlayer(GameBaseApp app, Team team, HashMap<String, Player> players) {
		Player p = new Player(app);
		p.setUser(new User(app, "", "neutral"));
		p.getUser().team = team;
		p.getUser().player = p;
		p.getUser().setOnline(true);
		p.color = app.color(150);
		if (team != null)
			p.getUser().setIp("0");
		else if (team == Team.LEFTSIDE)
			p.getUser().setIp("-1");
		else if (team == Team.RIGHTSIDE)
			p.getUser().setIp("-2");

		//players.put(p.getUser().getIp(), p);
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
		if (getUser().isOnline()) {
			return "[" + getUser().name + "]";
		}
		return "offline:[" + getUser().name + "]";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		System.out.println("Player.setUser()");
		this.user = user;
	}

	public Champion getChampion() {
		return champion;
	}

	public void setChampion(Champion champion) {
		this.champion = champion;
		app.getUpdater().handleChampionInit(champion);

	}

}
