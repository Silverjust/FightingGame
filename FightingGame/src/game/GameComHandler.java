package game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.naming.NoInitialContextException;

import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.baseBuffs.Buff;
import processing.core.PApplet;
import shared.ComHandler;
import shared.Helper;;

public class GameComHandler extends ComHandler {

	private GameBaseApp app;
	public GameComHandler(GameBaseApp app) {
		super(app);
		this.app = app;

	}

	@SuppressWarnings("unused")
	public void executeCom(String com) {
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
