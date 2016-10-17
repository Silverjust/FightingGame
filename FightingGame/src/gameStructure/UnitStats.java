package gameStructure;

import shared.GameBaseApp;

public class UnitStats extends EntityStats {
	public UnitStats(GameBaseApp app) {
		super(app);
	}

	private EntityStat_MovementSpeed movementSpeed = new EntityStat_MovementSpeed(this, MOVEMENT_SPEED);
	private EntityStat_LevelScaling basicAttackRange = new EntityStat_LevelScaling(this, BASICATTACK_Range);

	public EntityStat_MovementSpeed getMovementSpeed() {
		return movementSpeed;
	}

	public EntityStat_LevelScaling getBasicAttackRange() {
		return basicAttackRange;
	}

}