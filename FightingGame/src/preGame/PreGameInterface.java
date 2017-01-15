package preGame;

import java.util.HashMap;

import g4p_controls.GAbstractControl;
import g4p_controls.GAlign;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GOption;
import g4p_controls.GToggleGroup;
import gameStructure.Champion;
import gameStructure.GameObject;
import shared.ClientHandler;
import shared.Coms;
import shared.ContentListManager;
import shared.Mode;
import shared.Team;

public class PreGameInterface {

	private PreGameApp app;
	private GButton startButton;
	private GToggleGroup championGroup;
	private HashMap<String, GOption> champsOptionsMap = new HashMap<String, GOption>();
	private GAbstractControl switchSideButton;
	private UserLabelManager userLabelManager;
	private ClientHandler clientHandler;
	private boolean isFirstLoop = true;

	public PreGameInterface(PreGameApp app) {
		this.app = app;
		app.setContentListManager(new ContentListManager(app));
		clientHandler = app.getClientHandler();
		app.frame.setTitle(app.getStartscreen().name + " - " + app.gameName);
		if (app.getServerApp() != null) {
			startButton = new GButton(app, 850, 550, 100, 30, "start Game");
			startButton.addEventHandler(this, "startButtonEvents");
		}
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
		if (app.getMode() == Mode.PREGAME)
			userLabelManager.update();

	}

	void update() {
		app.background(150);
		if (isFirstLoop) {
			champsOptionsMap.get("Ticul").setSelected(true);
			clientHandler.send(Coms.SET_CHAMP + " " + clientHandler.getIdentification() + " Ticul");
			if (app.getServerApp() == null)
				clientHandler.send(Coms.SET_TEAM + " " + clientHandler.getIdentification() + " " + Coms.RIGHTSIDE);
			isFirstLoop = false;
		}
		if (app.frameCount % 100 == 0)
			updateUsers(app);
	}

	public void champOption_clicked(GOption option, GEvent event) {
		for (String champ : champsOptionsMap.keySet()) {
			GOption o = champsOptionsMap.get(champ);
			if (option == o) {
				clientHandler.send(Coms.SET_CHAMP + " " + clientHandler.getIdentification() + " " + champ);

			}
		}
	}

	public void switchSideButtonEvents(GButton button, GEvent event) {
		Team team = app.getPreGameInfo().getUser("").team;
		if (team == Team.LEFTSIDE) {
			clientHandler.send(Coms.SET_TEAM + " " + clientHandler.getIdentification() + " " + Coms.RIGHTSIDE);
		} else {
			clientHandler.send(Coms.SET_TEAM + " " + clientHandler.getIdentification() + " " + Coms.LEFTSIDE);
		}
		/*
		 * app.getPreGameInfo().getUser("").team = Team.RIGHTSIDE; else
		 * app.getPreGameInfo().getUser("").team = Team.LEFTSIDE;
		 */
		updateUsers(app);
	}

	public void startButtonEvents(GButton button, GEvent event) {
		boolean canStart = true;
		for (String ip : app.getPreGameInfo().getUsers().keySet()) {
			String championName = app.getPreGameInfo().getUser(ip).championName;
			if (championName != null && !championName.equals("") && !championName.equals("null")) {
				ContentListManager contentListManager = app.getContentListManager();
				GameObject obj = contentListManager.createGObj(contentListManager.getChampClass(championName));
				if (!(obj instanceof Champion))
					canStart = false;
			} else
				canStart = false;
		}
		if (canStart)
			clientHandler.send(Coms.START_GAMEAPPS + " " + clientHandler.getIdentification());
	}

	public void dispose() {
		startButton.dispose();
		switchSideButton.dispose();
		userLabelManager.dispose();
		for (String s : champsOptionsMap.keySet()) {
			champsOptionsMap.get(s).dispose();
		}
	}
}
