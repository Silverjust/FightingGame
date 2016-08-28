package gameStructure;

import java.util.ArrayList;

import champs.TestProjectile;
import game.GameBaseApp;
import gameStructure.animation.Death;
import gameStructure.baseBuffs.Buff;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Coms;
import shared.Helper;
import shared.Player;

public class Entity extends GameObject {

	private int hp;
	private int hp_max;
	private int bonus_hp;

	private int armor = 0;
	private int magicResist = 0;

	private int ArmorPen;
	private int MagicPen;
	private int PercArmorPen;
	private int PercMagicPen;

	private int attackDamage;
	private int abilityPower;

	private int attackDamageMult;
	private int abilityPowerMult;

	private boolean isSilenced;
	public PImage iconImg;
	protected String descr = " ";
	protected String stats = " ";
	public Death death;
	public int hpBarLength;
	private ArrayList<Buff> buffs = new ArrayList<Buff>();
	private ArrayList<Buff> buffsToRemove = new ArrayList<Buff>();

	public Entity(GameBaseApp app, String[] c) {
		super(app, c);
	}

	@Override
	public void display() {
		drawHpBar();
	}

	public void sendDamage(TestProjectile testProjectile, Damage damage, Player player, String origin) {
		player.app.getUpdater()
				.send(Coms.HIT + " " + number + " " + damage.get() + " " + player.getUser().ip + " " + origin);
	}

	@Override
	public void updateDecisions(boolean isServer) {
		for (int i = buffsToRemove.size() - 1; i >= 0; i--) {
			Buff buff = buffsToRemove.get(i);
			if (buff != null) {
				buffs.remove(buff);
				buffsToRemove.remove(i);
			}
		}
		for (Buff buff : buffs) {
			buff.updateDecisions(isServer);
		}
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

	}

	public void info() {
		player.app.getDrawer().getHud().chat.println(this.getClass().getSimpleName() + "_" + number,
				"(" + getX() + "|" + getY() + ")" + "\nhp:" + getCurrentHp());
	}

	public void hit(int damage, Player attacker, String origin) {
		System.out.println("Entity.hit()" + getInternName() + " got hit with " + damage + " from "
				+ attacker.getUser().name + " with " + origin);
		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);

			if (damage > 0) {
				setHp((int) (getCurrentHp() - damage));
				/** check if it was lasthit */
				if (getCurrentHp() <= 0 && getCurrentHp() != Integer.MAX_VALUE) {// marker
					setHp(-32768);
					onDeath();
				}
			}
		}
	}

	public void heal(int heal) {
		setHp(getCurrentHp() + heal);
		/** check if it was overheal */
		if (getCurrentHp() > getTotalHp()) {
			setHp(getTotalHp());
		}
	}

	protected void onDeath() {
		sendAnimation("death");
	}

	void drawHpBar() {
		int h = 5;
		if (isAlive() && isMortal()) {//
			player.app.fill(0, 150);
			player.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, hpBarLength, h);
			player.app.tint(player.color);
			player.app.getDrawer().imageHandler.drawImage(player.app, hpImg, xToGrid(getX()),
					yToGrid(getY()) - getRadius() * 1.5f, hpBarLength * getCurrentHp() / getTotalHp(), h);
			player.app.tint(255);
		}
	}

	public float calcImportanceOf(Entity e) {
		float importance = PApplet.abs(10000
				/ (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - getRadius() - e.getRadius()));
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

	// getters&setters*****************************************************

	/** gives you the current-health */
	public int getCurrentHp() {
		return hp;
	}

	/**
	 * sets the current-health
	 * <p>
	 * (better use damage or heal effects)
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * sets health and base-max-health to the same value
	 * <p>
	 * (use at spawn)
	 */
	public void initHp(int hp) {
		setHp(hp);
		setHp_max(hp);
	}

	/** gives you the real maximum of health */
	public int getTotalHp() {
		return getHp_max() + getBonus_hp();
	}

	/** gives you the base-max-health */
	public int getHp_max() {
		return hp_max;
	}

	/** sets the base-max-health */
	public void setHp_max(int hp_max) {
		this.hp_max = hp_max;
	}

	/** gives you the bonus-max-health */
	public int getBonus_hp() {
		return bonus_hp;
	}

	/** sets the bonus-max-health */
	public void setBonus_hp(int bonus_hp) {
		this.bonus_hp = bonus_hp;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getMagicResist() {
		return magicResist;
	}

	public void setMagicResist(int magicResist) {
		this.magicResist = magicResist;
	}

	public int getArmorPen() {
		return ArmorPen;
	}

	public void setArmorPen(int armorPen) {
		ArmorPen = armorPen;
	}

	public int getMagicPen() {
		return MagicPen;
	}

	public void setMagicPen(int magicPen) {
		MagicPen = magicPen;
	}

	public int getPercArmorPen() {
		return PercArmorPen;
	}

	public void setPercArmorPen(int percArmorPen) {
		PercArmorPen = percArmorPen;
	}

	public int getPercMagicPen() {
		return PercMagicPen;
	}

	public void setPercMagicPen(int percMagicPen) {
		PercMagicPen = percMagicPen;
	}

	public void setRooted(boolean b) {
	}

	public boolean isRooted() {
		return true;
	}

	public boolean isSilenced() {
		return isSilenced;
	}

	public void setSilenced(boolean isSilenced) {
		this.isSilenced = isSilenced;
	}

	public void addBuff(Buff buff) {
		System.out.println("Entity.addBuff()");
		if (!Helper.listContainsInstanceOf(buff.getClass(), buffs)) {
			buff.onFirstApply(buffs);
		} else {
			buff.onReapply(buffs);
		}
	}

	public void removeBuff(Buff buff) {
		buff.onEnd();
		buffsToRemove.add(buff);

	}

	public int getAttackDamage() {
		return (int) (attackDamage * attackDamageMult / 100.0f);
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getAbilityPower() {
		return (int) (abilityPower * abilityPowerMult / 100.0f);
	}

	public void setAbilityPower(int abilityPower) {
		this.abilityPower = abilityPower;
	}

	public int getAttackDamageMult() {
		return attackDamageMult;
	}

	public void setAttackDamageMult(int attackDamageMult) {
		this.attackDamageMult = attackDamageMult;
	}

	public int getAbilityPowerMult() {
		return abilityPowerMult;
	}

	public void setAbilityPowerMult(int abilityPowerMult) {
		this.abilityPowerMult = abilityPowerMult;
	}

}
