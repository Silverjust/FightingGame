package shared;

import javax.naming.NoInitialContextException;

import g4p_controls.GCScheme;
import game.ClientHandler;
import game.GameBaseApp;
import game.GameDrawer;
import gameStructure.GameObject;
import processing.core.PApplet;
import server.Protocol;
import shared.Updater.GameState;

public class CommandHandler {
	private GameBaseApp app;
	private ClientHandler clientHandler;

	public CommandHandler(GameBaseApp app) {
		this.app = app;
		clientHandler = app.clientHandler;
	}

	public void executeCommands(String command) {
		String[] c = PApplet.splitTokens(command, " ");

		try {
			int i;
			float f;
			GameObject e;
			switch (c[0]) {
			case "/dmg":
				clientHandler.send(Coms.DAMAGE + " " + c[1] + " " + c[2]);
				break;
			case "/tp":
				clientHandler.send("<tp " + c[1] + " " + c[2] + " " + c[3]);
				break;
			case "/spawn":
				c[0] = c[0].replaceFirst("/", "<");
				c[2] = Helper.nameToIP(app, c[2]);
				clientHandler.send(PApplet.join(c, " "));
				break;
			case "/kill":
				clientHandler.send(command.replaceFirst("/", "<"));
				break;
			case "/remove":
				clientHandler.send(command.replaceFirst("/", "<"));
				break;
			case "/say":
				clientHandler.send(command.replaceFirst("/", "<"));
				break;
			case "/info":
				i = Integer.parseInt(c[1]);
				e = app.getUpdater().getGameObject(i);
				if (e != null) {
					e.info();
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case "/zoom":
				f = Float.parseFloat(c[1]);
				GameDrawer.zoom = f;
				GameDrawer.xMapOffset *= f;
				GameDrawer.yMapOffset *= f;
				break;
			case "/reloadImages":
				app.getDrawer().imageHandler.requestAllImages();
				break;
			case "/saveMap":
				app.getUpdater().mapHandler.saveMap(c[1], c[2]);
				break;
			case "/fps":
				app.write("fps", app.frameRate + "");
				break;
			case "/scheme":
				i = Integer.parseInt(c[1]);
				int r = Integer.parseInt(c[2]);
				int g = Integer.parseInt(c[3]);
				int b = Integer.parseInt(c[4]);
				GCScheme.setScheme(8, i, app.color(r, g, b));
				break;
			case "/proto":
				Protocol.createFile();
				break;
			case "/pause":
				if (app.getUpdater().gameState == GameState.PAUSE) {
					app.getUpdater().send(Coms.PAUSE + " false");
				} else {
					app.getUpdater().send(Coms.PAUSE + " true");
				}
				break;
			case "/gamerule":
				if (c[1].equals("commandoutput")) {
					GameDrawer.commandoutput = Boolean.valueOf(c[2]);
				}
				if (c[1].equals("godeye")) {
					GameDrawer.godeye = Boolean.valueOf(c[2]);
				}
				if (c[1].equals("godhand")) {
					GameDrawer.godhand = Boolean.valueOf(c[2]);
				}
				if (c[1].equals("nocosts")) {
					GameDrawer.nocosts = Boolean.valueOf(c[2]);
				}
				if (c[1].equals("showrange")) {
					GameDrawer.showRanges = Boolean.valueOf(c[2]);
				}
				break;
			default:
				throw new NoInitialContextException("no command found");
			}
		} catch (IllegalArgumentException e) {
			System.err.println("error " + command);
			e.printStackTrace();
			app.write("Chat", "error");
		} catch (ClassCastException e) {
			System.err.println("wrong entity " + command);
			app.write("Chat", "wrong entity");
		} catch (NoInitialContextException e) {
			System.err.println(command + " was not found");
			app.write("Chat", "command was not found");
		} catch (Exception e) {
			System.err.println("command error in " + command);
			e.printStackTrace();
			app.write("Chat", "command error");
		}

	}
}
