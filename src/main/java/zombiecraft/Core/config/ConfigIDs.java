package zombiecraft.Core.config;

import modconfig.IConfigCategory;

public class ConfigIDs implements IConfigCategory {

	public static int ID_B_WAVEMOBSPAWNER = 190;
	public static int ID_B_PURCHASEPLATE = 191;
	public static int ID_B_BARRICADE0 = 192;
	public static int ID_B_BARRICADE1 = 193;
	public static int ID_B_BARRICADE2 = 194;
	public static int ID_B_BARRICADE3 = 195;
	public static int ID_B_BARRICADE4 = 196;
	public static int ID_B_BARRICADE5 = 197;
	public static int ID_B_BARRIER = 198;
	public static int ID_B_BARRICADEPLACEABLE = 199;
	public static int ID_B_BETTY = 200;
	public static int ID_B_PACKETTESTER = 201;
	public static int ID_B_SESSION = 202;
	public static int ID_B_TOWER = 203;
	public static int ID_B_WALL = 204;
	public static int ID_B_MYSTERYBOX = 205;
	
	public static int ID_I_BARRICADE = 22701;
	public static int ID_I_EDITTOOL = 22702;
	public static int ID_I_SWORD = 22703;
	public static int ID_I_DEAGLE = 22704;
	public static int ID_I_AK47 = 22705;
	public static int ID_I_SHOTGUN = 22706;
	public static int ID_I_M4 = 22707;
	public static int ID_I_SNIPER = 22708;
	public static int ID_I_FLAMETHROWER = 22709;
	public static int ID_I_M1911 = 22710;
	public static int ID_I_RIFLE = 22711;
	public static int ID_I_UZI = 22712;
	public static int ID_I_RAYGUN = 22713;
	public static int ID_I_RPG = 22714;
	public static int ID_I_CHICKENGUN = 22715;
	public static int ID_I_PERKSPEED = 22716;
	public static int ID_I_PERKEXSTATIC = 22717;
	public static int ID_I_PERKJUGG = 22718;
	public static int ID_I_PERKCHARGE = 22719;
	public static int ID_I_PERKCOMRADE = 22720;
	public static int ID_I_PICKUPDOUBLEPOINTS = 22721;
	public static int ID_I_PICKUPINSTAKILL = 22722;
	public static int ID_I_PICKUPMAXAMMO = 22723;
	public static int ID_I_PICKUPNUKE = 22724;
	public static int ID_I_PICKUPBARRICADEPLACEABLE = 22725;
	public static int ID_I_PICKUPTURBOHEAL = 22726;
	public static int ID_I_PLACERTOWER = 22727;
	public static int ID_I_PLACERWALL = 22728;
	
	@Override
	public String getConfigFileName() {
		return "ZombiecraftIDs";
	}

	@Override
	public String getCategory() {
		return "ZombieCraft IDs Config";
	}

	@Override
	public void hookUpdatedValues() {

	}

}
