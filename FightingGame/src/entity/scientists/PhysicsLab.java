package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;
import shared.Nation;
import game.AimHandler;
import game.AimHandler.Cursor;
import game.ImageHandler;
import game.aim.CustomAim;
import gameStructure.AimingActive;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.actives.MultiCDActive;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class PhysicsLab extends Lab {

	private static PImage standingImg;
	public static PImage teleportImg;

	private Ability sendTeleport;
	private Animation recievieTeleport;
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
		standingImg = ImageHandler.load(path, "PhysicsLab");
		teleportImg = ImageHandler.load(Nation.SCIENTISTS.toString()
				+ "/symbols/", "teleport");
	}

	public PhysicsLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		sendTeleport = new Ability(standingImg, 800);
		recievieTeleport = new Animation(standingImg, 800);

		setAnimation(walk);
		
		// ************************************

		kerit = 600;
		pax = 0;
		arcanum = 30;
		prunam = 0;
		trainTime = TRAINTIME;

		sendTeleport.cooldown = 60000;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if ("sendTeleport".equals(c[2])) {
			setAnimation(sendTeleport);
		} else if ("recievieTeleport".equals(c[2])) {
			setAnimation(recievieTeleport);
		}
	}

	public Ability getTeleport() {
		return sendTeleport;
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

	public static class TeleportActive extends MultiCDActive implements
			AimingActive {

		private PhysicsLab origin;

		public TeleportActive(int x, int y, char n) {
			super(x, y, n, teleportImg);
			// cooldown = 60000;
			setClazz(PhysicsLab.class);
			setAbilityGetter("getTeleport");
		}

		@Override
		public void onActivation() {
			origin = null;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof PhysicsLab
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk))
					origin = (PhysicsLab) e;
			}
			if (origin != null) {
				origin.sendAnimation("sendTeleport", this);
				AimHandler.setAim(new CustomAim(this, Cursor.SELECT));
			}
		}

		@Override
		public String getDescription() {
			return "teleports units  from §physicslab to physicslab";
		}

		@Override
		public void execute(float x, float y) {
			// float x, y;
			GameObject target = null;
			// x = Entity.xToGrid(Entity.gridToX());
			// y = Entity.xToGrid(Entity.gridToY());
			for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
				if (e.isAllyTo(GameBaseApp.getPlayer()) && e instanceof PhysicsLab
						&& PApplet.dist(x, y, e.getX(), e.getY() - e.flyHeight()) <= e.getStats().getRadius())
					target = e;
			}
			if (target != null) {
				PhysicsLab origin = this.origin;
				origin.getTeleport().startCooldown();
				target.sendAnimation("recieveTeleport", this);
				for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
					if (e.isAllyTo(GameBaseApp.getPlayer())
							&& e instanceof Unit
							&& !(e instanceof Lab)
							&& e.isInRange(origin.getX(), origin.getY(),
									origin.equipRange))
						GameBaseApp.getUpdater().sendDirect("<tp " + e.getNumber() + " "
								+ (e.getX() + target.getX() - origin.getX()) + " "
								+ (e.getY() + target.getY() - origin.getY()));
				}
				startCooldown();
				AimHandler.end();
			}
		}
	}

}