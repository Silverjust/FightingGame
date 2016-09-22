package gameStructure;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.PlayerInterface;
import processing.core.PConstants;
import processing.core.PImage;
import shared.Coms;
import shared.GameBaseApp;
import shared.Player;
import shared.SpellHandler;
import shared.Updater;
import shared.Updater.GameState;

/**
 * Aktive Fähigkeit
 */
public abstract class Spell {
	SpellDisplay spellDisplay;

	private int pos;

	private int cooldown;
	private int cooldownTimer;

	protected GameBaseApp app;

	private Champion champ;

	public Spell(GameBaseApp app, SpellHandler inter, int pos, PImage symbol) {
		this.app = app;
		System.out.println("Spell.Spell()" + symbol);
		if (inter instanceof PlayerInterface)
			spellDisplay = new SpellDisplay(app, this, (PlayerInterface) inter, pos, symbol);
		this.pos = pos;
	}

	public void updateButton() {
		/*
		 * if (isActivateable) { button.setCooldownState(1 -
		 * getCooldownPercent()); if (button.isVisible() && !button.isEnabled()
		 * && isNotOnCooldown()) button.setEnabled(true); else if
		 * (button.isVisible() && button.isEnabled() && !isNotOnCooldown())
		 * button.setEnabled(false); } else { if (button.isVisible()/* &&
		 * button.isEnabled()
		 *//*
			 * ) { button.setEnabled(false); button.setCooldownState(0.999f); }
			 * }
			 */
		spellDisplay.update();
	}

	public void handleActiveEvents(GGameButton gamebutton, GEvent event) {
		if (app.keyPressed || app.mouseButton != PConstants.RIGHT) {
			if (event == GEvent.PRESSED && gamebutton.isVisible() && isNotOnCooldown()
					&& app.getUpdater().gameState == GameState.PLAY) {
				onActivation();
				// startCooldown();
			}
		} else {
		}
	}

	/** clientside */
	protected void onActivation() {
		app.getUpdater().send(Coms.INPUT + " " + app.getPlayer().getUser().getIp() + " " + getPos() + " 1");
		startCooldown();
	}

	public void setVisible(boolean b) {
		spellDisplay.setVisible(b);
	}

	public boolean isVisible() {
		return spellDisplay.isVisible();
	}

	public boolean isActivateable() {
		if (champ.getStats().isSilenced())
			return false;
		return true;
	}

	public void pressManually() {
		if (isNotOnCooldown())
			onActivation();
	}

	public void startCooldown() {
		cooldownTimer = Updater.Time.getMillis() + getCooldown();
		if (spellDisplay != null)
			spellDisplay.setEnabled(false);
	}

	public boolean isNotOnCooldown() {
		return cooldownTimer <= Updater.Time.getMillis();
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - Updater.Time.getMillis()) / getCooldown();
		return f > 1 || f < 0 ? 1 : f;
	}

	/*
	 * public void drawIcon(PGraphics graphic, float x, float y, int size) { //
	 * TODO gleich wie entity if (symbol != null) graphic.image(symbol, x, y,
	 * size, size); }
	 */

	public abstract String getDescription();

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getPos() {
		return pos;
	}

	public String getInternName() {
		return this.getClass().getSimpleName();
	}

	protected void setPassive(boolean b) {
		if (spellDisplay != null)
			spellDisplay.setEnabled(b);
	}

	/** Serverside */
	public abstract void recieveInput(String[] c, Player player);

	public void registerChampion(Champion champion) {
		champ = champion;
	}

}
