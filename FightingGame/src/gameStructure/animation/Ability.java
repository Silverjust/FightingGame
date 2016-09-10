package gameStructure.animation;

import gameStructure.GameObject;
import processing.core.PImage;
import shared.GameBaseApp;
import shared.Updater;

public class Ability extends Animation {

	protected int eventTime;
	public int cooldown;
	int cooldownTimer;

	public boolean doRepeat = false;

	public Ability(GameBaseApp app,PImage[][] IMG, int duration) {
		super(app, IMG, duration);
	}

	public Ability(GameBaseApp app,PImage[] IMG, int duration) {
		super(app, IMG, duration);
	}

	public Ability(GameBaseApp app,PImage IMG, int duration) {
		super(app, IMG, duration);
	}

	@Override
	public void setup(GameObject e) {
		super.setup(e);
		if (e.getAnimation() != this && isNotOnCooldown()) {
			consumeCosts();
			startCooldown();
		}
	}

	public boolean isNotOnCooldown() {
		return cooldownTimer <= Updater.Time.getMillis();
	}

	public boolean isEvent() {
		return start + eventTime <= Updater.Time.getMillis();
	}

	public void updateAbility(GameObject e, boolean isServer) {
		if (isSetup() && isEvent()) {
			/** do smthing */
			// startCooldown();
		}
	}

	public void setCastTime(int castTime) {
		eventTime = castTime;
	}

	public void startCooldown() {
		cooldownTimer = Updater.Time.getMillis() + cooldown;
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - Updater.Time.getMillis())
				/ cooldown;
		// System.out.println("Ability.getCooldownPercent()" + f);
		return f > 1 || f < 0 ? 1 : f;
	}

	public float getProgressPercent() {
		float f = 1 - (float) (start + eventTime - Updater.Time.getMillis())
				/ eventTime;
		return f > 1 || f < 0 ? 1 : f;
	}

	protected void consumeCosts() {

	}

	public void drawAbility(GameObject e, byte d) {
	}

	public boolean isSetup() {
		return false;
	}

	public void removeCooldown(int time) {
		cooldownTimer -= time;
	}

	@Override
	public boolean doRepeat(GameObject e) {
		return doRepeat;
	}
}
