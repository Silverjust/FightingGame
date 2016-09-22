package gameStructure;

import shared.GameBaseApp;

public class EntityStats extends Stats {
	public EntityStats(GameBaseApp app) {
		super(app);
	}

	protected int hp;
	protected int hp_max;
	protected int bonus_hp;
	protected int armor;
	protected int magicResist;
	protected int ArmorPen;
	protected int MagicPen;
	protected int PercArmorPen;
	protected int PercMagicPen;
	protected int attackDamage;
	protected int abilityPower;
	protected int attackDamageMult;
	protected int abilityPowerMult;

	protected boolean isRooted;
	protected boolean isSilenced;
	protected boolean isStunned;
	protected boolean isDisplaced;

	/** gives you the current-health */
	public int getCurrentHp() {
		return hp;
	}

	/**
	 * sets the current-health
	 * <p>
	 * (better use damage or heal effects)
	 * 
	 * @param hp
	 *            TODO
	 */
	public void setHp(int hp) {
		this.hp = hp;
	}

	/**
	 * sets health and base-max-health to the same value
	 * <p>
	 * (use at spawn)
	 * 
	 * @param hp
	 *            TODO
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

	/**
	 * sets the base-max-health
	 * 
	 * @param hp_max
	 *            TODO
	 */
	public void setHp_max(int hp_max) {
		this.hp_max = hp_max;
	}

	/**
	 * sets the bonus-max-health
	 * 
	 * @param bonus_hp
	 *            TODO
	 */
	public void setBonus_hp(int bonus_hp) {
		this.bonus_hp = bonus_hp;
	}

	public int getArmor() {
		return armor;
	}

	/**
	 * gives you the bonus-max-health
	 */
	public int getBonus_hp() {
		return bonus_hp;
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

	/**
	 * is rooted in some sort: root, stunn, displace,etc
	 * <p>
	 * does not include self-root
	 */
	public boolean isRooted() {
		return isRooted || isStunned || isDisplaced;
	}

	public void setRooted(boolean b) {
		isRooted = b;
	}

	/**
	 * is silenced in some sort: silence, stunn, displace,etc
	 */
	public boolean isSilenced() {
		return isSilenced || isStunned || isDisplaced;
	}

	public void setSilenced(boolean isSilenced) {
		this.isSilenced = isSilenced;
	}

	public boolean isStunned() {
		return isStunned;
	}

	public void setStunned(boolean isStunned) {
		this.isStunned = isStunned;
	}

	public boolean isDisplaced() {
		return isDisplaced;
	}

	public void setDisplaced(boolean isDisplaced) {
		this.isDisplaced = isDisplaced;
	}

}