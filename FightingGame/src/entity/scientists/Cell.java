package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import game.GameApplet;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;

public class Cell extends Unit implements Attacker {

	private static PImage standingImg;

	MeleeAttack heal;

	private byte aggroRange;

	private byte healAmount;

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
		standingImg = game.ImageHandler.load(path, "Cell");
	}

	public Cell(String[] c) {
		super(c);
		GuineaPig.setupEquip(this, c);

		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		heal = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(15);
		setySize(15);

		kerit = 35;
		pax = 40;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		setHp(hp_max = 30);
		armor = 1;
		setSpeed(0.9f);
		setRadius(7);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = 60;
		healAmount = 15;
		heal.damage = 10;
		heal.pirce = 0;
		heal.cooldown = 1500;
		heal.range = 30;
		heal.setCastTime(100);

		descr = " ";
		stats = "heal/s: " + healAmount + "/" + (heal.cooldown / 1000.0);
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e.isAllyTo(this)) {
					if (e.isInRange(getX(), getY(), aggroRange + e.getRadius())
							&& heal.canTargetable(e)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance && e.getCurrentHp() < e.hp_max) {
							importance = newImportance;
							importantEntity = e;
						}
					}

				}

			}
			if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, heal.range);
			}
		}
		if (heal.isNotOnCooldown()) {
			heal.startCooldown();
			heal.setTargetFrom(this, this);
		}
		heal.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (GameObject e : GameApplet.updater.gameObjects) {
			if (e != null && e.isInRange(getX(), getY(), e.getRadius() + a.range))
				if (e.isAllyTo(this)) {
					GameApplet.updater.send("<heal " + e.number + " " + healAmount);
				} else if (e.isEnemyTo(this)) {
					GameApplet.updater.send("<hit " + e.number + " " + heal.damage
							+ " 0");
				}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		heal.drawAbility(this, direction);
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