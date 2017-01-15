package gameStructure;

import java.util.ArrayList;

import processing.core.PApplet;
import shared.Coms;
import shared.GameBaseApp;

public class Projectile extends GameObject {
	private float xTarget;
	private float yTarget;
	private GameObject targetObject;
	private boolean isMoving = true;
	private float speed;
	private ArrayList<Entity> collidedEntities = new ArrayList<Entity>();
	protected String originInfo;
	protected GameObject origin;

	public Projectile(GameBaseApp app, String[] c) {
		super(app, c);
		if (c != null && c.length > 9) {
			int n = Integer.parseInt(c[6]);
			origin = player.app.getUpdater().getGameObject(n);
			if (c[7].equals(HOMING)) {
				targetObject = app.getUpdater().getGameObject(Integer.parseInt(c[8]));
				System.out.println("Projectile.Projectile() homing to" + targetObject);
			} else {
				xTarget = Float.parseFloat(c[7]);
				yTarget = Float.parseFloat(c[8]);
			}
			originInfo = c[9];
		} else if (c != null) {
			System.err.println("Projectile.Projectile() wrong spawn command");
		}

	}

	@Override
	public void updateMovement() {

		if (isMoving() && PApplet.dist(getX(), getY(), getxTarget(), getyTarget()) > getSpeed()) {
			setX(xNext(getxTarget(), getyTarget()));
			setY(yNext(getxTarget(), getyTarget()));
		}
	}

	@Override
	public void updateDecisions(boolean isServer) {

		if (isMoving()) {// ****************************************************
			for (GameObject o : player.app.getUpdater().getGameObjects()) {
				if (o != this && o instanceof Entity) {
					if (isCollision(o)) {
						if (!collidedEntities.contains(o)) {
							collidedEntities.add((Entity) o);
							onHit(o, isServer);
						}
					}
				}
			}
		} // ****************************************************

		if (PApplet.dist(getX(), getY(), getxTarget(), getyTarget()) < getSpeed()) {
			// System.out.println(1000000000+" "+(animation == walk));
			setMoving(false);
			onEnd(isServer);
		}

		super.updateDecisions(isServer);
	}

	protected void onEnd(boolean isServer) {
		// setSight(0);
		if (isServer)
			player.app.getUpdater().send(Coms.REMOVE + " " + getNumber());
	}

	protected void onHit(GameObject o, boolean isServer) {

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
		if (isMoving()) {
			getAnimation().draw(this, player.app.millis() / 1000.f, getCurrentFrame());
		}
	}

	/** gives you the movement-speed */
	public float getSpeed() {
		return speed / 100.f;
	}

	/** sets the movement-speed */
	public void setSpeed(int speed) {
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

	static public float cutProjectileRangeX(float x, float y, int maxRange, int minRange) {
		float a = PApplet.atan2(y, x);
		float d = PApplet.dist(0, 0, x, y);
		if (d > maxRange)
			d = maxRange;
		if (d < minRange)
			d = minRange;
		return d * PApplet.cos(a);
	}

	static public float cutProjectileRangeY(float x, float y, int maxRange, int minRange) {

		float a = PApplet.atan2(y, x);
		float d = PApplet.dist(0, 0, x, y);
		if (d > maxRange)
			d = maxRange;
		if (d < minRange)
			d = minRange;
		return d * PApplet.sin(a);
	}
}
