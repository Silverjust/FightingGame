package game;

import gameStructure.GameObject;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import shared.GameBaseApp;
import shared.Helper;

public class Minimap {
	private PGraphics graphics;

	private float s;
	private int w, h;
	private int dw, dh;

	private GameBaseApp app;
	private HUD hud;

	public Minimap(GameBaseApp app) {
		this.app = app;
		hud = app.getDrawer().getHud();
		w = app.getUpdater().map.width;
		h = app.getUpdater().map.height;
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

		graphics.image(app.getUpdater().map.textur, 0, 0, w, h);
		graphics.imageMode(PConstants.CENTER);
		graphics.rectMode(PConstants.CENTER);
		for (GameObject e : app.getUpdater().getGameObjects()) {
			e.drawOnMinimapUnder(graphics);
		}
		graphics.imageMode(PConstants.CORNER);
		graphics.rectMode(PConstants.CORNER);
		graphics.blendMode(PConstants.ADD);
		graphics.fill(20);
		graphics.rect(0, 0, w, h);
		graphics.blendMode(PConstants.MULTIPLY);
		graphics.image(app.getUpdater().map.fogOfWar.get(), 0, 0, w, h);
		// get to convert to img
		// else it would crash
		graphics.blendMode(PConstants.BLEND);
		graphics.noStroke();
		graphics.rectMode(PConstants.CENTER);
		for (GameObject e : app.getUpdater().getGameObjects()) {
			e.drawOnMinimap(graphics);
		}
		graphics.rectMode(PConstants.CORNER);
		graphics.fill(255, 50);
		graphics.rect(Helper.gridToX(0), Helper.gridToY(0), app.width / GameDrawer.zoom,
				(app.height - hud.height) / GameDrawer.zoom * 2);
		graphics.popMatrix();
		graphics.endDraw();
		app.image(graphics, 10 + dw, app.height - hud.height + 10 + dh);

	}

	public void click(int x, int y, boolean doMouseCommands) {
		if (Helper.isOver(x, y, 10 + dw, app.height - hud.height + 10 + dh, 10 + dw + (w * s),
				app.height - hud.height + 10 + dh + (h * s))) {
			float xGrid = PApplet.map(x, 10 + dw, 10 + dw + (w * s), 0, w), yGrid = PApplet.map(y,
					app.height - hud.height + 10 + dh, app.height - hud.height + 10 + dh + (h * s), 0, h);
			if (doMouseCommands)
				((GameUpdater) app.getUpdater()).input.mouseCommands(xGrid, yGrid);
			if (app.mouseButton == ((GameApp) app).settingHandler.getSetting().mouseMove) {
				GameDrawer.xMapOffset = -xGrid * GameDrawer.zoom + app.getWidth() / 2;
				GameDrawer.yMapOffset = -yGrid * GameDrawer.zoom / 2 + app.getHeight() / 2;
			}
		}
	}
}
