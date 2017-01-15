package gameStructure;

import gameStructure.baseBuffs.events.Event.HitTypes;
import gameStructure.baseBuffs.events.GettingHitEvent;
import gameStructure.baseBuffs.events.HitEvent;

public class Damage {

	private Entity target;
	private GameObject attacker;
	private DmgType dmg_type;
	private boolean isKrit;

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

	public Damage setKrit(boolean isKrit) {
		this.isKrit = isKrit;
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
		PHYSICAL_DMG('p'), MAGIC_DMG('m'), TRUE_DMG('t'), HEAL('h'), BLOCK('b');

		private char name;

		private DmgType(char name) {
			this.name = name;
		}

		public char getName() {
			return name;
		}
	}

	public String getDamageType() {
		String s = "";
		s += dmg_type.getName() + "";

		if (isKrit)
			s += "k";
		return s;
	}

	/**
	 * does onhit, on-getting-hit, sends damage
	 */
	public void doDamage(GameObject target, GameObject origin, GameObject source, String originInfo, HitTypes hitType,
			boolean isServer) {
		origin.onEvent(new HitEvent(this, target, source, hitType), isServer);
		target.onEvent(new GettingHitEvent(this, origin, source, hitType), isServer);
		if (isServer)
			((Entity) target).sendDamage(this, origin, originInfo);
	}
}
