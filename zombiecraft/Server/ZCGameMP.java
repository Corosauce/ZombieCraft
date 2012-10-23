package zombiecraft.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import build.world.Build;

import cpw.mods.fml.common.FMLCommonHandler;

import CoroAI.entity.*;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import zombiecraft.Core.AmmoDataLatcher;
import zombiecraft.Core.CommandTypes;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.EnumGameMode;
import zombiecraft.Core.MCInt;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.PacketMLMP;
import zombiecraft.Forge.ZCServerTicks;
import zombiecraft.Forge.ZombieCraftMod;

public class ZCGameMP extends ZCGame {
	
	public MinecraftServer mc;
	
	public List<String> playersToWatchForSpawn = new LinkedList();
	
	public ZCGameMP(boolean serverMode) {
		super(serverMode);
		System.out.println("new ZCGameMP");
		
		//this.levelInit();
	}
	
	@Override
	public void tick() {
		
		if (!levelHasInit) this.levelInit();
		
		if (this.getPlayers().size() == 0) {
			if (wMan.wave_Stage > 0) {
				this.wMan.stopGame();
			}
		}
		
		List<EntityPlayer> players = getPlayers();
		
		for(int i = 0; i < players.size(); i++) {
			EntityPlayer plEnt = (EntityPlayer)players.get(i);
			
			check(plEnt);
		}
		
		//for(int i = 0; i < zcLevel.playersInGame.size(); i++) {
		for(int i = 0; i < players.size(); i++) {
			EntityPlayer plEnt = (EntityPlayer)players.get(i);
			
			
			//ZCGame.instance.updateAmmoData(plEnt);
			
			int timeout = (Integer)getData(plEnt, DataTypes.purchaseTimeout);
			
			if (timeout > 0) {
				timeout--;
				if (timeout == 0) {
					//System.out.println("server side reset - " + timeout + " - " + plEnt);
					ZCServerTicks.sendPacket(plEnt, PacketTypes.MENU_BUY_TIMEOUT, new int[] {0});
					resetBuyState(plEnt);
				}
				setData(plEnt, DataTypes.purchaseTimeout, timeout);
			}
			
			if (!plEnt.isDead) {
				this.playerTick(plEnt);
			} else {
				markPlayerForReadding(plEnt);
			}
		}
		
		watchWorldHook();
		watchForRespawn();
		
		super.tick();
	}
	
	public void watchForRespawn() {
		List<EntityPlayer> allPlayers = getPlayers();
		for(int h = 0; h < playersToWatchForSpawn.size(); h++) {
			for(int i = 0; i < allPlayers.size(); i++) {
				EntityPlayer plEnt = allPlayers.get(i);
	
				if (!plEnt.isDead && plEnt.username.equals(playersToWatchForSpawn.get(h))) {
					System.out.println("readding: " + plEnt.username);
					zcLevel.playersInGame.add(plEnt);
					zcLevel.playersInGame_Names.add(plEnt.username);
					playersToWatchForSpawn.remove(h);
					
					if (mapMan.zcLevel.player_spawnY != 999)  {
						mapMan.movePlayerToSpawn(plEnt);
					}
				}
				
			}
		}
		
	}
	
	public void markPlayerForReadding(EntityPlayer player) {
		System.out.println("Marking for readd: " + player.username);
		playersToWatchForSpawn.add(player.username);
		removeOldPlayersMatching(player.username);
	}
	
	@Override
	public List<EntityPlayer> getPlayers() {
		List<EntityPlayer> players = new LinkedList();
		World world = this.mc.worldServerForDimension(activeZCDimension);
		if (world != null) {
			for(int i = 0; i < world.playerEntities.size(); i++) {
				players.add((EntityPlayer)world.playerEntities.get(i));
			}
			return players;
		} else return null;
	}
	
	@Override
	public boolean isRemote() {
		return false;
	}
	
	@Override
	public void playerTick(EntityPlayer player) {
		super.playerTick(player);
		player.getFoodStats().addStats(20, 1F);
		if (player instanceof c_EntityPlayerMPExt) {
			((c_EntityPlayerMPExt)player).setFoodLevel(20);
		}
		handleBarricadeProximity(player);
	}
	
	/*@Override
	public void resetPlayers() {
		for (int var1 = 0; var1 < ModLoader.getMinecraftServerInstance().configManager.playerEntities.size(); ++var1)
        {
			resetPlayer((EntityPlayer)ModLoader.getMinecraftServerInstance().configManager.playerEntities.get(var1));
        }
	}*/
	
