package main;

import shared.GameBaseApp;

public class LoadingScreen {

	private String[] ASCIItitle = { "___________.__  .__  __                    ",
			"\\_   _____/|  | |__|/  |_  ____            ", " |    __)_ |  | |  \\   __\\/ __ \\           ",
			" |        \\|  |_|  ||  | \\  ___/           ", "/_______  /|____/__||__|  \\___  >          ",
			"        \\/                    \\/           ", "___________              .__               ",
			"\\_   _____/ ____    ____ |__| ____   ____  ", " |    __)_ /    \\  / ___\\|  |/    \\_/ __ \\ ",
			" |        \\   |  \\/ /_/  >  |   |  \\  ___/ ", "/_______  /___|  /\\___  /|__|___|  /\\___  >",
			"        \\/     \\//_____/         \\/     \\/ ", "", "" };
	private GameBaseApp app;

	public LoadingScreen(GameBaseApp app) {

		this.app = app;
		for (int i = 0; i < ASCIItitle.length; i++) {
			System.out.println(ASCIItitle[i]);
		}

	}

	static float f;

	public void update() {
		app.rect(100, 100, app.width - 200, app.height - 200);// provisorisch

		app.rect(110, app.height - 150, app.width - 220, 40);
		app.fill(100);
		app.rect(110, app.height - 150, (app.width - 220) * f, 40);
		app.fill(255);
	}

	public void setPercent(float F) {
		f = F;
	}

}
