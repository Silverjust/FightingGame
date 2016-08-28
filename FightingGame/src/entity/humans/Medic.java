package entity.humans;

import processing.core.PApplet;
import processing.core.PImage;
import game.GameBaseApp;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;

public class Medic extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

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
		standingImg = ImageHandler.load(path, "Medic");
	}

	public Medic(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 500);// 1000

		setAnimation(walk);

		// ************************************
		setxSize(20);
		setySize(20);

		kerit = 20;
		pax = 50;
		arcanum = 0;
		prunam = 0;
		trainTime = 1000;

		setHp(hp_max = 120);
		armor = 1;
		setSpeed(0.9f);
		setRadius(5);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.damage = 3;// heal
		basicAttack.pirce = -1;// heal
		basicAttack.cooldown = 500;
		basicAttack.range = 15;
		basicAttack.setCastTime(400);// eventtime is defined by target distance
		basicAttack.doRepeat = true;

		descr = "medic heals";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e.isAllyTo(this) && e instanceof Scout
						|| e instanceof HeavyAssault || e instanceof Exo
						|| e instanceof Medic) {
					if (e.isInRange(getX(), getY(), aggroRange + e.getRadius())) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance && e.getCurrentHp() < e.hp_max) {
							importance = newImportance;
							importantEntity = e;
							if (e.isInRange(getX(), getY(), basicAttack.range + e.getRadius())) {
								isEnemyInHitRange = true;
							}
						}
					}

				}

			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()
					&& !basicAttack.isSetup()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		isTaged = true;
		GameBaseApp.updater.send("<heal " + basicAttack.getTarget().number + " "
				+ a.damage);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		drawShot();
		drawTaged();
	}

	public void drawShot() {
		if (basicAttack.getTarget() != null&&basicAttack.getTarget().isAlive()) {
			GameObject e = basicAttack.getTarget();
			GameBaseApp.app.stroke(0, 255, 0);
			GameBaseApp.app.line(xToGrid(getX()), yToGrid(getY()), xToGrid(e.getX()), yToGrid(e.getY()));
			GameBaseApp.app.stroke(0);
		}
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
			GameBaseApp.app.fill(0, 150);
			GameBaseApp.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, getRadius() * 2, h);
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, hpImg, xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f,
					getRadius() * 2 * getCurrentHp() / hp_max, h);
			GameBaseApp.app.tint(255);
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