	public boolean triggerBuyMenu(Entity entity, int x, int y, int z, int itemIndex) {
		
		if (super.triggerBuyMenu(entity, x, y, z, itemIndex)) {
			EntityPlayer player = (EntityPlayer)entity;
			ZCServerTicks.sendPacket(player, PacketTypes.MENU_BUY_PROMPT, new int[] {itemIndex, x, y, z, 30});
			return true;
		}
		return false;
	}
	
	@Override
	public void keySendHook(EntityPlayer player, int key) {
		check(player);
		
		//setData(player, DataTypes.purchaseTimeout, 30);
		if (false) {
			
		} else if (key == 210) {
			//if (mc.configManager.isOp(player.username)) {
				//wMan.startGameFromPlayer(player);
			//}
		}
		
	}
	
	@Override
	public void updateAmmoData(EntityPlayer ent) {
		//prepare dataInt
		
		//AmmoDataLatcher ammoDL = (AmmoDataLatcher)this.getData(ent, DataTypes.ammoAmounts);
		HashMap ammoMap = ((AmmoDataLatcher)this.getData(ent, DataTypes.ammoAmounts)).values;
		
		
		
		//LinkedList data = new LinkedList();
		//System.out.println("hashmap size() " + ((AmmoDataLatcher)((zombiecraft.Core.DataLatcher)entFields.get(ent.username)).values.get(DataTypes.ammoAmounts)));
		System.out.println("ammoMap.size() " + ammoMap.size());
		
		int[] dataInt = new int[1 + (ammoMap.size() * 2)];
		int index = 0;
		
		dataInt[index++] = ammoMap.size();
		
		Iterator it = ammoMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        //first id, second value, and so on
	        //data.add(pairs.getKey());
	        //data.add(pairs.getValue());
	        dataInt[index++] = (Integer)pairs.getKey();
	        dataInt[index++] = (Integer)pairs.getValue();
	        //index+=2;
	        
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    //data.toArray(new Integer[0]); 
	    
		updateInfo(ent, PacketTypes.PLAYER_AMMO, dataInt);
	}
	
	@Override
	public void updateInfo(EntityPlayer player, int pt, int[] dataInt) {
		ZCServerTicks.sendPacket(player, pt, dataInt);
	}
	
	@Override
	public void updateInfo(EntityPlayer player, int pt, int[] dataInt, String[] dataString) {
		ZCServerTicks.sendPacket(player, pt, dataInt, dataString);
	}
	
	@Override
	public void updateInfoRun(EntityPlayer player, int pt, int[] dataInt, String[] dataString) {
		ZCServerTicks.sendPacket(player, pt, dataInt, dataString);
		runInfoCommand(player, pt, dataInt, dataString);
	}
	
	
	
	@Override
	public void triggerBarricadeRepair(EntityPlayer player) {
		super.triggerBarricadeRepair(player);
		if (player instanceof EntityPlayerMP) {
			int x = (Integer)this.getData(player, DataTypes.purchaseCoordX);
			int y = (Integer)this.getData(player, DataTypes.purchaseCoordY);
			int z = (Integer)this.getData(player, DataTypes.purchaseCoordZ);
			notifyBlockUpdates(x, y, z);
			
			/*((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x, y, z, getWorld()));
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x, y+1, z, getWorld()));
			
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x+1, y, z, getWorld()));
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x+1, y+1, z, getWorld()));
			
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x, y, z+1, getWorld()));
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x, y+1, z+1, getWorld()));
			
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x-1, y, z, getWorld()));
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x-1, y+1, z, getWorld()));
			
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x, y, z-1, getWorld()));
			((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new Packet53BlockChange(x, y+1, z-1, getWorld()));*/
			/*getWorld().markBlocksDirty(x, y, z, x, y, z);
			getWorld().markBlockNeedsUpdate(x, y, z);*/
		}
	}
	
