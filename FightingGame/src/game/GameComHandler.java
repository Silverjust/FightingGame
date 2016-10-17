package game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.baseBuffs.Buff;
import gameStructure.items.InventoryItem;
import processing.core.PApplet;
import shared.ComHandler;
import shared.GameBaseApp;
import shared.Helper;
import shared.Mode;
import shared.Team;
import shared.Updater;
import shared.User;
import shared.VersionControle;;

public class GameComHandler extends ComHandler {

	public GameComHandler(GameBaseApp app) {
		super(app);
	}

	@SuppressWarnings("unused")
	public void executeCom(String com, boolean isIntern, String ip) {
		String[] c = PApplet.splitTokens(com, S + app.getClientHandler().endSymbol);

		try {
			byte b;
			int n;
			float x, y;
			Entity e = null;
			GameObject o = null;
			switch (c[0]) {
			case DAMAGE:
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o instanceof Entity)
					e = (Entity) o;
				else
					throw new ClassCastException(o + " is no entity");
				n = Integer.parseInt(c[2]);
				if (e != null) {
					e.hit(n, updater.players.get(c[3]), c[4]);
				} else {
					throw new IllegalArgumentException("no entity found for " + c[1]);
				}
				break;
			case HEAL:
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o instanceof Entity)
					e = (Entity) o;
				else
					throw new ClassCastException(o + " is no entity");
				n = Integer.parseInt(c[2]);
				if (e != null) {
					e.heal(n);
				} else {
					throw new IllegalArgumentException("no entity found for " + c[1]);
				}
				break;
			case TP:
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);

				if (o instanceof Entity)
					e = (Entity) o;
				else
					throw new ClassCastException(o + " is no entity");
				x = Float.parseFloat(c[2]);
				y = Float.parseFloat(c[3]);
				if (e != null) {
					if (c.length > 4)
						((Unit) e).tp(x, y, Boolean.valueOf(c[4]));
					else
						((Unit) e).tp(x, y, true);
				} else {
					throw new IllegalArgumentException("no entity found for " + c[1]);
				}
				break;
			case SPAWN:
				Class<?> clazz = contentListHandler.getGObjectClass(c[1]);
				Constructor<?> ctor = clazz.getConstructor(GameBaseApp.class, String[].class);
				o = (GameObject) ctor.newInstance(new Object[] { app, c });
				n = Integer.parseInt(c[2]);
				o.setNumber(n);
				System.out.println("GameComHandler.executeCom()spawn " + n);
				updater.addGameObject(o);
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
			case ITEM:
				System.out.println("GameComHandler.executeCom()item");
				Class<?> iClazz = contentListHandler.getItemClass(c[1]);
				Constructor<?> iCtor = iClazz.getConstructor(GameBaseApp.class, String[].class);
				InventoryItem item = (InventoryItem) iCtor.newInstance(new Object[] { app, c });
				n = Integer.parseInt(c[2]);
				o = updater.getGameObject(n);
				System.out.println("GameComHandler.executeCom() i 2");
				if (o instanceof Entity) {
					System.out.println("GameComHandler.executeCom() i 3");
					((Entity) o).addItem(item);
				}
				System.out.println("GameComHandler.executeCom() i 4");
				break;
			case EXECUTE:// who what info
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o != null) {
					o.exec(c);
				} else {
					throw new IllegalArgumentException("no entity found for " + c[1]);
				}
				break;
			case REMOVE:
				n = Integer.parseInt(c[1]);
				o = updater.getGameObject(n);
				if (o != null) {
					updater.toRemove.add(o);
				} else {
					throw new IllegalArgumentException("no entity found for " + c[1]);
				}
				break;
			case SAY:
				String name = Helper.ipToName(c[1], app);
				String completeText = "";
				for (int i = 2; i < c.length; i++) {// c[0] und c[1] auslassen
					completeText = completeText.concat(" ").concat(c[i]);
				}
				app.write(name, completeText);

				break;// before game
			case IDENTIFY:
				if (c[1].equals(app.getClientHandler().getIdentification())) {
					User u = new User(app, app.getClientHandler().getIdentification(),
							app.getClientHandler().getIdentification());
					if (app.getPreGameInfo() == null) {
						throw new Exception("game-client not correctly initialized");
					}
					app.getPreGameInfo().addUser(u);
					System.out.println("identifying " + app.getPreGameInfo().getUser("").name);
				}
				app.getClientHandler().send(IDENTIFYING + S + app.getClientHandler().getIdentification() + S
						+ app.getPreGameInfo().getUser("").name + S + GAMECLIENT + S + VersionControle.version);

				break;
			case IDENTIFYING: {
				User u = new User(app, c[1], c[2]);
				app.getPreGameInfo().addUser(u);
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
				if (c[2].equals(LEFTSIDE)) {
					u.team = Team.LEFTSIDE;
				} else if (c[2].equals(RIGHTSIDE)) {
					u.team = Team.RIGHTSIDE;
				} else {
					throw new IllegalArgumentException(c[2] + " is not a team");
				}
				break;
			case SET_CHAMP:
				u = app.getPreGameInfo().getUser(c[1]);
				if (u != null)
					u.championName = c[2];
				break;
			case START_GAMEAPPS:
				break;
			case START_LOADING:
				System.out.println("GameComHandler.executeCom()loading  #############################################");
				app.setLoader(new MainLoader(app));
				app.setMode(Mode.LADESCREEN);
				break;
			case START_GAME:
				app.getLoader().startGame();
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
				throw new Exception("no command found");
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
