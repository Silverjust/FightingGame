package shared;

import game.GameBaseApp;

public abstract class ComHandler implements Coms{

	protected GameBaseApp app;
	protected ContentListManager contentListHandler;
	protected Updater updater;

	public ComHandler(GameBaseApp app) {
		this.app = app;
	}

	public abstract void executeCom(String com);

	public void addUpdater(Updater updater) {
		this.updater = updater;
		
	}


}
