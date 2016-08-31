package preGame;

import javax.naming.NoInitialContextException;

import game.GameBaseApp;
import game.MainLoader;
import gameStructure.Entity;
import gameStructure.GameObject;
import processing.core.PApplet;
import server.ServerUpdater;
import shared.ComHandler;
import shared.Mode;
import shared.VersionControle;;

public class PreGameComHandler extends ComHandler  {

	public PreGameComHandler(GameBaseApp app) {
		super(app);
	}

	@SuppressWarnings("unused")
	public void executeCom(String com) {
		String[] c = PApplet.splitTokens(com, S + app.clientHandler.endSymbol);

		try {
			byte b;
			int n;
			float x, y;
			Entity e = null;
			GameObject o = null;
			switch (c[0]) {
			case SAY:
				app.write(c[1], c);
				break;

			// before game
			case IDENTIFY:
				if (c[1].equals("reconnect")) {
					if (app.getMode() == Mode.STARTSCREEN) {
						/*
						 * while (( app.getPreGameInfo()).display == null) { }
						 */
						app.delay(10);
						// ((MainPreGame)
						// app.getPreGameInfo()).display.dispose();
						app.setLoader(new MainLoader(app));
						System.out.println("reconnect");
						((MainLoader) app.getLoader()).isReconnectLoad = true;
						app.setMode(Mode.LADESCREEN);
					} else {// other player identify
						app.clientHandler.send(IDENTIFYING + S + app.clientHandler.identification + S
								+ app.getPreGameInfo().getUser("").name);
					}
				} else {
					if (c[1].equals("server")) {
						app.setMode(Mode.PREGAME);
					}
					System.out.println("identifying " + app.getPreGameInfo().getUser("").name);
					app.clientHandler.send(IDENTIFYING + " " + app.clientHandler.identification + S
							+ app.getPreGameInfo().getUser("").name + S + VersionControle.version);
					if (app.getPreGameInfo().getUser("").nation != null)
						app.clientHandler.send(SET_NATION + S + app.clientHandler.identification + S
								+ app.getPreGameInfo().getUser("").nation.toString());
					if (app.getPreGameInfo().map != null)
						app.clientHandler
								.send(SET_MAP + S + app.clientHandler.identification + S + app.getPreGameInfo().map);
					// TODO send color
					// nur an clienthandler

				}
				break;
			case IDENTIFYING:
				// app.getPreGameInfo().addPlayer(c[1], c[2]);
				if (c.length > 3 && c[3] != null && !c[3].equals(VersionControle.version)) {
					System.err.println("player " + c[2] + " has different version than Server " + c[3] + " "
							+ VersionControle.version + " (VersionCombiner.java:11)");
					/*
					 * app.getPreGameInfo().write("WARNING", "player " + c[2] +
					 * " has different version than Server " + c[3] + " " +
					 * VersionControle.version + " (VersionCombiner.java:11)");
					 */
				}
				break;
			case SET_MAP:
				// app.getPreGameInfo().setMap(c[2]);
				break;
			case LOAD:
				app.setLoader(new MainLoader(app));
				app.setMode(Mode.LADESCREEN);
				break;
			case RECONNECT:
				((ServerUpdater) updater).reconnect();
				break;
			case READY:
				// System.out.println(ref.preGame.player);
				app.getPreGameInfo().users.get(c[1]).isReady = true;
				app.getLoader().tryStartGame();
				break;
			default:
				System.err.println(com + " was not found");
				throw new NoInitialContextException("no command found");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// can be ignored
		} catch (ClassCastException e) {
			e.printStackTrace();
			// can be ignored
		}  catch (Exception e) {
			System.err.println("com error in " + com);
			e.printStackTrace();
		}

	}
}
