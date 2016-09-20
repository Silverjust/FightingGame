package entity.aliens;

import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.Unit;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.ShootAttack;

public class Rug extends Unit implements Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;
	RuglingSpawn spawn;

	private int spawnRange;

	private byte splashrange;

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
		standingImg = game.ImageHandler.load(path, "Rug");
	}

	public Rug(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 500);
		spawn = new RuglingSpawn(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(35);
		setySize(35);

		kerit = 230;
		pax = 400;
		arcanum = 0;
		prunam = 0;
		trainTime = 5000;

		setHp(hp_max = 120);
		getStats.setSpeed(0.7f);
		stats.setRadius(8);
		animation.setSight(90);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = (byte) (stats.getRadius() + 10);
		splashrange = 15;
		basicAttack.range = 35;
		basicAttack.damage = 20;
		basicAttack.cooldown = 1500;
		basicAttack.setCastTime(100);
		basicAttack.speed = 1;

		spawnRange = 150;
		spawn.cooldown = 4000;
		spawn.setCastTime(500);

		descr = " ";
		stats = "spawns/s: " + 1 + "/" + spawn.cooldown / 1000.0;
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		// isTaged = false;
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyTooClose = false;
			boolean isEnemyInHitRange = false;
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), aggroRange + e.getStats().getRadius())) {
							isEnemyTooClose = true;
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(getX(), getY(), basicAttack.range + e.getStats().getRadius())
								&& basicAttack.canTargetable(e)) {
							isEnemyInHitRange = true;
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(getX(), getY(), spawnRange + e.getStats().getRadius())) {
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
				sendAnimation("basicAttack " + importantEntity.getNumber(), this);
			} else if (isEnemyTooClose && importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			} else if (importantEntity != null && spawn.isNotOnCooldown()) {
				sendAnimation("spawn " + importantEntity.getNumber(), this);
			}
		}
		basicAttack.updateAbility(this, isServer);
		spawn.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("spawn")) {
			getStats.setMoving(this, false);
			int n = Integer.parseInt(c[3]);
			GameObject e = GameBaseApp.getUpdater().getNamedObjects().get(n);
			spawn.setTarget(e);
			setAnimation(spawn);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		GameObject target = ((ShootAttack) a).getTarget();
		for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(target.getX(), target.getY(), e.getStats().getRadius() + splashrange)) {
				GameBaseApp.getUpdater().sendDirect("<hit " + e.getNumber() + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(GameObject target, float progress) {
		float x = PApplet.lerp(this.getX(), target.getX(), progress);
		float y = PApplet.lerp(this.getY() - getHeight(), target.getY() - target.getHeight(),
				progress);
		GameBaseApp.app.fill(100, 100, 0);
		GameBaseApp.app.strokeWeight(0);
		GameBaseApp.app.ellipse(xToGrid(x), yToGrid(y), 3, 3);
		GameBaseApp.app.strokeWeight(1);
	}

	@Override
	public void renderRange() {
		super.renderRange();
		drawCircle(spawnRange);
		drawCircle((int) (spawnRange * spawn.getCooldownPercent()));
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
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
		sendAnimation("death", this);
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			GameBaseApp.app.fill(0, 150);
			GameBaseApp.app.rect(xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f, stats.getRadius() * 2, h);
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, hpImg, xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f,
					stats.getRadius() * 2 * getCurrentHp() / hp_max, h);
			GameBaseApp.app.tint(255);
		}
	}

	public float calcImportanceOf(GameObject e) {
		float importance = PApplet.abs(
				10000 / (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - stats.getRadius() - e.getStats().getRadius()));
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
					GameBaseApp.getUpdater().sendDirect("<spawn Rugling " + e.player.getUser().getIp() + " "
							+ e.getX() + " " + (e.getY() + e.getStats().getRadius() + 8) + " " + target.getX()
							+ " " + target.getY());
					/*
					 * ref.updater.send("<spawn Rugling " + e.player.ip + " " +
					 * e.x + " " + (e.y - e.radius - 8) + " " + target.x + " " +
					 * target.y);
					 */
				}
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
