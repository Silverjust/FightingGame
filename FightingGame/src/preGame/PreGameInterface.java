package preGame;

import java.util.HashMap;

import g4p_controls.GAbstractControl;
import g4p_controls.GAlign;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GOption;
import g4p_controls.GToggleGroup;
import gameStructure.Champion;
import shared.ContentListManager;
import shared.Team;

public class PreGameInterface {

	private PreGameApp app;
	private GButton startButton;
	private GToggleGroup championGroup;
	private HashMap<String, GOption> champsOptionsMap = new HashMap<String, GOption>();
	private GAbstractControl switchSideButton;
	private UserLabelManager userLabelManager;

	public PreGameInterface(PreGameApp app) {
		this.app = app;
		app.setContentListManager(new ContentListManager(app));
		app.frame.setTitle(app.getPreGameInfo().getUser("").name + " - " + app.gameName);

		startButton = new GButton(app, 850, 550, 100, 30, "start Game");
		startButton.addEventHandler(this, "startButtonEvents");

		championGroup = new GToggleGroup();
		Class<? extends Champion>[] champs = app.getContentListManager().getChampsArray();
		int i = 0;
		for (Class<? extends Champion> c : champs) {
			Champion champ = (Champion) app.getContentListManager().createGObj(c);
			String ingameName = champ.getIngameName();

			GOption option = new GOption(app, 150, 100 + 40 * i, 120, 30);
			option.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
			option.setText(ingameName);
			option.setOpaque(true);
			option.addEventHandler(this, "champOption_clicked");
			champsOptionsMap.put(ingameName, option);
			championGroup.addControl(option);
			i++;
		}
		userLabelManager = new UserLabelManager(app);
		updateUsers(app);

		switchSideButton = new GButton(app, 50, 550, 100, 30, "switch Team");
		switchSideButton.addEventHandler(this, "switchSideButtonEvents");
	}

	public void updateUsers(PreGameApp app) {
		userLabelManager.update();
	}

	void update() {
		app.background(150);
	}

	public void champOption_clicked(GOption option, GEvent event) {
		for (String s : champsOptionsMap.keySet()) {
			GOption o = champsOptionsMap.get(s);
			if (option == o) {
				app.getPreGameInfo().getUser("").champion = s;
				updateUsers(app);
			}
		}
	}

	public void switchSideButtonEvents(GButton button, GEvent event) {
		Team team = app.getPreGameInfo().getUser("").team;
		if (team == Team.LEFTSIDE)
			app.getPreGameInfo().getUser("").team = Team.RIGHTSIDE;
		else
			app.getPreGameInfo().getUser("").team = Team.LEFTSIDE;
		updateUsers(app);
	}

	public void startButtonEvents(GButton button, GEvent event) {
	}
}
