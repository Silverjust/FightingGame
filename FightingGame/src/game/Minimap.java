package game;

import gameStructure.GameObject;
import main.appdata.SettingHandler;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import shared.Helper;

public class Minimap {
	private PGraphics graphics;

	private float s;
	private int w, h;
	private int dw, dh;

	private GameApplet app;

	public Minimap(GameApplet app) {

		this.app = app;
		w = app.updater.map.width;
		h = app.updater.map.height;
		s = ((w) < (h)) ? (180.0f / h) : (180.0f / w);
		dw = (int) ((180 - w * s) / 2);
		dh = (int) ((180 - h * s) / 2);
		graphics = app.createGraphics((int) (w * s), (int) (h * s));
	}

	public void update() {
		graphics.beginDraw();
		graphics.clear();
		graphics.pushMatrix();
		// graphics.translate(10, graphics.height - HUD.height + 10);
		graphics.scale(s);
		// graphics.translate(dw, dh);
		graphics.noStroke();

		graphics.image(app.updater.map.textur, 0, 0, w, h);
		graphics.imageMode(PConstants.CENTER);
		graphics.rectMode(PConstants.CENTER);
		for (GameObject e : app.updater.getGameObjects()) {
			e.drawOnMinimapUnder(graphics);
		}
		graphics.imageMode(PConstants.CORNER);
		graphics.rectMode(PConstants.CORNER);
		graphics.blendMode(PConstants.ADD);
		graphics.fill(20);
		graphics.rect(0, 0, w, h);
		graphics.blendMode(PConstants.MULTIPLY);
		graphics.image(app.updater.map.fogOfWar.get(), 0, 0, w, h);
		// get to convert to img
		// else it would crash
		graphics.blendMode(PConstants.BLEND);
		graphics.noStroke();
		graphics.rectMode(PConstants.CENTER);
		for (GameObject e : app.updater.getGameObjects()) {
			e.drawOnMinimap(graphics);
		}
		graphics.rectMode(PConstants.CORNER);
		graphics.fill(255, 50);
		graphics.rect(Helper.gridToX(0), Helper.gridToY(0), app.width / GameDrawer.zoom,
				(app.height - HUD.height) / GameDrawer.zoom * 2);
		graphics.popMatrix();
		graphics.endDraw();
		app.image(graphics, 10 + dw, app.height - HUD.height + 10 + dh);

	}

	public void click(int x, int y, boolean doMouseCommands) {
		if (Helper.isOver(x, y, 10 + dw, app.height - HUD.height + 10 + dh, 10 + dw + (w * s),
				app.height - HUD.height + 10 + dh + (h * s))) {
			float xGrid = PApplet.map(x, 10 + dw, 10 + dw + (w * s), 0, w), yGrid = PApplet.map(y,
					app.height - HUD.height + 10 + dh, app.height - HUD.height + 10 + dh + (h * s), 0, h);
			if (doMouseCommands)
				((GameUpdater) app.updater).input.mouseCommands(xGrid, yGrid);
			if (app.mouseButton == SettingHandler.setting.mouseMove) {
				GameDrawer.xMapOffset = -xGrid * GameDrawer.zoom + app.getWidth() / 2;
				GameDrawer.yMapOffset = -yGrid * GameDrawer.zoom / 2 + app.getHeight() / 2;
			}
		}
	}
}
