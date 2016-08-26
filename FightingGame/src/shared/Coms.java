package shared;

public interface Coms {

	String S = " ";

	/**
	 * when client ends game
	 * <p>
	 * -, win/loss, ip, ranking
	 */
	String GAMEEND = "<gameend";

	/**
	 * pause or unpause
	 * <p>
	 * -, bool
	 */
	String PAUSE = "<pause";

	/** starts the game on client and serverside */
	String START_GAME = "<startGame";

	/** tells clients or server it finished loading */
	String READY = "<ready";

	/**
	 * command to start the reconnect
	 * <p>
	 * 
	 * @see server.ServerUpdater#reconnect
	 */
	String RECONNECT = "<reconnect";

	/**
	 * starts the loading sequence
	 * 
	 * @see shared.Loader
	 */
	String LOAD = "<load";

	String SET_MAP = "<setMap";
	String SET_NATION = "<setNation";

	/** sends identity to server and clients */
	String IDENTIFYING = "<identifying";

	/** commands a new client to identify itself */
	String IDENTIFY = "<identify";

	String SAY = "<say";
	@Deprecated
	String GIVE = "<give";
	String REMOVE = "<remove";
	String EXECUTE = "<execute";
	String SPAWN = "<spawn";
	/**
	 * adds a buff to a entity
	 * <p>
	 * -, buff, target, origin, time
	 */
	String BUFF = "<buff";
	String TP = "<tp";
	String HEAL = "<heal";
	String HIT = "<hit";
	@Deprecated
	String MOVE = "<move";
	/**
	 * use when spawning a procectile to make it homing
	 * <p>
	 * place in [5]
	 */
	String HOMING = "homing";

}