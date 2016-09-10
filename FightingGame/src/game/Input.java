package game;

import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;

import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import shared.Coms;
import shared.GameBaseApp;
import shared.Helper;
import shared.Mode;
import shared.Updater.GameState;

public class Input {

	GameApp app;

	boolean isChatVisible;
	boolean isOpeningChat;
	boolean isMPressedOutOfFocus;
	boolean strgMode;// rename
	boolean shiftMode;// rename

	int doubleClickIntervall;
	int doubleClickStart;

	public Input(GameBaseApp app) {
		this.app = (GameApp) app;
		app.registerMethod("mouseEvent", this);
		app.registerMethod("keyEvent", this);

		doubleClickIntervall = (int) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
	}

	public boolean isMouseFocusInGame() {
		return Helper.isMouseOver(app, 0, 0, app.width, app.height - app.getDrawer().getHud().height)
				|| !app.mousePressed;
	}

	public boolean isKeyFocusInGame() {
		return !app.getDrawer().getHud().chat.hasFocus();
	}

	public void update() {// ********************************************************
		int screenSpeed = 30;
		int rimSize = 10;
		if (app.focused && !isMPressedOutOfFocus) {
			if (Helper.isMouseOver(app, 0, 0, rimSize, app.height) && GameDrawer.xMapOffset < 0)
				GameDrawer.xMapOffset += screenSpeed;
			if (Helper.isMouseOver(app, app.width - rimSize, 0, app.width, app.height)
					&& -GameDrawer.xMapOffset + app.width <= app.getUpdater().map.width * GameDrawer.zoom)
				GameDrawer.xMapOffset -= screenSpeed;
			if (Helper.isMouseOver(app, 0, 0, app.width, rimSize) && GameDrawer.yMapOffset < 0)
				GameDrawer.yMapOffset += screenSpeed;
			if (Helper.isMouseOver(app, 0, app.height - rimSize, app.width, app.height) && -GameDrawer.yMapOffset
					+ app.height - app.getDrawer().getHud().height <= app.getUpdater().map.height / 2 * GameDrawer.zoom)
				GameDrawer.yMapOffset -= screenSpeed;
		}
	}

	public void keyPressed() {// ********************************************************
		app.getDrawer().getHud().chat.update();
		if (isKeyFocusInGame()) {
			if (app.key == app.settingHandler.getSetting().togglePause) {
				if (app.getUpdater().gameState == GameState.PAUSE) {
					app.getUpdater().send(Coms.PAUSE + " false");
				} else if (app.getUpdater().gameState == GameState.PLAY) {
					app.getUpdater().send(Coms.PAUSE + " true");
				}
			}
			/*
			 * if (app.keyCode == SettingHandler.setting.changeAbilityMode) { if
			 * (GroupHandler.recentGroup != null) {
			 * GroupHandler.recentGroup.unitActives =
			 * !GroupHandler.recentGroup.unitActives;
			 * app.getDrawer().getHud().activesGrid
			 * .selectionChange(GroupHandler.recentGroup.unitActives); } else {
			 * app.getDrawer().getHud().activesGrid
			 * .selectionChange(!ActivesGridHandler.showUnitActives); } }
			 */
			if (app.keyCode == app.settingHandler.getSetting().strg) {
				strgMode = true;
			}
			if (app.keyCode == app.settingHandler.getSetting().shift) {
				shiftMode = true;
			}
			if (app.key == app.settingHandler.getSetting().centerView) {
				System.out.println("Input.keyPressed()");
			}
			for (int i = 0; i < app.settingHandler.getSetting().baseShortcuts.length; i++) {
				char c = app.settingHandler.getSetting().baseShortcuts[i];
				if (app.key == c) {
					app.getDrawer().getHud().playerInterface.handleKeyPressed(c);
				}
			}
			if (app.key == app.settingHandler.getSetting().centerView) {
				System.out.println("Input.keyPressed()");
			}
			// System.out.println(app.key);
			for (int i = 0; i < app.settingHandler.getSetting().hotKeys.length; i++) {

			}
			for (int x = 0; x < 7; x++) {
				for (int y = 0; y < 3; y++) {
				}
			}

		}
	}

	public void keyReleased() {// ********************************************************
		app.getDrawer().getHud().chat.justOpened = false;
		if (app.keyCode == app.settingHandler.getSetting().strg) {
			strgMode = false;
		}
		if (app.keyCode == app.settingHandler.getSetting().shift) {
			shiftMode = false;
		}
	}

	public void mouseClicked() {// ********************************************************
	}

	public void mousePressed() {// ********************************************************
		isMPressedOutOfFocus = !isMouseFocusInGame();
		// app.getDrawer().getHud().chat.println("", "" + isMPressedOutOfFocus);
		if (doubleClickStart + doubleClickIntervall > app.millis()) {

		} else {

		}
		if (isMouseFocusInGame()) {
			mouseCommands(Helper.gridToX(app.mouseX), Helper.gridToY(app.mouseY));
		} else {
			app.getDrawer().getHud().minimap.click(app.mouseX, app.mouseY, true);
		}
		// unabhängig von mouse fokus
	}

	public void mouseReleased() {// ********************************************************
		isMPressedOutOfFocus = false;
	}

	public void mouseDragged() {// ********************************************************
		if (!isMouseFocusInGame()) {
			app.getDrawer().getHud().minimap.click(app.mouseX, app.mouseY, true);
		}
	}

	public void mouseMoved() {// ********************************************************

	}

	public void mouseWheelMoved(MouseWheelEvent e) {// ********************************************************
		if (app.getDrawer().getHud().chat.hasFocus()) {
		} else {// chat out of focus
		} // unabhängig von chat fokus

	}

	public void keyEvent(KeyEvent event) {
		if (app.getMode() == Mode.GAME) {
			switch (event.getAction()) {
			case KeyEvent.PRESS:
				keyPressed();
				break;
			case KeyEvent.RELEASE:
				keyReleased();
				break;
			default:
				break;
			}
			if (app.key == PConstants.ESC) {
				app.key = 0;
			}
		}
	}

	public void mouseEvent(MouseEvent event) {
		if (app.getMode() == Mode.GAME) {
			switch (event.getAction()) {
			case MouseEvent.PRESS:
				mousePressed();
				break;
			case MouseEvent.RELEASE:
				mouseReleased();
				break;
			case MouseEvent.DRAG:
				mouseDragged();
				break;
			case MouseEvent.MOVE:
				mouseMoved();
				break;
			default:
				break;
			}
		}
	}

	void mouseCommands(float x, float y) {
		if (app.mouseButton == app.settingHandler.getSetting().mouseMove) {
			app.getDrawer().getAimHandler().move(x, y);
		} else if (app.mouseButton == app.settingHandler.getSetting().mouseCommand) {
			app.getDrawer().getAimHandler().execute(x, y);
		}
	}
}
