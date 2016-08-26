package shared;

public abstract class Loader {
	protected State state = State.NEWGAME;

	public abstract void update();

	public abstract void startGame();

	public abstract void tryStartGame();

	protected enum State {
		STARTIMAGES, IMAGES, NEWGAME, MAP, ENTITIES, WAIT, END, ERROR
	}
}
