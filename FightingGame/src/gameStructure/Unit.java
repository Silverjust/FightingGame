package gameStructure;

import game.ImageHandler;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import processing.core.PApplet;
import processing.core.PGraphics;
import shared.GameBaseApp;
import shared.Helper;

public abstract class Unit extends Entity implements Attacker {
	// TODO Flocking einfügen
	// FIXME ADD Verhalten der Einheiten
	// TODO deglich verbessern
	public float xTarget;
	public float yTarget;
	protected byte direction;
	private boolean isMoving;
	private boolean isAggro;
	public Animation walk;

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		/* code */ }

	public Unit(GameBaseApp app, String[] c) {
		super(app, c);
		if (c != null) {
			xTarget = c.length > 6 ? Float.parseFloat(c[6]) : getX();
			yTarget = c.length > 7 ? Float.parseFloat(c[7]) : getY();
			setMoving(true);
		}
	}

	@Override
	public void initStats(GameBaseApp app) {
		stats = new UnitStats(app);
	}

	@Override
	public UnitStats getStats() {
		return (UnitStats) stats;
	}

	@Override
	public void updateMovement() {
		float xDeglich = 0;
		float yDeglich = 0;
		boolean hasColided = false;
		if (isMoving() && !getStats().isRooted()) {// ****************************************************
			for (GameObject e : player.visibleGObjects) {
				if (e != this) {
					if (isCollision(e)) {
						if (e.getAnimation() == e.stand
								&& e.isInRange(xTarget, yTarget, e.getStats().getRadius().getTotalAmount()))
							sendAnimation("stand", this);
						hasColided = true;
						xDeglich += getX() - e.getX();
						yDeglich += getY() - e.getY();
					}
				}
			}
		} else {// ****************************************************
			// stand still
		}

		if (PApplet.dist(getX(), getY(), xTarget, yTarget) < (float) getStats().getMovementSpeed().getTotalAmountF()
				&& getAnimation() == walk) {
			// System.out.println(1000000000+" "+(animation == walk));
			setMoving(false);
			setAnimation(stand);
			sendAnimation("stand", this);
		}

		if (isMoving() && !hasColided && !getStats().isRooted() && PApplet.dist(getX(), getY(), xTarget + xDeglich,
				yTarget + yDeglich) > (float) getStats().getMovementSpeed().getTotalAmountF()) {
			setX(xNext(xTarget + xDeglich, yTarget + yDeglich));
			setY(yNext(xTarget + xDeglich, yTarget + yDeglich));
		} else if (!getStats().isRooted() && PApplet.dist(getX(), getY(), getX() + xDeglich,
				getY() + yDeglich) > (float) getStats().getMovementSpeed().getTotalAmountF()) {
			setX(xNext(getX() + xDeglich, getY() + yDeglich));
			setY(yNext(getX() + xDeglich, getY() + yDeglich));
		}
	}

	@Override
	public void renderUnder() {
		direction = Helper.getDirection(getX(), getY(), xTarget, yTarget);
		if (getBasicAttack().getTarget() != null && getBasicAttack() == getAnimation()) {
			Attack a = getBasicAttack();
			direction = Helper.getDirection(getX(), getY(), a.getTarget().getX(), a.getTarget().getY());
		}
		drawShadow();
	}

	protected float xNext(float X, float Y) {
		return getX() + (X - getX()) / PApplet.dist(getX(), getY(), X, Y)
				* (float) getStats().getMovementSpeed().getTotalAmountF();
	}

	protected float yNext(float X, float Y) {
		return getY() + (Y - getY()) / PApplet.dist(getX(), getY(), X, Y)
				* (float) getStats().getMovementSpeed().getTotalAmountF();
	}

	@Override
	public void drawOnMinimap(PGraphics graphics) {
		graphics.fill(player.color);
		graphics.ellipse(getX(), getY(), getStats().getRadius().getTotalAmount() * 2,
				getStats().getRadius().getTotalAmount() * 2);
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
			if (PApplet.dist(getX(), getY(), xTarget,
					yTarget) >= (float) getStats().getMovementSpeed().getTotalAmountF()) {
				setMoving(true);
				if (c.length > 5)
					isAggro = Boolean.valueOf(c[5]);
				setAnimation(walk);
			} else {
				setMoving(false);
				setAnimation(stand);
			}
		}
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
		player.app.write(this.getClass().getSimpleName() + "_" + getNumber(), "(" + getX() + "|" + getY() + ")->("
				+ xTarget + "|" + yTarget + ")\nhp:" + getStats().getHp().getCurrentAmount());
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (PApplet.dist(getX(), getY(), xTarget, yTarget) >= (float) getStats().getMovementSpeed().getTotalAmountF())
			sendAnimation("walk " + xTarget + " " + yTarget + " " + isAggro, this);
		else {
			sendAnimation("stand", this);
			if (Animation.observe.isAssignableFrom(this.getClass())) {
				System.out.println(
						"Unit.sendDefaultAnimation()send stand" + PApplet.dist(getX(), getY(), xTarget, yTarget));
			}
		}
	}

	/** tells if it is normaly able to move */
	public boolean isMoving() {
		return isMoving;
	}

	/** sets own ability to move */
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
}
