package preGame;

import javax.naming.NoInitialContextException;

import game.GameBaseApp;
import game.MainLoader;
import processing.core.PApplet;
import server.ServerUpdater;
import shared.ComHandler;
import shared.Mode;
import shared.User;
import shared.VersionControle;;

public class PreGameComHandler extends ComHandler {

	private PreGameApp preGameApp;
	private ClientHandler clientHandler;

	public PreGameComHandler(GameBaseApp app) {
		super(app);
		preGameApp = (PreGameApp) app;
		clientHandler = app.getClientHandler();
	}

	@Override
	public void executeCom(String com) {
		String[] c = PApplet.splitTokens(com, S + app.getClientHandler().endSymbol);
		try {
			switch (c[0]) {
			case SAY:
				break;

			// before game
			case IDENTIFY:
				if (c[1].equals(clientHandler.identification)) {
					User u = new User(app, app.getClientHandler().identification, preGameApp.getStartscreen().name);
					app.getPreGameInfo().addUser(u);
					System.out.println("identifying " + app.getPreGameInfo().getUser("").name);
				}
				app.getClientHandler().send(IDENTIFYING + S + app.getClientHandler().identification + S
						+ app.getPreGameInfo().getUser("").name + S + VersionControle.version);

				break;
			case IDENTIFYING: {
				User u = new User(app, c[1], c[2]);
				app.getPreGameInfo().addUser(u);
				if (preGameApp.getPreGameInterface() != null)
					preGameApp.getPreGameInterface().updateUsers(preGameApp);
				if (c.length > 3 && c[3] != null && !c[3].equals(VersionControle.version)) {
					System.err.println("player " + c[2] + " has different version than Server " + c[3] + " "
							+ VersionControle.version + " (VersionCombiner.java:11)");
					app.write("WARNING", "player " + c[2] + " has different version than Server " + c[3] + " "
							+ VersionControle.version + " (VersionCombiner.java:11)");
				}
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
		} catch (

		IllegalArgumentException e)

		{
			e.printStackTrace();
			// can be ignored
		} catch (

		ClassCastException e)

		{
			e.printStackTrace();
			// can be ignored
		} catch (

		Exception e)

		{
			System.err.println("com error in " + com);
			e.printStackTrace();
		}

	}
}
