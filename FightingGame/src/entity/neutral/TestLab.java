package entity.neutral;

import processing.core.PApplet;
import processing.core.PImage;
import game.GameApplet;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

@Deprecated
public class TestLab extends Unit {

	private static PImage standingImg;

	byte aggroRange;

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
		standingImg = game.ImageHandler.load(path, "TestLab");

	}

	public TestLab(String[] c) {
		super(c);

		iconImg = standingImg;
		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 1000);
		death = new Death(standingImg, 200);

		setAnimation(stand);
		
		// ************************************
		setxSize(15);
		setySize(15);

		setHp(hp_max = 500);
		setSpeed(2.0f);
		setRadius(10);
		setHeight(40);
		setSight(125);
		groundPosition = GameObject.GroundPosition.AIR;
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		super.updateDecisions(isServer);
		isTaged = false;
		// boolean hasColided = false;
		// float xDeglich = 0;
		// float yDeglich = 0;

		if (getAnimation() == walk) {// ****************************************************
			/*
			 * for (Entity e : player.visibleEntities) { if (e != this) { if
			 * (e.isCollision(this)) { hasColided = true; if (isMoving) {
			 * xDeglich += x - e.x; yDeglich += y - e.y; } } if
			 * (e.isEnemyTo(this)) { } } }
			 */
		} else if (getAnimation() == death) {
			// isMoving = false;
		} else if (true) {
		}

		/*
		 * if (PApplet.dist(x, y, xTarget, yTarget) < 2) { isMoving = false;
		 * sendAnimation("stand"); }
		 */
		/*
		 * if (isMoving && !hasColided) { x = xNext(xTarget + xDeglich, yTarget
		 * + yDeglich); y = yNext(xTarget + xDeglich, yTarget + yDeglich); }
		 * else if (PApplet.dist(x, y, x + xDeglich, y + yDeglich) > radius) { x
		 * = xNext(x + xDeglich, y + yDeglich); y = yNext(x + xDeglich, y +
		 * yDeglich); // isTaged = true; } else { }
		 */

	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
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
			GameApplet.app.fill(0, 150);
			GameApplet.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, getRadius() * 2, h);
			GameApplet.app.tint(player.color);
			ImageHandler.drawImage(GameApplet.app, hpImg, xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f,
					getRadius() * 2 * getCurrentHp() / hp_max, h);
			GameApplet.app.tint(255);
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
