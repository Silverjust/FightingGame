package main.preGame;

import g4p_controls.GControlMode;
import g4p_controls.GSlider;
import game.GameApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class PlayerPanel {
	public GSlider playerSlider;
	public PGraphics playerList;

	public PlayerPanel() {
		playerList = GameApplet.app.createGraphics(281, 300);// 200 height?

		playerSlider = new GSlider(GameApplet.app, GameApplet.app.width - 10, 100, 300, 30, 20);
		playerSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		playerSlider.setLimits(0, 0, 1);
	}

	void draw(PreGameDisplay display) {
		playerList.beginDraw();
		playerList.background(255);
		if (!GameApplet.preGame.users.isEmpty()) {
			playerSlider.setLimits(0, GameApplet.preGame.users.size() * 20 - 19);
			int i = 0;
			for (String key : display.preGame.users.keySet()) {
				display.preGame.users.get(key).display(playerList, 0, 20 * i - playerSlider.getValueI());
				i++;
			}
		}
		playerList.endDraw();
		GameApplet.app.image(playerList, GameApplet.app.width - playerList.width - 40, 100);
	}

	public void dispose() {
		playerSlider.dispose();
	}

	public void setEnabled(boolean b) {
		playerSlider.setEnabled(b);
	}
}