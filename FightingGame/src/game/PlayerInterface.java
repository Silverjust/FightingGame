package game;

import java.util.ArrayList;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import gameStructure.Champion;
import gameStructure.Spell;
import main.appdata.SettingHandler;
import shared.ref;

public class PlayerInterface {
	Champion champ;
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	public int x = 400;
	public int y;

	public PlayerInterface() {
		y = ref.app.height - HUD.height + 5;
		champ = ref.player.champion;
		champ.setupSpells(this);
	}

	public void update() {
		for (Spell spell : spells) {
			spell.update();
		}
	}

	public void handleSpellEvent(GGameButton gamebutton, GEvent event) {
		System.out.println("PlayerInterface.handleSpellEvent()" + event);
	}

	public Spell addSpell(Spell spell) {
		spells.add(spell);
		return spell;
	}

	public void handleKeyPressed(char c) {
		for (Spell s : spells) {
			if (s.getPos() == getPosOfKey(c) && s.isActivateable())
				s.pressManually();
		}
	}

	private int getPosOfKey(char c) {
		char[] k = SettingHandler.setting.baseShortcuts;
		if (c == k[0]) {
			return 1;
		} else if (c == k[1]) {
			return 2;
		} else if (c == k[2]) {
			return 3;
		} else if (c == k[3]) {
			return 4;
		} else if (c == k[4]) {
			return 5;
		} else {
			return 6;
		}
	}

	public static String getKeyFromPos(int pos) {
		System.out.println("PlayerInterface.getKeyFromPos()"+SettingHandler.setting);
		char[] k = SettingHandler.setting.baseShortcuts;
		if (pos == 0) {
			return " ";
		} else if (pos == 1) {
			return k[0] + "";
		} else if (pos == 2) {
			return k[1] + "";
		} else if (pos == 3) {
			return k[2] + "";
		} else if (pos == 4) {
			return k[3] + "";
		} else {
			return "error";
		}
	}

}
