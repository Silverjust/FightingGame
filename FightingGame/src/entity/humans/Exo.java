package entity.humans;

import game.GameBaseApp;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import processing.core.PApplet;
import processing.core.PImage;

public class Exo extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	MeleeAttack instaAttack;
	Hook hook;

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
		standingImg = game.ImageHandler.load(path, "Exo");
	}

	public Exo(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);
		instaAttack = new MeleeAttack(standingImg, 800);
		hook = new Hook(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(20);
		setySize(20);

		kerit = 127;
		pax = 0;
		arcanum = 40;
		prunam = 0;
		trainTime = 3000;

		setHp(hp_max = 160);
		setSpeed(0.7f);
		setRadius(8);
		setSight(80);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.range = (byte) (getRadius() + 10);
		basicAttack.damage = 30;
		basicAttack.cooldown = 1200;
		basicAttack.setCastTime(500);
		basicAttack.targetable=groundPosition;

		instaAttack.range = (byte) (getRadius() + 10);
		instaAttack.damage = 30;
		instaAttack.cooldown = 4000;
		instaAttack.setCastTime(100);

		hook.range = (byte) (getRadius() + 10);
		hook.damage = 55;
		hook.pirce = 2;
		hook.cooldown = 5000;
		hook.speed = 2.2f;

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
								&& basicAttack.canTargetable(e)) {
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
			} else if (importantEntity != null && !isEnemyInHitRange
					&& hook.isNotOnCooldown()) {
				sendAnimation("hook " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		instaAttack.updateAbility(this, isServer);
		hook.updateAbility(this, isServer);
	}

	@Override
	public void updateMovement() {
		if (getAnimation() == hook) {
			setSpeed(getSpeed() + hook.speed);
		}
		super.updateMovement();
		if (getAnimation() == hook) {
			setSpeed(getSpeed() - hook.speed);
		}
	}

	@Override
	public boolean isCollision(GameObject e) {
		boolean b = !(getAnimation() == hook && e != hook.getTarget());
		return super.isCollision(e) && b;
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("hook")) {
			int n = Integer.parseInt(c[3]);
			GameObject e = GameBaseApp.updater.getNamedObjects().get(n);
			hook.setTargetFrom(this, e);
			xTarget = e.getX();
			yTarget = e.getY();
			setAnimation(hook);
		} else if (c[2].equals("instaAttack")) {
			int n = Integer.parseInt(c[3]);
			GameObject e = GameBaseApp.updater.getNamedObjects().get(n);
			instaAttack.setTargetFrom(this, e);
			setAnimation(instaAttack);
		}
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (oldAnimation == hook && ((Hook) oldAnimation).getTarget() != null) {
			sendAnimation("instaAttack "
					+ ((MeleeAttack) oldAnimation).getTarget().number);
		} else {
			sendAnimation("walk " + xTarget + " " + yTarget + " true");
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		GameBaseApp.updater.send("<hit " + ((MeleeAttack) a).getTarget().number + " "
				+ a.damage + " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		if (getAnimation() == hook)
			drawShot();
		drawTaged();
	}

	public void drawShot() {
		if (hook.getTarget() != null) {
			GameObject e = hook.getTarget();
			GameBaseApp.app.stroke(150);
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

	public static class Hook extends MeleeAttack {

		public float speed;

		public Hook(PImage IMG, int duration) {
			super(IMG, duration);
		}

		@Override
		public void updateAbility(GameObject e, boolean isServer) {
			if (isSetup() && isNotOnCooldown()
					&& target.isInRange(e.getX(), e.getY(), e.getRadius() + target.getRadius())) {
				if (isServer) {
					e.sendDefaultAnimation(this);
				}
				isSetup = false;
				// startCooldown();
			}
		}

	}

}
