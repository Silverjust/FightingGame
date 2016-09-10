package server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.naming.NoInitialContextException;

import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.baseBuffs.Buff;
import processing.core.PApplet;
import shared.ComHandler;
import shared.GameBaseApp;
import shared.Helper;
import shared.Mode;
import shared.Team;
import shared.Updater;
import shared.User;
import shared.VersionControle;;

public class ServerComHandler extends ComHandler {
	private ServerApp serverApp;

	public ServerComHandler(GameBaseApp app) {
		super(app);
		serverApp = (ServerApp) app;
	}

	@SuppressWarnings("unused")
	@Override
	public void executeCom(String com, boolean isIntern) {
		String[] c = PApplet.splitTokens(com, S + app.getServerHandler().endSymbol);

		try {
			byte b;
			int n;
			float x, y;
			Entity e;
			GameObject o;
			User u;
			switch (c[0]) {
			case DAMAGE:
				if (isIntern) {
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
				} else
					System.err.println(com + " blocked by server");
				break;
			case HEAL:
				if (isIntern) {
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
				} else
					System.err.println(com + " blocked by server");
				break;
			case TP:
				if (isIntern) {
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
				} else
					System.err.println(com + " blocked by server");
				break;
			case SPAWN:
				if (isIntern) {
					Class<?> clazz = contentListHandler.getGObjectClass(c[1]);
					Constructor<?> ctor = clazz.getConstructor(GameBaseApp.class, String[].class);
					o = (GameObject) ctor.newInstance(new Object[] { app, c });
					n = Integer.parseInt(c[2]);
					o.setNumber(n);
					updater.addGameObject(o);
				} else
					System.err.println(com + " blocked by server");
				break;
			case BUFF:
				if (isIntern) {
					Class<?> bClazz = contentListHandler.getBuffClass(c[1]);
					Constructor<?> bCtor = bClazz.getConstructor(GameBaseApp.class, String[].class);
					Buff buff = (Buff) bCtor.newInstance(new Object[] { app, c });
					n = Integer.parseInt(c[2]);
					o = updater.getGameObject(n);
					if (o instanceof Entity) {
						((Entity) o).addBuff(buff);
					}
				} else
					System.err.println(com + " blocked by server");
				break;
			case REMOVE:
				if (isIntern) {
					n = Integer.parseInt(c[1]);
					o = updater.getGameObject(n);
					if (o != null) {
						updater.toRemove.add(o);
					} else {
						throw new IllegalArgumentException("no entity found");
					}
				} else
					System.err.println(com + " blocked by server");
				break;
			case EXECUTE:// who what info
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o != null) {
					o.exec(c);
					sendBack(com);
				} else {
					throw new IllegalArgumentException("no entity found");
				}
				break;
			case INPUT:
				((ServerSpellHandler) ((ServerUpdater) app.getUpdater()).getSpellHandler(c[1])).input(c);
				break;
			case HIT:

				break;
			case SAY:
				String name = Helper.ipToName(c[1], app);
				String completeText = "";
				for (int i = 2; i < c.length; i++) {// c[0] und c[1] auslassen
					completeText = completeText.concat(" ").concat(c[i]);
				}
				app.write(name, completeText);
				break;
			case IDENTIFYING: {
				u = new User(app, c[1], c[2]);
				if (c.length > 4 && c[4] != null && !c[4].equals(VersionControle.version)) {
					System.err.println("player " + c[2] + " has different version than Server " + c[4] + " "
							+ VersionControle.version + " (VersionControle.java:12)");

					app.write("WARNING", "player " + c[2] + " has different version than Server " + c[4] + " "
							+ VersionControle.version + " (VersionControle.java:12)");

				}
				sendBack(com);
				serverApp.serverPreGameManager.sendPregameInfo();
				if (c[3].equals(PGCLIENT))
					app.getPreGameInfo().addUser(u);
				else if (c[3].equals(GAMECLIENT)) {
					app.getPreGameInfo().getUser(c[1]).setConnectedWithGameClient(true);
					boolean allConnected = true;
					for (String ip : app.getPreGameInfo().getUsers().keySet()) {
						System.out.println("ServerComHandler.executeCom()" + app.getPreGameInfo().getUser(ip).name + " "
								+ app.getPreGameInfo().getUser(ip).isConnectedWithGameClient());
						if (!app.getPreGameInfo().getUser(ip).isConnectedWithGameClient())
							allConnected = false;
					}
					System.out.println("ServerComHandler.executeCom()allConnected" + allConnected);
					if (allConnected) {
						app.setLoader(new ServerLoader(app));
						app.setMode(Mode.LADESCREEN);
						app.getServerHandler().sendDirect(START_LOADING);
					}
				}
			}
				break;
			case SET_TEAM:
				u = app.getPreGameInfo().getUser(c[1]);
				if (c[2].equals(LEFTSIDE)) {
					u.team = Team.LEFTSIDE;
					sendBack(com);
				} else if (c[2].equals(RIGHTSIDE)) {
					u.team = Team.RIGHTSIDE;
					sendBack(com);
				} else {
					throw new IllegalArgumentException(c[2] + " is not a team");
				}
				break;
			case SET_CHAMP:
				u = app.getPreGameInfo().getUser(c[1]);
				u.championName = c[2];
				sendBack(com);
				break;
			case START_GAMEAPPS:
				sendBack(com);
				break;
			case READY:
				u = app.getPreGameInfo().getUser(c[1]);
				u.setReady(true);
				boolean allReady = true;
				for (String ip : app.getPreGameInfo().getUsers().keySet()) {
					if (!app.getPreGameInfo().getUser(ip).isReady())
						allReady = false;
				}
				System.out.println("ServerComHandler.executeCom()allReady" + allReady);
				if (allReady) {
					app.getLoader().tryStartGame();
				}
				break;

			case PAUSE:
				if (Boolean.valueOf(c[1])) {
					Updater.Time.startPause();
					app.getUpdater().startPause();
				} else {
					Updater.Time.endPause();
					app.getUpdater().endPause();
				}
				break;
			default:
				handleErrorNoCommandFound(com, c);
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

	/** send the same command back to clients */
	private void sendBack(String com) {
		app.getServerHandler().sendDirect(com.replace(app.getServerHandler().endSymbol + "", ""));
	}
}
