package gameStructure.animation;

import gameStructure.GameObject;
import processing.core.PImage;
import shared.GameBaseApp;
import shared.Updater;

public class Ability extends Animation {

	private int eventTime;
	private int cooldown;
	int cooldownTimer;

	public boolean doRepeat = false;

	public Ability(GameBaseApp app, GameObject animated, PImage[][] IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	public Ability(GameBaseApp app, GameObject animated, PImage[] IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	public Ability(GameBaseApp app, GameObject animated, PImage IMG, int duration) {
		super(app, animated, IMG, duration);
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
		return start + getEventTime() <= Updater.Time.getMillis();
	}

	public void updateAbility(GameObject e, boolean isServer) {
		if (isSetup() && isEvent()) {
			/** do smthing */
			// startCooldown();
		}
	}

	public void setCastTime(int castTime) {
		setEventTime(castTime);
	}

	public void startCooldown() {
		cooldownTimer = Updater.Time.getMillis() + getCooldown();
	}

	public float getCooldownPercent() {
		float f = 1 - (float) (cooldownTimer - Updater.Time.getMillis()) / getCooldown();
		// System.out.println("Ability.getCooldownPercent()" + f);
		return f > 1 || f < 0 ? 1 : f;
	}

	public float getProgressPercent() {
		float f = 1 - (float) (start + getEventTime() - Updater.Time.getMillis()) / getEventTime();
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

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getEventTime() {
		return eventTime;
	}

	public void setEventTime(int eventTime) {
		this.eventTime = eventTime;
	}
}
