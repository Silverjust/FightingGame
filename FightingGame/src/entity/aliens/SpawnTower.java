package entity.aliens;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import game.GameBaseApp;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.GameObject;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Build;
import gameStructure.animation.Death;

public class SpawnTower extends Building implements Commander {
	private int commandingRange;

	RuglingSpawn spawn;

	private int spawnRange;

	private byte maxShootlings;

	private int hp;

	private int hp_max;

	private int armor;

	public PImage iconImg;

	protected String descr = " ";

	protected String stats = " ";

	public Death death;

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		previewImg = standImg = ImageHandler.load(path, "SpawnTower");
	}

	public SpawnTower(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 100);
		spawn = new RuglingSpawn(standImg, 800);

		setAnimation(build);

		// ************************************
		setxSize(30);
		setySize(30);

		kerit = 450;
		pax = 600;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(10000);

		setSight(70);

		setHp(hp_max = 200);
		setRadius(15);

		spawnRange = 150;
		maxShootlings = 12;
		spawn.cooldown = 1900;
		spawn.setCastTime(500);

		commandingRange = 250;

		descr = " ";
		stats = "spawns/s: " + 1 + "/" + spawn.cooldown / 1000.0;
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		float importance = 0;
		GameObject importantEntity = null;
		if (isServer && (getAnimation() == stand)) {// ****************************************************
			for (GameObject e : player.visibleEntities) {
				if (e.isEnemyTo(this)) {
					if (e.isInRange(getX(), getY(), spawnRange + e.getRadius())
							&& !(e instanceof Building)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance) {
							importance = newImportance;
							importantEntity = e;
						}
					}
				}
			}
			if (importantEntity != null && spawn.isNotOnCooldown()
					&& getShootlingsInRange() < maxShootlings) {
				sendAnimation("spawn " + importantEntity.number);
			}
		}
		spawn.updateAbility(this, isServer);

	}

	private int getShootlingsInRange() {
		int n = 0;
		for (GameObject e : player.visibleEntities) {
			if (e instanceof Shootling && e.isAllyTo(this)
					&& e.isInRange(getX(), getY(), spawnRange))
				n++;
		}
		return n;
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("spawn")) {
			int n = Integer.parseInt(c[3]);
			GameObject e = GameBaseApp.updater.getNamedObjects().get(n);
			spawn.setTarget(e);
			setAnimation(spawn);
		}
	}

	@Override
	public void renderTerrain() {
		ImageHandler.drawImage(GameBaseApp.app, AlienMainBuilding.groundImg, xToGrid(getX()),
				yToGrid(getY()), commandingRange * 2, commandingRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, getX(), getY(), commandingRange * 2,
				commandingRange * 2);
	}
	@Override
	public void renderUnder() {}//dont display buildrange
	@Override
	public void renderRange() {
		drawCircle(spawnRange);
		drawCircle((int) (spawnRange * spawn.getCooldownPercent()));
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

	public PImage preview() {
		return previewImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

	public void hit(int damage, byte pirce) {
	
		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);
	
			setHp((int) (getHp() - damage * (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.1)));
			/** check if it was lasthit */
			if (getHp() <= 0 && getHp() != Integer.MAX_VALUE) {// marker
				setHp(-32768);
				onDeath();
			}
	
		}
	}

	public void heal(int heal) {
		setHp(getHp() + heal);
		/** check if it was overheal */
		if (getHp() > hp_max) {
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
					getRadius() * 2 * getHp() / hp_max, h);
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
			return (getAnimation().getClass() != death.getClass()) && getHp() > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public int getHp() {
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
					float yt = target.getY();
					float xt = target.getX();
					byte shootlingRadius = 5;
					GameBaseApp.updater.send("<spawn Shootling "
							+ e.player.getUser().ip
							+ " "
							+ (e.getX() + (xt - e.getX())
									/ PApplet.dist(e.getX(), e.getY(), xt, yt)
									* (e.getRadius() + shootlingRadius))
							+ " "
							+ (e.getY() + (yt - e.getY())
									/ PApplet.dist(e.getX(), e.getY(), xt, yt)
									* (e.getRadius() + shootlingRadius)) + " " + xt
							+ " " + yt + " " + e.number);
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
