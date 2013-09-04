package zombiecraft.Core.towers;

import java.util.HashMap;

public class TowerMapping {

	public static String packagePrefix = "zombiecraft.Core.towers."; // not needed since using a mapping?
	public static HashMap<String, Class> lookupNameToBuilding = new HashMap<String, Class>();
	//public static HashMap<BuildingBase, String> lookupBuildingToName = new HashMap<BuildingBase, String>();
	
	public static void initData() {
		//lookupBuildingToName.clear();
		lookupNameToBuilding.clear();
		
		addMapping("canon", Canon.class);
		addMapping("teslacoil", TeslaCoil.class);
		addMapping("gatling", Gatling.class);
		
		//lookupNameToBuilding.get("command").
	}
	
	public static void addMapping(String name, Class building) {
		lookupNameToBuilding.put(name, building);
	}
	
	public static TowerBase newTower(String name) {
		TowerBase bb = null;
		try {
			bb = (TowerBase)lookupNameToBuilding.get(name).getConstructor(new Class[] {}).newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (bb != null) {
			bb.name = name; //to make tower name match the mapping name
			return bb;
		} else {
			System.out.println("critical error creating new tower instance");
		}
		return null;
	}
}
