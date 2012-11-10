package zombiecraft.Core.GameLogic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import build.world.Build;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;

import CoroAI.Persister;
import CoroAI.entity.EnumJob;
import CoroAI.entity.c_EnhAI;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import zombiecraft.Core.AmmoDataLatcher;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.CommandTypes;
import zombiecraft.Core.DataLatcher;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.EnumGameMode;
import zombiecraft.Core.MCInt;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.ZCItems;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Entities.EntityWorldHook;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Core.Items.ItemPerk;
import zombiecraft.Core.World.Level;
import zombiecraft.Core.World.MapManager;
import zombiecraft.Core.Blocks.*;
import zombiecraft.Forge.PacketMLMP;
import zombiecraft.Forge.ZCClientTicks;
import zombiecraft.Forge.ZCServerTicks;
import zombiecraft.Forge.ZombieCraftMod;

public abstract class ZCGame {
	
	public static final int ZCDimensionID = -66;
	public static final int ZCWorldHeight = 50;
	public static boolean autostart = false;
	public static boolean autoload = false;
	public static String curLevelOverride = "";
	//public static MCInt mcInt;
	
	//game objects always used, master/slave mode
	//public static ZCGame instance;
	public WaveManager wMan;
	public MapManager mapMan;
	public EntityWorldHook worldSaver;
	public NBTTagCompound gameData = null;
	public Level zcLevel; //need for dummy data
	public boolean levelHasInit = false;
	
	//game objects only created on the master controller
	//none!
	
	public boolean resetWaveManager = false;
	public boolean serverMode; 
	public int activeZCDimension = ZCDimensionID;
	
	public HashMap entFields;
	
	
	//New vars
	public boolean gameActive = false;
	//public boolean editMode = false;
	
	//public static int key_Buy = 16; //q
	
	public boolean debug = false;
	
	//Configurable settings?
	public boolean cfg_ff = false;
	
	public int packetUpdateDelay = 40;
	
