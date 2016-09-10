package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ContentListManager;
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

public class GuineaPig extends Unit implements Attacker, Shooter, Equiping {
	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;
	Equip equip;
	String EquipedUnit;

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
		standingImg = game.ImageHandler.load(path, "GuineaPig");
	}

	public GuineaPig(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		equip = new Equip(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(15);
		setySize(15);

		kerit = 100;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 2000;

		setHp(hp_max = 100);
		armor = 2;
		setSpeed(0.9f);
		setRadius(5);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.damage = 20;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 1500;
		basicAttack.range = 40;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		descr = "just a guineapig§the possibilities are endless";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), aggroRange + e.getRadius())
								&& basicAttack.canTargetable(e)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(getX(), getY(), basicAttack.range + e.getRadius())
								&& basicAttack.canTargetable(e)) {
							isEnemyInHitRange = true;
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
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		equip.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		GameBaseApp.getUpdater().sendDirect("<hit " + basicAttack.getTarget().getNumber() + " "
				+ a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("equip")) {
			Unit unit = null;
			try {
				String name = ContentListManager.getEntityContent().getString(
						c[3]);
				unit = (Unit) Class.forName(name)
						.getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (unit != null) {
				setMoving(false);
				setAnimation(equip);
				equip.setUnit(unit);
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
		GameBaseApp.app.fill(255, 100, 0);
		GameBaseApp.app.strokeWeight(0);
		GameBaseApp.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		GameBaseApp.app.strokeWeight(1);
	}

	@Override
	public void display() {
		super.display();
		if (getAnimation() == equip)
			drawBar(equip.getCooldownPercent());
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

	static void setupEquip(GameObject e, String[] c) {
		if (c != null && c.length > 7 && c[7] != null && c[7].equals("select")) {
			e.select();
		}
	}

	public static class Equip extends Ability {

		private String unit;

		public Equip(PImage IMG, int duration) {
			super(IMG, duration);
		}

		public void setUnit(Unit unit) {
			this.unit = unit.getClass().getSimpleName();
			cooldown = unit.trainTime;
			startCooldown();
		}

		@Override
		public void updateAbility(GameObject e, boolean isServer) {
			if (isSetup() && isEvent()) {
				if (isServer) {
					GameBaseApp.getUpdater().sendDirect("<remove " + e.getNumber());
					GameBaseApp.getUpdater().sendDirect(//
							"<spawn " + unit + " " + e.player.getUser().getIp() + " " + e.getX()
									+ " " + e.getY() + " " + ((Unit) e).xTarget
									+ " " + ((Unit) e).yTarget
									+ (e.isSelected ? " select" : ""));
				}
				unit = null;
			}
		}

		@Override
		public boolean isSetup() {
			return unit != "" && unit != null;
		}

		@Override
		public boolean isInterruptable() {
			return false;
		}
	}

}