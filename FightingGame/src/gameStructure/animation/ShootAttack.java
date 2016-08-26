package gameStructure.animation;

import gameStructure.GameObject;
import gameStructure.Shooter;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Updater;

public class ShootAttack extends MeleeAttack {
	private int beginTime;
	public float speed;

	public ShootAttack(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public ShootAttack(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public ShootAttack(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void setTargetFrom(GameObject from, GameObject to) {
		target = to;
		isSetup = true;
		isExploding = false;
		eventTime = beginTime
				+ (int) (PApplet.dist(from.getX(), from.getY(), to.getX(), to.getY()) / speed);
	}

	@Override
	public void setCastTime(int castTime) {
		beginTime = castTime;
	}

	@Override
	public void drawAbility(GameObject e, byte d) {
		if (e instanceof Shooter) {
			if (isSetup() && getProgressPercent() < 1) {
				((Shooter) e).drawShot(target, getProgressPercent());
			}
		} else {
			System.err.println(e.getClass().getSimpleName()
					+ " should be shooter");
		}
		super.drawAbility(e, d);
	}

	@Override
	public float getProgressPercent() {
		float f = 1
				- (float) (start + beginTime + eventTime - Updater.Time
						.getMillis()) / eventTime;
		return f > 1 || f < 0 ? 1 : f;
	}
}
/*
 * float importance = 0; Entity importantEntity = null; for (Entity e :
 * player.visibleEntities) { if (e != this) { if (e.isEnemyTo(this)) { if
 * (e.isCollision(x, y, basicAttack.range + e.radius) && e.groundPosition ==
 * GroundPosition.GROUND) { float newImportance = calcImportanceOf(e); if
 * (newImportance > importance) { importance = newImportance; importantEntity =
 * e; } } } } } if (importantEntity != null &&
 * getBasicAttack().isNotOnCooldown()) { // System.out.println(thread);
 * sendAnimation("basicAttack " + importantEntity.number); }
 */
