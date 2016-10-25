package gameStructure;

import shared.GameBaseApp;
import shared.SpellHandler;

public abstract class Champion extends Unit {
	protected String championName;

	public Champion(GameBaseApp app, String[] c) {
		super(app, c);
		try {
			player.setChampion(this);
		} catch (Exception e) {
		}
		setHpBarLength(40);
	}

	public abstract void setupSpells(GameBaseApp app, SpellHandler spellHandler);

	@Override
	public String getIngameName() {
		if (championName != null && !championName.equals(""))
			return championName;
		return super.getIngameName();
	}

	public void handleWalkInput(float x, float y) {
		player.app.getUpdater().sendInput(player, "walk", x + " " + y);
	}

	public void handleAttackInput(GameObject e) {
		System.out.println("Champion.handleAttackInput() 			"+e.getNumber());
		player.app.getUpdater().sendInput(player, "basicAttack", e.getNumber() + "");
	}

}
