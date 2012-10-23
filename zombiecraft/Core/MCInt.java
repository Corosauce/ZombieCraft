package zombiecraft.Core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import zombiecraft.Core.GameLogic.WaveManager;

import net.minecraft.src.*;

public class MCInt {
	
	public static boolean serverMode = false;
	
	public static Object mcObj;
	
	//public static Minecraft mc;
	//public static MinecraftServer mcServer;
	public static WaveManager wMan;
	//public static InterfaceManager iMan;
	
	public MCInt(boolean serverMode) {
		this.serverMode = serverMode;
	}
	
	public void initZCServer(Object obj) {
		serverMode = true;
		mcObj = obj;
	}
	
	public void initZCClient(Object obj) {
		serverMode = false;
		mcObj = obj;
	}
	
	public int getTest() {
		return 0;
	}
	
}
