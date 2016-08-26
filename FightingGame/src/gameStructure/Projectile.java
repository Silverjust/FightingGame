package gameStructure;

import java.util.ArrayList;

import game.GameApplet;
import processing.core.PApplet;
import shared.Coms;

public class Projectile extends GameObject {
	private float xTarget;
	private float yTarget;
	private GameObject targetObject;
	private boolean isMoving = true;
	private float speed;
	private ArrayList<Entity> collidedEntities = new ArrayList<Entity>();
	protected String origin;

	public Projectile(String[] c) {
		super(c);
		if (c != null && c.length > 7) {
			if (c[5].equals(HOMING)) {
				targetObject = GameApplet.updater.getGameObject(Integer.parseInt(c[6]));
				System.out.println("Projectile.Projectile() homing to" + targetObject);
			} else {
				xTarget = Float.parseFloat(c[5]);
				yTarget = Float.parseFloat(c[6]);
			}
			origin = c[7];
		}

	}

	@Override
	public void updateMovement() {
		if (isMoving()) {// ****************************************************
			for (GameObject o : GameApplet.updater.getGameObjects()) {
				if (o != this && o instanceof Entity) {
					if (isCollision(o)) {
						if (o.isInRange(getX(), getY(), o.getRadius() + getRadius()))
							if (!collidedEntities.contains(o)) {
								collidedEntities.add((Entity) o);
								onHit(o);
							}
					}
				}
			}
		} else {// ****************************************************
			// stand still
		}

		if (PApplet.dist(getX(), getY(), getxTarget(), getyTarget()) < getSpeed()) {
			// System.out.println(1000000000+" "+(animation == walk));
			setMoving(false);
			onEnd();
		}
		if (isMoving() && PApplet.dist(getX(), getY(), getxTarget(), getyTarget()) > getSpeed()) {
			setX(xNext(getxTarget(), getyTarget()));
			setY(yNext(getxTarget(), getyTarget()));
		}
	}

	protected void onEnd() {
		// setSight(0);
		GameApplet.updater.send(Coms.REMOVE + " " + number);
	}

	protected void onHit(GameObject o) {

	}

	protected float xNext(float X, float Y) {
		return getX() + (X - getX()) / PApplet.dist(getX(), getY(), X, Y) * getSpeed();
	}

	protected float yNext(float X, float Y) {
		return getY() + (Y - getY()) / PApplet.dist(getX(), getY(), X, Y) * getSpeed();
	}

	@Override
	public void exec(String[] c) {
		// TODO Auto-generated method stub
		super.exec(c);
	}

	@Override
	public void renderAir() {
		if (isMoving())
			getAnimation().draw(this, 0, getCurrentFrame());
	}

	/** gives you the movement-speed */
	public float getSpeed() {
		return speed;
	}

	/** sets the movement-speed */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	private void setMoving(boolean b) {
		isMoving = b;
	}

	private boolean isMoving() {
		return isMoving;
	}

	public float getxTarget() {
		if (getTargetObject() != null)
			return getTargetObject().getX();
		return xTarget;
	}

	public float getyTarget() {
		if (getTargetObject() != null)
			return getTargetObject().getY();
		return yTarget;
	}

	public GameObject getTargetObject() {
		return targetObject;
	}

	@Override
	public boolean isCollidable(GameObject entity) {
		return false;
	}
}
