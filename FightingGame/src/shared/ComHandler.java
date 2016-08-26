package shared;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.naming.NoInitialContextException;

import game.HUD;
import game.endGameMenu;
import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.baseBuffs.Buff;
import main.ClientHandler;
import main.MainApp;
import main.MainLoader;
import main.appdata.ProfileHandler;
import main.preGame.MainPreGame;
import processing.core.PApplet;
import server.Protocol;
import server.ServerApp;
import server.ServerUpdater;
import shared.Updater.GameState;;

public class ComHandler implements Coms {
	private static ContentListManager contentListHandler;
	private static Updater updater;

	public static void addUpdater(Updater updater2) {
		updater = updater2;
	}

	public static void setup() {
		contentListHandler = ((MainApp) ref.app).contentListHandler;
	}

	@SuppressWarnings("unused")
	public static void executeCom(String com) {
		String[] c = PApplet.splitTokens(com, S + ClientHandler.endSymbol);

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
				Constructor<?> ctor = clazz.getConstructor(String[].class);
				o = (GameObject) ctor.newInstance(new Object[] { c });
				updater.addGameObject(o, "ch");
				break;
			case BUFF:
				Class<?> bClazz = contentListHandler.getBuffClass(c[1]);
				Constructor<?> bCtor = bClazz.getConstructor(String[].class);
				Buff buff = (Buff) bCtor.newInstance(new Object[] { c });

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
				ref.preGame.write(c[1], c);
				break;

			// before game
			case IDENTIFY:
				if (c[1].equals("reconnect")) {
					if (((MainApp) ref.app).mode == Mode.HAUPTMENUE) {
						while (((MainPreGame) ref.preGame).display == null) {
						}
						ref.app.delay(10);
						((MainPreGame) ref.preGame).display.dispose();
						ref.loader = new MainLoader();
						System.out.println("reconnect");
						((MainLoader) ref.loader).isReconnectLoad = true;
						((MainApp) ref.app).mode = Mode.LADESCREEN;
					} else {// other player identify
						ClientHandler.send(
								IDENTIFYING + S + ClientHandler.identification + S + ref.preGame.getUser("").name);
					}
				} else {
					if (c[1].equals("server")) {
						((MainApp) ref.app).mode = Mode.PREGAME;
					}
					System.out.println("identifying " + ref.preGame.getUser("").name);
					ClientHandler.send(IDENTIFYING + " " + ClientHandler.identification + S
							+ ref.preGame.getUser("").name + S + VersionControle.version);
					if (ref.preGame.getUser("").nation != null)
						ClientHandler.send(SET_NATION + S + ClientHandler.identification + S
								+ ref.preGame.getUser("").nation.toString());
					if (ref.preGame.map != null)
						ClientHandler.send(SET_MAP + S + ClientHandler.identification + S + ref.preGame.map);
					// TODO send color
					// nur an clienthandler

				}
				break;
			case IDENTIFYING:
				ref.preGame.addPlayer(c[1], c[2]);
				if (c.length > 3 && c[3] != null && !c[3].equals(VersionControle.version)) {
					System.err.println("player " + c[2] + " has different version than Server " + c[3] + " "
							+ VersionControle.version + " (VersionCombiner.java:11)");
					ref.preGame.write("WARNING", "player " + c[2] + " has different version than Server " + c[3] + " "
							+ VersionControle.version + " (VersionCombiner.java:11)");
				}
				break;
			case SET_NATION:
				// System.out.println(c[2]);
				ref.preGame.users.get(c[1]).nation = Nation.fromString(c[2]);
				break;
			case SET_MAP:
				ref.preGame.setMap(c[2]);
				break;
			case LOAD:
				ref.preGame.startLoading();
				break;
			case RECONNECT:
				((ServerUpdater) updater).reconnect();
				break;
			case READY:
				// System.out.println(ref.preGame.player);
				ref.preGame.users.get(c[1]).isReady = true;
				ref.loader.tryStartGame();
				break;
			case START_GAME:
				if (Updater.resfreeze != null)
					Updater.resfreeze.startCooldown();
				ref.loader.startGame();
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
					if (ref.app instanceof ServerApp) {
						Protocol.createFile();
					} else if (ref.player.gameState != GameState.PLAY) {
						HUD.menue = new endGameMenu();
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
