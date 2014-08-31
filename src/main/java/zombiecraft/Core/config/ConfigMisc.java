package zombiecraft.Core.config;

import modconfig.IConfigCategory;

public class ConfigMisc implements IConfigCategory {

	public static boolean zcCamOutsideDim = false;
	public static int chunkClearRadius = 8;
	
	
	@Override
	public String getConfigFileName() {
		return "ZombiecraftMisc";
	}

	@Override
	public String getCategory() {
		return "ZombieCraft Misc";
	}

	@Override
	public void hookUpdatedValues() {

	}

}
