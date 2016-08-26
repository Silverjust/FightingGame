package main;

import game.GameApplet;

public class LoadingScreen {

	static String[] ASCIItitle = {
			"___________.__  .__  __                    ",
			"\\_   _____/|  | |__|/  |_  ____            ",
			" |    __)_ |  | |  \\   __\\/ __ \\           ",
			" |        \\|  |_|  ||  | \\  ___/           ",
			"/_______  /|____/__||__|  \\___  >          ",
			"        \\/                    \\/           ",
			"___________              .__               ",
			"\\_   _____/ ____    ____ |__| ____   ____  ",
			" |    __)_ /    \\  / ___\\|  |/    \\_/ __ \\ ",
			" |        \\   |  \\/ /_/  >  |   |  \\  ___/ ",
			"/_______  /___|  /\\___  /|__|___|  /\\___  >",
			"        \\/     \\//_____/         \\/     \\/ ", "", "" };

	public static void setup() {
		for (int i = 0; i < ASCIItitle.length; i++) {
			System.out.println(ASCIItitle[i]);
		}

	}

	static float f;

	public static void update() {
		GameApplet.app.rect(100, 100, GameApplet.app.width - 200, GameApplet.app.height - 200);// provisorisch

		GameApplet.app.rect(110, GameApplet.app.height - 150, GameApplet.app.width - 220, 40);
		GameApplet.app.fill(100);
		GameApplet.app.rect(110, GameApplet.app.height - 150, (GameApplet.app.width - 220) * f, 40);
		GameApplet.app.fill(255);
	}

	public static void setPercent(float F) {
		f = F;
	}

}
