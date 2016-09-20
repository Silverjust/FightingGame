package gameStructure;

import shared.GameBaseApp;

public class UnitStats extends EntityStats {
	public UnitStats(GameBaseApp app) {
		super(app);
	}

	public float speed;
	public float speedMult = 100;

	/**
	 * gives you the movement-speed
	 */
	public float getSpeed() {
		return speed * speedMult / 100.0f;
	}

	/**
	 * sets the movement-speed
	 * 
	 * @param speed
	 *            TODO
	 */
	public void setSpeed(float speed) {
		if (speed <= 0)
			System.out.println("Unit.setSpeed() cant root this way");
		else
			this.speed = speed;
	}

	public float getSpeedMult() {
		return speedMult;
	}

	public void setSpeedMult(float speedMult) {
		if (speed <= 0)
			System.out.println("Unit.setSpeed() cant root this way");
		else
			this.speedMult = speedMult;
	}

}