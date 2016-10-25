package gameStructure;

public class Damage {

	private Entity target;
	private GameObject attacker;
	private DmgType dmg_type;

	private float baseDmg;
	private int spellArmorPen;
	private int spellMagicPen;
	private int spellPercArmorPen;
	private int spellPercMagicPen;

	public Damage(Entity target, GameObject origin, float baseDmg, DmgType dmg_type) {
		this.target = target;
		this.attacker = origin;
		this.baseDmg = baseDmg;
		this.dmg_type = dmg_type;
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

	public float get() {
		float dmg = baseDmg;
		if (dmg_type == DmgType.PHYSICAL_DMG) {
			int armor = target.getStats().getArmor().getTotalAmount();
			if (attacker instanceof Entity) {
				armor *= (100
						- (((Entity) attacker).getStats().getPercentArmorPen().getTotalAmount() + spellPercArmorPen))
						/ 100.0;
				armor -= ((Entity) attacker).getStats().getArmorPen().getTotalAmount() + spellArmorPen;
			}
			dmg *= getDmgFactor(armor);
		} else if (dmg_type == DmgType.MAGIC_DMG) {
			int mr = target.getStats().getMagicResist().getTotalAmount();
			if (attacker instanceof Entity) {
				mr *= (100 - (((Entity) attacker).getStats().getPercentMagicPen().getTotalAmount() + spellPercMagicPen))
						/ 100.0;
				mr -= ((Entity) attacker).getStats().getMagicPen().getTotalAmount() + spellMagicPen;
			}
			dmg *= getDmgFactor(mr);
		} else if (dmg_type == DmgType.TRUE_DMG) {
			dmg *= getDmgFactor(0);
		}
		return dmg;
	}

	public enum DmgType {
		PHYSICAL_DMG, MAGIC_DMG, TRUE_DMG
	}
}
