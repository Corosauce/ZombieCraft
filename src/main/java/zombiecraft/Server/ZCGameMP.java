package zombiecraft.Server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import zombiecraft.Core.AmmoDataLatcher;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.CommandTypes;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.EnumGameMode;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Forge.PacketMLMP;
import zombiecraft.Forge.ZCServerTicks;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.OldUtil;
import CoroUtil.util.CoroUtilEntity;
import build.world.Build;
import cpw.mods.fml.common.FMLCommonHandler;

public class ZCGameMP extends ZCGame {
	
	public MinecraftServer mc;
	
	public List<String> playersToWatchForSpawn = new LinkedList();
	
	public int lastPlayerCount = 0;
	public int reinitWaitTime = 10;
	
	//more fun singleplayer convinience fixes
	public static boolean adjustedPlayer = true;
	
	
	
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
		
		if (ZCGame.autoload) {
			List<EntityPlayer> players = getPlayers(0);
			
			for(int i = 0; i < players.size(); i++) {
				EntityPlayer plEnt = (EntityPlayer)players.get(i);
				
				//System.out.println(plEnt.capabilities);
				
				//plEnt.capabilities.isCreativeMode = !ZCGame.autostart;
				ZombieCraftMod.teleportPlayerToDim((EntityPlayerMP)plEnt, ZCGame.ZCDimensionID);
			}
			
			if (!adjustedPlayer) {
				players = getPlayers(activeZCDimension);
				
				for(int i = 0; i < players.size(); i++) {
					EntityPlayerMP plEnt = (EntityPlayerMP)players.get(i);
					
					plEnt.setGameType(ZCGame.autostart ? WorldSettings.GameType.SURVIVAL : WorldSettings.GameType.CREATIVE);
					adjustedPlayer = true;
				}
			}
		}
		
		List<EntityPlayer> players = getPlayers();
		
