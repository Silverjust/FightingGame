package gameStructure.animation;

import gameStructure.Attacker;
import gameStructure.EntityStat;
import gameStructure.GameObject;
import processing.core.PImage;
import shared.GameBaseApp;

public class BasicAttackAnim extends Attack {

	public BasicAttackAnim(GameBaseApp app,GameObject animated, PImage IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	public BasicAttackAnim(GameBaseApp app, GameObject animated, PImage[][] IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	@Override
	public int getRange() {
		return ((EntityStat) ((Attacker) animated).getStats().getBasicAttackRange()).getTotalAmount();
	}
}
