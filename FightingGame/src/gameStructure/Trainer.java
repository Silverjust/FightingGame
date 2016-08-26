package gameStructure;

import gameStructure.animation.Ability;

public interface Trainer {

	Ability getTraining();

	float getXTarget();

	float getYTarget();

	void setTarget(float x, float y);

}
