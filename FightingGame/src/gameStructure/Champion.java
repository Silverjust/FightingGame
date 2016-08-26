package gameStructure;

import game.PlayerInterface;

public abstract class Champion extends Unit {

	public Champion(String[] c) {
		super(c);
		try {
			player.champion = this;
		} catch (Exception e) {
		}
		hpBarLength = 40;
	}

	public abstract void setupSpells(PlayerInterface playerInterface);

}
