package game;

import gameStructure.Champion;
import gameStructure.GameObject;
import gameStructure.Spell;
import gameStructure.baseBuffs.Buff;
import main.appdata.SettingHandler;
import shared.ContentListManager;
import shared.GameBaseApp;
import shared.Helper;
import shared.SpellHandler;

public class PlayerInterface extends SpellHandler {
	public int x = 400;
	public int y;
	private SettingHandler settingHandler;
	private float statsDisplayX = 10;
	private String championName;
	private GameObject target;

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {

	}

	public PlayerInterface(GameBaseApp app, HUD hud) {
		this.app = app;
		hud.playerInterface = this;
		settingHandler = ((GameApp) app).settingHandler;
		y = app.height - hud.height + 5;
	}

	public void setup() {
		player = app.getPlayer();
		championName = player.getUser().championName;
		if (championName != null && !championName.equals("") && !championName.equals("null")) {
			ContentListManager contentListManager = app.getContentListManager();
			Champion champ = (Champion) contentListManager.createGObj(contentListManager.getChampClass(championName));
			champ.setupSpells(app, this);
		} else {
			System.err.println("PlayerInterface.PlayerInterface()   " + championName + " is not a champion");
		}
	}

	public void update() {
		if (getTarget() != null)
			getTarget().drawTargetInfo();
		int i = 0;
		for (Buff buff : player.getChampion().getBuffs()) {
			buff.draw(x + i * 60 -60, y-60, 50);
			i++;
		}
		for (Spell spell : spells) {
			spell.updateButton();
		}
		String string = "§img armor§" + player.getChampion().getStats().getArmor().getTotalAmount()//
				+ "\n§img mr§" + player.getChampion().getStats().getMagicResist().getTotalAmount()//
				+ "\n§img ms§" + (float) player.getChampion().getStats().getMovementSpeed().getTotalAmount()//
				+ "\n§img ad§" + player.getChampion().getStats().getAttackDamage().getTotalAmount()//
				+ "\n§img ap§" + player.getChampion().getStats().getAbilityPower().getTotalAmount();
		Helper.text(app, string, statsDisplayX, y + app.textAscent());
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

	public GameObject getTarget() {
		return target;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}

}
