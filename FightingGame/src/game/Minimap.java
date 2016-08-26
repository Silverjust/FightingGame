package game;

import gameStructure.GameObject;
import main.FrameInfo;
import main.appdata.SettingHandler;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import shared.Helper;
import shared.ref;

public class Minimap {
	private PGraphics graphics;

	private float s;
	private int w, h;
	private int dw, dh;

	public  Minimap() {

		w = ref.updater.map.width;
		h = ref.updater.map.height;
		s = ((w) < (h)) ? (180.0f / h) : (180.0f / w);
		dw = (int) ((180 - w * s) / 2);
		dh = (int) ((180 - h * s) / 2);
		graphics = ref.app.createGraphics((int) (w * s), (int) (h * s));
	}

	public  void update() {
		graphics.beginDraw();
		graphics.clear();
		graphics.pushMatrix();
		// graphics.translate(10, graphics.height - HUD.height + 10);
		graphics.scale(s);
		// graphics.translate(dw, dh);
		graphics.noStroke();

		graphics.image(ref.updater.map.textur, 0, 0, w, h);
		graphics.imageMode(PConstants.CENTER);
		graphics.rectMode(PConstants.CENTER);
		for (GameObject e : ref.updater.getGameObjects()) {
			e.drawOnMinimapUnder(graphics);
		}
		graphics.imageMode(PConstants.CORNER);
		graphics.rectMode(PConstants.CORNER);
		graphics.blendMode(PConstants.ADD);
		graphics.fill(20);
		graphics.rect(0, 0, w, h);
		graphics.blendMode(PConstants.MULTIPLY);
		graphics.image(ref.updater.map.fogOfWar.get(), 0, 0, w, h);
		// get to convert to img
		// else it would crash
		graphics.blendMode(PConstants.BLEND);
		graphics.noStroke();
		graphics.rectMode(PConstants.CENTER);
		for (GameObject e : ref.updater.getGameObjects()) {
			e.drawOnMinimap(graphics);
		}
		graphics.rectMode(PConstants.CORNER);
		graphics.fill(255, 50);
		graphics.rect(Helper.gridToX(0), Helper.gridToY(0), ref.app.width / GameDrawer.zoom,
				(ref.app.height - HUD.height) / GameDrawer.zoom * 2);
		graphics.popMatrix();
		graphics.endDraw();
		ref.app.image(graphics, 10 + dw, ref.app.height - HUD.height + 10 + dh);

	}

	public  void click(int x, int y, boolean doMouseCommands) {
		if (Helper.isOver(x, y, 10 + dw, ref.app.height - HUD.height + 10 + dh, 10 + dw + (w * s),
				ref.app.height - HUD.height + 10 + dh + (h * s))) {
			float xGrid = PApplet.map(x, 10 + dw, 10 + dw + (w * s), 0, w), yGrid = PApplet.map(y,
					ref.app.height - HUD.height + 10 + dh, ref.app.height - HUD.height + 10 + dh + (h * s), 0, h);
			if (doMouseCommands)
				((GameUpdater) ref.updater).input.mouseCommands(xGrid, yGrid);
			if (ref.app.mouseButton == SettingHandler.setting.mouseMove) {
				GameDrawer.xMapOffset = -xGrid * GameDrawer.zoom + FrameInfo.width / 2;
				GameDrawer.yMapOffset = -yGrid * GameDrawer.zoom / 2 + FrameInfo.height / 2;
			}
		}
	}
}
