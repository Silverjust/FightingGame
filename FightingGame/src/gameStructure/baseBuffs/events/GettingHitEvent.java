package gameStructure.baseBuffs.events;

import gameStructure.Damage;
import gameStructure.GameObject;

public class GettingHitEvent extends Event {

	private Damage damage;
	private GameObject origin;
	private GameObject source;
	private HitTypes type;

	public GettingHitEvent(Damage damage, GameObject origin, GameObject source, HitTypes type) {
		this.damage = damage;
		this.origin = origin;
		this.source = source;
		this.type = type;
	}

	public Damage getDamage() {
		return damage;
	}

	public GameObject getOrigin() {
		return origin;
	}

	public GameObject getSource() {
		return source;
	}

	public HitTypes getHitType() {
		return type;
	}

}
