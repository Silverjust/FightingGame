package game;

import java.util.ArrayList;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.AudioSnippet;
import processing.core.PApplet;
import shared.Helper;

@SuppressWarnings("deprecation")
public class SoundHandler {
	// TODO sound
	static AudioPlayer player;
	static AudioSample sample;
	static ArrayList<IngameSound> ingameSounds = new ArrayList<IngameSound>();
	static ArrayList<IngameSound> toRemove = new ArrayList<IngameSound>();
	private GameApplet app;

	/*
	 * private void set() { JSMinim }
	 */

	public SoundHandler(GameApplet app) {
		this.app = app;
	}

	public void update() {
		for (IngameSound sound : ingameSounds) {
			if (sound.sample.isPlaying())
				sound.calcVolume();
			else
				toRemove.add(sound);
		}
		for (int i = 0; i < toRemove.size(); i++) {
			if (toRemove.get(i) != null) {
				ingameSounds.remove(toRemove.get(i));
				toRemove.remove(i);
			}
		}
	}

	public void startIngameSound(AudioSnippet sample, float x, float y) {
		ingameSounds.add(new IngameSound(sample, x, y));
	}

	class IngameSound {
		AudioSnippet sample;
		float x;
		float y;

		private IngameSound(AudioSnippet sample, float x, float y) {
			this.sample = sample;
			sample.rewind();
			sample.play();
			this.x = x;
			this.y = y;
		}

		private void calcVolume() {
			float dist = PApplet.dist(x, y, Helper.gridToX(app.getxCenter()),
					Helper.gridToY(app.getyCenter() - HUD.height / 2));
			float f = PApplet.map(dist, 0, PApplet.dist(0, 0, app.updater.map.width, app.updater.map.height), 0, -50);
			System.out.println(dist + "= " + x + " " + y + " " + Helper.gridToX(app.getxCenter()) + " "
					+ Helper.gridToY(app.getyCenter() - HUD.height / 2));
			sample.setGain(f);
		}
	}
}
