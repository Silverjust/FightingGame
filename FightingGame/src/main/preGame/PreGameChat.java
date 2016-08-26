package main.preGame;

import g4p_controls.G4P;
import g4p_controls.GEvent;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;
import game.GameApplet;

import java.awt.Font;
import java.util.ArrayList;

import processing.core.PConstants;
import main.ClientHandler;
import shared.CommandHandler;
import shared.Helper;

public class PreGameChat {

	private GTextField chatLine;
	private GTextArea chatHistory;

	String out;
	//boolean justOpened;

	PreGameChat() {
		chatLine = new GTextField(GameApplet.app, 10, GameApplet.app.height - 30, 500, 20);
		chatLine.setPromptText("chat");
		chatLine.setFont(new Font("PLAIN", Font.BOLD, 15));
		chatLine.addEventHandler(this, "chatEvents");

		chatHistory = new GTextArea(GameApplet.app, 10, GameApplet.app.height - 250, 500, 200,
				G4P.SCROLLBARS_VERTICAL_ONLY | G4P.SCROLLBARS_AUTOHIDE);
		chatHistory.setTextEditEnabled(false);
		chatHistory.setOpaque(false);
		chatHistory.setFont(new Font("PLAIN", Font.BOLD, 17));
		chatHistory.addEventHandler(this, "handleTextEvents");
	}

	public void update() {
		if (GameApplet.app.keyCode == PConstants.ENTER) {
			if (!chatLine.isVisible()) {
				show();
			}
		} else if (GameApplet.app.keyCode == PConstants.BACKSPACE) {
			String s = Helper.secureInput(chatLine.getText());
			System.out.println("Chat.update()\n" + chatLine.getText() + "\n" + s);

			if (s.equals("")) {
				hide();
			}
		}
	}

	public void show() {
		// System.out.println("Chat.show()");
		//justOpened = true;
		//chatLine.setVisible(true);
		//chatLine.setFocus(true);
	}

	public void hide() {
		// System.out.println("Chat.hide()");
		//if (chatLine.isVisible())
		//	chatLine.setVisible(false);
	}

	public void println(String name, String s) {
		if (!name.equals("SERVER")) {
			ArrayList<String> chatText = new ArrayList<String>();
			chatText.add(name + ">>" + s);
			// chatHistory.setText((String[]) chatText.toArray());
			chatHistory.appendText(name + ">>" + s + "\n");
			System.out.println(name + ">>" + s);
		}
	}

	public void chatEvents(GTextField textfield, GEvent event) {
		switch (event) {
		case ENTERED:
			String s = Helper.secureInput(textfield.getText());
			if (s.equals("")) {
				/*if (!justOpened) {
					System.out.println("Chat.chatEvents() hide cause empty and enter");
					hide();
				}*/
			} else {
				if (s.length() > 0 && s.charAt(0) == '/') {
					println(GameApplet.preGame.getUser("").name, s);
					CommandHandler.executeCommands(s);
				} else {
					ClientHandler.send("<say " + GameApplet.preGame.getUser("").ip + " " + s);
				}
			}
			textfield.setText("");
			break;
		case LOST_FOCUS:
			if (textfield.isVisible()) {
				//hide();
			}
			break;
		default:
			break;
		}
	}

	public void handleTextEvents(GTextArea textarea, GEvent event) {
		if (event == GEvent.CHANGED) {
			// GameDrawer.mouseSelection = null;
		}
	}

	public void dispose() {
		chatLine.dispose();
		chatHistory.dispose();
	}

	public boolean hasFocus() {
		return chatLine.hasFocus();
	}

}
