package gameStructure.animation;

import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;

public abstract class Attack extends Ability {
	public byte range;
	public byte damage;
	public byte pirce;
	protected GameObject target;

	public Attack(GameBaseApp app, PImage[][] IMG, int duration) {
		super(app, IMG, duration);
	}

	public Attack(GameBaseApp app, PImage[] IMG, int duration) {
		super(app, IMG, duration);
	}

	public Attack(GameBaseApp app, PImage IMG, int duration) {
		super(app, IMG, duration);
	}

	public void setTargetFrom(GameObject attacker, GameObject e) {
	}

	public GameObject getTarget() {
		return target;
	}

	public static void updateExecAttack(GameBaseApp app, String[] c, GameObject attacker) {
		if (c[2].equals("basicAttack") && attacker instanceof Attacker) {
			Attack a = ((Attacker) attacker).getBasicAttack();
			if (a.isNotOnCooldown() && !a.isSetup()) {
				int n = Integer.parseInt(c[3]);
				GameObject e = app.getUpdater().getGameObject(n);
				a.setTargetFrom(attacker, e);
				attacker.setAnimation(a);
				if (attacker instanceof Unit) {
					((Unit) attacker).setMoving(false);
				}
			}
		} else if (c[2].equals("setTarget") && attacker instanceof Attacker) {
			// Attack a = ((Attacker) attacker).getBasicAttack();
			int n = Integer.parseInt(c[3]);
			GameObject e = app.getUpdater().getGameObject(n);
			attacker.sendAnimation("walk " + e.getX() + " " + e.getY() + " true", "Attack.updateExecAttack()");
			// a.setTargetFrom(attacker, e);

			// walk to target and attack
		}
	}

	public static void sendWalkToEnemy(GameObject e, GameObject target, byte range) {
		if (e instanceof Attacker) {
			if (range < PApplet.dist(target.getX(), target.getY(), e.getX(), e.getY())) {
				e.sendAnimation("walk "
						+ (target.getX() + (e.getX() - target.getX())
								/ PApplet.dist(target.getX(), target.getY(), e.getX(), e.getY())
								* (range + target.getRadius() - 1))
						+ " "
						+ (target.getY() + (e.getY() - target.getY())
								/ PApplet.dist(target.getX(), target.getY(), e.getX(), e.getY())
								* (range + target.getRadius() - 1))
						+ " true", "Attack.sendWalkToEnemy()");
			} else {
				e.sendAnimation("stand", "Attack.sendWalkToEnemy()");
			}
		}
	}
}
