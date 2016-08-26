package gameStructure;

public class Damage {

	private Entity target;
	private Entity attacker;
	private boolean isPhysicalDmg;

	private int baseDmg;
	private int spellArmorPen;
	private int spellMagicPen;
	private int spellPercArmorPen;
	private int spellPercMagicPen;
	private boolean trueDmg;

	public Damage(Entity target, Entity attacker, int dmg, boolean isPhysicalDmg) {
		this.target = target;
		this.attacker = attacker;
		baseDmg = dmg;
		this.isPhysicalDmg = isPhysicalDmg;
	}

	public Damage setTrueDamage() {
		trueDmg = true;
		return this;
	}

	public Damage addFlatArmorPen(int spellArmorPen) {
		this.spellArmorPen = spellArmorPen;
		return this;
	}

	public Damage addFlatMagicPen(int spellArmorPen) {
		this.spellMagicPen = spellArmorPen;
		return this;
	}

	public Damage addPercentArmorPen(int spellArmorPen) {
		this.spellPercArmorPen = spellArmorPen;
		return this;
	}

	public Damage addPercentMagicPen(int spellArmorPen) {
		this.spellPercMagicPen = spellArmorPen;
		return this;
	}

	private float getDmgFactor(int def) {
		return 100.0f / (100.0f + def);
	}

	public int get() {
		int dmg = baseDmg;
		if (trueDmg) {

		} else if (isPhysicalDmg) {
			int armor = target.getArmor();
			armor *= (100 - (attacker.getPercArmorPen() + spellPercArmorPen)) / 100.0;
			armor -= attacker.getArmorPen() + spellArmorPen;
			dmg *= getDmgFactor(armor);
		} else {
			int mr = target.getMagicResist();
			mr *= 100 - (attacker.getPercMagicPen() + spellPercMagicPen);
			mr -= attacker.getMagicPen() + spellMagicPen;
			dmg *= getDmgFactor(mr);
		}
		return dmg;
	}
}
