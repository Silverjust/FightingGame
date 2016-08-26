package gameStructure;

import gameStructure.animation.Attack;

public interface Attacker {

	Attack getBasicAttack();
	
	void calculateDamage(Attack a);
	
}
