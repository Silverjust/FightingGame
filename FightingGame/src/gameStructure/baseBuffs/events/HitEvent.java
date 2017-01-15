package gameStructure.baseBuffs.events;

import gameStructure.Damage;
import gameStructure.GameObject;

public class HitEvent extends Event {

	private Damage damage;
	private GameObject target;
	private GameObject source;
	private HitTypes type;

	public HitEvent(Damage damage, GameObject target, GameObject source, HitTypes type) {
		this.damage = damage;
		this.target = target;
		this.source = source;
		this.type = type;
	}

	public Damage getDamage() {
		return damage;
	}

	public GameObject getTarget() {
		return target;
	}

	public GameObject getSource() {
		return source;
	}

	public HitTypes getHitType() {
		return type;
	}

}
