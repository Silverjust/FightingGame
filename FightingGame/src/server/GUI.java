package server;

import processing.core.PConstants;
import processing.core.PGraphics;
import shared.CommandHandler;
import shared.Helper;
import shared.Mode;
import shared.Server;
import shared.ref;
import g4p_controls.GCheckbox;
import g4p_controls.GControlMode;
import g4p_controls.GEvent;
import g4p_controls.GSlider;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;

public class GUI {
	String chatText = "";

	GTextField ipAdress;
	GTextField modeDisplay;
	GTextArea clients;
	GTextField chatLine;

	GCheckbox displayCommands;

	GSlider chatSlider, playerSlider;

	PGraphics chat, player;

	public GUI() {

		ipAdress = new GTextField(ref.app, 0, 0, 300, 20);
		ipAdress.setText(Server.ip()+ "");

		modeDisplay = new GTextField(ref.app, 300, 0, 200, 20);
		modeDisplay.setTextEditEnabled(false);

		displayCommands = new GCheckbox(ref.app, 0, ref.app.height - 300, 30, 20);

		chat = ref.app.createGraphics(500, 280);
		player = ref.app.createGraphics(300, 178);

		chatSlider = new GSlider(ref.app, 500, 220, 280, 20, 20);
		chatSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		chatSlider.setLimits(0, 0, 1);

		playerSlider = new GSlider(ref.app, 300, 20, 180, 20, 20);
		playerSlider.setRotation(PConstants.PI / 2, GControlMode.CORNER);
		playerSlider.setLimits(0, 0, 1);

		chatLine = new GTextField(ref.app, 17, 200, 500, 20);
		chatLine.addEventHandler(this, "chatEvents");
	}

	public void update() {
		modeDisplay.setText(((ServerApp) ref.app).mode.toString());

		player.beginDraw();
		player.background(255);
		if (!ref.preGame.users.isEmpty()) {
			playerSlider.setLimits(0, ref.preGame.users.size() * 20 - 19);
			int i = 0;
			for (String key : ref.preGame.users.keySet()) {
				ref.preGame.users.get(key).display(player, 0, 20 * i - playerSlider.getValueI());
				i++;
			}
		}
		player.endDraw();
		ref.app.image(player, 0, 21);

		chat.beginDraw();
		chat.background(255);
		chat.fill(0);
		chat.text(chatText.toString(), 10, ref.app.textAscent() - chatSlider.getValueI());
		chat.endDraw();
		ref.app.image(chat, 0, ref.app.height - 280);
	}

	public void addChatText(String s) {
		if (!s.equals("")) {
			chatText = s.concat("\n").concat(chatText);
			int i = chatText.split("\n").length;
			if (i < 500) {
				chatSlider.setLimits(0, i * ref.app.textAscent());
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
					if (((ServerApp) ref.app).mode == Mode.GAME || ((ServerApp) ref.app).mode == Mode.PREGAME)
						ref.updater.send("<say " + "SERVER" + " " + s);
				}
			}
			textfield.setText("");
		}
	}
}
