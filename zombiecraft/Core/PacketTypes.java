package zombiecraft.Core;

public class PacketTypes {
	public static int i = 0;
	
	//Server to Client packets
	public static int GAME_RESET = i++;
	public static int GAME_NEXTWAVE = i++;
	public static int GAME_PAUSE = i++;
	public static int GAME_RESUME = i++;
	public static int GAME_END = i++;
	public static int GAME_CREDITS = i++;
	
	public static int INFO_WAVE = i++;
	
	public static int MENU_BUY_PROMPT = i++;
	public static int MENU_BUY_TIMEOUT = i++;
	public static int MENU_BUY_TRANSACTCONFIRM = i++; //done by keystroke instead - not anymore!
	
	public static int MENU_TRAP_ENABLE = i++;
	public static int MENU_POWER_ENABLE = i++;
	
	public static int PLAYER_POINTS = i++;
	public static int PLAYER_AMMO = i++;
	
	public static int EDITOR_EDITMODE = i++;
	public static int EDITOR_NOCLIP = i++;
	public static int EDITOR_SETSPAWN = i++;
	public static int EDITOR_SETLEVELNAME = i++;
	public static int EDITOR_SETLEVELCOORDS = i++;
	public static int EDITOR_SETLEVELTEXTUREPACK = i++;
	
	//Client to Server packets
	public static int COMMAND = i++;
	
}
