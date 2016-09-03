package game;

import shared.Coms;

public abstract class CommandHandler implements Coms {
	protected GameBaseApp app;

	public CommandHandler(GameBaseApp app) {
		this.app = app;
	}

	public void executeCommand(String command) {

	}

	protected void send(String input) {
		app.getUpdater().send(input);
	}
}
