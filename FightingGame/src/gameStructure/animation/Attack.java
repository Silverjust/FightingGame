package gameStructure.animation;

import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Unit;
import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;

public class Attack extends Ability {
	private int range;
	private int damage;
	protected GameObject target;
	private boolean isSetup;

	public Attack(GameBaseApp app, GameObject animated, PImage[][] IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	public Attack(GameBaseApp app, GameObject animated,PImage[] IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	public Attack(GameBaseApp app,GameObject animated, PImage IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	public GameObject getTarget() {
		return target;
	}

	public void setTargetFrom(GameObject from, GameObject to) {
		target = to;
		isSetup = true;
	}

	@Override
	public void setup(GameObject e) {
		super.setup(e);
		if (isNotOnCooldown() && doRepeat(e))
			if (getTarget() != null && getTarget().isAlive()) {
				// System.out.println("MeleeAttack.setup()");
				isSetup = true;
				startCooldown();
			} else {
				e.sendDefaultAnimation(this);
			}
	}

	@Override
	public void onEnd() {
		startCooldown();
	}

	@Override
	public void updateAbility(GameObject e, boolean isServer) {
		/*
		 * if (isSetup())System.out.println("setup"); if
		 * (isEvent())System.out.println("event"); if
		 * (isNotOnCooldown())System.out.println("ncool");
		 */
		if (isSetup() && isEvent() && e.getAnimation() == this) {
			if (getTarget().isAlive() && getTarget().isTargetable() && e.isInRange(getTarget(), getRange())) {
				if (isServer)
					((Entity) e).doAttack(this);
				isSetup = false;
				startCooldown();
			}
		}
	}

	public static void updateExecAttack(GameBaseApp app, String[] c, GameObject attacker) {
		if (c[2].equals("basicAttack") && attacker instanceof Entity) {
			System.out.println("Attack.updateExecAttack()");
			Attack a = ((Entity) attacker).getBasicAttack();
			if (a.isNotOnCooldown() && !a.isSetup()) {
				int n = Integer.parseInt(c[3]);
				GameObject e = app.getUpdater().getGameObject(n);
				a.setTargetFrom(attacker, e);
				attacker.setAnimation(a);
				if (attacker instanceof Unit) {
					((Unit) attacker).setMoving(false);
				}
			}
		} else if (c[2].equals("setTarget") && attacker instanceof Entity) {
			// Attack a = ((Attacker) attacker).getBasicAttack();
			int n = Integer.parseInt(c[3]);
			GameObject e = app.getUpdater().getGameObject(n);
			attacker.sendAnimation("walk " + e.getX() + " " + e.getY() + " true", "Attack.updateExecAttack()");
			// a.setTargetFrom(attacker, e);

			// walk to target and attack
		}
	}

	public static void sendWalkToEnemy(GameObject e, GameObject target, byte range) {
		if (e instanceof Entity) {
			if (range < PApplet.dist(target.getX(), target.getY(), e.getX(), e.getY())) {
				e.sendAnimation(
						"walk " + (target.getX() + (e.getX() - target.getX())
								/ PApplet.dist(target.getX(), target.getY(), e.getX(), e.getY())
								* (range + target.getStats().getRadius().getTotalAmount() - 1))
						+ " "
						+ (target.getY() + (e.getY() - target.getY())
								/ PApplet.dist(target.getX(), target.getY(), e.getX(), e.getY())
								* (range + target.getStats().getRadius().getTotalAmount() - 1)) + " true",
						"Attack.sendWalkToEnemy()");
			} else {
				e.sendAnimation("stand", "Attack.sendWalkToEnemy()");
			}
		}
	}

	@Override
	public boolean isSetup() {
		return getTarget() != null && isSetup;
	}

	public boolean canTargetable(GameObject e) {
		return true;
	}

	@Override
	public boolean doRepeat(GameObject e) {
		return true;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int i) {
		this.damage = i;
	}
}
