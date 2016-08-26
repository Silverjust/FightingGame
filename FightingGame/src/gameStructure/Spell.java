package gameStructure;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.GameApplet;
import game.HUD;
import game.PlayerInterface;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.Updater;
import shared.Updater.GameState;

/**
 * Aktive Fähigkeit
 */
public abstract class Spell {
	public GGameButton button;

	private int pos;

	private int cooldown;
	private int cooldownTimer;

	private PImage symbol;

	private boolean isActivateable = true;

	static int buttonWidth = 50;

	public Spell(PlayerInterface inter, int pos, PImage symbol) {

		button = new GGameButton(GameApplet.app, getXbyPos(inter, pos), getYbyPos(inter, pos), buttonWidth, buttonWidth,
				HUD.buttonImageFilename());
		button.setText(PlayerInterface.getKeyFromPos(pos));
		button.setSymbol(symbol);
		button.addEventHandler(this, "handleActiveEvents");
		this.symbol = symbol;
		this.pos = pos;
	}

	public int getXbyPos(PlayerInterface inter, int pos) {
		return inter.x + (pos - 1) * (buttonWidth + 10);
	}

	public int getYbyPos(PlayerInterface inter, int pos) {
		return inter.y + 0 * (buttonWidth + 10);
	}

	public void update() {
		if (isActivateable) {
			button.setCooldownState(1 - getCooldownPercent());
			if (button.isVisible() && !button.isEnabled() && isNotOnCooldown())
				button.setEnabled(true);
			else if (button.isVisible() && button.isEnabled() && !isNotOnCooldown())
				button.setEnabled(false);
		} else {
			if (button.isVisible()/* && button.isEnabled() */) {
				button.setEnabled(false);
				button.setCooldownState(0.999f);
			}
		}
	}

	public void selectionUpdate() {
		if (button.isVisible() && !button.isEnabled() && isNotOnCooldown() && isActivateable()) {
			isActivateable = true;
		} else if (button.isVisible() && button.isEnabled() && (!isNotOnCooldown() || !isActivateable())) {
			isActivateable = false;
		}
	}

	public void handleActiveEvents(GGameButton gamebutton, GEvent event) {
		if (GameApplet.app.keyPressed || GameApplet.app.mouseButton != PConstants.RIGHT) {
			if (event == GEvent.PRESSED && gamebutton.isVisible() && isNotOnCooldown()
					&& GameApplet.updater.gameState == GameState.PLAY) {
				onActivation();
				// startCooldown();
			}
		} else {
		}
	}

	protected abstract void onActivation();

	public void setVisible(boolean b) {
		button.setVisible(b);
	}

	public boolean isVisible() {
		return button.isVisible();
	}

	public boolean isActivateable() {
		return true;
	}

	public void pressManually() {
		if (isNotOnCooldown())
			button.pressManually();
	}

	public void startCooldown() {
		cooldownTimer = Updater.Time.getMillis() + getCooldown();
		button.setEnabled(false);
	}

	public boolean isNotOnCooldown() {
		return cooldownTimer <= Updater.Time.getMillis();
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - Updater.Time.getMillis()) / getCooldown();
		return f > 1 || f < 0 ? 1 : f;
	}

	public void drawIcon(PGraphics graphic, float x, float y, int size) {
		// TODO gleich wie entity
		if (symbol != null)
			graphic.image(symbol, x, y, size, size);
	}

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

}
