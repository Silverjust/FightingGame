package gameStructure;

import game.GameBaseApp;
import game.PlayerInterface;

public abstract class Champion extends Unit {

	public Champion(GameBaseApp app, String[] c) {
		super(app, c);
		try {
			player.champion = this;
		} catch (Exception e) {
		}
		hpBarLength = 40;
	}

	public abstract void setupSpells(PlayerInterface playerInterface);

}
