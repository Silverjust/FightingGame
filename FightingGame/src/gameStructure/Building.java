package gameStructure;

import game.ImageHandler;
import game.aim.BuildAim;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.GameBaseApp;

public abstract class Building extends GameObject {

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler){ 

		// String path = path(new Object() {
		// });
		// setTarget = ImageHandler.load(path, "setTarget");
	}

	public Building(GameBaseApp app, String[] c) {
		super(app, c);
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (this instanceof Trainer) {
			((Trainer) this).getTraining().updateAbility(this, isServer);
		}
	}

	@Override
	public void renderUnder() {
		if (this instanceof Trainer && isAlive()) {
			Trainer t = (Trainer) this;
			player.app.stroke(player.color);
			player.app.line(xToGrid(getX()), yToGrid(getY()), xToGrid(t.getXTarget()), yToGrid(t.getYTarget()));
			player.app.stroke(0);
		}
		if (this instanceof Commander && isAlive()
				&& player.app.getDrawer().getAimHandler().getAim() instanceof BuildAim) {
			Commander c = (Commander) this;
			player.app.tint(player.color);
			player.app.getDrawer().imageHandler.drawImage(player.app, selectedImg, xToGrid(getX()), yToGrid(getY()),
					c.commandRange() * 2, c.commandRange());
			player.app.tint(255);
		}
	}
	/*
	 * public static float xToGrid(float x) { return Math.round(x / 20) * 20; }
	 * 
	 * public static float yToGrid(float y) { return Math.round(y / 20) * 10; }
	 */

	@Override
	public void drawOnMinimap(PGraphics graphics) {
		graphics.fill(player.color);
		graphics.rect(getX(), getY(), getStats().getRadius() * 2, getStats().getRadius() * 2);
	}

	/*
	 * @Override protected void drawShadow() {
	 * System.out.println(this.getClass().getSimpleName() +
	 * "should not have a shadow"); Chat.println(this.getClass().getSimpleName()
	 * + "", "should not have a shadow"); }
	 */

	public abstract PImage preview();

	protected void onDeath() {
		// TODO Auto-generated method stub

	}

}
