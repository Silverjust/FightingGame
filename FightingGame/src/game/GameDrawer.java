package game;

import java.util.ArrayList;

import gameStructure.GameObject;
import processing.core.PConstants;
import processing.core.PImage;
import shared.GameBaseApp;
import shared.Updater.GameState;

public class GameDrawer {

	public static float xMapOffset, yMapOffset;
	public static float zoom = 4f;

	public static boolean commandoutput;

	public static boolean godeye;
	public static boolean godhand;
	public static boolean nocosts;
	public static boolean showRanges;
	private static SymbolManager symbolManager;
	private GameUpdater updater;
	private GameBaseApp app;
	private AimHandler aimHandler;
	private HUD hud;
	private ImageHandler imageHandler;

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		app.getUpdater().map.loadImages(app, imageHandler);
		app.getDrawer().symbolManager = new SymbolManager(app);
	}

	public GameDrawer(GameBaseApp app) {
		this.app = app;
		this.updater = (GameUpdater) app.getUpdater();

		app.setDrawer(this);// set oficial drawer so hud, etc can access it
		setHud(new HUD(app, this));
		setAimHandler(new AimHandler(app));

		commandoutput = true;

	}

	public void update() {
		app.clear();
		app.background(100);
		app.pushMatrix();
		app.translate(xMapOffset, yMapOffset);
		app.scale(zoom);
		app.stroke(0);

		getImageHandler().drawImage(app, updater.map.textur, 0, 0, updater.map.width, updater.map.height / 2);
		app.imageMode(PConstants.CENTER);
		app.rectMode(PConstants.CENTER);
		ArrayList<GameObject> entities = updater.getGameObjects();
		for (int i = entities.size() - 1; i >= 0; i--) {
			GameObject e = entities.get(i);
			e.renderTerrain();
		}
		app.imageMode(PConstants.CORNER);
		app.rectMode(PConstants.CORNER);
		updater.map.updateFogofWar(app.getPlayer());
		app.blendMode(PConstants.MULTIPLY);
		getImageHandler().drawImage(app, updater.map.fogOfWar, 0, 0, updater.map.width, updater.map.height / 2);// hä?
		app.blendMode(PConstants.BLEND);
		app.imageMode(PConstants.CENTER);
		app.rectMode(PConstants.CENTER);
		for (GameObject e : app.getPlayer().visibleEntities) {
			e.renderUnder();
		}
		if (showRanges) {
			for (GameObject e : app.getPlayer().visibleEntities) {
				e.renderRange();
			}
		}
		for (GameObject e : app.getPlayer().visibleEntities) {
			e.renderGround();
		}
		for (GameObject e : app.getPlayer().visibleEntities) {
			e.renderAir();
		}

		for (GameObject e : app.getPlayer().visibleEntities) {
			e.display();
		}

		getAimHandler().update();
		app.rectMode(PConstants.CORNER);
		app.popMatrix();
		app.imageMode(PConstants.CORNER);

		getHud().update();

		if (updater.gameState != GameState.PLAY) {
			app.fill(100, 100);
			app.rect(0, 0, app.width, app.height);
			String s = "bla";
			if (updater.gameState == GameState.PAUSE) {
				s = "PAUSE";
			} else if (updater.gameState == GameState.WON) {
				s = "WON";
			} else if (updater.gameState == GameState.LOST) {
				s = "LOST";
			}
			app.fill(255);
			app.text(s, app.width / 2 - app.textWidth(s) / 2, app.height / 2);
		}
		// AimHandler.update();
		// app.image(render, 0, 0);
	}

	public void dispose() {
		getImageHandler().dispose();
	}

	public AimHandler getAimHandler() {
		return aimHandler;
	}

	public void setAimHandler(AimHandler aimHandler) {
		this.aimHandler = aimHandler;
	}

	public HUD getHud() {
		if (hud == null)
			System.err.println("hud not created");
		return hud;
	}

	public void setHud(HUD hud) {
		this.hud = hud;
	}

	public ImageHandler getImageHandler() {
		return imageHandler;
	}

	public void setImageHandler(ImageHandler imageHandler) {
		this.imageHandler = imageHandler;
	}

	public PImage getSymbol(String string) {
		return symbolManager.getSymbol(string);
	}

}
