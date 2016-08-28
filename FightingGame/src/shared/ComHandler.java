package shared;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.naming.NoInitialContextException;

import game.ClientHandler;
import game.GameBaseApp;
import game.HUD;
import game.MainLoader;
import game.endGameMenu;
import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.baseBuffs.Buff;
import main.MainApp;
import main.appdata.ProfileHandler;
import main.preGame.MainPreGame;
import processing.core.PApplet;
import server.Protocol;
import server.ServerApp;
import server.ServerUpdater;
import shared.Updater.GameState;;

public class ComHandler implements Coms {
	private ContentListManager contentListHandler;
	private Updater updater;
	private GameBaseApp app;

	public void addUpdater(Updater updater2) {
		updater = updater2;
	}

	public ComHandler(GameBaseApp app) {

		this.app = app;
		contentListHandler = app.getContentListHandler();
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
			case HIT:
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o instanceof Entity)
					e = (Entity) o;
				else
					throw new InstantiationError(o + " is no entity");
				n = Integer.parseInt(c[2]);
				if (e != null) {
					e.hit(n, updater.players.get(c[3]), c[4]);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case HEAL:
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o instanceof Entity)
					e = (Entity) o;
				else
					throw new InstantiationError(o + " is no entity");
				n = Integer.parseInt(c[2]);
				if (e != null) {
					e.heal(n);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case TP:
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);

				if (o instanceof Entity)
					e = (Entity) o;
				else
					throw new InstantiationError(o + " is no entity");
				x = Float.parseFloat(c[2]);
				y = Float.parseFloat(c[3]);
				if (e != null) {
					if (c.length > 4)
						((Unit) e).tp(x, y, Boolean.valueOf(c[4]));
					else
						((Unit) e).tp(x, y, true);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case SPAWN:
				Class<?> clazz = contentListHandler.getGObjectClass(c[1]);
				Constructor<?> ctor = clazz.getConstructor(GameBaseApp.class, String[].class);
				o = (GameObject) ctor.newInstance(new Object[] { app, c });
				updater.addGameObject(o, "ch");
				break;
			case BUFF:
				Class<?> bClazz = contentListHandler.getBuffClass(c[1]);
				Constructor<?> bCtor = bClazz.getConstructor(GameBaseApp.class, String[].class);
				Buff buff = (Buff) bCtor.newInstance(new Object[] { app, c });

				n = Integer.parseInt(c[2]);
				o = updater.getGameObject(n);
				if (o instanceof Entity) {
					((Entity) o).addBuff(buff);
				}
				break;
			case EXECUTE:// who what info
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o != null) {
					o.exec(c);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case REMOVE:
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o != null) {
					updater.toRemove.add(o);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case SAY:
				app.write(c[1], c);
				break;

			// before game
			case IDENTIFY:
				if (c[1].equals("reconnect")) {
					if (app.getMode() == Mode.HAUPTMENUE) {
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
			case START_GAME:
				if (Updater.resfreeze != null)
					Updater.resfreeze.startCooldown();
				app.getLoader().startGame();
				break;
			case PAUSE:
				if (Boolean.valueOf(c[1])) {
					Updater.Time.startPause();
					updater.startPause();
				} else {
					Updater.Time.endPause();
					updater.endPause();
				}
				break;
			case GAMEEND:
				boolean finished = updater.map.mapCode.handleGameEnd(c);
				if (finished) {
					if (app instanceof ServerApp) {
						Protocol.createFile();
					} else if (app.getPlayer().gameState != GameState.PLAY) {
						app.getDrawer().getHud().menue = new endGameMenu(app);
						float f = Float.parseFloat(c[3]);
						ProfileHandler.gameEndCalculations(f);
					}
				}
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
		} catch (InvocationTargetException e) {
			System.err.println("com error in " + com);
			e.printStackTrace();
			// can be ignored
		} catch (Exception e) {
			System.err.println("com error in " + com);
			e.printStackTrace();
		}

	}
}
