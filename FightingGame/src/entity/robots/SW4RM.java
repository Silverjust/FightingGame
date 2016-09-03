package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import game.GameBaseApp;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;

public class SW4RM extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	public static void loadImages() {
		// String path = path(new Object() {
		// });
		// standingImg = game.ImageHandler.load(path, "SW4RM");
	}

	public SW4RM(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 2000);

		setAnimation(walk);

		// ************************************
		setxSize(15);
		setySize(15);

		kerit = 170;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 2500;

		setHp(100);
		hp_max = 400;
		armor = 1;
		setSpeed(0.9f);
		setRadius(hpToRadius());
		setSight(70);
		groundPosition = GameObject.GroundPosition.AIR;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.damage = 10;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 1000;
		basicAttack.range = getRadius();
		basicAttack.setCastTime(500);// eventtime is defined by target distance

		descr = "B0T§can move while§attack";
		// ************************************
	}

	private byte hpToRadius() {
		return (byte) (getCurrentHp() * 0.1);
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
			} else if (importantEntity != null && basicAttack.isNotOnCooldown()) {
				// änderung wegen kiter
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		// move while attack
		if ("walk".equals(c[2])) {
			xTarget = Float.parseFloat(c[3]);
			yTarget = Float.parseFloat(c[4]);
			if (PApplet.dist(getX(), getY(), xTarget, yTarget) >= getSpeed()) {
				setMoving(true);
				// isAggro = Boolean.valueOf(c[5]);
				// setAnimation(walk);
			} else {
				setMoving(false);
				// setAnimation(stand);
			}
		}
		if (c[2].equals("basicAttack"))
			setMoving(true);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
			if (e != null && e.isInRange(getX(), getY(), e.getRadius() + a.range)
					&& a.canTargetable(e) && e.isEnemyTo(this)) {
				GameBaseApp.updater.sendDirect("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
				if (e.isMortal())
					setHp(getCurrentHp() + a.damage
							* (1.0 - ((e.armor - a.pirce > 0) ? e.armor
									- a.pirce : 0) * 0.05));
			}
		}
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

}