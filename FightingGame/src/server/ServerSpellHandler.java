package server;

import java.util.ArrayList;

import gameStructure.Champion;
import gameStructure.Spell;
import shared.ContentListManager;
import shared.GameBaseApp;
import shared.Player;
import shared.SpellHandler;

public class ServerSpellHandler extends SpellHandler {
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private Player player;

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
		for (Spell spell : spells) {
			if (spell.getPos() == Integer.parseInt(c[2])) {
				spell.recieveInput(c, player);
			}
		}
	}

}
