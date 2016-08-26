package main.preGame;

import g4p_controls.GControlMode;
import g4p_controls.GSlider;
import processing.core.PConstants;
import processing.core.PGraphics;
import shared.ref;

public class PlayerPanel {
	public GSlider playerSlider;
	public PGraphics playerList;

	public PlayerPanel() {
		playerList = ref.app.createGraphics(281, 300);// 200 height?

		playerSlider = new GSlider(ref.app, ref.app.width - 10, 100, 300, 30, 20);
		playerSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		playerSlider.setLimits(0, 0, 1);
	}

	void draw(PreGameDisplay display) {
		playerList.beginDraw();
		playerList.background(255);
		if (!ref.preGame.users.isEmpty()) {
			playerSlider.setLimits(0, ref.preGame.users.size() * 20 - 19);
			int i = 0;
			for (String key : display.preGame.users.keySet()) {
				display.preGame.users.get(key).display(playerList, 0, 20 * i - playerSlider.getValueI());
				i++;
			}
		}
		playerList.endDraw();
		ref.app.image(playerList, ref.app.width - playerList.width - 40, 100);
	}

	public void dispose() {
		playerSlider.dispose();
	}

	public void setEnabled(boolean b) {
		playerSlider.setEnabled(b);
	}
}