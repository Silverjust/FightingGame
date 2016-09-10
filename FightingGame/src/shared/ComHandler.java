package shared;

import java.lang.reflect.Field;

import javax.naming.NoInitialContextException;

public abstract class ComHandler implements Coms {

	protected GameBaseApp app;
	protected ContentListManager contentListHandler;
	protected Updater updater;

	public ComHandler(GameBaseApp app) {
		this.app = app;
		contentListHandler = app.getContentListManager();
	}

	public abstract void executeCom(String com, boolean isIntern);

	public void addUpdater(Updater updater) {
		this.updater = updater;

	}

	public void handleErrorNoCommandFound(String com, String[] c)
			throws IllegalAccessException, NoInitialContextException {
		for (Field f : Coms.class.getDeclaredFields()) {
			// System.out.println("PreGameComHandler.executeCom()" + f);
			if (f.get(null) instanceof String) {
				String s = (String) f.get(null);
				if (s.equals(c[0])) {
					c[0] = f.getName();
				}
			}
		}
		System.err.println(com + " was not found, add:\n	case " + c[0] + ":\n\t\t//TODO \n\t\tbreak;");
	}

}
