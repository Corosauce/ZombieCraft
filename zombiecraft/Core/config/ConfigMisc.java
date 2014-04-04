package zombiecraft.Core.config;

import modconfig.IConfigCategory;

public class ConfigMisc implements IConfigCategory {

	public static boolean zcCamOutsideDim = false;
	
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
