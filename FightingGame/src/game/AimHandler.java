package game;

import game.aim.Aim;
import game.aim.EmptyAim;
import gameStructure.GameObject;
import processing.core.PConstants;
import processing.core.PImage;
import shared.GameBaseApp;

public class AimHandler {

	private static PImage buildImg, selectImg, shootImg, moveImg;
	static Aim aim;
	private GameBaseApp app;

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		buildImg = imageHandler.load("", "build");
		selectImg = imageHandler.load("", "select");
		shootImg = imageHandler.load("", "shoot");
		moveImg = imageHandler.load("", "move");
	}

	public AimHandler(GameBaseApp app) {
		this.app = app;
		aim = new EmptyAim();
	}

	public void update() {
		if (aim != null) {
			aim.update();
		}
	}

	public void execute(float x, float y) {
		if (aim != null) {
			aim.execute(x, y);
		}
	}

	public void cancel(float x, float y) {
		if (aim != null) {
			aim.move(x, y);
		}
		
	}

	public void end() {
		if (aim != null) {
			aim.end();
		}
		aim = new EmptyAim();
		setCursor(Cursor.ARROW);
	}

	public void setAim(Aim a) {
		end();
		aim = a;
		setCursor(aim.getCursor());
	}

	public void setCursor(Cursor c) {
		switch (c) {
		case ARROW:
			app.cursor(PConstants.ARROW);
			break;
		case BUILD:
			app.cursor(buildImg, 15, 15);
			break;
		case SELECT:
			app.cursor(selectImg, 15, 15);
			break;
		case SHOOT:
			app.cursor(shootImg, 15, 15);
			break;
		case MOVE:
			app.cursor(moveImg, 15, 31);
			break;
		default:
			break;
		}
	}

	public enum Cursor {
		ARROW, BUILD, SHOOT, SELECT, MOVE
	}

	public Aim getAim() {
		return aim;
	}

	public boolean isDefault() {
		return aim instanceof EmptyAim;
	}

	public void attack(GameObject e) {
		// TODO Auto-generated method stub
		
	}
}
