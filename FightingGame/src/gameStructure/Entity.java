package gameStructure;

import java.util.ArrayList;

import gameStructure.BasicAttackProjectile.BaAtInfo;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.BasicAttackAnim;
import gameStructure.animation.Death;
import gameStructure.baseBuffs.Buff;
import gameStructure.items.InventoryItem;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Coms;
import shared.GameBaseApp;
import shared.Helper;
import shared.Player;

public class Entity extends GameObject {

	public PImage iconImg;
	public Death death;
	public BasicAttackAnim basicAttack;
	public Animation stand;

	private int hpBarLength;
	private ArrayList<Buff> buffs = new ArrayList<Buff>();
	private ArrayList<Buff> buffsToRemove = new ArrayList<Buff>();
	protected int aggroRange = 50;
	private ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();

	public Entity(GameBaseApp app, String[] c) {
		super(app, c);
	}

	@Override
	public void initStats(GameBaseApp app) {
		stats = new EntityStats(app);
	}

	@Override
	public EntityStats getStats() {
		return (gameStructure.EntityStats) stats;
	}

	@Override
	public void display() {
		drawHpBar();
	}

	public void sendDamage(Damage damage, Player player, String origin) {
		player.app.getUpdater().send(
				Coms.DAMAGE + " " + getNumber() + " " + damage.get() + " " + player.getUser().getIp() + " " + origin);
	}

	@Override
	public void updateDecisions(boolean isServer) {
		for (int i = buffsToRemove.size() - 1; i >= 0; i--) {
			Buff buff = buffsToRemove.get(i);
			if (buff != null) {
				getBuffs().remove(buff);
				buffsToRemove.remove(i);
			}
		}
		for (Buff buff : getBuffs()) {
			buff.updateDecisions(isServer);
		}

		if (basicAttack != null)
			updateBasicAttackBehavior(isServer);
	}

	public void updateBasicAttackBehavior(boolean isServer) {
		// System.out.println("Entity.updateBasicAttackBehavior()");
		/*
		 * if (isServer && (getAnimation().isInterruptable() && isAggro ||
		 * getAnimation() == stand)) {//
		 * ****************************************************
		 * System.out.println("Entity.updateBasicAttackBehavior()"); boolean
		 * isEnemyInHitRange = false; float importance = 0; Entity
		 * importantEntity = null; for (GameObject e : player.visibleGObjects) {
		 * if (e instanceof Entity && e != this) { if (e.isEnemyTo(this)) { if
		 * (e.isInRange(getX(), getY(), aggroRange + e.getStats().getRadius())
		 * && basicAttack.canTargetable(e)) { float newImportance =
		 * calcImportanceOf((Entity) e); if (newImportance > importance) {
		 * importance = newImportance; importantEntity = (Entity) e; } } if
		 * (e.isInRange(getX(), getY(), basicAttack.range +
		 * e.getStats().getRadius()) && basicAttack.canTargetable(e)) {
		 * isEnemyInHitRange = true; float newImportance =
		 * calcImportanceOf((Entity) e); if (newImportance > importance) {
		 * importance = newImportance; importantEntity = (Entity) e; } } } } }
		 * if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
		 * System.out.println("Entity.updateBasicAttackBehavior()attack!!!!");
		 * sendAnimation("basicAttack " + importantEntity.getNumber(), this); }
		 * else if (importantEntity != null) {
		 * System.out.println("Entity.updateBasicAttackBehavior() walk");
		 * Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range); } }
		 */
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		try {
			String string = c[2];
			if ("death".equals(string)) {
				if (this instanceof Unit)
					((Unit) this).setMoving(false);
				setAnimation(death);
			}
		} catch (Exception e) {
			System.err.println(getAnimation() + " " + getNextAnimation());
			PApplet.printArray(c);
			e.printStackTrace();
		}
		Attack.updateExecAttack(player.app, c, this);
	}

	public void info() {
		player.app.getDrawer().getHud().chat.println(this.getClass().getSimpleName() + "_" + getNumber(),
				"(" + getX() + "|" + getY() + ")" + "\nhp:" + getStats().getHp().getCurrentAmount());
	}

