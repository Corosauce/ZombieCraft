package zombiecraft.Core.World;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.Dimension.ZCWorldProvider;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.Items.ItemAbility;
import zombiecraft.Forge.EventHandlerPacket;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.packet.PacketHelper;
import CoroUtil.util.CoroUtilNBT;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LevelConfig {

	//Ultimate map config!
	
	//existing stuff to pull over
	//map name
	//map size? wouldnt really be usefull to edit this way
	
	//map scope stuff:
	
	//x time: lock to day/night, or cycle (note, locking means just add time to make day once night hits, dont actually stop world time)
	//x dim has sun? aka has a sky
	//x lighting rules:
	//- nether lighting?
	//- custom lighting for all 16 layers?
	//WEATHER STATE!! - no, its not per dimension, add custom lightning later on maybe? could even do your rain if the mod gets extended renderer added to it
	//max players (should be overrideable via lobby leader setting change)
	//max inv slots usable from left to right
	//starting gun and ammo
	//max comrades per player
	//comrade price
	
	
	//keep in mind, it would be good if these configs could be used as templates on tiles which can then customize it further for just that tile:
	//- make as a standard class shared between this and tiles for more detailed customizing
	
	//config, current hardcoded game logic that should be considered for config, with defaults of current hardcoded ways for backwards compatibility:
	
	//- wave config:
	//-- customizable down to per wave:
	//--- monsters used
	//--- total wave monster count, i guess this would override overall wave count increase calc
	//--- dont allow for rate or speed change per wave
	//-- rate of difficulty increase: count, rate, speed (i dont think rate increases in default)
	//-- base values: zombie count, rate, speed
	//total max number of active zombies allowed, just incase level design can make it go higher since extra conditions are possible now
	
	//- item prices:
	//-- anything in more detail needed here?
	//-- items are actually a base value * multiplier, so supply a base value? or total customization? might need an elegant, expandable solution
	
	//- mystery box items
	//-- usable to a per tile basis
	
	//- session/lobby stuff
	//- lobby leader can override max players if wants
	//- 
	
	//design plan:
	
	//client edits and sends nbt, gets stored in server nbt as 'mapData' nbt tag
	//server saves this tag out with schematic, edits what it needs to for server side (or not?)
	//will automatically get sent back to client as needed since its the same nbt used
	
	public List<ItemStack> mysteryBoxItems = new ArrayList<ItemStack>();

	//Server side nbt stored info, to be added onto saveout in schematic as extra tag
	public static NBTTagCompound nbtInfoServerMapConfig = new NBTTagCompound();
	
	//Client side MapConfig stuff
	public static NBTTagCompound nbtInfoClientMapConfig = new NBTTagCompound();
	
	//lookup vals
	public static String nbtStrCustomLightingUse = "customLightingUse";
	public static String nbtStrCustomLightingMode = "customLighting";
	public static String nbtStrCustomTimeUse = "customTimeUse";
	public static String nbtStrCustomTimeVal = "customTimeVal";
	public static String nbtStrWaveDefaultMobSpawned = "defaultMobSpawned";
	public static String nbtStrWaveSpawnCountBase = "spawnCountBase";
	public static String nbtStrWaveSpawnCountMultiplier = "spawnCountMultiplier";
	public static String nbtStrWaveHealth = "spawnHealth";
	public static String nbtStrWaveHealthAmp = "spawnHealthAmp";
	public static String nbtStrWaveSpeedBase = "spawnSpeed";
	public static String nbtStrWaveSpeedRand = "spawnRand";
	public static String nbtStrWaveSpeedAmp = "spawnSpeedAmp";
	public static String nbtStrWaveSpeedAmpMax = "spawnSpeedAmpMax";
	public static String nbtStrWaveMoveLeadDist = "moveLeadDist";
	public static String nbtStrComradeMaxPerPlayer = "comradeMaxPerPlayer";
	
	//public static String nbtStrCustomLightingVals = "customLighting_CustomVals";
	
	public LevelConfig() {
		
	}
	
	public static String get(String name) {
		return nbtInfoServerMapConfig.getCompoundTag("mapData").getString(name);
	}
	
	public static void loadNBT(NBTTagCompound data) {
		if (data == null) data = new NBTTagCompound();
		
		nbtInfoServerMapConfig = data;
		
		//defaults here! - FYI i dont think nbt can return a null value, just a default blank
		NBTTagCompound mapData = nbtInfoServerMapConfig.getCompoundTag("mapData");
		if (mapData == null) mapData = new NBTTagCompound();
		try {
			String str = mapData.getString(nbtStrWaveDefaultMobSpawned);
			if (str == null || str.equals("")) {
				mapData.setString(nbtStrWaveDefaultMobSpawned, "ZombieCraftMod.EntityZCZombie");
			}
			
			//internally its all strings, finally parsed as its needed in code
			//1. caching needed?
			//2. these defaults.... should be in string after checking if they are < 0 in their type..... or just check if equals ""
			
			if (mapData.getString(nbtStrWaveSpawnCountBase).equals("")) mapData.setString(nbtStrWaveSpawnCountBase, "10");
			if (mapData.getString(nbtStrWaveSpawnCountMultiplier).equals("")) mapData.setString(nbtStrWaveSpawnCountMultiplier, "3");
			if (mapData.getString(nbtStrWaveHealth).equals("")) mapData.setString(nbtStrWaveHealth, "20");
			if (mapData.getString(nbtStrWaveHealthAmp).equals("")) mapData.setString(nbtStrWaveHealthAmp, "0.05");
			if (mapData.getString(nbtStrWaveSpeedBase).equals("")) mapData.setString(nbtStrWaveSpeedBase, "0.45");
			if (mapData.getString(nbtStrWaveSpeedRand).equals("")) mapData.setString(nbtStrWaveSpeedRand, "0.1");
			if (mapData.getString(nbtStrWaveSpeedAmp).equals("")) mapData.setString(nbtStrWaveSpeedAmp, "0.002");
			if (mapData.getString(nbtStrWaveSpeedAmpMax).equals("")) mapData.setString(nbtStrWaveSpeedAmpMax, "0.80");
			if (mapData.getString(nbtStrWaveMoveLeadDist).equals("")) mapData.setString(nbtStrWaveMoveLeadDist, "2");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		nbtInfoServerMapConfig.setTag("mapData", mapData);
		
		updateCallback();
		
		sync();
	}
	
	public static NBTTagCompound saveNBT() {
		return nbtInfoServerMapConfig;
	}
	
	public static void handleServerSentNBTMapConfig(NBTTagCompound par1nbtTagCompound) {
		//System.out.println("handleServerSentNBTMapConfig");
		nbtInfoClientMapConfig = par1nbtTagCompound;
    	
    	//here would be a good spot to do some important visual update callbacks, for example, recalculating the world provider brightness table for new lighting
		updateCallback();
	}
	
	public static void updateCallback() {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			updateCallbackClient();
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void updateCallbackClient() {

		World world = Minecraft.getMinecraft().theWorld;
		if (world != null && world.provider instanceof ZCWorldProvider) {
			((ZCWorldProvider)world.provider).generateLightBrightnessTableInt();
		}
	}

	//called only if player is op
    public static void handleClientSentNBTMapConfig(World world, NBTTagCompound par1nbtTagCompound) {
    	//System.out.println("handleClientSentNBTMapConfig");
    	//we might need to do a sub nbt tag here too, since client needs to send commands via the nbt param here, eg 'sync' command
    	//if we dont, these commands might get saved out into the schematic pointlessly
    	
    	//additionally, since we are receiving a partial build of the clientData, we need a way to overlay the new available tags onto the existing ones, some generic iteration comparison method is needed   	
    	NBTTagCompound nbtPartialClientData = par1nbtTagCompound.getCompoundTag("mapData");
    	NBTTagCompound tempCopyOfServerNBT = nbtInfoServerMapConfig.getCompoundTag("mapData");
    	tempCopyOfServerNBT = CoroUtilNBT.copyOntoNBT(nbtPartialClientData, tempCopyOfServerNBT);
    	nbtInfoServerMapConfig.setTag("mapData", tempCopyOfServerNBT);
    	
    	if (par1nbtTagCompound.getBoolean("sync")) {
    		sync();
    	}
    }
    
    public static void sync() {
    	NBTTagCompound nbtForClient = nbtInfoServerMapConfig.getCompoundTag("mapData");
    	if (nbtForClient == null) nbtForClient = new NBTTagCompound();
    	nbtForClient.setBoolean("markUpdated", true); //set in wrong context, gets saved out, owell
    	ZombieCraftMod.eventChannel.sendToAll(PacketHelper.createPacketForNBTHandler("MapConfig", ZombieCraftMod.eventChannelName, nbtForClient));
    	//MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(EventHandlerPacket.getNBTPacket(nbtForClient, "MapConfig"));
    }
    
    public static ItemStack[] getMysteryBoxDefaultItems(int bufferSize) {
    	
    	//until we have a global default to copy, just use all items minus abilities for now
    	
    	ItemStack[] defStacks = new ItemStack[bufferSize];
    	int placeIndex = 0;
    	for (int i = 0; i < Buyables.items.size(); i++) {
    		if (Buyables.getBuyItem(i) != null && !(Buyables.getBuyItem(i).getItem() instanceof ItemAbility)) {
    			defStacks[placeIndex] = Buyables.getBuyItem(i);
    			placeIndex++;
    		}
    		
    	}
    	return defStacks;
    }
    
}
