package entity.aliens;

import game.GameBaseApp;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import processing.core.PApplet;
import processing.core.PImage;

public class Arol extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	byte attackDistance;

	private float xDirection, yDirection;

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
		standingImg = game.ImageHandler.load(path, "Arol");
	}

	public Arol(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(50);
		setySize(35);

		kerit = 650;
		pax = 0;
		arcanum = 100;
		prunam = 0;
		trainTime = 10000;

		setHp(hp_max = 1900);
		armor = 3;
		setSpeed(0.4f);
		setRadius(10);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.range = 10;
		basicAttack.damage = 40;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(100);
		basicAttack.targetable = groundPosition;
		attackDistance = 10;

		descr = " ";
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
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), aggroRange + e.getRadius())
								&& basicAttack.canTargetable(e)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(getX(), getY(), basicAttack.range + e.getRadius())
								&& e.groundPosition == GroundPosition.GROUND) {
							isEnemyInHitRange = true;
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("basicAttack")) {
			xDirection = basicAttack.getTarget().getX();
			yDirection = basicAttack.getTarget().getY();
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		float x, y;
		x = (this.getX() + (xDirection - this.getX())
				/ PApplet.dist(this.getX(), this.getY(), xDirection, yDirection)
				* (attackDistance));
		y = (this.getY() + (yDirection - this.getY())
				/ PApplet.dist(this.getX(), this.getY(), xDirection, yDirection)
				* (attackDistance));
		for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(x, y, e.getRadius() + a.range)
					&& basicAttack.canTargetable(e)) {
				GameBaseApp.updater.sendDirect("<hit " + e.number + " "
						+ (e instanceof Building ? a.damage * 2 : a.damage)
						+ " " + a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
	}

	@Override
	public void renderRange() {
		if (this instanceof Unit) {
			GameBaseApp.app.line(getX(), getY() / 2, ((Unit) this).xTarget,
					((Unit) this).yTarget / 2);
		}
		float x, y;
		x = (this.getX() + (xDirection - this.getX())
				/ PApplet.dist(this.getX(), this.getY(), xDirection, yDirection)
				* (attackDistance));
		y = (this.getY() + (yDirection - this.getY())
				/ PApplet.dist(this.getX(), this.getY(), xDirection, yDirection)
				* (attackDistance));
		drawCircle(x, y, basicAttack.range);
		drawCircle(x, y,
				(byte) (basicAttack.range * basicAttack.getCooldownPercent()));
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