	public static ZCGame instance() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			return ZCServerTicks.zcGame;
		} else {
			return ZCClientTicks.zcGame;
		}
	}
	
	public ZCGame(boolean serverMode) {
		this.serverMode = serverMode;
		entFields = new HashMap();
		
		//instance() = this;
		
		System.out.println("ZC Items and Blocks Init");
		
		
		mapMan = new MapManager(this);
		
		//for dummy mode data
		zcLevel = new Level(this);
		mapMan.zcLevel = zcLevel;
		
		
		
		mapMan.updateEditState();
		ZCUtil.setBlocksMineable(true);
		
		
		new Persister();
	}
	
	public void resetDimensionID() {
		activeZCDimension = ZCGame.ZCDimensionID;
	}
	
	public void setActiveDimension(int id) {
		System.out.println("Setting active ZC dimension to " + id);
		activeZCDimension = id;
	}
	
	public int noHookTicks = 0;
    public void watchWorldHook() {
    	//if (var4 instanceof EntityWorldHook) {
            if (worldSaver == null) {
            	noHookTicks++;
            	if (noHookTicks > 10) {
            		worldSaver = new EntityWorldHook(ZCServerTicks.worldRef);
            		EntityPlayer plEnt = getAlivePlayer();
            		if (plEnt != null) {
            			worldSaver.setPosition(plEnt.posX, 128, plEnt.posZ);
            		} else {
            			worldSaver.setPosition(0, 128, 0);
            		}
                    
            		ZCServerTicks.worldRef.spawnEntityInWorld(worldSaver);
                    System.out.println("new world saver");
            	}
            } else {
            	noHookTicks = 0;
            }
        //}
    }
    
    public synchronized void resetWorldHook() {
    	noHookTicks = 0;
    	worldSaver = null;
    }
	
	//Should only be called on the side that manages level locking/restoring
	public void levelInit() {
		zcLevel = new Level(this);
		mapMan.zcLevel = zcLevel;
		
		readGameNBT(getWorld());
		
		mapMan.loadLevel();
		
		levelHasInit = true;
	}
	
	public int writeOutTimeout = 200;
	public void handleWriteOut() {
		if (writeOutTimeout == 0) {
			writeOutTimeout = 200;
			this.writeGameNBT();
		}
	}
	
	public int syncTimeout = 60;
	public void handleSyncFixing() {
		if (syncTimeout == 0) {
			syncTimeout = 200;
			List<EntityPlayer> players = this.getPlayers();
			for (int i = 0; i < players.size(); i++) {
				EntityPlayer player = players.get(i);
				
				int zcPoints = (Integer)this.getData(player, DataTypes.zcPoints);
				//System.out.println("syncing, invaders: " + wMan.wave_Invaders_Count + " | gameactive: " + this.gameActive + " | " + "editmode: " + mapMan.editMode);
				//this.updateInfo(player, PacketTypes.PLAYER_POINTS, new int[] {zcPoints});
				syncPlayer(player);
			}
			
		}
	}
	
	public void syncPlayer(EntityPlayer entPl) {
		updateInfo(entPl, PacketTypes.EDITOR_EDITMODE, new int[] {(mapMan.editMode ? 1 : 0)});
		updateAmmoData(entPl);
		updateInfo(entPl, PacketTypes.INFO_WAVE, new int[] {wMan.wave_Stage, (int)wMan.wave_StartDelay, wMan.wave_MaxKills, wMan.wave_Invaders_Count});
		updateInfo(entPl, PacketTypes.EDITOR_SETLEVELNAME, new int[0], new String[] {mapMan.curLevel});
		Build l = mapMan.zcLevel.buildData;
		if (l != null) {
			updateInfo(entPl, PacketTypes.EDITOR_SETLEVELCOORDS, new int[] {l.map_coord_minX, l.map_coord_minY, l.map_coord_minZ, l.map_coord_minX+l.map_sizeX, l.map_coord_minY+l.map_sizeY, l.map_coord_minZ+l.map_sizeZ});
			updateInfo(null, PacketTypes.EDITOR_SETSPAWN, new int[] {ZCGame.instance().zcLevel.player_spawnX_world, ZCGame.instance().zcLevel.player_spawnY_world, ZCGame.instance().zcLevel.player_spawnZ_world});
		} else {
			System.out.println("cant sync build data coords!");
		}
	}
	
	public void gameInit() {
		wMan = new WaveManager(this);
		
	}
	
	public void resetPlayers() {
		for (int var1 = 0; var1 < zcLevel.playersInGame.size(); ++var1)
        {
			resetPlayer(zcLevel.playersInGame.get(var1));
			mapMan.movePlayerToSpawn(zcLevel.playersInGame.get(var1));
        }
	}
	
	public abstract List<EntityPlayer> getPlayers();
	
	public abstract List<EntityPlayer> getPlayers(int dim);
	
	public void resetPlayer(EntityPlayer player) {
		
		
		entFields.put(player.username, new DataLatcher());
		
		player.func_82242_a(0);
		player.experience = 0;
		player.score = 0;
		player.heal(20);
		if (!player.capabilities.isCreativeMode) {
			for (int i = 0; i < player.inventory.mainInventory.length; i++) {
				player.inventory.mainInventory[i] = null;
			}
		}
		setData(player, DataTypes.zcPoints, 0);
		
		giveStartItems(player);
		
		
	}
	
	public void giveStartItems(EntityPlayer player) {
		givePlayerItem(player, ZombieCraftMod.itemDEagle, 0);
	}
	
	/*public void contentInit() {
		
	}*/		
	
	public long lastWorldTime = 0;
	
	public void tick() {
		
		World world = null;
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			world = ZCServerTicks.worldRef;
		} else {
			world = ZCClientTicks.worldRef;
		}
		
		if (lastWorldTime != world.getWorldTime()) {
			lastWorldTime = world.getWorldTime();
			
			
			
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				if (mapMan != null) mapMan.tick();
				
				
				if (writeOutTimeout > 0) writeOutTimeout--;
				handleWriteOut();
				if (syncTimeout > 0) syncTimeout--;
				handleSyncFixing();
			}
		}
		
		
        //if (Keyboard.isKeyDown(Keyboard.KEY_INSERT)) {
        	//wMan.start();
        //}

		wMan.tick();
		Persister.tick(world);
        //iMan.tick();
	}
	
	public boolean isRemote() {
		return false;
	}
	
	public EntityPlayer getAlivePlayer() {
		List list = getPlayers();
		for(int i = 0; i < list.size(); i++) {
			EntityPlayer plEnt = (EntityPlayer)list.get(i);
			if (plEnt != null && !plEnt.isDead) {
				return plEnt;
			}
		}
		return null;
	}
	
	public void playerTick(EntityPlayer player) {
		//int lastPoints = (Integer)this.getData(player, DataTypes.lastPoints);
	
		/*if (player.score > lastPoints) {
			int diff = player.score - lastPoints;
			lastPoints = player.score;
			
			int zcPoints = (Integer)this.getData(player, DataTypes.zcPoints) + (int)(diff * wMan.expToPointsFactor);
			
			System.out.println("zcPoints " + zcPoints);
			
			this.setData(player, DataTypes.lastPoints, lastPoints);
			this.setData(player, DataTypes.zcPoints, zcPoints);
			this.updateInfo(player, PacketTypes.PLAYER_POINTS, new int[] {zcPoints});
		}*/
		
		//System.out.println(player.dimension);
		
		handleBarricadeProximity(player);
	}
	
	public void playerKillEvent(EntityPlayer player, Entity killed) {
		
		givePoints(player, 20);
		
	}
	
	public void givePoints(EntityPlayer player, int amount) {
		
		int zcPoints = 0;
		try {
			zcPoints = (Integer)this.getData(player, DataTypes.zcPoints) + (int)(amount/* * wMan.expToPointsFactor*/);
		} catch (Exception ex) {
			System.out.println("Something horrible has happened!");
			ex.printStackTrace();
		}
		
		System.out.println("zcPoints " + zcPoints);
		
		//this.setData(player, DataTypes.lastPoints, lastPoints);
		this.setData(player, DataTypes.zcPoints, zcPoints);
		this.updateInfo(player, PacketTypes.PLAYER_POINTS, new int[] {zcPoints});
	}
	
	public void entTick(c_EnhAI ent) {
		
	}
	
	public void keySendHook(EntityPlayer player, int key) {
		
	}
	
	public boolean triggerBuyMenu(Entity entity, int x, int y, int z, int itemIndex) {
		EntityPlayer player = (EntityPlayer)entity;
		
		check(player);
		
		
		//System.out.println("server side setting of timeout - " + 30 + " - " + player);
		
		setData(player, DataTypes.purchaseItemIndex, itemIndex);
		setData(player, DataTypes.purchaseCoordX, x);
		setData(player, DataTypes.purchaseCoordY, y);
		setData(player, DataTypes.purchaseCoordZ, z);
		
		
		setData(player, DataTypes.purchaseTimeout, 30);
		setData(player, DataTypes.purchaseState, 1);
		return true;
		
		/*if ((Integer)getData(player, DataTypes.purchaseState) == 0) {
			
		} else return false;*/
		
	}
	

	
	public void handleBarricadeProximity(EntityPlayer player) {
		//nearBarricade = false;
		
		int range = 1;
		
    	for (int x = ((int)(player.posX-1.0))-range; x <= ((int)(player.posX+0.0))+range; x++) {
    		for (int z = ((int)(player.posZ-1.0))-range; z <= ((int)(player.posZ+0.0))+range; z++) {
    			int adjY = (int)player.posY-0;
    			int id = player.worldObj.getBlockId(x,adjY,z);
    			
    			//x,(int)player.posY-0,z
    			//System.out.println(x + " - " + (int)player.posY + " - " + z);
    			if (id > 0) {
    				//System.out.println(Block.blocksList[id]);
    			}
    			if (((BlockBarricade)ZCBlocks.barricadeS5).isFixableBarricade(player.worldObj,x,adjY,z)) {
    				triggerBuyMenu(player, x, adjY, z, -2);
    				//barricadeX = x;
    				//barricadeY = (int)player.posY;
    				//barricadeZ = z;
    				//nearBarricade = true;
    				break;
    			}
    		}
    		//if (nearBarricade) break;
    	}
	}
	
	
	//Generic stuff
	public void check(EntityPlayer me) {
		if (!(entFields.containsKey(me.username))) {
			entFields.put(me.username, new DataLatcher());
		}
	}
	
	public void setData(EntityPlayer ent, DataTypes dtEnum, Object obj) {
		//System.out.println("set: " + ent.username + " -> " + obj);
		//DataLatcher dl = (DataLatcher)entFields.get(ent.entityId);
		//System.out.println("set: " + ent.entityId + "|" + ((DataLatcher)entFields.get(ent.entityId)).values.get(dtEnum) + " -> " + obj.toString());
		((DataLatcher)entFields.get(ent.username)).values.put(dtEnum, obj);
	}
	
	public Object getData(EntityPlayer ent, DataTypes dtEnum) {
		//DataLatcher dl = (DataLatcher)entFields.get(ent.entityId);
		//System.out.println("get: " + ent.entityId + "|" + ((DataLatcher)entFields.get(ent.entityId)).values.get(dtEnum));
		try {
			return ((DataLatcher)entFields.get(ent.username)).values.get(dtEnum);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public boolean tryBuy(EntityPlayer player) {
		if ((Integer)getData(player, DataTypes.purchaseTimeout) > 0) {
			resetBuyState(player);
			
			int itemIndex = (Integer)getData(player, DataTypes.purchaseItemIndex);
			
			//Buy state differences
			if (itemIndex >= 0) {
				buyItem(player, itemIndex);
			} else if (itemIndex == -1) {
				triggerBarrierBreak(player);
			} else if (itemIndex == -2) {
				triggerBarricadeRepair(player);
				resetBuyState(player, Buyables.barricadeRepairCooldown);
			}
			
			//player.inventory.addItemStackToInventory(ItemStack.copyItemStack(this.buyableItems.get(itemIndex)));
			return true;
		}
		return false;
	}
	
	public void resetBuyState(Entity entity) {
		resetBuyState(entity, Buyables.barricadeRepairCooldown);
	}
	
	public void resetBuyState(Entity entity, int cooldown) {
		EntityPlayer player = (EntityPlayer)entity;
		
		check(player);
		
		setData(player, DataTypes.purchaseState, 0);
		setData(player, DataTypes.purchaseTimeout, 0);
	}
	
	public void buyItem(EntityPlayer player, int itemIndex) {
		
		int cost = Buyables.getBuyItemCost(itemIndex);
		int currentPoints = (Integer)this.getData(player, DataTypes.zcPoints);
		if (currentPoints < cost) return;
		
		InventoryPlayer inventory = player.inventory;
		
		ItemStack newItem = Buyables.getBuyItem(itemIndex);
		Item itemtype = null;
		if (newItem != null) {
			itemtype = newItem.getItem();
		}
		
		//temp
		
		
		boolean buyItem = true;
		
		int tryID = -1;
		tryID = InvHelper.getOptimalBuySlot(player, inventory, newItem);
		
		//System.out.println("itemIndex: " + itemIndex);
		
		if (tryID == -2) buyItem = false;
		
		if (tryID != -1) {
			
			int newPoints = (Integer)this.getData(player, DataTypes.zcPoints) - cost;
			
			this.setData(player, DataTypes.zcPoints, newPoints);
			//this.setData(player, DataTypes.zcPoints, 0);
			//mc.thePlayer.score = mc.thePlayer.score - cost;
			
			if (itemtype instanceof ItemPerk) {
				ItemPerk itemP = (ItemPerk)itemtype;
				itemP.onItemRightClick(newItem, player.worldObj, player);
			} else {
				givePlayerItem(player, itemtype, tryID, buyItem);
			}
			//inventory.setInventorySlotContents(tryID, new ItemStack(blocktype, count));
			
			
			updateInfo(player, PacketTypes.MENU_BUY_TRANSACTCONFIRM, new int[] {newItem.itemID, newPoints});
			
		}
		
		
	}
	
	public void givePlayerItem(EntityPlayer player, Item item, int slot) {
		givePlayerItem(player, item, slot, true);
	}
	
	public void givePlayerItem(EntityPlayer player, Item item, int slot, boolean giveGun) {
		InventoryPlayer inventory = player.inventory;
		
		//modify for grenades, etc
		int count = 1;
		
		//Ammo, soon unneeded
		if (item instanceof ItemGun) 
		{
			int maxStackSize = ((ItemGun)item).magSize;
			int ammoID = ((ItemGun)item).ammoType.ordinal();
			
			int curAmmoCount = ZCUtil.getAmmoData(player, ammoID); 
			
			ZCUtil.setAmmoData(player, ammoID, curAmmoCount + (maxStackSize * 3));
			updateAmmoData(player);
			
			if (giveGun) inventory.setInventorySlotContents(slot, new ItemStack(item, count));
		} else {
			inventory.setInventorySlotContents(slot, new ItemStack(item, count));
		}
		
		
	}
	
	public void triggerBarricadeRepair(EntityPlayer player) {
		int x = (Integer)this.getData(player, DataTypes.purchaseCoordX);
		int y = (Integer)this.getData(player, DataTypes.purchaseCoordY);
		int z = (Integer)this.getData(player, DataTypes.purchaseCoordZ);
		
		int points = (Integer)this.getData(player, DataTypes.zcPoints);
		
		int id = player.worldObj.getBlockId(x, y, z);
		
		if (((BlockBarricade)ZCBlocks.barricadeS5).isFixableBarricade(player.worldObj,x,y,z)) {
			
			if (((BlockBarricade)Block.blocksList[id]).tryRepairDoor(player.worldObj,x,y,z,player)) {
				points += Buyables.barricadeRepairRedeem;
				this.setData(player, DataTypes.zcPoints, points);
				updateInfo(player, PacketTypes.MENU_BUY_TRANSACTCONFIRM, new int[] {-2, points, Buyables.barricadeRepairCooldown});
				this.playSound("sdkzc.repair", x, y, z, 1, 1);
				this.playSound("sdkzc.chaching", x, y, z, 1, 0.8F);
				/*mc.theWorld.playSoundEffect(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, "repair", 1F, 1.0F);
				mc.theWorld.playSoundEffect(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, "chaching", 1F, 0.8F);*/
			}
		}
	}
	
	public void triggerBarrierBreak(EntityPlayer player) {
		int x = (Integer)this.getData(player, DataTypes.purchaseCoordX);
		int y = (Integer)this.getData(player, DataTypes.purchaseCoordY);
		int z = (Integer)this.getData(player, DataTypes.purchaseCoordZ);
		
		int points = (Integer)this.getData(player, DataTypes.zcPoints);
		
		if (points > Buyables.barrierCost) {
			points -= Buyables.barrierCost;
			
			this.setData(player, DataTypes.zcPoints, points);
			updateInfo(player, PacketTypes.MENU_BUY_TRANSACTCONFIRM, new int[] {-1, points});
			
			player.worldObj.setBlockWithNotify(x, y, z, 0);
		}
	}
    
    public void spawnWaveEntity(c_EnhAI ent) {
		ent.worldObj.spawnEntityInWorld(ent);
    	
    	//spawning as weather effect, Persister automatically converts to chunk entity if within range, so far no issues
		//ent.worldObj.addWeatherEffect(ent);
		
		ent.spawnExplosionParticle();
		wMan.wave_Invaders.add(ent);
		
		//wave based settings moved to entity init so client on smp can also have the data
		
		ent.initJobAndStates(EnumJob.INVADER);
	}
    
    public void writeGameNBT() {
    	//System.out.println("Saving ZC game..." + zcLevel.map_coord_minX);
    	gameData = new NBTTagCompound();
    	try {
    		
    		//Player data
    		NBTTagCompound playerData = new NBTTagCompound();
    		Iterator it = entFields.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        Object val = pairs.getValue();
		        
		        NBTTagCompound playerNBT = new NBTTagCompound();
		        
		        ((DataLatcher)entFields.get(pairs.getKey().toString())).writeToNBT(playerNBT);
		        
		        playerData.setCompoundTag(pairs.getKey().toString(), playerNBT);
		    }
    		gameData.setCompoundTag("playerData", playerData);
    		
    		//Level position and name data, rest should be done by level
    		gameData.setString("levelName", mapMan.curLevel);
    		
    		gameData.setInteger("map_coord_minX", zcLevel.buildData.map_coord_minX);
    		gameData.setInteger("map_coord_minY", zcLevel.buildData.map_coord_minY);
    		gameData.setInteger("map_coord_minZ", zcLevel.buildData.map_coord_minZ);
    		
    		String saveFolder = getWorldSavePath();
    		
    		
    		
    		saveFolder = getSaveFolderPath() + saveFolder;
    		
    		//System.out.println(saveFolder);
    		
    		//Write out to file
    		FileOutputStream fos = new FileOutputStream(saveFolder + "ZCGame.dat");
	    	CompressedStreamTools.writeCompressed(gameData, fos);
	    	fos.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
    	
    }
    
    public String getSaveFolderName() {
    	return "";
    }
    
    public String getSaveFolderPath() {
    	return MinecraftServer.getServer().getFile(".").getAbsolutePath() + "/";
    }
    
    public String getMapSaveFolderPath() {
    	return MinecraftServer.getServer().getFile(".").getAbsolutePath() + "/" + getMapFolder() + "/";
    }
    
    public static String getMapFolder() {
    	return "ZCMaps";
    }
    
    public void readGameNBT(World worldRef) {
    	gameData = null;
		
		try {
			
			String saveFolder = getWorldSavePath();
			saveFolder = getSaveFolderPath() + saveFolder;
			gameData = CompressedStreamTools.readCompressed(new FileInputStream(saveFolder + "ZCGame.dat"));
			
			//NBTTagList var14 = gameData.getTagList("playerData");
			if (!this.serverMode && !this.isRemote()) {
				NBTTagCompound playerData = gameData.getCompoundTag("playerData");
				Collection playerDataCl = playerData.getTags();
				Iterator it = playerDataCl.iterator();
				
				while (it.hasNext()) {
					NBTTagCompound var16 = (NBTTagCompound)it.next();
					
					DataLatcher dl = new DataLatcher();
	            	dl.readFromNBT(var16);
	    			entFields.put(var16.getName(), dl);
				}
			}
			
			//Level position and name data, rest should be done by level
			ZCGame.instance().mapMan.setMapName(gameData.getString("levelName"));
    		
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }
    
    public boolean isMaster() {
    	if (serverMode || !isRemote()) return true;
    	
    	return false;
    }
	
	//functions meant to be overidden
    public abstract void playerJoined(EntityPlayer player);
    
    public abstract void updateAmmoData(EntityPlayer ent);
    
    public abstract void setNewNav(EntityLiving ent, PathNavigate nav);
    
    public abstract int getItemMaxStackSize(Item item);
    
	public abstract void updateInfo(EntityPlayer player, int pt, int[] dataInt);
	
	public abstract void updateInfo(EntityPlayer player, int pt, int[] dataInt, String[] dataString);
	
	public abstract void updateInfoRun(EntityPlayer player, int pt, int[] dataInt, String[] dataString);
	
	public abstract void updateTileInfo(TileEntity tEnt, int dataInt);
	
	public abstract void handlePacket(EntityPlayer player, PacketMLMP packet);
	
	public abstract void handleTileEntityPacket(int var1, int var2, int var3, int var4, int[] var5, float[] var6, String[] var7);
	
	public abstract TileEntity getTileEntity(int var1, int var2, int var3);
	
	public abstract void updateWaveInfo();
	
	public abstract void addTasks(EntityAITasks tasks, EntityAITasks targetTasks, EntityLiving ent);
	
	public abstract EntityPlayer newFakePlayer();
	
	public abstract void playSoundEffect(String sound, EntityPlayer player, float vol, float pitch);
	
	public abstract void playSound(String sound, int x, int y, int z, float vol, float pitch);
	
	//functions only used on the client side
	public void showEditToolMode() {};
	
	public void renderLevelSize() {};
	
	public void setLevelSize(int x1, int y1, int z1, int x2, int y2, int z2) {};
	
	public void setModeMessage(String msg) {};
	
	public boolean trySetTexturePack(String name) { return true; }
	
	public abstract World getWorld();
	
	public abstract String getWorldSavePath();
	
	public abstract boolean canEdit(EntityPlayer player);
	
	// base class methods for ssp, ZCGameMP overrides and doesnt call super \\
	
	public void setPlayerSpawn(EntityPlayer player, int x, int y, int z) {
		if (isMaster()) {
			if (isOp(player)) {
				mapMan.setPlayerSpawn(x, y, z);
			}
		} else sendPacket(player, PacketTypes.COMMAND, new int[] {CommandTypes.SET_PLAYERSPAWN, x, y, z});
	}
	
	public void setSpawnerWatch(EntityPlayer player, int xCoord, int yCoord, int zCoord, int x, int y, int z) {
		if (isMaster()) {
			if (isOp(player)) {
				TileEntity tileent =  player.worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
				if (tileent instanceof TileEntityMobSpawnerWave)			
				{
					if (tileent != null) {
							((TileEntityMobSpawnerWave)tileent).watchX = x;
							((TileEntityMobSpawnerWave)tileent).watchY = y;
							((TileEntityMobSpawnerWave)tileent).watchZ = z;
							((TileEntityMobSpawnerWave)tileent).act_Watch = true;
							
							ZCServerTicks.sendPacketToAll(tileent.getDescriptionPacket());
					}
				}
			}
		} else {
			sendPacket(player, PacketTypes.COMMAND, new int[] {CommandTypes.SET_SPAWNWATCH, xCoord, yCoord, zCoord, x, y, z});
		}
	}
	
	public void regenerateLevel(EntityPlayer player) {
		if (isMaster()) {
			if (isOp(player)) {
	    		ZCGame.instance().mapMan.loadLevel();
	    		ZCGame.instance().mapMan.buildStart(player);
			}
		} else sendPacket(player, PacketTypes.COMMAND, new int[] {CommandTypes.REGENERATE});
	}
	
	public void sendPacket(EntityPlayer player, int packetType, int[] dataInt) {
		
	}
	
	public void sendPacket(EntityPlayer player, int packetType, int[] dataInt, String[] dataString) {
		
	}
	
	public int sx; public int sy; public int sz;
	public boolean settingSize = false;
	
	public void renderInWorldOverlay() {
		
	}
	
	public void teleportPlayer(EntityPlayer player, double x, double y, double z) {
		player.setPosition(x, y, z);
	}
	
	public void setMapName(EntityPlayer player, String name) {
		setMapName(player, name, false);
	}
	
	public void setMapName(EntityPlayer player, String name, boolean force) {
		if (isMaster() || force) {
			if (isOp(player)) {
				mapMan.setMapName(name);
			}
		} else {
			sendPacket(player, PacketTypes.COMMAND, new int[] {CommandTypes.SET_LEVELNAME}, new String[] {name});
		}
		/*if (name == "") name = "NewWorld";
		mapMan.curLevel = name;
		this.zcLevel.levelName = name;*/
	}
	
	public void saveLevel(EntityPlayer player) {
		if (isMaster()) {
			if (isOp(player)) {
				mapMan.saveLevel();
			}
		} else {
			sendPacket(player, PacketTypes.COMMAND, new int[] {CommandTypes.LEVEL_SAVE}, null);
		}
	}
	
	public void loadLevel(EntityPlayer player) {
		if (isMaster()) {
			if (isOp(player)) {
				mapMan.loadLevel();
			}
		} else {
			sendPacket(player, PacketTypes.COMMAND, new int[] {CommandTypes.LEVEL_LOAD}, null);
		}
	}
	
	public boolean isOp(EntityPlayer player) {
		return true;
	}
	
	public static boolean origPVPState;
    
	//do nothing! zcgamemp will though
    public void pvpFixPre() {
    	
    }
    
    public void pvpFixPost() {
    	
    }
    
    public void notifyBlockUpdates(int x, int y, int z) {
    	
    }
    
    public void setCurBuildPercent(int percent) {

    	mapMan.curBuildPercent = percent;
		
    }
    
    public void runInfoCommand(EntityPlayer player, int pt, int[] dataInt, String[] dataString) {
		if (pt == PacketTypes.GAME_END) {
			
			wMan.stopGame();
			
        	wMan.wave_StartDelay = 0;
			
			//win
			if (dataInt[0] == 1) {
				playSoundEffect("sdkzc.round_over", null, 2F, 1.2F);
				if (debug) System.out.println("runInfoCommand win");
			} else {
				playSoundEffect("sdkzc.round_over", null, 2F, 1.2F);
				if (debug) System.out.println("runInfoCommand lose");
			}
		} else {
			
		}
	}
	
}