		zcLevel.playersInGame = players;
		
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
				if (!playersToWatchForSpawn.contains(CoroUtilEntity.getName(plEnt))) markPlayerForReadding(plEnt);
			}
		}
		
		if (players.size() != lastPlayerCount)  {
			lastPlayerCount = players.size();
			
		}
		
		
		if (mapMan.buildActive()) {
			
			//System.out.println(zcLevel.buildData.curTick + " - " + zcLevel.buildData.maxTicks);
			float percent = 0;
			if (zcLevel.buildJob != null) {
				percent = ((float)zcLevel.buildJob.curTick + 1) / ((float)zcLevel.buildJob.maxTicks) * 100F;
			}
			//System.out.println("build percent: " + percent);
			updateInfo(null, PacketTypes.EDITOR_BUILDSTATE, new int[] {(int)percent});
			if (FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
				//mapMan.curBuildPercent = (int) percent;
			}
		} else {
			if (zcLevel.buildJob != null) {
				if (zcLevel.buildJob.curTick != -1) {
					zcLevel.buildJob.curTick = -1; //mark that the build is done after observing a finished state
					updateInfo(null, PacketTypes.EDITOR_BUILDSTATE, new int[] {-1}); //tell client
				}
			} else {
				//System.out.println("buildJob is null, just letting you know, this might not matter, but if running into build state issues, find me!");
			}
		}
		
		watchWorldHook();
		watchForRespawn();
		
		World world2 = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
		
		/*GameRules gr = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0).getGameRules();
		gr.addGameRule("doMobLoot", "true");
		gr.addGameRule("doFireTick", "true");
		gr.addGameRule("doTileDrops", "true");*/
		
		if (activeZCDimension == ZCDimensionID) {
			if (this.getWorld() != null) {
				World world = this.getWorld();
				GameRules gr = this.getWorld().getWorldInfo().getGameRulesInstance();
				
				//dont use any till it works right, fire tick not needed since no items make fire now, keep till fixed
				//according to yourkit, these are causing numberformat exceptions
				gr.addGameRule("doMobLoot", "true");
				gr.addGameRule("doFireTick", "true");
				gr.addGameRule("doTileDrops", "true");
				
				//temp fix, bugs out other dimensions, need real fix, same for explosions - does it REALLY bug them out? i bet it doesnt
				if (gameActive) {
					//gr.addGameRule("doFireTick", "false");
					
				} else {
					//gr.addGameRule("doFireTick", "true");
					//gr.addGameRule("doMobLoot", "true");
				}
				//uhh this isnt per dimension
				//gr.addGameRule("doFireTick", "true");
				//gr.addGameRule("mobGriefing", "true");
		        //gr.addGameRule("doMobLoot", "true");
		        //gr.addGameRule("doTileDrops", "true");
			}
		} else {
			
		}
		
		
		
		super.tick();
	}
	
	public void updatePlayerList() {
		List<EntityPlayer> allPlayers = getPlayers();
		for(int i = 0; i < allPlayers.size(); i++) {
			EntityPlayer plEnt = (EntityPlayer)allPlayers.get(i);
			
		}
	}
	
	public void watchForRespawn() {
		
			
			List<EntityPlayer> allPlayers = getPlayers();
			for(int h = 0; h < playersToWatchForSpawn.size(); h++) {
				for(int i = 0; i < allPlayers.size(); i++) {
					EntityPlayer plEnt = allPlayers.get(i);
		
					if (!plEnt.isDead && CoroUtilEntity.getName(plEnt).equals(playersToWatchForSpawn.get(h))) {
						if (reinitWaitTime > 0) {
							reinitWaitTime--;
							return; //return to prevent loop overticking cooldown, code needs to be here to make sure a player is waiting to be reinitilized (list match)
						} else {
							reinitWaitTime = 10;
							System.out.println("readding: " + CoroUtilEntity.getName(plEnt));
							zcLevel.playersInGame.add(plEnt);
							zcLevel.playersInGame_Names.add(CoroUtilEntity.getName(plEnt));
							playersToWatchForSpawn.remove(h);
							//System.out.println("TEMP DISABLE in watchForRespawn()");
							resetPlayer(plEnt, true, true);
							return; //force it to rewait per player, incase someone just respawned while other player spawned
						}
					}
					
				}
			}
		
	}
	
	public void markPlayerForReadding(EntityPlayer player) {
		System.out.println("Marking for readd: " + CoroUtilEntity.getName(player));
		playersToWatchForSpawn.add(CoroUtilEntity.getName(player));
		removeOldPlayersMatching(CoroUtilEntity.getName(player));
	}
	
	@Override
	public List<EntityPlayer> getPlayers() {
		return getPlayers(activeZCDimension);
	}
	
	@Override
	public List<EntityPlayer> getPlayers(int dim) {
		List<EntityPlayer> players = new LinkedList();
		World world = this.mc.worldServerForDimension(dim);
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
		if (this.gameActive) {
			player.getFoodStats().addStats(20, 1F);
			/*if (player instanceof c_EntityPlayerMPExt) {
				((c_EntityPlayerMPExt)player).setFoodLevel(20);
			}*/
		}
	}
	
	@Override
	public void handlePlayerAbilities(EntityPlayer player) {
		super.handlePlayerAbilities(player);
		
		//juggernog logic
		int juggTime = (Integer)getData(player, DataTypes.juggTime);
		if (juggTime > 0) {
			if (player.inventory.armorInventory[2] == null) {
				player.inventory.armorInventory[2] = new ItemStack(Items.iron_chestplate);
        	}
			if (juggTime == 1) {
				player.inventory.armorInventory[2] = null;
			}
			setData(player, DataTypes.juggTime, --juggTime);
		}
		
		//exstatic logic
		int exStaticTime = (Integer)getData(player, DataTypes.exStaticTime);
		if (exStaticTime > 0) {
			setData(player, DataTypes.exStaticTime, --exStaticTime);
		}
		
		int exStaticCooldown = (Integer)getData(player, DataTypes.exStaticCooldown);
		if (exStaticCooldown > 0) {
			setData(player, DataTypes.exStaticCooldown, --exStaticCooldown);
		}
		
		List var4 = player.worldObj.getEntitiesWithinAABB(BaseEntAI.class, player.boundingBox.expand(1.0D, 2.0D, 1.0D));
        if (var4 != null && !var4.isEmpty())
        {
        	if (exStaticTime > 0) {
	        	for (int i = 0; i < var4.size(); i++) {
	        		BaseEntAI ent = (BaseEntAI)var4.get(i);
	        		
	        		if (ent.isEnemy(player)) {
	        			ent.attackEntityFrom(DamageSource.causeMobDamage(player), 5);
	        			ent.knockBack(player, 0, player.posX - ent.posX, player.posZ - ent.posZ);
	        		}
	        	}
        	} else {
        		if (exStaticCooldown == 0) {
        			setData(player, DataTypes.exStaticCooldown, Buyables.perkCooldownExStatic);
        			setData(player, DataTypes.exStaticTime, Buyables.perkLengthExStatic);
        		}
        	}
        }
        
        //double points counter
        int doublePointsTime = (Integer)getData(player, DataTypes.doublePointsTime);
		if (doublePointsTime > 0) {
			setData(player, DataTypes.doublePointsTime, --doublePointsTime);
		}
		
		//insta kill counter
        int instaKillTime = (Integer)getData(player, DataTypes.instaKillTime);
		if (instaKillTime > 0) {
			setData(player, DataTypes.instaKillTime, --instaKillTime);
		}
		
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
		//System.out.println("ammoMap.size() " + ammoMap.size());
		
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
		
		System.out.println("notifyBlockUpdates needs a proper fix!!!");
		/*ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y, z, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y+1, z, getWorld()));
		
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x+1, y, z, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x+1, y+1, z, getWorld()));
		
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y, z+1, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y+1, z+1, getWorld()));
		
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x-1, y, z, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x-1, y+1, z, getWorld()));
		
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y, z-1, getWorld()));
		ZCServerTicks.sendPacketToAll(new Packet53BlockChange(x, y+1, z-1, getWorld()));*/
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
		return ZCServerTicks.worldRef.getTileEntity(var1, var2, var3);
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
		} else if (dataInt[0] == CommandTypes.RELOAD) {
			ItemStack is = player.getCurrentEquippedItem();
			if (is != null && is.getItem() instanceof ItemGun) {
				ItemGun ig = (ItemGun)is.getItem();
				
				ig.setClipAmount(is, player.worldObj, player, ig.magSize);
				ig.setReloadDelay(is, player.worldObj, player, ig.reloadTime);
				//ig.clipAmount = ig.magSize;
				//ig.reloadDelay = ig.reloadTime;
				playSoundEffect("zc.gun.reload", player, 1.0F, 1.0F / (player.worldObj.rand.nextFloat() * 0.4F + 0.8F));
			}
		}
		
		try {
			
			if (mc.worldServers == null) {
				System.out.println("WORLD SERVERS IS NULL, RESTART MC O_o, command cancelled");
				return;
			}
			
			if (mc.getConfigurationManager().func_152596_g(player.getGameProfile())) {
				if (dataInt[0] == CommandTypes.SET_WAVE) {
					handleWaveSet(player, dataInt, true);
				} else if (dataInt[0] == CommandTypes.TOGGLE_EDIT) {
					mapMan.toggleEditMode();
					updateInfo(null, PacketTypes.EDITOR_EDITMODE, new int[] {(mapMan.editMode ? 1 : 0)});
				} else if (dataInt[0] == CommandTypes.TOGGLE_DOORNOCLIP) {
					mapMan.toggleNoClip();
					updateInfo(null, PacketTypes.EDITOR_NOCLIP, new int[] {(mapMan.doorNoClip ? 1 : 0)});
				} else if (dataInt[0] == CommandTypes.SET_LEVELSIZE) {
					mapMan.zcLevel.buildData.recalculateLevelSize(dataInt[1], dataInt[2], dataInt[3], dataInt[4], dataInt[5], dataInt[6], true);
					setActiveDimension(player.dimension);
				} else if (dataInt[0] == CommandTypes.SET_PLAYERSPAWN) {
					mapMan.setPlayerSpawn(dataInt[1], dataInt[2], dataInt[3]);
					updateInfo(null, PacketTypes.EDITOR_SETSPAWN, new int[] {dataInt[1], dataInt[2], dataInt[3]});
				} else if (dataInt[0] == CommandTypes.REGENERATE) {
					movePlayersToLobby();
					ZCGame.instance().mapMan.loadLevel();
		    		ZCGame.instance().mapMan.buildStart(player);
		    		ZCGame.instance().trySetTexturePack(ZCGame.instance().zcLevel.texturePack);
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
				boolean startResult = false;
				if (zcLevel.player_spawnY != 999) {
					//mapMan.movePlayerToSpawn(player); 
					startResult = wMan.startGameFromPlayerSpawn(player);
				} else {
					startResult = wMan.startGameFromPlayer(player);
				}
				if (startResult) wMan.setWaveAndStart(wave);
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
		return (Integer)ZCUtil.getPrivateValueBoth(Item.class, item, OldUtil.refl_s_Item_maxStackSize, OldUtil.refl_mcp_Item_maxStackSize);
	}
	
	@Override
	public void setNewNav(EntityLivingBase ent, PathNavigate nav) {
		
	}
	
	@Override
	public void addTasks(EntityAITasks tasks, EntityAITasks targetTasks, EntityLivingBase ent) {
		
		//tasks.addTask(2, new EntityAI_ZA_Pathfind(ent, EntityPlayer.class, 128.0F, 0, false));
	}
	
	@Override
	public void entTick(BaseEntAI ent) {
		if (ent.agent == null) return;
		if (MinecraftServer.getServer().getConfigurationManager().playerEntityList.size() > 0) {
			//player.playerNetServerHandler = ((EntityPlayerMP)ModLoader.getMinecraftServerInstance().configManager.playerEntities.get(0)).playerNetServerHandler;
			ent.agent.homeX = (int)((EntityPlayerMP)MinecraftServer.getServer().getConfigurationManager().playerEntityList.get(0)).posX;
			ent.agent.homeY = (int)((EntityPlayerMP)MinecraftServer.getServer().getConfigurationManager().playerEntityList.get(0)).posY;
			ent.agent.homeZ = (int)((EntityPlayerMP)MinecraftServer.getServer().getConfigurationManager().playerEntityList.get(0)).posZ;
		}
	}
	
	//this has stopped being used since i lost the hook for a while, now its been accounted for in other ways....
	@Override
	public void playerJoined(EntityPlayer player) {
		check(player);
		
		//If username is in the active game (reconnect from connection loss)
		if (zcLevel != null && (zcLevel.playersInGame_Names.contains(CoroUtilEntity.getName(player)) || mapMan.gameMode == EnumGameMode.CLOSED)) {
			removeOldPlayersMatching(CoroUtilEntity.getName(player));
			zcLevel.playersInGame.add(player);
			//updateAmmoData(player);
			//updateInfo(player, PacketTypes.INFO_WAVE, new int[] {wMan.wave_Stage, (int)wMan.wave_StartDelay, wMan.wave_MaxKills, wMan.wave_Invaders_Count});
			
			syncPlayer(player);
			
			int zcPoints = (Integer)this.getData(player, DataTypes.zcPoints);
			
			System.out.println("player " + CoroUtilEntity.getName(player) + " joined, pts: " + zcPoints + " wave: " + wMan.wave_Stage + " count: " + wMan.wave_Invaders_Count + " editmode: " + mapMan.editMode);
			
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
			if (entPl.isDead || CoroUtilEntity.getName(entPl).equals(name)) {
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
			ZCServerTicks.worldRef.playSoundAtEntity(player, ZombieCraftMod.modID + ":" + sound, vol, pitch);
		}
	}
	
	@Override
	public void playSound(String sound, int x, int y, int z, float vol, float pitch) {
		ZCServerTicks.worldRef.playSoundEffect(x, y, z, ZombieCraftMod.modID + ":" + sound, vol, pitch);
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
		return isOp(CoroUtilEntity.getName(player));
	}
	
	public boolean isOp(String parName) {
		EntityPlayer entP = mc.getConfigurationManager().func_152612_a(parName);
		if (entP == null) {
			System.out.println("ZC: couldnt find player from username");
			return false;
		}
		return this.lobbyLeader.equals(parName) || mc.getConfigurationManager().func_152596_g(entP.getGameProfile());
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
	//Setting to -1 tells client build is done
	public void setCurBuildPercent(int percent) {
		super.setCurBuildPercent(percent);
		updateInfo(null, PacketTypes.EDITOR_BUILDSTATE, new int[] {percent});
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
