package main.appdata;

public interface appdataInfos {

	static String nameOfFolder = "FightingGame";
	static String path = System.getProperty("user.home").replace("\\", "/")
	+ "/AppData/Roaming/" + nameOfFolder + "/";

}