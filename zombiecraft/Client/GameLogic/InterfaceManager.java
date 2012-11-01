package zombiecraft.Client.GameLogic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;

import zombiecraft.Core.Buyables;
import zombiecraft.Core.CommandTypes;
import zombiecraft.Core.DataTypes;
import zombiecraft.Core.EnumGameMode;
import zombiecraft.Core.PacketTypes;
import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Camera.EnumCameraState;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Client.*;
import zombiecraft.Forge.ZCClientTicks;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

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
	
	public static final String difficulties[] = {
        "Peaceful", "Easy", "Normal", "Hard"
    };
	
	public boolean isRemote = false;
	
	public boolean usingGun = false;
	public KeyBinding keyBindAttackOrig;
	public KeyBinding keyBindUseItemOrig;
	
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
		
		if (key.keyDescription.equals("ZC_Use")) {
    		
    		holdingUse = true;
    		if (keyUp) holdingUse = false;
    		
    	}
		
		if (keyUp) return;
		
		if (key.keyDescription.equals("ZC_Menu")) {
			if (mc.currentScreen instanceof GuiEditorCP) {
				if (consoleOpenDelay == 0) {
					consoleOpenDelay = 5;
		    		this.mc.displayGuiScreen((GuiScreen)null);
		            this.mc.setIngameFocus();
		    	}
				//this.mc.displayGuiScreen((GuiScreen)null);
	            //this.mc.setIngameFocus();
			} else {
				if (consoleOpenDelay == 0) {
					consoleOpenDelay = 5;
					ModLoader.openGUI(mc.thePlayer, new GuiEditorCP());
				}
				
			}
	    } else if (key.keyDescription.equals("ZC_Reload")) {
	    	if (ZCClientTicks.camMan.camState != EnumCameraState.OFF && (!ZCGame.instance().gameActive || ZCGame.instance().wMan.wave_StartDelay > 0)) {
	    		mc.thePlayer.respawnPlayer();
	    		ZCClientTicks.camMan.disableCamera();
	    	}
	    }
		
	}
	
	public void tick() {
		
		if (mc.thePlayer == null) return;
		
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
                ScaledResolution var2 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
                int var3 = var2.getScaledWidth();
                int var4 = var2.getScaledHeight();
                ((GuiScreen)mc.currentScreen).setWorldAndResolution(mc, var3, var4);
                mc.skipRenderWorld = false;
            }
            else
            {
                mc.setIngameFocus();
            }
		}
		
		if (!ZCClientTicks.ingui) {
			showEditOverlays();
			if (toolModeTimeout > 0) showEditToolMode();
			if (showZCOverlay) {
				showMainOverlay();
			}
			
			if (zcGame.mapMan.editMode) {
				//zcGame.renderLevelSize();
			}
		}
		//Game tick based stuff
		if (lastWorldTime != ZCClientTicks.worldRef.getWorldTime()) {
			lastWorldTime = ZCClientTicks.worldRef.getWorldTime();
			
			if (reBuyDelay > 0) reBuyDelay--;
			if (toolModeTimeout > 0) toolModeTimeout--;
			if (consoleOpenDelay > 0) consoleOpenDelay--;
			
			//System.out.println(reBuyDelay);
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if (mc.currentScreen instanceof GuiEditorCP) {
				if (consoleOpenDelay == 0) {
					consoleOpenDelay = 5;
		    		this.mc.displayGuiScreen((GuiScreen)null);
		            this.mc.setIngameFocus();
		    	}
			}
		}
		
		//Dynamic gun using control rebinds
		handleGunBinds();
		//handleBarricadeProximity();
		
	}
	
	public void handleGunBinds() {
		//used to check !ingui
		if (!mc.isGamePaused && mc.currentScreen == null) {
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
	
	public void showMainOverlay() {
		int color = 0xE52626;
		//String intMode = "";
		//if (isRemote) intMode = "SMP";
		String gameMode = "Open";
		if (zcGame.mapMan.gameMode == EnumGameMode.CLOSED) gameMode = "Closed";
		
		int yPos = 4;
		int yOffset = 14;
		
		ScaledResolution var8 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int width = var8.getScaledWidth();
        int height = var8.getScaledHeight();
		
        FontRenderer fr = FMLClientHandler.instance().getClient().fontRenderer;
        
        String title = "ZombieCraft " + "["+gameMode+"]";
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
        		int roundsInClip = totalAmmo - (clips * ig.magSize);
        		
        		if (roundsInClip == 0 && clips > 1) {
        			clips -= 1;
        			roundsInClip = ig.magSize;
        		}
        		
        		ammoVal = String.valueOf(roundsInClip + " | " + clips);
        		ammo = "Ammo";
        	}
        }
        
        int per = zcGame.mapMan.curBuildPercent;
        
        if (per != -1) {
        	String str = "Level Build in Progress: " + per + "%";
        	
        	drawString(str, (width/2) - (fr.getStringWidth(str) / 2), 80, color);
        }
        
        
        
		drawString(title, width/2 - fr.getStringWidth(title)/2, yPos+(yOffset*0), color);
		drawString(waveLeft, width/2 - fr.getStringWidth(waveAll)/2 - 20, yPos+(yOffset*1), color);
		drawString(waveRight, width/2 + fr.getStringWidth(waveLeft)/2 - 24, yPos+(yOffset*1), 0xFFFFFF);
		drawString(mobs, width/2 - fr.getStringWidth(mobs+String.valueOf(zcGame.wMan.wave_Invaders_Count))/2 + 20, yPos+(yOffset*1), color);
		drawString(String.valueOf(zcGame.wMan.wave_Invaders_Count), width/2 - (fr.getStringWidth(mobs)/2) + 45, yPos+(yOffset*1), 0xFFFFFF);
		
		
		drawString(points, width/2 - 125, height-20, 0xFFFFFF);
		drawString(pointsVal, width/2 - 125 + (fr.getStringWidth(points) / 2) - (fr.getStringWidth(pointsVal) / 2), height-10, 0xFFFFFF);
		
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
		ScaledResolution var8 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
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
		ScaledResolution var8 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
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
		//this also has a display timeout
		if (buyID >= 0) {
			ZCClientTicks.displayMessage("Purchase " + Buyables.getBuyItem(buyID).getItem().getItemDisplayName(Buyables.getBuyItem(buyID)) + " for " + Buyables.getBuyItemCost(buyID) + " points");
		} else if (buyID == -1) {
			ZCClientTicks.displayMessage("Break barrier for " + Buyables.barrierCost + " points");
		} else if (buyID == -2) {
			ZCClientTicks.displayMessage("Fix barricade");
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
