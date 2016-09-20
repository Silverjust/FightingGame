package entity.aliens;

import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;

public class Colum extends Unit implements Attacker {

	private static PImage standingImg;

	MeleeAttack heal;

	private int hp;

	private int hp_max;

	private int armor;

	public PImage iconImg;

	protected String descr = " ";

	protected String stats = " ";

	public Death death;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		standingImg = game.ImageHandler.load(path, "Colum");
	}

	public Colum(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = walk = heal = new MeleeAttack(standingImg, 500);
		death = new Death(standingImg, 500);

		setAnimation(walk);

		// ************************************
		setxSize(45);
		setySize(45);

		kerit = 600;
		pax = 300;
		arcanum = 0;
		prunam = 0;
		trainTime = 5000;

		setHp(hp_max = 300);
		getStats.setSpeed(0.9f);
		stats.setRadius(7);
		animation.setSight((byte) 127);
		groundPosition = GameObject.GroundPosition.AIR;
		setHeight(50);

		heal.range = (byte) (stats.getRadius() + 25);
		heal.damage = 25;// heal
		heal.pirce = -1;// heal
		heal.cooldown = 5000;
		heal.setCastTime(100);

		descr = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {// colum: no info to client
		if (isServer&&heal.isNotOnCooldown()) {
			heal.startCooldown();
			heal.setTargetFrom(this, this);
		}
		heal.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
			if (e != null && e.isAllyTo(this)
					&& e.isInRange(getX(), getY(), e.getStats().getRadius() + a.range)) {
				GameBaseApp.getUpdater().sendDirect("<heal " + e.getNumber() + " " + heal.damage);
			}
		}
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return heal;
	}

	public void hit(int damage, byte pirce) {
	
		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);
	
			setHp((int) (getCurrentHp() - damage * (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.1)));
			/** check if it was lasthit */
			if (getCurrentHp() <= 0 && getCurrentHp() != Integer.MAX_VALUE) {// marker
				setHp(-32768);
				onDeath();
			}
	
		}
	}

	public void heal(int heal) {
		setHp(getCurrentHp() + heal);
		/** check if it was overheal */
		if (getCurrentHp() > hp_max) {
			setHp(hp_max);
		}
	}

	protected void onDeath() {
		sendAnimation("death", this);
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			GameBaseApp.app.fill(0, 150);
			GameBaseApp.app.rect(xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f, stats.getRadius() * 2, h);
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, hpImg, xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f,
					stats.getRadius() * 2 * getCurrentHp() / hp_max, h);
			GameBaseApp.app.tint(255);
		}
	}

	public float calcImportanceOf(GameObject e) {
		float importance = PApplet.abs(
				10000 / (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - stats.getRadius() - e.getStats().getRadius()));
		// TODO speziefische Thread werte
		if (e instanceof Attacker) {
			importance *= 20;
		}
		return importance;
	}

	public boolean isAlive() {
		if (isMortal())
			return (getAnimation().getClass() != death.getClass()) && getCurrentHp() > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public int getCurrentHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void initHp(int hp) {
		this.hp = hp;
		this.hp_max = hp;
	}
}
