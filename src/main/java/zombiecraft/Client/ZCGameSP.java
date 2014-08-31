package zombiecraft.Client;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zombiecraft.Client.GameLogic.InterfaceManagerMP;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.ZCItems;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Blocks.TileEntityPurchasePlate;
import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.Entities.Zombie;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.PacketMLMP;
import zombiecraft.Forge.ZCClientTicks;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.OldUtil;
import CoroUtil.pathfinding.c_IEnhPF;
import build.render.Overlays;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

public class ZCGameSP extends ZCGame {
	
	public Minecraft mc;
	public boolean waitingToSpawn = false;
	
	public ZCGameSP(boolean serverMode) {
		super(serverMode);
		System.out.println("new ZCGameSP");
	}
	
	public void tick() {
		
		if (mc.thePlayer == null) return;
		
		if (wMan == null || resetWaveManager) {
			resetWaveManager = false;
			gameInit(); 
			
			//Single player init only
			if (!ZCClientTicks.worldRef.isRemote) {
				if (!levelHasInit) this.levelInit();
			} else {
				this.zcLevel = null; //to make sure the client isnt using it when in smp
			}
		}
		
		//if (!isRemote()) {
			
			check(mc.thePlayer);
			
			int timeout = (Integer)getData(mc.thePlayer, DataTypes.purchaseTimeout);
			
			if (timeout > 0) {
				
				timeout--;
				
				if (timeout == 0) {
					resetBuyState(mc.thePlayer);
				}
				
				setData(mc.thePlayer, DataTypes.purchaseTimeout, timeout);
			}
			
			this.playerTick(mc.thePlayer);
		//}
		
		super.tick();
	}
	
	//Used only in ZCGameMP class
	@Override
	public void updateInfo(EntityPlayer player, int pt, int[] dataInt) {}
	
	public void updateInfoRun(EntityPlayer player, int pt, int[] dataInt, String[] dataString) {
		PacketMLMP packet = new PacketMLMP();
        packet.packetType = pt;
        packet.dataInt = dataInt;
        packet.dataString = dataString;
        
        this.handlePacket(player, packet);
	}
	
	@Override
	public void updateInfo(EntityPlayer player, int pt, int[] dataInt, String[] dataString) {}
	
	@Override
	public void updateTileInfo(TileEntity tEnt, int dataInt) {}
	
	@Override
	public void updateWaveInfo() {}
	
	@Override
	public void updateAmmoData(EntityPlayer ent) {
		
	}
	
	@Override
	public List<EntityPlayer> getPlayers() {
		List<EntityPlayer> players = new LinkedList();
		//for(int i = 0; i < this.mc.worldMngr[activeZCDimension].playerEntities.size(); i++) {
			players.add(ZCClientTicks.player);
		//}
		return players;
	}
	
	@Override
	public boolean isRemote() {
		return getWorld().isRemote;
	}
	
