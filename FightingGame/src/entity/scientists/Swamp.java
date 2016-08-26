package entity.scientists;

import game.GameApplet;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Helper.Timer;

public class Swamp extends Unit implements Attacker {

	private static PImage standingImg;

	MeleeAttack basicAttack;
	Timer decay = new Timer();

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
		standingImg = game.ImageHandler.load(path, "Swamp");
	}

	public Swamp(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = basicAttack = new MeleeAttack(standingImg, 800);
		setAnimation(stand);

		// ************************************
		setxSize(70);
		setySize(35);

		setSpeed(2.2f);
		setRadius(0);
		setSight((byte) (35 + 5));
		groundPosition = GameObject.GroundPosition.GROUND;

		basicAttack.range = 35;
		basicAttack.damage = 10;
		basicAttack.cooldown = 1000;
		basicAttack.setCastTime(500);
		basicAttack.targetable = groundPosition;

		decay.cooldown = 7000;
		// ************************************
	}

	@Override
	public void onSpawn(boolean isServer) {
		if (isServer)
			decay.startCooldown();
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (decay.isNotOnCooldown())
			GameApplet.updater.send("<remove " + number);
		if (isServer && basicAttack.isNotOnCooldown()) {
			basicAttack.startCooldown();
			basicAttack.setTargetFrom(this, this);
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public boolean isCollision(GameObject e) {
		return false;
	}

	@Override
	public boolean isCollidable(GameObject entity) {
		return false;
	}

	@Override
	public boolean isEnemyTo(GameObject e) {
		return false;
	}

	@Override
	public void calculateDamage(Attack a) {
		for (GameObject e : GameApplet.updater.gameObjects) {
			if (e != null && e.isEnemyTo(this)
					&& e.isInRange(getX(), getY(), e.getRadius() + a.range)) {
				GameApplet.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void select() {
		// do not select
	}

	public void renderTerrain() {
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
	}

	@Override
	public void renderUnder() {
		// render nothing
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
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
		sendAnimation("death");
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			GameApplet.app.fill(0, 150);
			GameApplet.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, getRadius() * 2, h);
			GameApplet.app.tint(player.color);
			ImageHandler.drawImage(GameApplet.app, hpImg, xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f,
					getRadius() * 2 * getCurrentHp() / hp_max, h);
			GameApplet.app.tint(255);
		}
	}

	public float calcImportanceOf(GameObject e) {
		float importance = PApplet.abs(
				10000 / (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - getRadius() - e.getRadius()));
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
