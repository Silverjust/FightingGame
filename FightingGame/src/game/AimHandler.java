package game;

import game.aim.Aim;
import game.aim.EmptyAim;
import processing.core.PConstants;
import processing.core.PImage;
import shared.ref;

public class AimHandler {

	private static PImage buildImg, selectImg, shootImg, moveImg;
	static Aim aim;

	public static void loadImages() {
		buildImg = ImageHandler.load("", "build");
		selectImg = ImageHandler.load("", "select");
		shootImg = ImageHandler.load("", "shoot");
		moveImg = ImageHandler.load("", "move");
	}

	public static void setup() {
		aim = new EmptyAim();
	}

	public static void update() {
		if (aim != null) {
			aim.update();
		}
	}

	public static void execute(float x, float y) {
		if (aim != null) {
			aim.execute(x, y);
		}
	}

	public static void move(float x, float y) {
		if (aim != null ) {
			aim.move(x, y);
		}
		ref.player.champion.sendAnimation("walk " + x + " " + y + "  " );
	}

	public static void end() {
		if (aim != null) {
			aim.end();
		}
		aim = new EmptyAim();
		setCursor(Cursor.ARROW);
	}

	public static void setAim(Aim a) {
		end();
		aim = a;
		setCursor(aim.getCursor());
	}

	public static void setCursor(Cursor c) {
		switch (c) {
		case ARROW:
			ref.app.cursor(PConstants.ARROW);
			break;
		case BUILD:
			ref.app.cursor(buildImg, 15, 15);
			break;
		case SELECT:
			ref.app.cursor(selectImg, 15, 15);
			break;
		case SHOOT:
			ref.app.cursor(shootImg, 15, 15);
			break;
		case MOVE:
			ref.app.cursor(moveImg, 15, 31);
			break;
		default:
			break;
		}
	}

	public enum Cursor {
		ARROW, BUILD, SHOOT, SELECT, MOVE
	}

	public static Aim getAim() {
		return aim;
	}

	public static boolean isDefault() {
		return aim instanceof EmptyAim;
	}
}
