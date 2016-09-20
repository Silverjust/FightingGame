package game;

import gameStructure.GameObject;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import shared.GameBaseApp;
import shared.Helper;

public class Minimap {
	private final float size = 180;

	private PGraphics graphics;

	private float s;
	private int w, h;
	private int dw, dh;

	private GameBaseApp app;
	private HUD hud;

	private int xOff;

	public Minimap(GameBaseApp app) {
		this.app = app;
		hud = app.getDrawer().getHud();
		w = app.getUpdater().map.width;
		h = app.getUpdater().map.height;
		s = ((w) < (h)) ? (size / h) : (size / w);
		dw = (int) ((size - w * s) / 2);
		dh = (int) ((size - h * s) / 2);
		xOff = (int) (app.width - (w * s) - 10);

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
		app.image(graphics, xOff + dw, app.height - hud.height + 10 + dh);

	}

	public void click(int x, int y, boolean doMouseCommands) {
		if (Helper.isOver(x, y, xOff + dw, app.height - hud.height + 10 + dh, xOff + dw + (w * s),
				app.height - hud.height + 10 + dh + (h * s))) {
			float xGrid = PApplet.map(x, xOff + dw, xOff + dw + (w * s), 0, w);
			float yGrid = PApplet.map(y, app.height - hud.height + 10 + dh, app.height - hud.height + 10 + dh + (h * s),
					0, h);
			if (doMouseCommands)
				((GameUpdater) app.getUpdater()).input.mouseCommands(xGrid, yGrid);
			if (app.mouseButton == ((GameApp) app).settingHandler.getSetting().mouseCommand) {
				GameDrawer.xMapOffset = -xGrid * GameDrawer.zoom + app.getWidth() / 2;
				GameDrawer.yMapOffset = -yGrid * GameDrawer.zoom / 2 + app.getHeight() / 2;
			}
		}
	}
}
