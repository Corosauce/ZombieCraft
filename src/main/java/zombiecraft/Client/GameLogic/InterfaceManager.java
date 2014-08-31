package zombiecraft.Client.GameLogic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import zombiecraft.Client.ZCGameSP;
import zombiecraft.Client.gui.GuiEditorCP;
import zombiecraft.Client.gui.GuiGameOverZC;
import zombiecraft.Core.Buyables;
import zombiecraft.Core.CommandTypes;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.EnumGameMode;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Camera.EnumCameraState;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Forge.RenderPlayerZC;
import zombiecraft.Forge.ZCClientTicks;
import zombiecraft.Forge.ZCKeybindHandler;
import zombiecraft.Forge.ZCServerTicks;
import CoroUtil.util.CoroUtilEntity;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class InterfaceManager {
	
	public ZCGameSP zcGame;
	public Minecraft mc;
	
	//public int buyMenuTimeout;
	public int toolModeTimeout;
	
	public int reBuyDelay = 0;
	
	//MP Use only? yes
	public int buyMenuState = 0;
	public boolean holdingUse = false;
	
	public boolean showZCOverlay = true;
	public int buyID;
	/*public boolean nearBarricade;
	public int barricadeX;
	public int barricadeY;
	public int barricadeZ;*/
	
	public long lastWorldTime = 0;
	
	public String modeMessage = "";
	
	//old code?
	public static final String difficulties[] = {
        "Peaceful", "Easy", "Normal", "Hard"
    };
	
	public boolean isRemote = false;
	
	public boolean usingGun = false;
	public KeyBinding keyBindAttackOrig;
	public KeyBinding keyBindUseItemOrig;
	
	//public boolean hasCharge = false;
	public int chargeLengthTicks = 30;
	public int chargeLengthCooldown = 20 * 30;
	public int chargeCooldown = 0;
	public int chargeTick = 0;
	
	public Render origPlayerRender;

    public float zoomState = 0;
	
	public InterfaceManager(Minecraft mc, ZCGame game) {
		this.mc = mc;
		zcGame = (ZCGameSP)game;
		
		//If client mode
		//System.out.println("REMOVAL OF CLIENT SIDE INIT CODE InterfaceManager construct");
		//if (!mc.isMultiplayerWorld()) {
			//zcGame.gameInit();
	    	
			//these were always off!
			//zcGame.mcInt = new MCInt(false);
	    	//zcGame.mcInt.initZCClient(mc);
    	//}
	}
	
	public int consoleOpenDelay = 0;
	public void keyEvent(KeyBinding key, boolean keyUp) {
		
		if (mc.currentScreen != null && !key.getKeyDescription().equals("ZC_Menu")) return;
		
		if (key.getKeyDescription().equals("ZC_Use")) {
    		
    		holdingUse = true;
    		if (keyUp) holdingUse = false;
    		
    	} else if (key.getKeyDescription().equals("ZC_Reload")) {
    		if (keyUp) {
	    		ItemStack is = mc.thePlayer.getCurrentEquippedItem();
	    		if (is != null && is.getItem() instanceof ItemGun) {
	    			ItemGun ig = (ItemGun)is.getItem();
	    			
	    			if (ig.reloadDelayClient <= 0 && ig.clipAmountClient != ig.magSize) {
	    				ig.setClipAmount(is, mc.thePlayer.worldObj, mc.thePlayer, ig.magSize);
	    				ig.setReloadDelay(is, mc.thePlayer.worldObj, mc.thePlayer, ig.reloadTime);
		    			//ig.clipAmountClient = ig.magSize;
		    			//ig.reloadDelayClient = ig.reloadTime;
		    			zcGame.sendPacket(mc.thePlayer, PacketTypes.COMMAND, new int[] {CommandTypes.RELOAD}, null);
	    			}
	    		}
    		}
    	} else if (key.getKeyDescription().equals("ZC_Zoom")) {
	    	if (!keyUp) {
		    	zoomState+=0.5F;
		    	System.out.println("zoomState: " + zoomState);
				if (zoomState > 2.5) {
					zoomState = 0;
				}
	    	}
	    }
		
		if (keyUp) return;
		
		if (key.getKeyDescription().equals("ZC_Menu")) {
			if (mc.currentScreen instanceof GuiEditorCP) {
				if (consoleOpenDelay == 0) {
					consoleOpenDelay = 5;
		    		this.mc.displayGuiScreen((GuiScreen)null);
		            this.mc.setIngameFocus();
		    	}
				//this.mc.displayGuiScreen((GuiScreen)null);
	            //this.mc.setIngameFocus();
			} else if (mc.currentScreen == null) {
				if (consoleOpenDelay == 0) {
					consoleOpenDelay = 5;
					FMLClientHandler.instance().getClient().displayGuiScreen(new GuiEditorCP());
					//TEMP SWAP!!!!
					//ModLoader.openGUI(mc.thePlayer, new GuiScrollableTest(null));
				}
				
			}
	    } else if (key.getKeyDescription().equals("ZC_Reload")) {
	    	if (ZCClientTicks.camMan.camState != EnumCameraState.OFF) {
	    		if ((!ZCGame.instance().gameActive || ZCGame.instance().wMan.wave_StartDelay > 0)) {
		    		mc.thePlayer.respawnPlayer();
		    		ZCClientTicks.camMan.disableCamera();
	                this.mc.displayGuiScreen((GuiScreen)null);
	    		}
	    	}
	    } else if (key.getKeyDescription().equals("ZC_Charge")) {
	    	if ((Integer)zcGame.getData(mc.thePlayer, DataTypes.hasCharge) == 1) {
	    		if (chargeCooldown == 0) {
	    			chargeCooldown = chargeLengthCooldown;
	    			chargeTick = chargeLengthTicks;
	    		} else {
	    			mc.thePlayer.setSprinting(true);
	    		}
			} else {
				mc.thePlayer.setSprinting(true);
			}
	    }
		
		
	}
	
	public void tick() {
		
		if (mc.thePlayer == null) return;
		
		//Game tick based stuff
		if (lastWorldTime != ZCClientTicks.worldRef.getWorldTime()) {
			lastWorldTime = ZCClientTicks.worldRef.getWorldTime();
			
			if (reBuyDelay > 0) reBuyDelay--;
			if (toolModeTimeout > 0) toolModeTimeout--;
			if (consoleOpenDelay > 0) consoleOpenDelay--;
			
			//System.out.println(reBuyDelay);
		}
		
		//this kinda needs to be done for other players too
		if (mc.thePlayer.worldObj.provider.dimensionId == ZCGame.ZCDimensionID) {
			if (!(RenderManager.instance.entityRenderMap.get(EntityOtherPlayerMP.class) instanceof RenderPlayerZC)) {
				origPlayerRender = (Render)RenderManager.instance.entityRenderMap.get(EntityPlayer.class);
				Render render = new RenderPlayerZC();
				render.setRenderManager(RenderManager.instance);
				RenderManager.instance.entityRenderMap.put(EntityOtherPlayerMP.class, render);
			}
		} else {
			if ((RenderManager.instance.entityRenderMap.get(EntityOtherPlayerMP.class) instanceof RenderPlayerZC)) {
				RenderManager.instance.entityRenderMap.put(EntityOtherPlayerMP.class, origPlayerRender);
			}
		}
		
		//Dynamic gun using control rebinds
		handleGunBinds();
		if (mc.currentScreen == null) {
			if (showZCOverlay) {
				showMainOverlay((mc.thePlayer.worldObj.provider.dimensionId != ZCGame.ZCDimensionID && !zcGame.mapMan.editMode));
			}
		}
		
		if (mc.thePlayer.worldObj.provider.dimensionId != ZCGame.ZCDimensionID && !zcGame.mapMan.editMode/* && !FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()*/) return;
		
		
		
		/*if (!mc.isMultiplayerWorld()) {
			zcGame.tick();
		}*/
		
		/*if ((Integer)zcGame.getData(mc.thePlayer, DataTypes.purchaseTimeout) > 0) {
			buyMenuTimeout--;
		}*/
		
		if (mc.currentScreen instanceof GuiGameOver) {
			
			mc.currentScreen = new GuiGameOverZC();
			//mc.currentScreen.guiParticles = new GuiParticle(mc);
			
			if (mc.currentScreen != null)
            {
                mc.setIngameNotInFocus();
                ScaledResolution var2 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                int var3 = var2.getScaledWidth();
                int var4 = var2.getScaledHeight();
                ((GuiScreen)mc.currentScreen).setWorldAndResolution(mc, var3, var4);
                mc.skipRenderWorld = false;
            }
            else
            {
                mc.setIngameFocus();
            }
		} else if (mc.currentScreen == null && this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed()) {
			/*mc.displayGuiScreen(new GuiLeaderboard());
			
			if (mc.currentScreen != null)
            {
                mc.setIngameNotInFocus();
            }*/
		}
		
		//Fix for weird double gui game over display
		if (mc.currentScreen instanceof GuiGameOverZC) {
			if (mc.thePlayer != null && mc.thePlayer.getHealth() > 1) {
				mc.currentScreen = null;
			}
		}
		
		if (mc.currentScreen == null) {
			showEditOverlays();
			if (toolModeTimeout > 0) showEditToolMode();
			/*if (showZCOverlay) {
				showMainOverlay();
			}*/
			
			if (zcGame.mapMan.editMode) {
				//zcGame.renderLevelSize();
			}
		}
		
		
		
		
		int timeout = (Integer)zcGame.getData(mc.thePlayer, DataTypes.purchaseTimeout);
		//System.out.println("reBuyDelay: " + reBuyDelay + " | " + "timeout: " + timeout + " | " + "buyMenuState: " + buyMenuState);
		
		if (reBuyDelay == 0) {
			if (timeout > 0) {
				showBuyOverlay();
			}
		}
		
		if (reBuyDelay == 0) {
			/*if (timeout > 0) {
				buyMenuState = 1;
			} else {
				buyMenuState = 0;
			}*/
			/*if (buyMenuState == 1 && Keyboard.isKeyDown(zcGame.key_Buy)) {
				buyMenuState = 0;
				
				ModLoaderMp.sendKey(ModLoaderMp.getModInstance(ClientTickHandler.class), zcGame.key_Buy);
				
			}*/
			
			
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Keyboard.isKeyDown(ZCKeybindHandler.consoleKey.getKeyCode())) {
			if (mc.currentScreen instanceof GuiEditorCP) {
				if (consoleOpenDelay == 0) {
					consoleOpenDelay = 5;
		    		this.mc.displayGuiScreen((GuiScreen)null);
		            this.mc.setIngameFocus();
		    	}
			}
		}
		
		
		//handleBarricadeProximity();
		
		if (holdingUse) {
			if (reBuyDelay == 0) {
				if ((Integer)zcGame.getData(mc.thePlayer, DataTypes.purchaseTimeout) > 0) {
					//buyMenuState = 0;
					zcGame.resetBuyState(mc.thePlayer, Buyables.barricadeRepairCooldown);
					ZCClientTicks.sendPacket(PacketTypes.COMMAND, new int[] {CommandTypes.USE});
					//resetBuyState(mc.thePlayer, Buyables.barricadeRepairCooldown);
					//ModLoaderMp.sendKey(ModLoaderMp.getModInstance(mod_ZombieCraft.class), CommandTypes.USE);
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isPaused() {
    	if (FMLClientHandler.instance().getClient().isGamePaused()) return true;
    	return false;
    }
	
	public void handleGunBinds() {
		//used to check !ingui
		if (!isPaused() && mc.currentScreen == null) {
			if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemGun) {
				if (!usingGun) {
					usingGun = true;
					//rebind
					keyBindAttackOrig = this.mc.gameSettings.keyBindAttack;
					keyBindUseItemOrig = this.mc.gameSettings.keyBindUseItem;
					this.mc.gameSettings.keyBindAttack = keyBindUseItemOrig;
					this.mc.gameSettings.keyBindUseItem = keyBindAttackOrig;
				}
				//set to false to make the item onUpdate undo this if item is still actually in hand (drop detection)
				//ZCUtil.lastTickGunInHand = false;
			} else {
				resetGunBinds();
			}
		} else {
			resetGunBinds();
		}
	}
	
	public void resetGunBinds() {
		if (usingGun) {
			usingGun = false;
			//un-rebind
			this.mc.gameSettings.keyBindAttack = keyBindAttackOrig;
			this.mc.gameSettings.keyBindUseItem = keyBindUseItemOrig;
		}
	}
	
	public void showMainOverlay(boolean justAmmo) {
		int color = 0xE52626;
		//String intMode = "";
		//if (isRemote) intMode = "SMP";
		String gameMode = "Open";
		if (zcGame.mapMan.gameMode == EnumGameMode.CLOSED) gameMode = "Closed";
		
		int yPos = 4;
		int yOffset = 14;
		
		ScaledResolution var8 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int width = var8.getScaledWidth();
        int height = var8.getScaledHeight();
		
        FontRenderer fr = FMLClientHandler.instance().getClient().fontRenderer;
        
        String title = "ZombieCraft " + "["+zcGame.mapMan.curLevel+"]";
        String waveAll = "Wave: "+String.valueOf(zcGame.wMan.wave_Stage);
        String waveLeft = "Wave:";
        String waveRight = String.valueOf(zcGame.wMan.wave_Stage);
        String mobs = "Mobs: "/*+String.valueOf(zcGame.wMan.wave_Invaders_Count)*/;
        String points = "Points";
        String pointsVal = String.valueOf(getZCPoints());
        
        String ammo = "";
        String ammoVal = "";
        
        ItemStack itemStack = mc.thePlayer.getCurrentEquippedItem();
        
        if (itemStack != null) {
        	Item item = itemStack.getItem();
        	if (item instanceof ItemGun) {
        		ItemGun ig = (ItemGun)item;
        		
        		int totalAmmo = ZCUtil.getAmmoData(mc.thePlayer, ig.ammoType.ordinal());
        		
        		int clips = totalAmmo / ig.magSize;
        		int roundsInClip = ig.clipAmountClient;//totalAmmo - (clips * ig.magSize);
        		
        		if (totalAmmo < roundsInClip) roundsInClip = totalAmmo;
        		
        		if (roundsInClip == -1) {
        			if (totalAmmo > 0) {
        				roundsInClip = ig.magSize;
        			} else {
        				roundsInClip = 0;
        			}
        		}
        		
        		if (roundsInClip == 0 && clips > 1) {
        			clips -= 1;
        			roundsInClip = ig.magSize;
        		}
        		
        		if (roundsInClip == ig.magSize) clips--;
        		
        		ammoVal = String.valueOf(roundsInClip + " | " + clips);
        		ammo = "Ammo";
        	}
        }
        
        float per = zcGame.mapMan.curBuildPercent;
        
        if (per != -1 || (FMLCommonHandler.instance().getMinecraftServerInstance() != null && FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer())) {
        	
        	if (ZCServerTicks.zcGame.zcLevel.buildJob != null && (FMLCommonHandler.instance().getMinecraftServerInstance() != null && FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) && ZCServerTicks.zcGame.zcLevel != null && ZCServerTicks.zcGame.zcLevel.buildData != null) {
        		per = ((float)ZCServerTicks.zcGame.zcLevel.buildJob.curTick + 1) / ((float)ZCServerTicks.zcGame.zcLevel.buildJob.maxTicks) * 100F;
        		per = (float)Math.floor(per * 100) / 100;
        		
        	}
        	
        	if (per > 0) {
	        	String str = "Level Build in Progress: " + per + "%";
	        	
	        	drawString(str, (width/2) - (fr.getStringWidth(str) / 2), 80, color);
        	}
        }
        
        
        if (!justAmmo) {
			drawString(title, width/2 - fr.getStringWidth(title)/2, yPos+(yOffset*0), color);
			drawString(waveLeft, width/2 - fr.getStringWidth(waveAll)/2 - 20, yPos+(yOffset*1), color);
			drawString(waveRight, width/2 + fr.getStringWidth(waveLeft)/2 - 24, yPos+(yOffset*1), 0xFFFFFF);
			drawString(mobs, width/2 - fr.getStringWidth(mobs+String.valueOf(zcGame.wMan.wave_Invaders_Count))/2 + 20, yPos+(yOffset*1), color);
			drawString(String.valueOf(zcGame.wMan.wave_Invaders_Count), width/2 - (fr.getStringWidth(mobs)/2) + 45, yPos+(yOffset*1), 0xFFFFFF);
        }
		
		if (zcGame.waitingToSpawn || ZCClientTicks.camMan.camState != EnumCameraState.OFF) {
			String mode = "";
			if (ZCClientTicks.camMan.camState == EnumCameraState.FOLLOW) {
				if (ZCClientTicks.camMan.spectateTarget != null && ZCClientTicks.camMan.spectateTarget instanceof EntityPlayer) {
					mode = "Following: " + CoroUtilEntity.getName(((EntityPlayer)ZCClientTicks.camMan.spectateTarget));
				}
				
			} else if (ZCClientTicks.camMan.camState == EnumCameraState.FREE) {
				mode = "Free Cam";
			}
			String msg1 = "Waiting to respawn";
			String msg2 = Keyboard.getKeyName(ZCKeybindHandler.cameraKey.getKeyCode()) + ": Cycle Mode";
			String msg3 = Keyboard.getKeyName(ZCKeybindHandler.chargeKey.getKeyCode()) + ": Cycle Player";
			if (zcGame.waitingToSpawn) {
				drawString(msg1, width/2 - fr.getStringWidth(msg1) / 2, height-70, color);
			}
			drawString(mode, width/2 - fr.getStringWidth(mode) / 2, yPos+(yOffset*4), color);
			
			drawString(msg2, 3, yPos+(yOffset*0), color);
			drawString(msg3, 3, yPos+(yOffset*1), color);
		}
		
		if (!justAmmo) {
			drawString(points, width/2 - 125, height-20, 0xFFFFFF);
			drawString(pointsVal, width/2 - 125 + (fr.getStringWidth(points) / 2) - (fr.getStringWidth(pointsVal) / 2), height-10, 0xFFFFFF);
		}
		
		drawString(ammo, width/2 + 95, height-20, 0xFFFFFF);
		drawString(ammoVal, width/2 + 95 + (fr.getStringWidth(ammo) / 2) - (fr.getStringWidth(ammoVal) / 2), height-10, 0xFFFFFF);
		
		/*drawString("ZombieCraft " + intMode + " - " + difficulties[ClientTickHandler.mc.gameSettings.difficulty] + " - " + zcGame.wMan.wave_StartDelay, 2, yPos+(yOffset*0), color);
		drawString("----------------", 2, yPos+(yOffset*1), color);
		drawString("Wave: "+String.valueOf(zcGame.wMan.wave_Stage)+" Mobs: "+String.valueOf(zcGame.wMan.wave_Invaders_Count)+" Points: "+String.valueOf(getZCPoints()), 2, yPos+(yOffset*2), color);
		drawString("ZombieKills: "+String.valueOf(zcGame.wMan.wave_Kills)+" MaxSpawns: "+String.valueOf(mc.theWorld.waveSpawnMax)+" KillsNeeded: "+String.valueOf(mc.theWorld.zombieWave*mc.theWorld.spawnerCount)+" SpawnCount: "+String.valueOf(mc.theWorld.waveSpawnCount), 2, yPos+(yOffset*3), color);*/
	}
	
	public void showEditOverlays() {
		int color = 0xE52626;		
		int yPos = 4;
		int yOffset = 14;
		ScaledResolution var8 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int width = var8.getScaledWidth();
        int height = var8.getScaledHeight();
        FontRenderer fr = FMLClientHandler.instance().getClient().fontRenderer;
		
		String editor = "IN MAP EDIT MODE";
		if (zcGame.mapMan.editMode)	drawString(editor, width/2 - fr.getStringWidth(editor)/2, yPos+(yOffset*2), color);
	}
	
	public void setShowEditToolMode() {
		toolModeTimeout = 5;
	}
	
	public void showEditToolMode() {
		int color = 0xE52626;		
		int yPos = 4;
		int yOffset = 14;
		ScaledResolution var8 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int width = var8.getScaledWidth();
        int height = var8.getScaledHeight();
        FontRenderer fr = FMLClientHandler.instance().getClient().fontRenderer;
		
		String editTool = "";
		if (zcGame.mapMan.editToolMode == 0) {
			editTool = "Link Break Block to Spawner";
		} else if (zcGame.mapMan.editToolMode == 1) {
			editTool = "Set Level Location & Size";
		} else if (zcGame.mapMan.editToolMode == 2) {
			editTool = "Set Player Spawn";
		}
		
		String editMsg = "Tool Mode: " + editTool; 
		
		drawString(editMsg, width/2 - fr.getStringWidth(editMsg)/2, yPos+(yOffset*3), color);
		
		if (!modeMessage.isEmpty()) {
			drawString(modeMessage, width/2 - fr.getStringWidth(modeMessage)/2, yPos+(yOffset*4), color);
		}
	}
	
	public int getZCPoints() {
		if (FMLClientHandler.instance().getClient().thePlayer == null) return -1;
		zcGame.check(FMLClientHandler.instance().getClient().thePlayer);
		return (Integer)zcGame.getData(FMLClientHandler.instance().getClient().thePlayer, DataTypes.zcPoints);
	}
	
	public void drawString(String str, int x, int y, int color) {
		//this.zcGame.mc.ingameGUI.drawString(zcGame.mc.fontRenderer, str, x, y, 0);
		FMLClientHandler.instance().getClient().fontRenderer.drawString(str, x, y, color);
	}
	
	public void showBuyOverlay() {
		
		String prefix = "Press <" + Keyboard.getKeyName(ZCKeybindHandler.useKey.getKeyCode()) + "> to ";
		
		//this also has a display timeout
		if (buyID >= 0) {
			ZCClientTicks.displayMessage(prefix + "purchase " + Buyables.getBuyItem(buyID).getDisplayName()/*.getItem().getItemDisplayName(Buyables.getBuyItem(buyID))*/ + " for " + Buyables.getBuyItemCost(buyID) + " points");
		} else if (buyID == -1) {
			ZCClientTicks.displayMessage(prefix + "break barrier for " + Buyables.barrierCost + " points");
		} else if (buyID == -2) {
			ZCClientTicks.displayMessage(prefix + "fix barricade");
		} else if (buyID == -3) {
			ZCClientTicks.displayMessage(prefix + "activate mystery box");
		} else {
			
		}
	}
	
	public boolean startGame() {
		return false;
	}
	
	public abstract void toggleEditMode();
	
	public abstract void toggleNoClip();
	
	public abstract void setStage(int wave, int reset);
	
	public abstract void setLevelSize(int x1, int y1, int z1, int x2, int y2, int z2);
	
	public void newCamPoint() {
		ZCClientTicks.sendPacket(PacketTypes.COMMAND, new int[] {CommandTypes.ADD_CAMPOINT});
	}
	
}