	public void hit(int damage, Player attacker, String origin) {
		System.out.println("Entity.hit()" + getInternName() + " got hit with " + damage + " from "
				+ attacker.getUser().name + " with " + origin);
		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);

			if (damage > 0) {
				getStats().getHp().setCurrentAmount((int) (getStats().getHp().getCurrentAmount() - damage));
				/** check if it was lasthit */
				if (getStats().getHp().getCurrentAmount() <= 0
						&& getStats().getHp().getCurrentAmount() != Integer.MAX_VALUE) {// marker
					getStats().getHp().setCurrentAmount(-32768);
					onDeath();
				}
			}
		}
	}

	public void heal(int heal) {
		getStats().getHp().setCurrentAmount(getStats().getHp().getCurrentAmount() + heal);
		/** check if it was overheal */
		if (getStats().getHp().getCurrentAmount() > getStats().getHp().getTotalAmount()) {
			getStats().getHp().setCurrentAmount(getStats().getHp().getTotalAmount());
		}
	}

	protected void onDeath() {
		sendAnimation("death", this);
	}

	void drawHpBar() {
		int h = 5;
		if (isAlive() && isMortal()) {//
			player.app.fill(0, 150);
			player.app.rect(xToGrid(getX()), yToGrid(getY()) - getStats().getRadius().getTotalAmount() * 1.5f,
					getHpBarLength(), h);
			player.app.tint(player.color);
			player.app.getDrawer().getImageHandler().drawImage(player.app, hpImg, xToGrid(getX()),
					yToGrid(getY()) - getStats().getRadius().getTotalAmount() * 1.5f,
					getHpBarLength() * getStats().getHp().getCurrentAmount() / getStats().getHp().getTotalAmount(), h);
			player.app.tint(255);
		}
	}

	public float calcImportanceOf(Entity e) {
		float importance = PApplet
				.abs(10000 / (e.getStats().getHp().getCurrentAmount() * PApplet.dist(getX(), getY(), e.getX(), e.getY())
						- getStats().getRadius().getTotalAmount() - e.getStats().getRadius().getTotalAmount()));
		// TODO speziefische Thread werte
		return importance;
	}

	public boolean isAlive() {
		if (isMortal())
			return (getAnimation().getClass() != death.getClass()) && getStats().getHp().getCurrentAmount() > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public void addBuff(Buff buff) {
		System.out.println("Entity.addBuff()");
		if (!Helper.listContainsInstanceOf(buff.getClass(), getBuffs())) {
			buff.onFirstApply(getBuffs());
		} else {
			buff.onReapply(getBuffs());
		}
	}

	public void removeBuff(Buff buff) {
		buff.onEnd();
		buffsToRemove.add(buff);

	}

	public Attack getBasicAttack() {
		return basicAttack;
	}

	public void doAttack(Attack a) {
		if (a == basicAttack)
			doBasicAttack(a);
	}

	private void doBasicAttack(Attack a) {
		System.out.println("Entity.doBasicAttack()");
		player.app.getUpdater().sendSpawn(BasicAttackProjectile.class, player,
				player.getChampion().getX() + " " + player.getChampion().getY() + " " + this.getNumber() + " " + HOMING
						+ " " + a.getTarget().getNumber() + " " + getInternName());
	}

	@Override
	public boolean isTargetable() {
		return true;
	}

	public BaAtInfo getBasicAttackAnim() {
		return null;
	}

	public int getHpBarLength() {
		return hpBarLength;
	}

	public void setHpBarLength(int hpBarLength) {
		this.hpBarLength = hpBarLength;
	}

	public void addItem(InventoryItem item) {
		items.add(item);
		item.giveStats();
		System.out.println("Entity.addItem()");
		for (Buff buff : getBuffs()) {
			System.out.println("Entity.addItem()"+buff.getInternName());
		}
	}

	public ArrayList<Buff> getBuffs() {
		return buffs;
	}

}