	@Override
	public void notifyBlockUpdates(int x, int y, int z) {
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y, z, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y+1, z, getWorld()));
		
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x+1, y, z, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x+1, y+1, z, getWorld()));
		
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y, z+1, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y+1, z+1, getWorld()));
		
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x-1, y, z, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x-1, y+1, z, getWorld()));
		
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y, z-1, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y+1, z-1, getWorld()));
	}
	
	
	
	@Override
	public void updateTileInfo(TileEntity tEnt, int dataInt) {
		//System.out.println("ZCGameMP updateTileInfo broken");
		ZCServerTicks.sendPacket(null, 0, new int[] {tEnt.xCoord, tEnt.yCoord, tEnt.zCoord, dataInt}, new String[] {""}, "TileEnt");
		//Packet packet = ModLoaderMp.getTileEntityPacket(ModLoaderMp.getModInstance(ZombieCraftMod.class), tEnt.xCoord, tEnt.yCoord, tEnt.zCoord, 0, new int[] {dataInt}, null, null);
		//System.out.println("Sending packet " + packet);
		//ServerTickHandler.sendPacketToAll(packet);
	}
	
	@Override
	public void handlePacket(EntityPlayer player, PacketMLMP packet) {
		if (packet.packetType == PacketTypes.COMMAND) {
			handleCommand(player, packet);
    	}
	}
	
	@Override 
	public TileEntity getTileEntity(int var1, int var2, int var3) {
		return ZCServerTicks.worldRef.getBlockTileEntity(var1, var2, var3);
	}
	
	@Override
	public void handleTileEntityPacket(int var1, int var2, int var3, int var4, int[] var5, float[] var6, String[] var7) {
		
	}
	
	public void handleCommand(EntityPlayer player, PacketMLMP packet) {
		
		int dataInt[] = packet.dataInt;
		
		if (dataInt[0] == CommandTypes.USE) {
			if (tryBuy(player)) {
				ZCServerTicks.sendPacket(player, PacketTypes.MENU_BUY_TIMEOUT, new int[] {0});
			}
		}
		try {
			
			if (mc.worldServers == null) {
				System.out.println("WORLD SERVERS IS NULL, RESTART MC O_o, command cancelled");
				return;
			}
			
			if (mc.getConfigurationManager().areCommandsAllowed(player.username)) {
				if (dataInt[0] == CommandTypes.SET_WAVE) {
					handleWaveSet(player, dataInt, true);
				} else if (dataInt[0] == CommandTypes.TOGGLE_EDIT) {
					//disable server side hardness editing for now? - back on
					//mapMan.editMode = !mapMan.editMode;
					mapMan.toggleEditMode();
					updateInfo(null, PacketTypes.EDITOR_EDITMODE, new int[] {(mapMan.editMode ? 1 : 0)});
				} else if (dataInt[0] == CommandTypes.TOGGLE_DOORNOCLIP) {
					mapMan.toggleNoClip();
					updateInfo(null, PacketTypes.EDITOR_NOCLIP, new int[] {(mapMan.doorNoClip ? 1 : 0)});
				} else if (dataInt[0] == CommandTypes.SET_LEVELSIZE) {
					mapMan.zcLevel.buildData.recalculateLevelSize(dataInt[1], dataInt[2], dataInt[3], dataInt[4], dataInt[5], dataInt[6], true);
					//updateInfo(null, PacketTypes.EDITOR_SETLEVELCOORDS, new int[] {dataInt[1], dataInt[2], dataInt[3], dataInt[4], dataInt[5], dataInt[6]});
				} else if (dataInt[0] == CommandTypes.SET_PLAYERSPAWN) {
					mapMan.setPlayerSpawn(dataInt[1], dataInt[2], dataInt[3]);
					updateInfo(null, PacketTypes.EDITOR_SETSPAWN, new int[] {dataInt[1], dataInt[2], dataInt[3]});
				} else if (dataInt[0] == CommandTypes.REGENERATE) {
					ZCGame.instance().mapMan.loadLevel();
		    		ZCGame.instance().mapMan.buildStart();
					//updateInfo(null, PacketTypes.EDITOR_SETSPAWN, new int[] {data[1], data[2], data[3]});
				} else if (dataInt[0] == CommandTypes.SET_LEVELNAME) {
					this.setMapName(player, packet.dataString[0]);
					updateInfo(null, PacketTypes.EDITOR_SETLEVELNAME, new int[0], packet.dataString);
				} else if (dataInt[0] == CommandTypes.LEVEL_SAVE) {
					mapMan.saveLevel();
				} else if (dataInt[0] == CommandTypes.LEVEL_LOAD) {
					mapMan.loadLevel();
				} else if (dataInt[0] == CommandTypes.SET_SPAWNWATCH) {
					this.setSpawnerWatch(player, dataInt[1], dataInt[2], dataInt[3], dataInt[4], dataInt[5], dataInt[6]);
				} else if (dataInt[0] == CommandTypes.ADD_CAMPOINT) {
					System.out.println("CommandTypes.ADD_CAMPOINT packet todo");
				}
				
				
				Build l = mapMan.zcLevel.buildData;
				if (l != null) {
					updateInfo(null, PacketTypes.EDITOR_SETLEVELCOORDS, new int[] {l.map_coord_minX, l.map_coord_minY, l.map_coord_minZ, l.map_coord_minX+l.map_sizeX, l.map_coord_minY+l.map_sizeY, l.map_coord_minZ+l.map_sizeZ});
				} else {
					System.out.println("cant sync build data coords!");
				}
			} else {
				if (dataInt[0] == CommandTypes.SET_WAVE) {
					handleWaveSet(player, dataInt, false);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void handleWaveSet(EntityPlayer player, int data[], boolean isOp) {
		int wave = data[1];
		int reset = data[2];
		
		if (reset == 1) {
			if (!gameActive) {
				if (zcLevel.player_spawnY != 999) {
					mapMan.movePlayerToSpawn(player); //since game starts with close players to spawn, make sure player starting it is at spawn 
					wMan.startGameFromPlayerSpawn(player);
				} else {
					wMan.startGameFromPlayer(player);
				}
				wMan.setWaveAndStart(wave);
			} else {
				if (isOp) {
					wMan.stopGame();
				}
			}
		} else {
			if (isOp) {
				wMan.setWaveAndStart(wave);
			}
		}
		updateInfo(null, PacketTypes.INFO_WAVE, new int[] {wMan.wave_Stage, (int)wMan.wave_StartDelay, wMan.wave_MaxKills, wMan.wave_Invaders_Count});
		//wMan.startGameFromPlayer(player);
	}
	
	//Monitor wave states for new info to send to 
	@Override
	public void updateWaveInfo() {
		
		if (wMan.wave_Invaders_Count_Last != wMan.wave_Invaders_Count || wMan.wave_StartDelay > 0) {
			updateInfo(null, PacketTypes.INFO_WAVE, new int[] {wMan.wave_Stage, (int)wMan.wave_StartDelay, wMan.wave_MaxKills, wMan.wave_Invaders_Count});
		}
		
		wMan.wave_Invaders_Count_Last = wMan.wave_Invaders_Count;
	}
	
	@Override
	public int getItemMaxStackSize(Item item) {
		return (Integer)ZCUtil.getPrivateValueBoth(Item.class, item, ZCUtil.refl_s_Item_maxStackSize, ZCUtil.refl_mcp_Item_maxStackSize);
	}
	
	@Override
	public void setNewNav(EntityLiving ent, PathNavigate nav) {
		
	}
	
	@Override
	public void addTasks(EntityAITasks tasks, EntityAITasks targetTasks, EntityLiving ent) {
		
		//tasks.addTask(2, new EntityAI_ZA_Pathfind(ent, EntityPlayer.class, 128.0F, 0, false));
	}
	
	@Override
	public EntityPlayer newFakePlayer() {
		c_EntityPlayerMPExt player = new c_EntityPlayerMPExt(mc, mc.worldServerForDimension(0), "fakePlayer", new ItemInWorldManager(mc.worldServerForDimension(0)));
		if (ModLoader.getMinecraftServerInstance().getConfigurationManager().playerEntityList.size() > 0) {
			player.playerNetServerHandler = ((EntityPlayerMP)ModLoader.getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(0)).playerNetServerHandler;
		} else {
			//player.playerNetServerHandler = new NetServerHandler();
			System.out.println("fakeplayer has no netserverhandler, expect crashes");
		}
		
		//mc.configManager.netManager
		//player.movementInput = new MovementInputFromOptions(ZombieCraftMod.mc.gameSettings);
		return player;
	}
	
	@Override
	public void entTick(c_EnhAI ent) {
		if (ModLoader.getMinecraftServerInstance().getConfigurationManager().playerEntityList.size() > 0) {
			//player.playerNetServerHandler = ((EntityPlayerMP)ModLoader.getMinecraftServerInstance().configManager.playerEntities.get(0)).playerNetServerHandler;
			ent.homeX = (int)((EntityPlayerMP)ModLoader.getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(0)).posX;
			ent.homeY = (int)((EntityPlayerMP)ModLoader.getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(0)).posY;
			ent.homeZ = (int)((EntityPlayerMP)ModLoader.getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(0)).posZ;
		}
	}
	
	@Override
	public void playerJoined(EntityPlayer player) {
		check(player);
		
		//If username is in the active game (reconnect from connection loss)
		if (zcLevel != null && (zcLevel.playersInGame_Names.contains(player.username) || mapMan.gameMode == EnumGameMode.CLOSED)) {
			removeOldPlayersMatching(player.username);
			zcLevel.playersInGame.add(player);
			//updateAmmoData(player);
			//updateInfo(player, PacketTypes.INFO_WAVE, new int[] {wMan.wave_Stage, (int)wMan.wave_StartDelay, wMan.wave_MaxKills, wMan.wave_Invaders_Count});
			
			syncPlayer(player);
			
			int zcPoints = (Integer)this.getData(player, DataTypes.zcPoints);
			
			System.out.println("player " + player.username + " joined, pts: " + zcPoints + " wave: " + wMan.wave_Stage + " count: " + wMan.wave_Invaders_Count + " editmode: " + mapMan.editMode);
			
			this.updateInfo(player, PacketTypes.PLAYER_POINTS, new int[] {zcPoints});
			
			if (!isOp(player)) {
				if (mapMan.zcLevel.player_spawnY != 999)  {
					mapMan.movePlayerToSpawn(player);
				}
			}
		}
		
	}
	
	public void removeOldPlayersMatching(String name) {
		for (int i = 0; i < zcLevel.playersInGame.size(); i++) {
			EntityPlayer entPl = (EntityPlayer)zcLevel.playersInGame.get(i);
			if (entPl.isDead || entPl.username.equals(name)) {
				zcLevel.playersInGame.remove(i);
				continue;
			}
			
		}
	}
	
	@Override
	public void playSoundEffect(String sound, EntityPlayer player, float vol, float pitch) {
		if (player == null) {
			//send to all
		} else {
			ZCServerTicks.worldRef.playSoundAtEntity(player, sound, vol, pitch);
		}
	}
	
	@Override
	public void playSound(String sound, int x, int y, int z, float vol, float pitch) {
		ZCServerTicks.worldRef.playSoundEffect(x, y, z, sound, vol, pitch);
	}
	
	@Override
	public World getWorld() {
		return /*getMCSReference()*/FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(activeZCDimension);
	}
	
	/*public MinecraftServer getMCSReference() {
		if (mc == null)
        {
            try
            {
                ThreadGroup var0 = Thread.currentThread().getThreadGroup();
                int var1 = var0.activeCount();
                Thread[] var2 = new Thread[var1];
                var0.enumerate(var2);
                int var3;

                for (var3 = 0; var3 < var2.length; ++var3)
                {
                    System.out.println(var2[var3].getName());
                }

                for (var3 = 0; var3 < var2.length; ++var3)
                {
                    if (var2[var3].getName().equals("Server thread"))
                    {
                    	ThreadServerApplication hm = (ThreadServerApplication)ModLoader.getPrivateValue(Thread.class, var2[var3], "me"); 
                        mc = (MinecraftServer)ModLoader.getPrivateValue(ThreadServerApplication.class, hm, "mcServer");
                        break;
                    }
                }
            }
            catch (SecurityException var4)
            {
                //logger.throwing("ModLoader", "getMinecraftInstance", var4);
                throw new RuntimeException(var4);
            }
            catch (NoSuchFieldException var5)
            {
                //logger.throwing("ModLoader", "getMinecraftInstance", var5);
                throw new RuntimeException(var5);
            }
        }

        return mc;
	}*/
	
	@Override
	public String getWorldSavePath() {
		return "";//(String)ZCUtil.getPrivateValueBoth(WorldInfo.class, getWorld().getWorldInfo(), "k", "levelName") + "/";
	}
	
	@Override
	public boolean canEdit(EntityPlayer player) {
		return ZCGame.instance().mapMan.editMode && isOp(player);
	}
	
	public boolean isOp(EntityPlayer player) {
		return mc.getConfigurationManager().areCommandsAllowed(player.username);
	}
	
	@Override
	public void setPlayerSpawn(EntityPlayer player, int x, int y, int z) {
		if (canEdit(player)) {
			mapMan.setPlayerSpawn(x, y, z);
		}
	}
	
	//dummy method for compile friendly mp code in ZCGame (trying new shortcut methods)
	@Override
	public void sendPacket(EntityPlayer player, int packetType, int[] dataInt, String[] dataString) {
		ZCServerTicks.sendPacket(player, packetType, dataInt, dataString);
	}
	
	@Override
	public void sendPacket(EntityPlayer player, int packetType, int[] dataInt) {
		ZCServerTicks.sendPacket(player, packetType, dataInt);
	}
	
	@Override
	public void teleportPlayer(EntityPlayer player, double x, double y, double z) {
		//player.setPositionAndUpdate(x, y, z);
		System.out.println("tele: " + x + "," + y + "," + z);
		((EntityPlayerMP)player).playerNetServerHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
	}
	
	@Override
	public boolean trySetTexturePack(String packFileName) {
		updateInfo(null, PacketTypes.EDITOR_SETLEVELTEXTUREPACK, new int[0], new String[] { packFileName });
		return true;
	}
	
	@Override
	public void pvpFixPre() {
    	origPVPState = this.mc.isPVPEnabled();
    	this.mc.setAllowPvp(true);
    }
    
    public void pvpFixPost() {
    	this.mc.setAllowPvp(origPVPState);
    }
}
