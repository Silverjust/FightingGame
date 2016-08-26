package main.preGame;

import g4p_controls.GButton;
import g4p_controls.GDropList;
import g4p_controls.GEvent;
import main.ClientHandler;
import main.MainApp;
import shared.Mode;
import shared.Nation;
import shared.PreGame;
import shared.ref;

public class SandboxSelection {
	public GDropList playerDroplist;
	public GDropList playerDroplistNation;
	public int previousNation;
	public GButton addPlayer;

	PreGame preGame;

	public SandboxSelection(PreGame preGame) {
		this.preGame = preGame;

		playerDroplist = new GDropList(ref.app, 200, 200, 200, 200, 6);
		playerDroplist.addEventHandler(this, "handleSelectPlayer");
		updatePlayerDroplist();
		playerDroplistNation = new GDropList(ref.app, 420, 200, 100, 200, 6);
		String[] nations = new String[Nation.values().length];
		int neutralIndex = -1;
		for (int i = 0; i < nations.length; i++) {
			nations[i] = Nation.values()[i].officialName();
			if (nations[i].equals("neutral")) {
				nations[i] = " ";
				neutralIndex = i;
			}
		}
		playerDroplistNation.setItems(nations, neutralIndex);
		playerDroplistNation.addEventHandler(this, "handleSelectNation");
		{
			handleSelectNation(playerDroplistNation, GEvent.SELECTED);
			ClientHandler.send("<setNation " + 1 + " " + "Aliens");
		}

		addPlayer = new GButton(ref.app, 540, 200, 100, 35, "add Player");
		addPlayer.addEventHandler(this, "handleAddPlayer");
	}

	private void updatePlayerDroplist() {
		if (!preGame.users.isEmpty()) {
			String[] enemyArr = new String[preGame.users.size()];
			int i = 0;
			for (String key : preGame.users.keySet()) {
				enemyArr[i] = preGame.users.get(key).name;
				i++;
			}
			playerDroplist.setItems(enemyArr, 0);
		}
	}

	public void handleSelectPlayer(GDropList droplist, GEvent event) {
		if (event == GEvent.SELECTED && ((MainApp) ref.app).mode == Mode.PREGAME) {
			String[] enemyArr = new String[preGame.users.size()];
			int i = 0;
			for (String key : preGame.users.keySet()) {
				enemyArr[i] = preGame.users.get(key).ip;
				i++;
			}
			Nation n = preGame.users.get(enemyArr[droplist.getSelectedIndex()]).nation;
			byte b = -1;
			if (n != null) {
				for (byte j = 0; j < Nation.values().length; j++) {
					if (Nation.values()[j].toString().equals(n.toString()))
						b = j;
				}
			}
			System.out.println(b);
			playerDroplistNation.setSelected(b);
			previousNation = b;
		}
	}

	public void handleSelectNation(GDropList droplist, GEvent event) {
		if (event == GEvent.SELECTED && ((MainApp) ref.app).mode == Mode.PREGAME) {
			String[] enemyArr = new String[preGame.users.size()];
			int i = 0;
			for (String key : preGame.users.keySet()) {
				enemyArr[i] = preGame.users.get(key).ip;
				i++;
			}
			int neutralIndex = -1;
			for (int j = 0; j < Nation.values().length; j++) {
				if (Nation.values()[j] == Nation.NEUTRAL) {
					neutralIndex = j;
				}
			}
			if (droplist.getSelectedIndex() == neutralIndex) {
				droplist.setSelected(previousNation);
				return;
			}
			previousNation = droplist.getSelectedIndex();
			ClientHandler.send("<setNation " + enemyArr[playerDroplist.getSelectedIndex()] + " "
					+ Nation.values()[droplist.getSelectedIndex()]);

		}
	}

	public void handleAddPlayer(GButton button, GEvent event) {
		if (event == GEvent.CLICKED && ((MainApp) ref.app).mode == Mode.PREGAME) {
			preGame.addPlayer(preGame.users.size() + 1 + "", "n000bBot" + preGame.users.size());
			updatePlayerDroplist();
		}
	}

	public void dispose() {
		playerDroplist.dispose();
		playerDroplistNation.dispose();
		addPlayer.dispose();
	}

	public void setEnabled(boolean b) {
		playerDroplist.setEnabled(b);
		playerDroplistNation.setEnabled(b);
		addPlayer.setEnabled(b);
	}
}