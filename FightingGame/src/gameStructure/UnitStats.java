package gameStructure;

import shared.GameBaseApp;

public class UnitStats extends EntityStats {
	public UnitStats(GameBaseApp app) {
		super(app);
	}

	private EntityStat_MovementSpeed movementSpeed = new EntityStat_MovementSpeed(this, MOVEMENT_SPEED);
	public EntityStat_MovementSpeed getMovementSpeed() {
		return movementSpeed;
	}

}