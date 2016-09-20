package gameStructure;

import shared.GameBaseApp;

public class Stats {
	private GameBaseApp app;

	private int radius;
	private int sight;

	public Stats(GameBaseApp app) {
		this.app = app;
	}

	public int getRadius() {
		return radius;
	}

	/**
	 * sets the colission-circle-size
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getSight() {
		return sight;
	}

	/**
	 * sets the sight-radius
	 */
	public void setSight(int sight) {
		this.sight = sight;
	}

}