package preGame;

import javax.naming.NoInitialContextException;

import processing.core.PApplet;
import server.ServerUpdater;
import shared.ComHandler;
import shared.GameBaseApp;
import shared.Mode;
import shared.Team;
import shared.User;
import shared.VersionControle;;

public class PreGameComHandler extends ComHandler {

	private PreGameApp preGameApp;

	public PreGameComHandler(GameBaseApp app) {
		super(app);
		preGameApp = (PreGameApp) app;
	}

	@Override
	public void executeCom(String com, boolean isIntern) {
		String[] c = PApplet.splitTokens(com, S + app.getClientHandler().endSymbol);
		try {
			switch (c[0]) {
			case SAY:
				break;

			// before game
			case IDENTIFY:
				if (c[1].equals(app.getClientHandler().getIdentification())) {
					User u = new User(app, app.getClientHandler().getIdentification(),
							preGameApp.getStartscreen().name);
					app.getPreGameInfo().addUser(u);
					System.out.println("identifying " + app.getPreGameInfo().getUser("").name);
				}
				app.getClientHandler().send(IDENTIFYING + S + app.getClientHandler().getIdentification() + S
						+ app.getPreGameInfo().getUser("").name + S + PGCLIENT + S + VersionControle.version);

				break;
			case IDENTIFYING: {
				User u = new User(app, c[1], c[2]);
				app.getPreGameInfo().addUser(u);
				if (preGameApp.getPreGameInterface() != null)
					preGameApp.getPreGameInterface().updateUsers(preGameApp);
				if (c.length > 4 && c[4] != null && !c[4].equals(VersionControle.version)) {
					System.err.println("player " + c[2] + " has different version than Server " + c[4] + " "
							+ VersionControle.version + " (VersionControle.java:12)");
					app.write("WARNING", "player " + c[2] + " has different version than Server " + c[4] + " "
							+ VersionControle.version + " (VersionControle.java:12)");
				}
			}
				break;
			case SET_MAP:
				// app.getPreGameInfo().setMap(c[2]);
				break;
			case SET_TEAM:
				User u = app.getPreGameInfo().getUser(c[1]);
				if (u != null) {
					if (c[2].equals(LEFTSIDE)) {
						u.team = Team.LEFTSIDE;
						preGameApp.getPreGameInterface().updateUsers(preGameApp);
					} else if (c[2].equals(RIGHTSIDE)) {
						u.team = Team.RIGHTSIDE;
						preGameApp.getPreGameInterface().updateUsers(preGameApp);
					} else {
						throw new IllegalArgumentException(c[2] + " is not a team");
					}
				} else {
					System.out.println("PreGameComHandler.executeCom()set team    client name:"
							+ app.getPreGameInfo().getUser("").name + " " + c[1]);
					for (String ip : app.getPreGameInfo().getUsers().keySet()) {
						System.out.println("PreGameComHandler.executeCom() " + ip);
					}
					throw new IllegalArgumentException(c[1] + " user not found");
				}
				break;
			case SET_CHAMP:
				u = app.getPreGameInfo().getUser(c[1]);
				u.championName = c[2];
				preGameApp.getPreGameInterface().updateUsers(preGameApp);
				break;
			case START_GAMEAPPS:
				PApplet.main(new String[] { "--present", "game.GameApp", app.getClientHandler().getServerIp() });
				app.setMode(Mode.GAME);
				break;
			case START_LOADING:
				/* code */
				break;
			case RECONNECT:
				((ServerUpdater) updater).reconnect();
				break;
			case READY:
				break;
			case START_GAME:
				break;

			case DAMAGE:
				break;
			case HEAL:
				break;
			case TP:
				break;
			case SPAWN:
				break;
			case BUFF:
				break;
			case EXECUTE:
				break;
			case REMOVE:
				break;
			case PAUSE:
				break;

			default:
				handleErrorNoCommandFound(com, c);
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
