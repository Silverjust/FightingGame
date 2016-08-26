package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import game.AimHandler;
import game.AimHandler.Cursor;
import game.GameApplet;
import game.ImageHandler;
import game.aim.CustomAim;
import gameStructure.Spell;
import gameStructure.AimingActive;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.Unit;
import gameStructure.actives.MultiCDActive;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.Explosion;
import gameStructure.animation.MeleeAttack;
import gameStructure.animation.ShootAttack;

public class Witcher extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack;
	MeleeAttack burst;
	Explosion burstplosion;
	static boolean displayBurstArea = false;

	private static PImage burstImg;

	float burstX, burstY;

	public byte upgradeRange;

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
		standingImg = game.ImageHandler.load(path, "Witcher");
		burstImg = game.ImageHandler.load(path, "burst");
	}

	public Witcher(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		burst = new MeleeAttack(standingImg, 300);
		burstplosion = new Explosion(burstImg, 1000);

		setAnimation(walk);

		// ************************************
		setxSize(15);
		setySize(15);
		setHeight(10);

		kerit = 300;
		pax = 200;
		arcanum = 0;
		prunam = 0;
		trainTime = 1500;

		setHp(hp_max = 100);
		armor = 1;
		setSpeed(0.9f);
		setRadius(7);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = 110;
		basicAttack.damage = 90;// x2
		basicAttack.pirce = 3;
		basicAttack.cooldown = 8000;
		basicAttack.range = 100;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 0.5f;

		burst.damage = 25;
		burst.pirce = 0;
		burst.cooldown = 20000;
		burst.range = 30;
		burst.setCastTime(1000);

		upgradeRange = 100;

		descr = " ";
		stats = "active: " + burst.damage + "/" + burst.cooldown / 1000.0
				+ " (" + burst.pirce + ")" + " _°§";
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
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		burst.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("burst")) {
			burst.setTargetFrom(this, this);
			burstX = Float.parseFloat(c[3]);
			burstY = Float.parseFloat(c[4]);
			burstplosion.setup(null);
			setAnimation(burst);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		if (a == basicAttack) {
			GameApplet.updater.send("<hit " + a.getTarget().number + " " + a.damage
					* 2 + " " + a.pirce);
			// SoundHandler.startIngameSound(HUD.hm, x, y);
		} else {
			for (GameObject e : GameApplet.updater.gameObjects) {
				if (e != null & e.isEnemyTo(this)
						&& e.isInRange(burstX, burstY, e.getRadius() + burst.range)) {
					GameApplet.updater.send("<hit " + e.number + " " + a.damage + " "
							+ a.pirce);
				}
			}
		}
	}

	@Override
	public void renderUnder() {
		super.renderUnder();
		if (!(AimHandler.getAim() instanceof CustomAim))
			displayBurstArea = false;
		if (isAlive() && displayBurstArea && burst.isNotOnCooldown()) {
			GameApplet.app.tint(player.color);
			ImageHandler.drawImage(GameApplet.app, selectedImg, xToGrid(getX()),
					yToGrid(getY()), basicAttack.range * 2, basicAttack.range);
			GameApplet.app.tint(255);
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
	public void renderAir() {
		if (burst.isSetup())
			burstplosion.draw(burstX, burstY);
	}

	@Override
	public void drawShot(GameObject target, float progress) {
		float x = PApplet.lerp(this.getX(), target.getX(), progress);
		float y = PApplet.lerp(this.getY() - getHeight(), target.getY() - target.getHeight(),
				progress);
		GameApplet.app.fill(50, 255, 0);
		GameApplet.app.strokeWeight(0);
		GameApplet.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		GameApplet.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public Attack getBurst() {
		return burst;
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

	public static class BurstActive extends MultiCDActive implements
			AimingActive {

		public BurstActive(int x, int y, char n) {
			super(x, y, n, burstImg);
			// cooldown = 60000;
			setClazz(Witcher.class);
			setAbilityGetter("getBurst");
		}

		@Override
		public void onActivation() {
			Witcher caster = null;
			for (GameObject e : GameApplet.updater.selected) {
				if (e instanceof Witcher
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk)
						&& ((Witcher) e).getBurst().isNotOnCooldown()
						&& !((Witcher) e).getBurst().isSetup())
					caster = (Witcher) e;
			}
			if (caster != null) {
				Witcher.displayBurstArea = true;
				AimHandler.setAim(new CustomAim(this, Cursor.SHOOT));
			}
		}

		@Override
		public String getDescription() {
			return "teleports units  from §physicslab to physicslab";
		}

		@Override
		public void execute(float x, float y) {
			Witcher caster = null;
			for (GameObject e : GameApplet.updater.selected) {
				if (e instanceof Witcher
						&& (e.getAnimation() == e.stand || e.getAnimation() == ((Unit) e).walk)
						&& ((Witcher) e).getBurst().isNotOnCooldown()
						&& !((Witcher) e).getBurst().isSetup()
						&& e.isInRange(x, y,
								((Witcher) e).getBasicAttack().range))
					caster = (Witcher) e;
			}
			if (caster != null) {
				Witcher.displayBurstArea = false;
				caster.sendAnimation("burst " + x + " " + y);
				startCooldown();
			}
		}
	}

	public static class UpgradeActive extends Spell {
		public UpgradeActive(int x, int y, char n) {
			super(x, y, n, Leuchte.healImg);
			setClazz(Witcher.class);
		}

		@Override
		public String getDescription() {
			return "upgrade lampe";
		}

		@Override
		public void onActivation() {
			for (GameObject e : GameApplet.updater.gameObjects) {
				if (e instanceof Leuchte && e.getAnimation() == e.stand) {
					for (GameObject e2 : GameApplet.updater.selected) {
						if (e2 instanceof Witcher
								&& e.isInRange(e2.getX(), e2.getY(),
										((Witcher) e2).upgradeRange)) {
							e.sendAnimation("heal");
						}
					}
				}
			}
		}
	}
}