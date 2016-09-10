package entity.ahnen;

import game.AimHandler;
import game.AimHandler.Cursor;
import game.ImageHandler;
import game.aim.Aim;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.actives.MultiCDActive;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;

public class Berserker extends Unit implements Attacker {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	Ability buildLeuchte;
	byte attackDistance;

	public byte buildRange;

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
		standingImg = game.ImageHandler.load(path, "Berserker");

	}

	public Berserker(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);
		buildLeuchte = new Ability(standingImg, 500);

		setAnimation(walk);

		// ************************************
		setxSize(15);
		setySize(15);
		setHeight(10);

		kerit = 300;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 4000;

		setHp(hp_max = 250);
		armor = 1;
		setSpeed(0.9f);
		setRadius(7);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.range = 10;
		basicAttack.damage = 45;
		basicAttack.cooldown = 1500;
		basicAttack.setCastTime(100);
		attackDistance = 10;

		buildRange = 127;
		buildLeuchte.cooldown = 40000;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer && (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), aggroRange + e.getRadius()) && basicAttack.canTargetable(e)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(getX(), getY(), basicAttack.range + e.getRadius()) && basicAttack.canTargetable(e)) {
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
		buildLeuchte.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("buildLeuchte")) {
			buildLeuchte.startCooldown();
			setAnimation(buildLeuchte);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		float x, y, xDirection = a.getTarget().getX(), yDirection = a.getTarget().getY();
		x = (this.getX() + (xDirection - this.getX()) / PApplet.dist(this.getX(), this.getY(), xDirection, yDirection) * (attackDistance));
		y = (this.getY() + (yDirection - this.getY()) / PApplet.dist(this.getX(), this.getY(), xDirection, yDirection) * (attackDistance));
		for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
			if (e != null & e.isEnemyTo(this) && e.isInRange(x, y, e.getRadius() + a.range)) {
				GameBaseApp.getUpdater().sendDirect(
						DAMAGE + S + e.getNumber() + " " + (e instanceof Building ? a.damage / 4 : a.damage) + " " + a.pirce);
			}
		}
	}

	@Override
	public void renderUnder() {
		super.renderUnder();
		if (isAlive() && AimHandler.getAim() instanceof LeuchteAim && buildLeuchte.isNotOnCooldown()) {
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, selectedImg, xToGrid(getX()), yToGrid(getY()), buildRange * 2, buildRange);
			GameBaseApp.app.tint(255);
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
	}

	@Override
	public void renderRange() {
		if (this instanceof Unit) {
			GameBaseApp.app.line(getX(), getY() / 2, ((Unit) this).xTarget, ((Unit) this).yTarget / 2);
		}
		if (basicAttack.getTarget() != null) {

			float x, y, xDirection = basicAttack.getTarget().getX(), //
					yDirection = basicAttack.getTarget().getY();
			x = (this.getX()
					+ (xDirection - this.getX()) / PApplet.dist(this.getX(), this.getY(), xDirection, yDirection) * (attackDistance));
			y = (this.getY()
					+ (yDirection - this.getY()) / PApplet.dist(this.getX(), this.getY(), xDirection, yDirection) * (attackDistance));
			drawCircle(x, y, basicAttack.range);
			drawCircle(x, y, (byte) (basicAttack.range * basicAttack.getCooldownPercent()));
		}
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public Ability getAbility() {
		return buildLeuchte;
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

	public static class LeuchteActive extends MultiCDActive {
		Class<? extends Building> building;
		String descr = " ", stats = " ";

		public LeuchteActive(int x, int y, char n, GameObject u, Class<?> trainer) {
			super(x, y, n, u.iconImg);
			setClazz(trainer);
			setAbilityGetter("getAbility");
			building = ((Building) u).getClass();
			descr = u.getDescription();
			stats = u.getStatistics();
		}

		@Override
		public void onActivation() {
			GameObject builder = null;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (getClazz().isAssignableFrom(e.getClass())) {
					builder = e;
				}
			}
			if (builder != null) {
				AimHandler.setAim(new LeuchteAim(this, builder));
			}
		}

		@Override
		public String getDescription() {
			return descr;
		}

		@Override
		public String getStatistics() {
			return "cooldown: " + getCooldown() + stats;
		}
	}

	public static class LeuchteAim extends Aim {

		private GameObject builder;
		private LeuchteActive active;
		private Leuchte buildable;

		public LeuchteAim(LeuchteActive leuchteActive, GameObject builder) {
			this.active = leuchteActive;
			this.builder = builder;
			buildable = new Leuchte(null);
		}

		@Override
		public Cursor getCursor() {
			return Cursor.BUILD;
		}

		@Override
		public void update() {
			float x, y;
			x = Building.xToGrid(Building.gridToX(app.mouseX));
			y = Building.xToGrid(Building.gridToY(player.app.mouseY));
			if (canPlaceAt(x, y)) {
				GameBaseApp.app.tint(255, 150);
			} else {
				GameBaseApp.app.tint(255, 100, 100, 150);
			}
			ImageHandler.drawImage(GameBaseApp.app, buildable.preview(), x, y / 2, buildable.getxSize(), buildable.getySize());
			GameBaseApp.app.tint(255);
		}

		@Override
		public void execute(float x, float y) {
			if (canPlaceAt(x, y)) {
				GameBaseApp.getUpdater().sendDirect(SPAWN + S + buildable.getClass().getSimpleName() + " " + builder.player.getUser().getIp()
						+ " " + x + " " + y);
				buildable.buyFrom(builder.player);
			}
			GameObject builder = null;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof Berserker && e.player == this.builder.player
						&& e.isInRange(x, y, ((Berserker) e).buildRange)
						&& ((Berserker) e).buildLeuchte.isNotOnCooldown()) {
					builder = e;
				}
			}
			if (builder != null) {
				active.startCooldown();
				builder.sendAnimation("buildLeuchte " + x + " " + y, this);
			}
		}

		protected boolean canPlaceAt(float x, float y) {
			boolean placeFree = true;
			boolean inBerserkerRange = false;
			for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
				if (e.isInRange(x, y, buildable.getRadius() + e.getRadius()) && e.groundPosition == GroundPosition.GROUND)
					placeFree = false;
				if (e instanceof Berserker && e.player == builder.player
						&& e.isInRange(x, y, ((Berserker) e).buildRange)
						&& ((Berserker) e).buildLeuchte.isNotOnCooldown()) {
					inBerserkerRange = true;
				}
			}
			return placeFree && inBerserkerRange;
		}
	}
}