	@Override
	public void handlePacket(EntityPlayer player, PacketMLMP packet) {
		
		if (mc.thePlayer.worldObj.provider.dimensionId != ZCGame.ZCDimensionID && !mapMan.editMode && !FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) return;
		
		try {
			boolean dbg = false;
			
			check(ZCClientTicks.player);
			
	    	if (ZCClientTicks.iMan == null) {
				/*if (!mc.isMultiplayerWorld()) {
					ClientTickHandler.iMan = new InterfaceManagerSP(mc, this);
		    	} else {*/
		    		ZCClientTicks.iMan = new InterfaceManagerMP(mc, this);
		    	//}
			}
	    	
	    	if (ZCClientTicks.zcGame.wMan == null) this.gameInit();
	    	
			int p = packet.packetType;
	    	
	        if (p == PacketTypes.MENU_BUY_PROMPT) {
	        	ZCClientTicks.iMan.buyMenuState = 1;
	        	ZCClientTicks.zcGame.setData(mc.thePlayer, DataTypes.purchaseTimeout, packet.dataInt[4]);
	        	//if (dbg) System.out.println("MENU_BUY_PROMPT");
	        	//ItemStack itemstack = BuyableItems.getBuyItem(packet.dataInt[0]);
				//String name = itemstack.getItem().getItemDisplayName(itemstack);
				ZCClientTicks.iMan.buyID = packet.dataInt[0];
				ZCClientTicks.zcGame.setData(mc.thePlayer, DataTypes.purchaseCoordX, packet.dataInt[1]);
				ZCClientTicks.zcGame.setData(mc.thePlayer, DataTypes.purchaseCoordY, packet.dataInt[2]);
				ZCClientTicks.zcGame.setData(mc.thePlayer, DataTypes.purchaseCoordZ, packet.dataInt[3]);
	        			
	        } else if (p == PacketTypes.MENU_BUY_TRANSACTCONFIRM) {
	        	if (packet.dataInt.length >= 3 && packet.dataInt[2] != 0) {
	        		resetBuyState(mc.thePlayer, packet.dataInt[2]);
	        	} else {
	        		resetBuyState(mc.thePlayer);
	        		if (ZCItems.itemPerkCharge.itemID == packet.dataInt[0]) {
	        			//ZCClientTicks.iMan.hasCharge = true;
	        			ZCUtil.setData(player, DataTypes.hasCharge, 1);
	        		} else if (ZCItems.itemPerkSpeed.itemID == packet.dataInt[0]) {
	        			ZCUtil.setData(player, DataTypes.speedTime, Buyables.perkLengthSpeed);
	        		}
	        	}
	        	ZCClientTicks.zcGame.setData(mc.thePlayer, DataTypes.zcPoints, packet.dataInt[1]);
	        	//ClientTickHandler.iMan.reBuyDelay = ;
	        	//Buyables.barricadeRepairCooldown
	        	if (packet.dataInt[0] == -2) {
		        	//this.playSoundEffect("sdkzc.repair", player, 1, 1);//("sdkzc.repair", x, y, z, 1, 1);
		        	//this.playSoundEffect("sdkzc.chaching", player, 1, 1);//
					//this.playSound("sdkzc.chaching", x, y, z, 1, 0.8F);
	        	}
	        } else if (p == PacketTypes.MENU_BUY_TIMEOUT) {
	        	resetBuyState(mc.thePlayer);
	        } else if (p == PacketTypes.INFO_WAVE) {
	        	if (packet.dataInt[0] > wMan.wave_Stage && wMan.wave_Stage != 0) {
	        		//playSoundEffect("sdkzc.round_over", player, 2F, 1.0F);
	        		//player.worldObj.playSoundAtEntity(player, "sdkzc.round_over", 1.0F, 1.0F);
	        		ZCClientTicks.mc.sndManager.playSoundFX(ZombieCraftMod.modID + ":zc.round_over", 1F, 1.0F);
	        		//System.out.println("HYYYYYYYYYYYAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
	        	} else if (packet.dataInt[0] == 1 && packet.dataInt[0] > wMan.wave_Stage) {
	        		ZCClientTicks.mc.sndManager.playSoundFX(ZombieCraftMod.modID + ":zc.start", 1F, 1.0F);
	        	}
	        	wMan.wave_Stage = packet.dataInt[0];
	        	wMan.wave_StartDelay = packet.dataInt[1];
	        	wMan.wave_MaxKills = packet.dataInt[2]; //unneeded? - a way to trigger local end of wave event without need of new packet event
	        	wMan.wave_Invaders_Count = packet.dataInt[3];
	        	
	        	if (wMan.wave_Stage == 0) {
	        		this.gameActive = false;
	        	} else { this.gameActive = true; }
	        	
	        } else if (p == PacketTypes.PLAYER_POINTS) {
	        	if (dbg) System.out.println("PacketTypes.PLAYER_POINTS: " + packet.dataInt[0]);
	        	setData(ZCClientTicks.player, DataTypes.zcPoints, packet.dataInt[0]);
	        } else if (p == PacketTypes.PLAYER_AMMO) {
	        	
	        	if (dbg) System.out.println("PacketTypes.PLAYER_AMMO");
	        	
	        	int size = packet.dataInt[0];
	        	
	        	//System.out.println("AMMO SIZE CODE ADJUST");
	        	//size = (packet.dataInt.length-1);
	        	
	        	//System.out.println("size: " + size);
	        	
	        	if (size > 0) {
		        	int i = 1;
		        	
		        	while (i <= size * 2) {
		        		ZCUtil.setAmmoData(ZCClientTicks.player, packet.dataInt[i], packet.dataInt[i+1]);
		        		if (dbg) System.out.println("cl ammo data -> id: " + packet.dataInt[i] + " val: " + packet.dataInt[i+1]);
		        		i+=2;
		        	}
	        	}
	        	
	        	
	        } else if (p == PacketTypes.EDITOR_EDITMODE) {
	        	if (dbg) System.out.println("PacketTypes.EDITOR_EDITMODE");
	        	if (packet.dataInt[0] == 1) {
	        		mapMan.editMode = true;
	        	} else {
	        		mapMan.editMode = false;
	        	}
	        	mapMan.updateEditState();
	        	//if (mc.currentScreen instanceof GuiEditorCP) mc.currentScreen.initGui();
	        } else if (p == PacketTypes.EDITOR_NOCLIP) {
	        	if (packet.dataInt[0] == 1) {
	        		mapMan.doorNoClip = true;
	        	} else {
	        		mapMan.doorNoClip = false;
	        	}
	        	//if (mc.currentScreen instanceof GuiEditorCP) mc.currentScreen.initGui();
	        } else if (p == PacketTypes.EDITOR_SETSPAWN) {
	        	mapMan.setPlayerSpawn(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2]);
	        } else if (p == PacketTypes.EDITOR_SETLEVELNAME) {
	        	this.setMapName(player, packet.dataString[0] + "", true);
	        } else if (p == PacketTypes.EDITOR_SETLEVELCOORDS) {
	        	mapMan.zcLevel.buildData.recalculateLevelSize(packet.dataInt[0], packet.dataInt[1], packet.dataInt[2], packet.dataInt[3], packet.dataInt[4], packet.dataInt[5]);
	        } else if (p == PacketTypes.EDITOR_SETLEVELTEXTUREPACK) {
	        	this.trySetTexturePack(packet.dataString[0] + "");
	        } else if (p == PacketTypes.EDITOR_BUILDSTATE) {
	        	//System.out.println("wat " + packet.dataInt[0]);
	        	this.setCurBuildPercent(packet.dataInt[0]);
	        } else {
	        	runInfoCommand(player, packet.packetType, packet.dataInt, packet.dataString);
	        }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public TileEntity getTileEntity(int var1, int var2, int var3) {
		return ZCClientTicks.worldRef.getTileEntity(var1, var2, var3);
	}
	
	@Override @Deprecated
	public void handleTileEntityPacket(int var1, int var2, int var3, int var4, int[] var5, float[] var6, String[] var7) {
		TileEntity tEnt = ZCClientTicks.mc.theWorld.getTileEntity(var1, var2, var3);
		//System.out.println("tEnt " + tEnt);
		if (tEnt != null && tEnt instanceof TileEntityPurchasePlate) {
			((TileEntityPurchasePlate)tEnt).itemIndex = var5[0];
		}
	}
	
	@Override
	public void playerTick(EntityPlayer player) {
		super.playerTick(player);
		
		if (this.gameActive) {
			player.getFoodStats().setFoodLevel(20);
		}
		
		if (waitingToSpawn) {
			if (/*ZCClientTicks.camMan.camState != EnumCameraState.OFF && */(!ZCGame.instance().gameActive || ZCGame.instance().wMan.wave_StartDelay > 0)) {
				waitingToSpawn = false;
				//System.out.println("TEMP DISABLED AUTO RESPAWN playerTick");
				ZCClientTicks.camMan.disableCamera();
	    		mc.thePlayer.respawnPlayer();
	    		this.mc.displayGuiScreen((GuiScreen)null);
	    	}
		}
		
		
	}
	
	@Override
	public void handlePlayerAbilities(EntityPlayer player) {
		super.handlePlayerAbilities(player);
		
		if (ZCClientTicks.iMan != null) {
			
			//charge logic
			if (ZCClientTicks.iMan.chargeCooldown > 0) {
				ZCClientTicks.iMan.chargeCooldown--;
			}
			if (ZCClientTicks.iMan.chargeTick > 0) {
				ZCClientTicks.iMan.chargeTick--;
				
				if (player.onGround && Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) > 0.1F) {
					player.motionX *= 1.5F;
					player.motionZ *= 1.5F;
				}
			}

			//speed cola logic
			int speedTime = (Integer)getData(player, DataTypes.speedTime);
			if (speedTime > 0) {
				speedTime--;
				setData(player, DataTypes.speedTime, speedTime);
				if (player.onGround && Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ) > 0.1F) {
					player.motionX *= 1.4F;
		        	player.motionZ *= 1.4F;
		        }
			}
		}
	}
	
	public boolean triggerBuyMenu(Entity entity, int x, int y, int z, int itemIndex) {
		if (false/*!mc.isMultiplayerWorld()*/) {
			ZCClientTicks.iMan.buyID = itemIndex;
			if (super.triggerBuyMenu(entity, x, y, z, itemIndex)) {
				//ItemStack itemstack = BuyableItems.getBuyItem(itemIndex);
				//String name = itemstack.getItem().getItemDisplayName(itemstack);
				
				//ClientTickHandler.iMan.buyMenuTimeout = 30;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void resetBuyState(Entity entity, int cooldown) {
		
		int adjustedCooldown = cooldown;
		
		if ((Integer)ZCUtil.getData((EntityPlayer)entity, DataTypes.speedTime) > 0) {
			adjustedCooldown /= 2;
		}
		
		super.resetBuyState(entity, adjustedCooldown);
		ZCClientTicks.iMan.reBuyDelay = adjustedCooldown;
		ZCClientTicks.iMan.buyMenuState = 0;
		ZCClientTicks.iMan.buyID = 0;
	}
	
	/*@Override
	public void resetPlayers() {
		resetPlayer(ClientTickHandler.mc.thePlayer);
	}*/
	
	@Override
	public int getItemMaxStackSize(Item item) {
		return (Integer)ZCUtil.getPrivateValueBoth(Item.class, item, OldUtil.refl_s_Item_maxStackSize, OldUtil.refl_mcp_Item_maxStackSize);
	}
	
	@Override
	public void setNewNav(EntityLivingBase ent, PathNavigate nav) {
		//this.setPrivateValueBoth(EntityLivingBase.class, ent, this.refl_c_Item_maxStackSize, this.refl_mcp_Item_maxStackSize);
	}
	
	@Override
	public void addTasks(EntityAITasks tasks, EntityAITasks targetTasks, EntityLivingBase ent) {
		float moveSpeed = 0.23F;
		if (ent instanceof Zombie) {
			moveSpeed = ((Zombie)ent).getMoveSpeed();
		}
		//tasks.addTask(2, new EntityAI_ZA_AttackPersist(ent, EntityPlayer.class, moveSpeed, false));
		//targetTasks.addTask(2, new EntityAI_ZA_Pathfind(ent, EntityPlayer.class, 128.0F, 0, false));
	}
	
	@Override
	public void entTick(BaseEntAI ent) {
		ent.agent.homeX = (int)mc.thePlayer.posX;
		ent.agent.homeY = (int)mc.thePlayer.posY;
		ent.agent.homeZ = (int)mc.thePlayer.posZ;
		
	}
	
	public static boolean renderLine = true;
	
	public static void renderPFLines(EntityLiving entityliving, double d, double d1, double d2, 
			float f, float f1) {
    	
    	if (renderLine) {
        	entityliving.ignoreFrustumCheck = true;
        } else {
        	entityliving.ignoreFrustumCheck = false;
        }
    	
    	if (renderLine && entityliving instanceof c_IEnhPF) {
    		//c_IEnhPF koa = ((c_IEnhPF)entityliving);
    		if (entityliving.getNavigator().getPath() != null/* && koa.getPath().points != null*/) {
	            if (entityliving.getNavigator().getPath().getCurrentPathLength() > 1) {
	            	int ii = 0;//koa.getPath().pathIndex - 1;
	            	if (ii < 0) ii = 0;
	            	for (int i = ii; i < entityliving.getNavigator().getPath().getCurrentPathLength()-1; i++) {
	            		PathPoint ppx = entityliving.getNavigator().getPath().getPathPointFromIndex(i);
	            		PathPoint ppx2 = entityliving.getNavigator().getPath().getPathPointFromIndex(i+1);//koa.getPath().points[i+1];
	
	        	        if(ppx == null || ppx2 == null)
	        	            return;
	        	
	        	        if (renderLine) {
	        	        	entityliving.ignoreFrustumCheck = true;
	        	            Overlays.renderLine(ppx, ppx2, d, d1, d2, f, f1, 0xFF0000);
	        	        } else {
	        	        	entityliving.ignoreFrustumCheck = false;
	        	        }
	            	}
	            	
	            }
    		}
    	}
    	
    }
	
	
	
	@Override
	public void renderInWorldOverlay() {
		renderLevelSize();
		renderPlayerSpawns();
		
		if (settingSize) {
			float x = (float)mc.thePlayer.posX;
			float y = (float)mc.thePlayer.posY-1;
			float z = (float)mc.thePlayer.posZ;
			if (mc.objectMouseOver != null) {
				x = mc.objectMouseOver.blockX;
				y = mc.objectMouseOver.blockY;
				z = mc.objectMouseOver.blockZ;
			}
			Overlays.renderBuildOutline(sx, sy, sz, x, y, z);
		}
	}
	
	public void renderPlayerSpawns() {
		Overlays.renderLineFromToBlock(mapMan.zcLevel.buildData.map_coord_minX + mapMan.zcLevel.player_spawnX
				, mapMan.zcLevel.buildData.map_coord_minY + mapMan.zcLevel.player_spawnY
				, mapMan.zcLevel.buildData.map_coord_minZ + mapMan.zcLevel.player_spawnZ
				, mapMan.zcLevel.buildData.map_coord_minX + mapMan.zcLevel.player_spawnX
				, mapMan.zcLevel.buildData.map_coord_minY + mapMan.zcLevel.player_spawnY+1
				, mapMan.zcLevel.buildData.map_coord_minZ + mapMan.zcLevel.player_spawnZ, 0xFFFFFF);
	}
	
	public void renderLevelSize() {
		float x = mapMan.zcLevel.buildData.map_coord_minX/* + 0.5F*/;
		float y = mapMan.zcLevel.buildData.map_coord_minY/* + 0.5F*/;
		float z = mapMan.zcLevel.buildData.map_coord_minZ/* + 0.5F*/;
		float x1 = mapMan.zcLevel.buildData.map_sizeX + x - 1F;
		float y1 = mapMan.zcLevel.buildData.map_sizeY + y - 1F;
		float z1 = mapMan.zcLevel.buildData.map_sizeZ + z - 1F;
		Overlays.renderBuildOutline(x, y, z, x1, y1, z1);
	}

	/*public static void renderLine(PathPoint ppx, PathPoint ppx2, double d, double d1, double d2, float f, float f1, int stringColor) {
		renderLineFromToBlock(ppx.xCoord, ppx.yCoord, ppx.zCoord, ppx2.xCoord, ppx2.yCoord, ppx2.zCoord, stringColor);
	}
	
	public static void renderLineFromToBlockCenter(double x1, double y1, double z1, double x2, double y2, double z2, int stringColor) {
		renderLineFromToBlock(x1+0.5D, y1+0.5D, z1+0.5D, x2+0.5D, y2+0.5D, z2+0.5D, 0D, 0D, 0D, 0F, 0F, stringColor);
	}
	
	public static void renderLineFromToBlock(double x1, double y1, double z1, double x2, double y2, double z2, int stringColor) {
		renderLineFromToBlock(x1, y1, z1, x2, y2, z2, 0D, 0D, 0D, 0F, 0F, stringColor);
	}
	
	public static void renderLineFromToBlock(double x1, double y1, double z1, double x2, double y2, double z2, double d, double d1, double d2, float f, float f1, int stringColor) {
	    Tessellator tessellator = Tessellator.instance;
	    RenderManager rm = RenderManager.instance;
	    
	    float castProgress = 1.0F;
	
	    float f10 = 0F;
	    double d4 = MathHelper.sin(f10);
	    double d6 = MathHelper.cos(f10);
	
	    double pirateX = x1;
	    double pirateY = y1;
	    double pirateZ = z1;
	    double entX = x2;
	    double entY = y2;
	    double entZ = z2;
	
	    double fishX = castProgress*(entX - pirateX);
	    double fishY = castProgress*(entY - pirateY);
	    double fishZ = castProgress*(entZ - pirateZ);
	    GL11.glDisable(3553);
	    GL11.glDisable(2896);
	    tessellator.startDrawing(3);
	    //int stringColor = 0x888888;
	    //if (((EntityNode)entitypirate).render) {
	    	//stringColor = 0x880000;
	    //} else {
	    	//stringColor = 0xEF4034;
		//}
	    tessellator.setColorOpaque_I(stringColor);
	    int steps = 16;
	
	    for (int i = 0; i <= steps; ++i) {
	        float f4 = i/(float)steps;
	        tessellator.addVertex(
	            pirateX - rm.renderPosX + fishX * f4,//(f4 * f4 + f4) * 0.5D + 0.25D,
	            pirateY - rm.renderPosY + fishY * f4,//(f4 * f4 + f4) * 0.5D + 0.25D,
	            pirateZ - rm.renderPosZ + fishZ * f4);
	    }
	    
	    tessellator.draw();
	    GL11.glEnable(2896);
	    GL11.glEnable(3553);
	}*/
	
	@Override
	public void playerJoined(EntityPlayer player) {
		
	}
	
	@Override
	public void playSoundEffect(String sound, EntityPlayer player, float vol, float pitch) {
		ZCClientTicks.worldRef.playSoundAtEntity(player, ZombieCraftMod.modID + ":" + sound, vol, pitch);
	}
	
	@Override
	public void playSound(String sound, int x, int y, int z, float vol, float pitch) {
		ZCClientTicks.worldRef.playSoundEffect(x, y, z, ZombieCraftMod.modID + ":" + sound, vol, pitch);
	}
	
	@Override
	public void showEditToolMode() {
		ZCClientTicks.iMan.setShowEditToolMode();
	}
	
	@Override
	public void setLevelSize(int x1, int y1, int z1, int x2, int y2, int z2) {
		ZCClientTicks.iMan.setLevelSize(x1, y1, z1, x2, y2, z2);
	}
	
	@Override
	public void setModeMessage(String msg) {
		ZCClientTicks.iMan.modeMessage = msg;
	};
	
	@Override
	public boolean trySetTexturePack(String packFileName) {
		
		System.out.println("TEXTURE PACK SETTING DISABLED");
		/*if (packFileName.equals("default")) {
			mc.texturePackList.setTexturePack(new TexturePackDefault());
			mc.renderEngine.refreshTextures();
			return true;
		} else {
		
			List var3 = mc.texturePackList.availableTexturePacks();
			
			for (int i = 0; i < var3.size(); i++) {
				ITexturePack it = (ITexturePack)var3.get(i);
				
				if ((packFileName+".zip").equals(it.getTexturePackFileName())) {
					mc.texturePackList.setTexturePack(it);
					mc.renderEngine.refreshTextures();
					return true;
				}
			}
			return false;
		}*/
		return false;
	}
	
	@Override
	public String getSaveFolderName() {
		String name = "";
	
		try {
			//name = mc.getSaveLoader().getSaveList().
		} catch (Exception ex) {
			
		}
    	return "";
    }
	
	@Override
	public World getWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
	@Override
	public String getWorldSavePath() {
		return "";//"saves/" + this.getWorld().getWorldInfo().getWorldName() + "/";
	}
	
	
	
	@Override
	public boolean canEdit(EntityPlayer player) {
		return ZCGame.instance().mapMan.editMode;
	}
	
	@Override
	//wont get called if sp is master, player always unused if sp
	public void sendPacket(EntityPlayer player, int packetType, int[] dataInt) {
		ZCClientTicks.sendPacket(packetType, dataInt);
	}
	
	@Override
	public void sendPacket(EntityPlayer player, int packetType, int[] dataInt, String[] dataString) {
		ZCClientTicks.sendPacket(packetType, dataInt, dataString);
	}

	@Override
	public List<EntityPlayer> getPlayers(int dim) {
		// TODO Auto-generated method stub
		return null;
	}
}
