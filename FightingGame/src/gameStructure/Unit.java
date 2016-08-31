package gameStructure;

import game.GameBaseApp;
import game.ImageHandler;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import processing.core.PApplet;
import processing.core.PGraphics;
import shared.Helper;

public abstract class Unit extends Entity {
	// TODO Flocking einfügen
	// FIXME ADD Verhalten der Einheiten
	// TODO deglich verbessern
	public float xTarget;
	public float yTarget;
	protected byte direction;
	private float speed;
	private float speedMult = 100;

	private boolean isMoving;
	private boolean isAggro;
	public Animation walk;
	
	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		/* code */ }

	public Unit(GameBaseApp app, String[] c) {
		super(app, c);
		if (c != null) {
			xTarget = c.length > 5 ? Float.parseFloat(c[5]) : getX();
			yTarget = c.length > 6 ? Float.parseFloat(c[6]) : getY();
			setMoving(true);
		}
	}

	@Override
	public void updateMovement() {
		float xDeglich = 0;
		float yDeglich = 0;
		boolean hasColided = false;
		if (isMoving() && !isRooted()) {// ****************************************************
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (isCollision(e)) {
						if (e.getAnimation() == e.stand && e.isInRange(xTarget, yTarget, e.getRadius()))
							sendAnimation("stand");
						hasColided = true;
						xDeglich += getX() - e.getX();
						yDeglich += getY() - e.getY();
					}
				}
			}
		} else {// ****************************************************
			// stand still
		}

		if (PApplet.dist(getX(), getY(), xTarget, yTarget) < getSpeed() && getAnimation() == walk) {
			// System.out.println(1000000000+" "+(animation == walk));
			setMoving(false);
			setAnimation(stand);
			sendAnimation("stand");
		}

		if (isMoving() && !hasColided && !isRooted()
				&& PApplet.dist(getX(), getY(), xTarget + xDeglich, yTarget + yDeglich) > getSpeed()) {
			setX(xNext(xTarget + xDeglich, yTarget + yDeglich));
			setY(yNext(xTarget + xDeglich, yTarget + yDeglich));
		} else if (!isRooted() && PApplet.dist(getX(), getY(), getX() + xDeglich, getY() + yDeglich) > getSpeed()) {
			setX(xNext(getX() + xDeglich, getY() + yDeglich));
			setY(yNext(getX() + xDeglich, getY() + yDeglich));
		}
	}

	@Override
	public void renderUnder() {
		direction = Helper.getDirection(getX(), getY(), xTarget, yTarget);
		if (this instanceof Attacker && ((Attacker) this).getBasicAttack().getTarget() != null
				&& ((Attacker) this).getBasicAttack() == getAnimation()) {
			Attack a = ((Attacker) this).getBasicAttack();
			direction = Helper.getDirection(getX(), getY(), a.getTarget().getX(), a.getTarget().getY());
		}
		drawShadow();
	}

	protected float xNext(float X, float Y) {
		return getX() + (X - getX()) / PApplet.dist(getX(), getY(), X, Y) * getSpeed();
	}

	protected float yNext(float X, float Y) {
		return getY() + (Y - getY()) / PApplet.dist(getX(), getY(), X, Y) * getSpeed();
	}

	@Override
	public void drawOnMinimap(PGraphics graphics) {
		graphics.fill(player.color);
		graphics.ellipse(getX(), getY(), getRadius() * 2, getRadius() * 2);
	}

	@Override
	public void exec(String[] c) {
		if (Animation.observe.isAssignableFrom(this.getClass())) {
			System.out.println("Unit.exec()" + c[2]);
		}
		// PApplet.printArray(c);
		super.exec(c);
		String string = c[2];
		if ("walk".equals(string) && getAnimation().isInterruptable()) {
			xTarget = Float.parseFloat(c[3]);
			yTarget = Float.parseFloat(c[4]);
			if (PApplet.dist(getX(), getY(), xTarget, yTarget) >= getSpeed()) {
				setMoving(true);
				if (c.length > 5)
					isAggro = Boolean.valueOf(c[5]);
				setAnimation(walk);
			} else {
				setMoving(false);
				setAnimation(stand);
			}
		}
		Attack.updateExecAttack(player.app, c, this);
	}

	@Deprecated
	public void move(float X, float Y) {
		xTarget = X;
		yTarget = Y;
		setMoving(true);
		setAnimation(walk);
	}

	public void tp(float X, float Y, boolean changeTarget) {
		setX(X);
		setY(Y);
		if (changeTarget) {
			xTarget = X;
			yTarget = Y;
		}
	}

	public void info() {
		player.app.getDrawer().getHud().chat.println(this.getClass().getSimpleName() + "_" + number,
				"(" + getX() + "|" + getY() + ")->(" + xTarget + "|" + yTarget + ")\nhp:" + getCurrentHp());
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (PApplet.dist(getX(), getY(), xTarget, yTarget) >= getSpeed())
			sendAnimation("walk " + xTarget + " " + yTarget + " " + isAggro);
		else {
			sendAnimation("stand");
			if (Animation.observe.isAssignableFrom(this.getClass())) {
				System.out.println(
						"Unit.sendDefaultAnimation()send stand" + PApplet.dist(getX(), getY(), xTarget, yTarget));
			}
		}
	}

	/** gives you the movement-speed */
	public float getSpeed() {
		return speed * speedMult / 100.0f;
	}

	/** sets the movement-speed */
	public void setSpeed(float speed) {
		if (speed <= 0)
			System.out.println("Unit.setSpeed() cant root this way");
		else
			this.speed = speed;
	}

	/** tells if it is normaly able to move */
	public boolean isMoving() {
		return isMoving;
	}

	/** sets own ability to move */
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public float getSpeedMult() {
		return speedMult;
	}

	public void setSpeedMult(float speedMult) {
		this.speedMult = speedMult;
	}
}
