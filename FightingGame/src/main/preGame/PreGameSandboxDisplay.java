package main.preGame;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;

public class PreGameSandboxDisplay extends PreGameDisplay {

	public PreGameSandboxDisplay() {
		super();
		sandboxSelection = new SandboxSelection(preGame);
		playerPanel = new PlayerPanel();
	}

	@Override
	public void update() {
		super.update();
		playerPanel.draw(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		sandboxSelection.dispose();
		playerPanel.dispose();
	}

	@Override
	public void setActive(boolean b) {
		super.setActive(b);
		sandboxSelection.setEnabled(b);
		playerPanel.setEnabled(b);
	}

	@Deprecated
	@Override
	public void handleSelectNation(GGameButton button, GEvent event) {
	}
}
