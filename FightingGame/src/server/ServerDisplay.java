package server;

import g4p_controls.G4P;
import game.ImageHandler;
import gameStructure.GameObject;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PConstants;
import shared.ref;

@SuppressWarnings("serial")
public class ServerDisplay extends PApplet {

	public static void main(String[] args) {
		PApplet.main(new String[] { "server.ServerDisplay" });
	}
	private float xMapOffset;
	private float yMapOffset;
	private float zoom;

	@Override
	public void setup() {
		size(displayWidth, displayHeight, P2D);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setResizable(true);
	//	frame.setTitle("EliteEngine");
		frameRate(60);
		noSmooth();
		G4P.messagesEnabled(false);
	}
	@Override
	public void draw() {

		background(255);
		pushMatrix();
		translate(xMapOffset, yMapOffset);
		scale(zoom);
		stroke(0);

		ImageHandler.drawImage(this, ref.updater.map.textur, 0, 0, ref.updater.map.width,
				ref.updater.map.height);
		ref.updater.map.updateFogofWar(ref.player);
		blendMode(PConstants.MULTIPLY);
		ImageHandler.drawImage(this, ref.updater.map.fogOfWar, 0, 0, ref.updater.map.width,
				ref.updater.map.height);
		blendMode(PConstants.BLEND);
		imageMode(PConstants.CENTER);
		rectMode(PConstants.CENTER);
		for (GameObject e : ref.player.visibleEntities) {
			e.renderTerrain();
		}
		for (GameObject e : ref.player.visibleEntities) {
			e.renderGround();
		}
		for (GameObject e : ref.player.visibleEntities) {
			e.renderAir();
		}

		imageMode(PConstants.CORNER);
		imageMode(PConstants.CENTER);
		for (GameObject e : ref.player.visibleEntities) {
			e.display();
		}
	}
}
