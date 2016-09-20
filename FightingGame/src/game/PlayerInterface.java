package game;

import java.util.ArrayList;

import gameStructure.Champion;
import gameStructure.Spell;
import main.appdata.SettingHandler;
import shared.ContentListManager;
import shared.GameBaseApp;
import shared.Player;
import shared.SpellHandler;

public class PlayerInterface extends SpellHandler {
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	public int x = 400;
	public int y;
	private SettingHandler settingHandler;
	private GameBaseApp app;
	private Player player;
	private float statsDisplayX = 10;

	public PlayerInterface(GameBaseApp app, HUD hud) {
		this.app = app;
		hud.playerInterface = this;
		settingHandler = ((GameApp) app).settingHandler;
		y = app.height - hud.height + 5;
		player = app.getPlayer();
		String championName = player.getUser().championName;
		if (championName != null && !championName.equals("") && !championName.equals("null")) {
			ContentListManager contentListManager = app.getContentListManager();
			Champion champ = (Champion) contentListManager.createGObj(contentListManager.getChampClass(championName));
			champ.setupSpells(app, this);
		} else {
			System.err.println("PlayerInterface.PlayerInterface()   " + championName + " is not a champion");
		}
	}

	public void update() {
		for (Spell spell : spells) {
			spell.updateButton();
		}
		app.text(player.getChampion().getStats().getSpeed(), statsDisplayX, y + app.textAscent());
	}

	public Spell addSpell(Spell spell) {
		spells.add(spell);
		return spell;
	}

	public void handleKeyPressed(char c) {
		for (Spell spell : spells) {
			if (spell.getPos() == getPosOfKey(c) && spell.isActivateable())
				spell.pressManually();
		}
	}

	private int getPosOfKey(char c) {
		char[] k = settingHandler.getSetting().baseShortcuts;
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

	public String getKeyFromPos(int pos) {
		System.out.println("PlayerInterface.getKeyFromPos()" + settingHandler.getSetting());
		char[] k = settingHandler.getSetting().baseShortcuts;
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
