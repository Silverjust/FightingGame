package server;

import processing.core.PConstants;
import processing.core.PGraphics;
import shared.CommandHandler;
import shared.Helper;
import shared.Mode;
import shared.Server;
import shared.User;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;

import g4p_controls.GButton;
import g4p_controls.GCheckbox;
import g4p_controls.GControlMode;
import g4p_controls.GEvent;
import g4p_controls.GSlider;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;
import game.GameBaseApp;

public class ServerInterface {
	String chatText = "";

	GButton ipButton;
	GTextField modeDisplay;
	GTextArea clients;
	GTextField chatLine;

	GCheckbox displayCommands;

	GSlider chatSlider, playerSlider;

	PGraphics chat, player;

	private ServerApp app;

	public ServerInterface(GameBaseApp app) {

		this.app = (ServerApp) app;
		ipButton = new GButton(app, 0, 0, 300, 20);
		ipButton.setText(Server.ip() + "");
		ipButton.addEventHandler(this, "handleButtonEvents");

		modeDisplay = new GTextField(app, 300, 0, 200, 20);
		modeDisplay.setTextEditEnabled(false);

		displayCommands = new GCheckbox(app, 0, app.height - 300, 30, 20);

		chat = app.createGraphics(500, 280);
		player = app.createGraphics(300, 178);

		chatSlider = new GSlider(app, 500, 220, 280, 20, 20);
		chatSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		chatSlider.setLimits(0, 0, 1);

		playerSlider = new GSlider(app, 300, 20, 180, 20, 20);
		playerSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		playerSlider.setLimits(0, 0, 1);

		chatLine = new GTextField(app, 17, 200, 500, 20);
		chatLine.addEventHandler(this, "chatEvents");
	}

	public void update() {
		modeDisplay.setText(app.getMode().toString());

		player.beginDraw();
		player.background(255);
		HashMap<String, User> users = app.preGame.getUsers();
		if (!users.isEmpty()) {
			playerSlider.setLimits(0, users.size() * 20 - 19);
			int i = 0;
			for (String key : users.keySet()) {
				users.get(key).display(player, 0, 20 * i - playerSlider.getValueI());
				i++;
			}
		}
		player.endDraw();
		app.image(player, 0, 21);

		chat.beginDraw();
		chat.background(255);
		chat.fill(0);
		chat.text(chatText.toString(), 10, app.textAscent() - chatSlider.getValueI());
		chat.endDraw();
		app.image(chat, 0, app.height - 280);
	}

	public void addChatText(String s) {
		if (!s.equals("")) {
			chatText = s.concat("\n").concat(chatText);
			int i = chatText.split("\n").length;
			if (i < 500) {
				chatSlider.setLimits(0, i * app.textAscent());
			} else {
				chatText = "";
			}
		}
	}

	public void chatEvents(GTextField textfield, GEvent event) {
		if (event == GEvent.ENTERED) {
			String s = textfield.getText().length() > 0 ? (textfield.getText().charAt(0) == ' '
					? (textfield.getText().substring(1)) : (textfield.getText())) : ("");
			s = Helper.secureInput(s);
			System.out.println(s);
			if (!s.equals("") && !s.equals(" ")) {
				if (s.length() > 0 && s.charAt(0) == '/') {
					addChatText(">>" + s);
					CommandHandler.executeCommands(s);
				} else {
					if (app.getMode() == Mode.GAME || app.getMode() == Mode.PREGAME)
						app.getUpdater().send("<say " + "SERVER" + " " + s);
				}
			}
			textfield.setText("");
		}
	}

	public void handleButtonEvents(GButton button, GEvent event) {
		if (button == ipButton) {
			StringSelection selection = new StringSelection(ipButton.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
		}
	}

}
