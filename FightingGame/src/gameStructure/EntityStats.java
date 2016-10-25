package gameStructure;

import shared.GameBaseApp;

public class EntityStats extends Stats {

	private EntityStat_Ressource hp = new EntityStat_Ressource(this, HEALTH);
	private EntityStat_LevelScaling armor = new EntityStat_LevelScaling(this, ARMOR);
	private EntityStat_LevelScaling magicResist = new EntityStat_LevelScaling(this, MR);
	private EntityStat armorPen = new EntityStat(this, ARMOR_PEN);
	private EntityStat magicPen = new EntityStat(this, MAGIC_PEN);
	private EntityStat percentArmorPen = new EntityStat(this, PERCENT_ARMOR_PEN);
	private EntityStat percentMagicPen = new EntityStat(this, PERCENT_MAGIC_PEN);
	private EntityStat_BonusAmount attackDamage = new EntityStat_BonusAmount(this, ATTACK_DAMAGE);
	private EntityStat_BonusAmount abilityPower = new EntityStat_BonusAmount(this, ABILITY_POWER);
	protected boolean isRooted;
	protected boolean isSilenced;
	protected boolean isStunned;
	protected boolean isDisplaced;
	private EntityStat_LevelScaling basicAttackRange = new EntityStat_LevelScaling(this, BASICATTACK_Range);

	public EntityStats(GameBaseApp app) {
		super(app);
	}

	public EntityStat_Ressource getHp() {
		return hp;
	}

	public EntityStat_LevelScaling getArmor() {
		return armor;
	}

	public EntityStat_LevelScaling getMagicResist() {
		return magicResist;
	}

	public EntityStat getArmorPen() {
		return armorPen;
	}

	public EntityStat getMagicPen() {
		return magicPen;
	}

	public EntityStat getPercentArmorPen() {
		return percentArmorPen;
	}

	public EntityStat getPercentMagicPen() {
		return percentMagicPen;
	}

	public EntityStat_BonusAmount getAttackDamage() {
		return attackDamage;
	}

	public EntityStat_BonusAmount getAbilityPower() {
		return abilityPower;
	}

	@Deprecated
	public void setArmor(int armor) {
		getArmor().setBrutoBaseAmount(armor);
	}

	@Deprecated
	public void setMagicResist(int magicResist) {
		getMagicResist().setBrutoBaseAmount(magicResist);
	}

	@Deprecated
	public void setArmorPen(int armorPen) {
		getArmorPen().setBrutoBaseAmount(armorPen);
	}

	@Deprecated
	public void setMagicPen(int magicPen) {
		getMagicPen().setBrutoBaseAmount(magicPen);
	}

	@Deprecated
	public void setPercArmorPen(int percArmorPen) {
		getPercentArmorPen().setBrutoBaseAmount(percArmorPen);
	}

	@Deprecated
	public void setPercMagicPen(int percMagicPen) {
		getPercentMagicPen().setBrutoBaseAmount(percMagicPen);
	}

	@Deprecated
	public void setAttackDamage(int attackDamage) {
		getAttackDamage().setBrutoBaseAmount(attackDamage);
	}

	@Deprecated
	public void setAbilityPower(int abilityPower) {
		getAbilityPower().setBrutoBaseAmount(abilityPower);
	}

	@Deprecated
	public int getAttackDamageMult() {
		return getAttackDamage().getTotalAmountMult();
	}

	@Deprecated
	public void setAttackDamageMult(int attackDamageMult) {
		getAttackDamage().setTotalAmountMult(attackDamageMult);
	}

	@Deprecated
	public int getAbilityPowerMult() {
		return getAbilityPower().getTotalAmountMult();
	}

	@Deprecated
	public void setAbilityPowerMult(int abilityPowerMult) {
		getAbilityPower().setTotalAmountMult(abilityPowerMult);
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

	public EntityStat_LevelScaling getBasicAttackRange() {
		return basicAttackRange;
	}

}