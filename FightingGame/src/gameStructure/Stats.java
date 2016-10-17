package gameStructure;

import java.util.HashMap;

import shared.GameBaseApp;

public class Stats {
	public static final String COLLISION_RADIUS = "radius";
	public static final String VISION_RADIUS = "visionRadius";
	public static final String ARMOR = "armor";
	public static final String MOVEMENT_SPEED = "ms";
	public static final String BASICATTACK_Range = "ba-range";
	public static final String MR = "mr";
	public static final String ARMOR_PEN = "armorpen";
	public static final String MAGIC_PEN = "mrpen";
	public static final String PERCENT_ARMOR_PEN = "p-armorpen";
	public static final String PERCENT_MAGIC_PEN = "p-mrpen";
	public static final String ATTACK_DAMAGE = "ad";
	public static final String ABILITY_POWER = "ap";
	public static final String HEALTH = "hp";

	private HashMap<String, EntityStat> stats = new HashMap<String, EntityStat>();

	private EntityStat radius = new EntityStat(this, COLLISION_RADIUS);
	private EntityStat sight = new EntityStat(this, VISION_RADIUS);
	private int level = 1;

	public Stats(GameBaseApp app) {
		getStats().put("radius", radius);
		getStats().put("sight", sight);
	}

	public EntityStat get(String stat) {
		EntityStat entityStat = getStats().get(stat);
		if (entityStat == null)
			System.out.println("Stats.get() this has no " + stat);
		return entityStat;
	}

	public HashMap<String, EntityStat> getStats() {
		return stats;
	}

	public int getLevel() {
		return level;
	}

	public EntityStat getRadius() {
		return radius;
	}

	public EntityStat getSight() {
		return sight;
	}

}