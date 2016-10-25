package shared;

public interface Coms {

	String S = " ";

	String SAY = "<say";
	@Deprecated
	String GIVE = "<give";

	String REMOVE = "<remove";

	String EXECUTE = "<execute";
	/**
	 * sends input from player
	 * <p>
	 * -, ip, spell-position, stage of the spell
	 */
	String INPUT = "<input";

	String HIT = "<hit";

	/**
	 * adds a Game-object to the game
	 * <p>
	 * -, object, number, owner, x, y
	 */
	String SPAWN = "<spawn";
	/**
	 * use when spawning a procectile to make it homing
	 * <p>
	 * place in [6]
	 */
	String HOMING = "homing";

	/**
	 * adds a buff to a entity
	 * <p>
	 * -, buff, target, origin, time
	 */
	String BUFF = "<buff";
	
	/**
	 * adds an Item to a entity
	 * <p>
	 * -, item, owner, slot
	 */
	String ITEM = "<item";

	String TP = "<tp";

	String HEAL = "<heal";
	/**
	 * sends dmg
	 * <p>
	 * -, target, amount, origin object, origin ability
	 */
	String DAMAGE = "<dmg";
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

	/** commands a new client to identify itself */
	String IDENTIFY = "<identify";

	/** sends identity to server and clients */
	String IDENTIFYING = "<identifying";
	String PGCLIENT = "preGameClient";
	String GAMECLIENT = "gameClient";

	/**
	 * starts the game-apps
	 */
	String START_GAMEAPPS = "<startGameApps";

	// **************************************
	String SET_MAP = "<setMap";
	/**
	 * sets team of player
	 * <p>
	 * -, ip, team
	 */

	String SET_TEAM = "<setTeam";
	/**
	 * right team
	 * <p>
	 * place in [2]
	 */
	String RIGHTSIDE = "rightSide";
	/**
	 * left team
	 * <p>
	 * place in [2]
	 */
	String LEFTSIDE = "leftSide";
	/**
	 * sets champion of player
	 * <p>
	 * -, ip, champ
	 */

	String SET_CHAMP = "<setChamp";
	// ****************************************
	/** inits loading on client and serverside */
	String START_LOADING = "<startLoading";

	/** tells clients or server it finished loading */
	String READY = "<ready";

	/** starts the game on client and serverside */
	String START_GAME = "<startGame";

	/**
	 * command to start the reconnect
	 * <p>
	 * 
	 * @see server.ServerUpdater#reconnect
	 */
	String RECONNECT = "<reconnect";

}