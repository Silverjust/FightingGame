package server;

import gameStructure.Champion;
import gameStructure.Spell;
import shared.ContentListManager;
import shared.GameBaseApp;
import shared.Player;
import shared.SpellHandler;

public class ServerSpellHandler extends SpellHandler {

	public ServerSpellHandler(GameBaseApp app, Player player) {
		this.player = player;
		String championName = player.getUser().championName;
		if (championName != null && !championName.equals("") && !championName.equals("null")) {
			ContentListManager contentListManager = app.getContentListManager();
			Champion champ = (Champion) contentListManager.createGObj(contentListManager.getChampClass(championName));
			champ.setupSpells(app, this);
		} else {
			System.err.println("ServerSpellHandler.ServerSpellHandler()   " + championName + " is not a champion");
		}
	}

	@Override
	public Spell addSpell(Spell spell) {
		spells.add(spell);
		return spell;
	}

	public void input(String[] c) {
		boolean isSpellInput;
		int pos = 0;
		try {
			pos = Integer.parseInt(c[2]);
			isSpellInput = true;
		} catch (Exception e) {
			isSpellInput = false;
		}

		if (isSpellInput) {
			for (Spell spell : spells) {
				if (spell.getPos() == pos) {
					if (spell.isNotOnCooldown() && spell.isActivateable())
						spell.recieveInput(c, player);
				}
			}

		} else {
			if (c[2].equals("walk")) {
				player.getChampion().sendAnimation(c[2] + " " + c[3] + " " + c[4], this);
			} else if (c[2].equals("basicAttack")) {
				player.getChampion().sendAnimation(c[2] + " " + c[3], this);
			}
		}
	}

}
