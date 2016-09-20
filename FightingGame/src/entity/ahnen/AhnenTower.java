package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;
import game.AimHandler;
import game.ImageHandler;
import game.AimHandler.Cursor;
import game.aim.CustomAim;
import gameStructure.Spell;
import gameStructure.AimingActive;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Build;
import gameStructure.animation.Death;

public class AhnenTower extends Building implements Commander {
	private byte unitHeight;
	private int commandingRange;
	private static PImage standImg;

	private int selectRange;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		standImg = ImageHandler.load(path, "AhnenTower");
	}

	Unit unit;
	private int hp;
	private int hp_max;
	private int armor;
	public PImage iconImg;
	protected String descr = " ";
	protected String stats = " ";
	public Death death;

	public AhnenTower(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);

		setAnimation(build);

		// ************************************
		setxSize(17);
		setySize(17);
		setHeight(5);
		unitHeight = 18;

		kerit = 400;
		pax = 300;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(2000);

		animation.setSight(80);

		setHp(hp_max = 500);
		stats.setRadius(12);

		commandingRange = 100;
		selectRange = 30;

		descr = " ";
		// ************************************
	}

	@Override
	public String getStatistics() {
		if (unit != null && unit instanceof Attacker
				&& ((Attacker) unit).getBasicAttack() != null) {
			stats = "";
			Attack a = ((Attacker) unit).getBasicAttack();
			if (a.pirce >= 0) {
				stats += "dps: " + a.damage + "/" + a.cooldown / 1000.0 + " ="
						+ PApplet.nfc(a.damage / (a.cooldown / 1000.0f), 2)
						+ " (" + a.pirce + ")";
				if (a.targetable == GroundPosition.GROUND)
					stats += " _§";
				else if (a.targetable == GroundPosition.AIR)
					stats += " °§";
				else if (a.targetable == null)
					stats += " _°§";
			} else {
				stats += "heal/s: " + a.damage + "/" + a.cooldown / 1000.0
						+ " ="
						+ PApplet.nfc(a.damage / (a.cooldown / 1000.0f), 2)
						+ "§";
			}
		}
		return super.getStatistics();
	}

	@Override
	public void updateAnimation() {
		super.updateAnimation();
		if (unit != null) {
			unit.updateAnimation();
		}
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (unit != null) {
			if (!GameApplet.GameBaseApp.gameObjects.contains(unit))
				unit.setNumber(number);
			unit.updateDecisions(isServer);
		}
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("in")) {
			int n = Integer.parseInt(c[3]);
			GameObject e = GameBaseApp.getUpdater().getNamedObjects().get(n);
			if (e.isAlive() && isAllyTo(player) && e instanceof Unit) {
				if (unit != null) {
					GameObject normalUnit = null;
					try {
						normalUnit = unit.getClass()
								.getConstructor(String[].class)
								.newInstance(new GameObject[] { null });
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					normalUnit.player = unit.player;
					normalUnit.setX(x);
					normalUnit.setY(getY() + stats.getRadius() + 10);
					normalUnit.setHp(unit.getCurrentHp());
					armor = 0;
					GameApplet.GameBaseApp.toAdd.add(normalUnit);
				}
				unit = (Unit) e;
				unit.setX(x);
				unit.setY(y);
				unit.getStats.setMoving(unit, false);
				unit.isSelected = false;
				unit.setHeight(unitHeight);
				GameApplet.GameBaseApp.toRemove.add(unit);
				if (unit instanceof Berserker)
					((Berserker) unit).getBasicAttack().range = 20;
				if (unit instanceof Warrior) {
					armor = 3;
					((Warrior) unit).getBasicAttack().cooldown = 400;
				}
				if (unit instanceof Astrator) {
					((Astrator) unit).getBasicAttack().range = 30;
					armor = 5;
				}
			}
		}
		if (unit != null)
			unit.exec(c);
	}

	@Override
	public void renderRange() {
		if (unit != null)
			unit.renderRange();
		else
			drawCircle(selectRange);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
		if (unit != null) {
			unit.renderGround();
			unit.renderAir();
		}
	}

	public PImage preview() {
		return standImg;
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
		sendAnimation("death", this);
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			GameBaseApp.app.fill(0, 150);
			GameBaseApp.app.rect(xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f, stats.getRadius() * 2, h);
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, hpImg, xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f,
					stats.getRadius() * 2 * getHp() / hp_max, h);
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

	public static class SelectActive extends Spell implements AimingActive {

		public SelectActive(int x, int y, char n) {
			super(x, y, n, null);
			// cooldown = 60000;
			setClazz(AhnenTower.class);
		}

		@Override
		public void onActivation() {
			GameObject tower = null;
			if (tower == null) {
				for (GameObject e : GameApplet.GameBaseApp.selected) {
					if (e instanceof AhnenTower && e.getAnimation() == e.stand)
						tower = (AhnenTower) e;
				}
			}
			if (tower != null) {
				AimHandler.setAim(new CustomAim(this, Cursor.SELECT));
			}
		}

		@Override
		public String getDescription() {
			return "select unit to stand in tower§unit gets buffed";
		}

		@Override
		public void execute(float x, float y) {
			// float x, y;
			GameObject target = null;
			// x = Entity.xToGrid(Entity.gridToX());
			// y = Entity.xToGrid(Entity.gridToY());
			for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
				if (e.isAllyTo(GameBaseApp.getPlayer())
						&& e instanceof Unit
						&& PApplet.dist(x, y, e.getX(), e.getY() - e.flyHeight()) <= e.getStats().getRadius())
					target = e;
			}
			if (target != null) {
				GameObject tower = null;
				for (GameObject e : GameApplet.GameBaseApp.selected) {
					if (e instanceof AhnenTower
							&& e.getAnimation() == e.stand
							&& target.isInRange(e.getX(), e.getY(),
									((AhnenTower) e).selectRange)
							&& ((AhnenTower) e).unit == null)
						tower = (AhnenTower) e;
				}
				if (tower == null) {
					for (GameObject e : GameApplet.GameBaseApp.selected) {
						if (e instanceof AhnenTower
								&& e.getAnimation() == e.stand
								&& target.isInRange(e.getX(), e.getY(),
										((AhnenTower) e).selectRange))
							tower = (AhnenTower) e;
					}
				}
				if (tower != null)
					tower.sendAnimation("in " + target.getNumber(), this);
				AimHandler.end();
			}
		}
	}
}
