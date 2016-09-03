package server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.naming.NoInitialContextException;

import game.GameBaseApp;
import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.baseBuffs.Buff;
import processing.core.PApplet;
import shared.ComHandler;
import shared.Helper;
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
	public void executeCom(String com) {
		String[] c = PApplet.splitTokens(com, S + app.getServerHandler().endSymbol);

		try {
			byte b;
			int n;
			float x, y;
			Entity e;
			GameObject o;
			switch (c[0]) {
			case DAMAGE:
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
				String name = Helper.ipToName(c[1], app);
				String completeText = "";
				for (int i = 2; i < c.length; i++) {// c[0] und c[1] auslassen
					completeText = completeText.concat(" ").concat(c[i]);
				}
				app.write(name, completeText);
				break;
			case IDENTIFYING: {
				User u = new User(app, c[1], c[2]);
				app.getPreGameInfo().addUser(u);
				if (c.length > 3 && c[3] != null && !c[3].equals(VersionControle.version)) {
					System.err.println("player " + c[2] + " has different version than Server " + c[3] + " "
							+ VersionControle.version + " (VersionCombiner.java:11)");

					app.write("WARNING", "player " + c[2] + " has different version than Server " + c[3] + " "
							+ VersionControle.version + " (VersionCombiner.java:11)");
					
				}
				sendBack(com);
				serverApp.serverPreGameManager.sendPregameInfo();
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

	/** send the same command back to clients */
	private void sendBack(String com) {
		app.getServerHandler().sendDirect(com.replace(app.getServerHandler().endSymbol + "", ""));
	}
}
