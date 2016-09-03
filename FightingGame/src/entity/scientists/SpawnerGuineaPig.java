package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import game.GameBaseApp;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class SpawnerGuineaPig extends Unit {

	private static PImage standingImg;

	RuglingSpawn spawn;

	private int spawnRange;

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
		standingImg = game.ImageHandler.load(path, "SpawnerGuineaPig");
	}

	public SpawnerGuineaPig(String[] c) {
		super(c);
		GuineaPig.setupEquip(this, c);

		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		spawn = new RuglingSpawn(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(25);
		setySize(25);

		kerit = 230;
		pax = 400;
		arcanum = 20;
		prunam = 0;
		trainTime = 5000;

		setHp(hp_max = 400);
		setSpeed(0.9f);
		setRadius(7);
		setSight(90);
		groundPosition = GameObject.GroundPosition.GROUND;

		spawnRange = 90;
		spawn.cooldown = 6000;
		spawn.setCastTime(500);

		descr = " ";
		stats = "spawns/s: " + 1 + "/" + spawn.cooldown / 1000.0;
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), spawnRange + e.getRadius())) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}

						}
					}
				}
			}
			if (importantEntity != null && spawn.isNotOnCooldown()) {
				sendAnimation("spawn " + importantEntity.number);
			}
		}
		spawn.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("spawn")) {
			setMoving(false);
			int n = Integer.parseInt(c[3]);
			GameObject e = GameBaseApp.updater.getNamedObjects().get(n);
			spawn.setTarget(e);
			setAnimation(spawn);
		}
	}

	@Override
	public boolean isCollidable(GameObject entity) {
		if (entity.getClass() == GuineaPig.class)
			return false;
		return true;
	}

	@Override
	public void renderGround() {
		drawSelected();
		// drawCircle(spawnRange);
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
	}

	@Override
	public void renderRange() {
		super.renderRange();
		drawCircle(spawnRange);
		drawCircle((int) (spawnRange * spawn.getCooldownPercent()));
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

	static class RuglingSpawn extends Ability {

		private GameObject target;

		public RuglingSpawn(PImage IMG, int duration) {
			super(IMG, duration);
		}

		public void setTarget(GameObject e) {
			target = e;
		}

		@Override
		public void updateAbility(GameObject e, boolean isServer) {
			if (target != null && isEvent()) {
				if (isServer) {
					GameBaseApp.updater.sendDirect("<spawn GuineaPig " + e.player.getUser().ip + " "
							+ e.getX() + " " + (e.getY() + e.getRadius() + 8) + " " + target.getX()
							+ " " + target.getY());
				}
				/*
				 * ref.updater.send("<spawn Rugling " + e.player.ip + " " + e.x
				 * + " " + (e.y - e.radius - 8) + " " + target.x + " " +
				 * target.y);
				 */
				target = null;
				// startCooldown();
			}
		}

		@Override
		public boolean isSetup() {
			return target != null;
		}
	}

}
