package zombiecraft.Core.config;

import modconfig.IConfigCategory;

public class ConfigControls implements IConfigCategory {

	public static String KEY_CONSOLE = "`";
	public static String KEY_USE = "E";
	public static String KEY_RELOAD = "R";
	public static String KEY_CAMERA = ";";
	
	public static String KEY_ABILITY_CHARGE = "????????????";
	
	public static String KEY_WEAPON_ZOOM = "Z";
	
	@Override
	public String getConfigFileName() {
		return "ZombiecraftControls";
	}

	@Override
	public String getCategory() {
		return "ZombieCraft Controls";
	}

	@Override
	public void hookUpdatedValues() {

	}

}
