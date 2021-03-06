package server;

import javax.naming.NoInitialContextException;

import game.CommandHandler;
import game.GameDrawer;
import gameStructure.GameObject;
import processing.core.PApplet;
import shared.Coms;
import shared.GameBaseApp;
import shared.Helper;
import shared.Updater.GameState;

public class ServerCommandHandler extends CommandHandler {

	public ServerCommandHandler(GameBaseApp app) {
		super(app);
	}

	public void executeCommand(String command) {
		String[] c = PApplet.splitTokens(command, " ");

		try {
			int i;
			GameObject e;
			switch (c[0]) {
			case "/dmg":
				send(DAMAGE + " " + c[1] + " " + c[2]);
				break;
			case "/tp":
				send(TP + " " + c[1] + " " + c[2] + " " + c[3]);
				break;
			case "/spawn":
				c[0] = c[0].replaceFirst("/", "<");
				c[2] = Helper.nameToIP(app, c[2]);
				send(PApplet.join(c, " "));
				break;
			case "/kill":
				send(command.replaceFirst("/", "<"));
				break;
			case "/remove":
				send(command.replaceFirst("/", "<"));
				break;
			case "/say":
				send(command.replaceFirst("/", "<"));
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
			case "/saveMap":
				app.getUpdater().mapHandler.saveMap(c[1], c[2]);
				break;
			case "/fps":
				app.write("fps", app.frameRate + "");
				break;
			case "/proto":
				Protocol.createFile();
				break;
			case "/pause":
				if (app.getUpdater().gameState == GameState.PAUSE) {
					send(Coms.PAUSE + " false");
				} else {
					send(Coms.PAUSE + " true");
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
			case "/reinit":
				ServerHandler serverHandler = new ServerHandler((ServerApp) app);
				((ServerApp) app).setServerHandler(serverHandler);
				if (serverHandler.isWorking()) {
					app.write("", "re-init succesfull");
				}
				break;
			case "/dummy":
				PApplet.main(new String[] { "preGame.PreGameApp", "dummy" });
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

	@Override
	protected void send(String input) {
		app.getUpdater().send(input);
	}
}
