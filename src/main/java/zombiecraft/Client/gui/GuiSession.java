package zombiecraft.Client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.EventHandlerPacket;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.client.GuiSlotImpl;
import CoroUtil.client.IScrollingElement;
import CoroUtil.client.IScrollingGUI;
import CoroUtil.packet.PacketHelper;
import CoroUtil.util.CoroUtilEntity;
import cpw.mods.fml.client.FMLClientHandler;

public class GuiSession extends GuiContainer implements IScrollingGUI {

	//TileEntitySession tEnt;
	
	//see paper notes for most of design, random extras here
	//game state: NONE/LOBBY/ACTIVE
	
	//dont forget, you semi have a map listing gui already, it scrolls...... copy modconfig gui or the zombiecraft level select gui?
	public int gameState = 0; //0 = inactive, 1 = lobby, 2 = active
	
	public int selectedWorld;
    public List<IScrollingElement> listElements = new ArrayList<IScrollingElement>();
    public GuiSlotImpl guiScrollable;
    public GuiButton guiSelectMap;
    
    public ResourceLocation resGUI = new ResourceLocation(ZombieCraftMod.modID + ":textures/gui/gui512.png");
	
    class SlotEntry implements IScrollingElement {

    	String title = "";
    	String info = "";
    	
    	public SlotEntry(String parName, String parInfo) {
    		title = parName;
    		info = parInfo;
    	}
    	
		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public String getExtraInfo() {
			return info;
		}

		@Override
		public List<String> getExtraInfo2() {
			return new ArrayList<String>();
		}
    	
    }
    
	public GuiSession (InventoryPlayer inventoryPlayer) {
		super(new ContainerSession(inventoryPlayer));
		//tEnt = tileEntity;
	}
	
	public boolean canUseMapSelect(String username) {
		//should return true if lobby leader or if voting
		if (username.equals(ZCGame.nbtInfoClientSession.getString("lobbyLeader"))) return true;
		return false;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		
		ScaledResolution var8 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scaledWidth = var8.getScaledWidth();
        int scaledHeight = var8.getScaledHeight();
		int xCenter = scaledWidth / 2;
        int yCenter = scaledHeight / 2;
        
        int xStart = xCenter - xSize/2;
        int yStart = yCenter - ySize/2;
        
        int xStartPadded = xStart + 8;
        int yStartPadded = yStart + 8 + 14;
	    int yTextSpacing = 12;
	    
	    int xSize = 140;
	    int ySize = 100;
	    int padding = 1;
	    int x1 = 364 - xSize;
	    int y1 = 8;
	    int x2 = x1+xSize;
	    int y2 = y1+ySize;
	    int strWidth = 0;
	    int leftRightDivide = 35;
	    
	    String strState = "";
	    ZCGame zcGame = ZCGame.instance();
	    
	    //gameState = 0;
	    
	    if (gameState == 0) {
	    	strState = "Game: Inactive";
	    } else if (gameState == 1) {
	    	strState = "In Lobby";
	    } else if (gameState == 2) {
	    	strState = "Game: " + "\u00A7" + '7' + "Active";
	    }
	    
	    drawRect(x1-padding, y1-padding, x2+padding, y2+padding, -6250336);
	    drawRect(x1, y1, x2, y2, -16777216);
	    if (gameState != 0) {
	    	
	    	String strTitle = "";
	    	if (gameState == 1) {
	    		strTitle = "Lobby Players";
		    } else if (gameState == 2) {
		    	strTitle = "Leaderboard";
		    }
	    	String strTitleName = "\u00A7" + "n" + "Name";
	    	String strTitleScore = "\u00A7" + "n" + "Score";
	    	strWidth = FMLClientHandler.instance().getClient().fontRenderer.getStringWidth(strTitle);
	    	FMLClientHandler.instance().getClient().fontRenderer.drawString("\u00A7" + "r" + strTitle, x1 + xSize/2 - strWidth/2, y1 + 6 + (yTextSpacing * 0), 0xFFFF00);
		    strWidth = FMLClientHandler.instance().getClient().fontRenderer.getStringWidth(strTitleName);
		    FMLClientHandler.instance().getClient().fontRenderer.drawString(strTitleName, x1 + xSize/2 - strWidth/2 - leftRightDivide, y1 + 6 + (yTextSpacing * 1), 0xFFFF00);
		    int strWidthScoreTitle = FMLClientHandler.instance().getClient().fontRenderer.getStringWidth(strTitleScore);
		    FMLClientHandler.instance().getClient().fontRenderer.drawString(strTitleScore, x1 + xSize/2 - strWidthScoreTitle/2 + leftRightDivide, y1 + 6 + (yTextSpacing * 1), 0xFFFF00);
		    int plCount = ZCGame.nbtInfoClientSession.getInteger("lobbyPlayerCount");
		    for (int i = 0; i < plCount; i++) {
		    	String str = ZCGame.nbtInfoClientSession.getString("lobbyPlayerName_" + i);
		    	FMLClientHandler.instance().getClient().fontRenderer.drawString(str.substring(0, Math.min(13, str.length())), x1 + 6, y1 + 6 + (yTextSpacing * (i+2)), 0xAAAAAA);
		    	String score = String.valueOf(ZCGame.nbtInfoClientSession.getInteger("lobbyPlayerScore_" + i));
		    	int scoreWidth = FMLClientHandler.instance().getClient().fontRenderer.getStringWidth(score);
		    	FMLClientHandler.instance().getClient().fontRenderer.drawString(score, x1 + xSize/2 - scoreWidth/2 + leftRightDivide, y1 + 6 + (yTextSpacing * (i+2)), 0xAAAAAA);
		    }
	    }
	    
	    int xSizePrev = xSize;
	    xSize = 120;
	    
	    x1 = 364 - xSizePrev - xSize - 8;
	    y1 = 8;
	    x2 = x1+xSize;
	    y2 = y1+ySize;
	    
	    
	    drawRect(x1-padding, y1-padding, x2+padding, y2+padding, -6250336);
	    drawRect(x1, y1, x2, y2, -16777216);
	    strWidth = FMLClientHandler.instance().getClient().fontRenderer.getStringWidth(strState);
	    FMLClientHandler.instance().getClient().fontRenderer.drawString("\u00A7" + "4" + strState, x1 + 6/*xSize/2 - strWidth/2*/, y1 + 6, 0xAAAAAA);
	    FMLClientHandler.instance().getClient().fontRenderer.drawString("\u00A7" + '4' + "Map: " + "\u00A7" + 'r' + zcGame.mapMan.curLevel, x1 + 6, y1 + 6 + (yTextSpacing * 1), 0xAAAAAA);
	    
	    String waveString = "\u00A7" + '4' + "Wave: " + "\u00A7" + '7' + zcGame.wMan.wave_Stage + "\u00A7" + "4" + " | Mobs: " + "\u00A7" + '7' + zcGame.wMan.wave_Invaders_Count;
    	strWidth = FMLClientHandler.instance().getClient().fontRenderer.getStringWidth(waveString);
    	if (gameState == 2) {
    		FMLClientHandler.instance().getClient().fontRenderer.drawString(waveString, x1 + 6/*xSize/2 - strWidth/2*/, y1 + 6 + (yTextSpacing * 2), 0xAAAAAA);
    	} else {
    		FMLClientHandler.instance().getClient().fontRenderer.drawString("\u00A7" + '4' + "Leader: " + "\u00A7" + 'r' + ZCGame.nbtInfoClientSession.getString("lobbyLeader"), x1 + 6, y1 + 6 + (yTextSpacing * 2), 0xAAAAAA);
    	}
    	
    	if (zcGame.mapMan.curBuildPercent > 0) {
    		FMLClientHandler.instance().getClient().fontRenderer.drawString("\u00A7" + '4' + "Map Gen % " + "\u00A7" + 'r' + (int)zcGame.mapMan.curBuildPercent, x1 + 6, y1 + 6 + (yTextSpacing * 3), 0xAAAAAA);
    	}
    	
    	if (canUseMapSelect(CoroUtilEntity.getName(mc.thePlayer))) {
    		FMLClientHandler.instance().getClient().fontRenderer.drawString("Map Commands", 12/*xSize/2 - strWidth/2*/, y1 + 6 + (yTextSpacing * 9), 0x000000);
    		FMLClientHandler.instance().getClient().fontRenderer.drawString("", 12/*xSize/2 - strWidth/2*/, y1 + 6 + (yTextSpacing * 9), 0x000000);
    	}
    	
    	FMLClientHandler.instance().getClient().fontRenderer.drawString("Steps to play:", 12 + 220, y1 + 6 + (yTextSpacing * 9), 0x000000);
    	FMLClientHandler.instance().getClient().fontRenderer.drawString("1. Click 'Setup Lobby'", 12 + 220, y1 + 6 + (yTextSpacing * 10), 0x000000);
    	FMLClientHandler.instance().getClient().fontRenderer.drawString("2. Select a map", 12 + 220, y1 + 6 + (yTextSpacing * 11), 0x000000);
    	FMLClientHandler.instance().getClient().fontRenderer.drawString("3. Click 'Set Map'", 12 + 220, y1 + 6 + (yTextSpacing * 12), 0x000000);
    	FMLClientHandler.instance().getClient().fontRenderer.drawString("4. Click 'Generate Map'", 12 + 220, y1 + 6 + (yTextSpacing * 13), 0x000000);
    	FMLClientHandler.instance().getClient().fontRenderer.drawString("5. Click 'Start Game' when", 12 + 220, y1 + 6 + (yTextSpacing * 14), 0x000000);
    	FMLClientHandler.instance().getClient().fontRenderer.drawString("all players are ready", 12 + 220, y1 + 6 + (yTextSpacing * 15), 0x000000);
    	
    	xSizePrev = xSize;
	    xSize = 120;
	    ySize = 120;
	    
	    x1 = 364 - xSizePrev - xSize - 8 - 20;
	    y1 = 116;
	    x2 = x1+xSize;
	    y2 = y1+ySize;
	    
	    if (canUseMapSelect(CoroUtilEntity.getName(mc.thePlayer))) {
		    drawRect(x1-padding, y1-padding, x2+padding, y2+padding, -6250336);
		    drawRect(x1, y1, x2, y2, -16777216);
	    }

        
        //listElements.clear();
        /*for (int i = 0; i < 20; i++) {
        	listElements.add(new SlotEntry("asdasdfsdfsdfsdas", "ssss"));
        }*/
	    
	    if (listElements.size() == 0) {
		    int mapCount = ZCGame.nbtInfoClientSession.getInteger("mapCount");
		    for (int i = 0; i < mapCount; i++) {
		    	String mapName = ZCGame.nbtInfoClientSession.getString("mapName_" + i);
		    	if (mapName.equals(ZCGame.instance().mapMan.curLevel)) {
		    		selectedWorld = i;
		    	}
		    	listElements.add(new SlotEntry(mapName, ""));
		    	//fontRenderer.drawString(mapName, x1 + 6/*xSize/2 - strWidth/2*/, y1 + 6 + (yTextSpacing * (i+0)), 0xAAAAAA);
		    }
	    }
	    
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
	            int par3) {
		
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    //this.mc.renderEngine.bindTexture("/mods/ZombieCraft/textures/gui/gui512.png");
	    mc.getTextureManager().bindTexture(resGUI);
	    int x = (width - xSize) / 2;
	    int y = (height - ySize) / 2;
	    this.drawTexturedModalRect(x, y, 0, 0, 512, 512);
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
    {
		super.drawScreen(par1, par2, par3);
		if (canUseMapSelect(CoroUtilEntity.getName(mc.thePlayer))) {
			this.guiScrollable.drawScreen(par1, par2, par3);
		}
        
        //TEMP!!!!!!
        //initGui();
    }
	
	@Override
    public void initGui()
    {

		xSize = 372;
    	ySize = 250;
		
		super.initGui();
		ScaledResolution var8 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scaledWidth = var8.getScaledWidth();
        int scaledHeight = var8.getScaledHeight();
        
        //System.out.println(scaledWidth);

        int xCenter = scaledWidth / 2;
        int yCenter = scaledHeight / 2;
        
        int xStart = xCenter - xSize/2;
        int yStart = yCenter - ySize/2;
        
        int xStartPadded = xStart + 8 - 1;
        int yStartPadded = yStart + 8 - 1;
        
        int btnWidth = 80;
        int btnHeight = 20;
        int padding = 1;
        int btnSpacing = 22;
        
        GuiButton button;
        this.buttonList.add(new GuiButton(EventHandlerPacket.CMD_GAMESETUPJOINLEAVE, xStartPadded, yStartPadded, btnWidth, btnHeight, "Setup Game")); //gets auto renamed depending on game state
        this.buttonList.add(button = new GuiButton(EventHandlerPacket.CMD_GAMESTART, xStartPadded, yStartPadded + (btnSpacing * 1), btnWidth, btnHeight, "Toggle Game")); //gets auto renamed depending on game state
        button.visible = false;
        this.buttonList.add(button = new GuiButton(EventHandlerPacket.CMD_TELEPORT, xStartPadded, yStartPadded + (int)(btnSpacing * 9.5F), btnWidth, btnHeight, "Dim. Teleport"));
        button.visible = false;
        this.buttonList.add(guiSelectMap = new GuiButton(EventHandlerPacket.CMD_MAPSETNAME, xStartPadded, yStartPadded + (btnSpacing * 6), btnWidth, btnHeight, "Set Map"));
        guiSelectMap.visible = false;
        this.buttonList.add(button = new GuiButton(EventHandlerPacket.CMD_MAPGENERATE, xStartPadded, yStartPadded + (btnSpacing * 7), btnWidth, btnHeight, "Generate Map"));
        button.visible = false;
        
        this.guiScrollable = new GuiSlotImpl(this, listElements, 180, this.height, 32, this.height - 32, 12);
        
        
        xStart += 96;
        yStart += 116;
        
        
        guiScrollable.slotSizeHalf = 40;
        int guiWidth = (/*guiScrollable.slotSizeHalf * 2*/40) + 80;
        guiScrollable.width = guiWidth;
        guiScrollable.height = 120;
        guiScrollable.top = yStart;
        guiScrollable.bottom = yStart + guiScrollable.height;
        //guiScrollable.slotHeight = 24;
        guiScrollable.left = xStart;
        guiScrollable.right = xStart + guiWidth;
        //guiScrollable.field_77242_t = 20;
        
        NBTTagCompound data = new NBTTagCompound();
        data.setBoolean("sync", true);
        ZombieCraftMod.eventChannel.sendToServer(PacketHelper.createPacketForNBTHandler("Session", ZombieCraftMod.eventChannelName, data));
        //PacketHelper.sendClientPacket(EventHandlerPacket.getSessionPacket(data));
    }
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		String username = "";
        if (mc.thePlayer != null) username = CoroUtilEntity.getName(mc.thePlayer);
		
		if (ZCGame.instance().gameActive) {
			gameState = 2;
		} else {
			if (ZCGame.nbtInfoClientSession.getBoolean("lobbyActive")) { //needs syncing
				gameState = 1;
			} else {
				gameState = 0;
			}
		}
		
        GuiButton guibutton = (GuiButton)this.buttonList.get(0);
        if (gameState == 0) {
        	guibutton.displayString = "Setup Lobby";
        } else if (gameState == 1) {
        	if (username.equals(ZCGame.nbtInfoClientSession.getString("lobbyLeader"))) {
        		guibutton.displayString = "Leave Lobby";
        	} else {
        		//needs check if they are in lobby
        		guibutton.displayString = "Join Lobby";
        	}
        } else if (gameState == 2) {
        	if (username.equals(ZCGame.nbtInfoClientSession.getString("lobbyLeader"))) {
        		guibutton.displayString = "Leave Lobby";
        	} else {
        		guibutton.displayString = "Join Game";
        	}
        	
        }
        
        
        GuiButton guibuttonStart = (GuiButton)this.buttonList.get(EventHandlerPacket.CMD_GAMESTART);
        GuiButton guibuttonSetMap = (GuiButton)this.buttonList.get(EventHandlerPacket.CMD_MAPSETNAME);
        GuiButton guibuttonGenerate = (GuiButton)this.buttonList.get(EventHandlerPacket.CMD_MAPGENERATE);
		if (username.equals(ZCGame.nbtInfoClientSession.getString("lobbyLeader"))) {
			guibuttonStart.visible = true;
			guibuttonSetMap.visible = true;
			guibuttonGenerate.visible = true;
			
			if (gameState == 2) {
				guibuttonStart.displayString = "Stop Game";
			} else {
				guibuttonStart.displayString = EnumChatFormatting.DARK_GREEN.toString() + "Start Game";
			}
		} else {
			guibuttonStart.visible = false;
			guibuttonSetMap.visible = false;
			guibuttonGenerate.visible = false;
		}
        GuiButton guibuttonTeleport = (GuiButton)this.buttonList.get(EventHandlerPacket.CMD_TELEPORT);
		if (mc.thePlayer.dimension == ZCGame.ZCDimensionID) {
			guibuttonTeleport.visible = true;
		} else {
			guibuttonTeleport.visible = false;
		}
        
	}
	
	@Override
	protected void actionPerformed(GuiButton var1)
    {
		String username = "";
        if (mc.thePlayer != null) username = CoroUtilEntity.getName(mc.thePlayer);
        
        NBTTagCompound nbt = new NBTTagCompound();
        //nbt.setString("username", username); //irrelevant, overriden server side for safety
        String mapName = "";
        if (listElements.size() > 0 && selectedWorld > -1) mapName = listElements.get(selectedWorld).getTitle();
        nbt.setString("mapName", mapName);
        nbt.setInteger("cmdID", var1.id);
        
        ZombieCraftMod.eventChannel.sendToServer(PacketHelper.createPacketForNBTHandler("Session", ZombieCraftMod.eventChannelName, nbt));
        //PacketHelper.sendClientPacket(EventHandlerPacket.getKeyDisplayString(nbt));
    }
	
	public int sanitize(int val) {
		return sanitize(val, 0, 9999);
	}
	
	public int sanitize(int val, int min, int max) {
		if (val > max) val = max;
        if (val < min) val = min;
		return val;
	}
    
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F / 2F;
        float f1 = 0.00390625F / 2F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
        tessellator.draw();
    }

	@Override
	public void onElementSelected(int par1) {
		selectedWorld = par1;
	}

	@Override
	public int getSelectedElement() {
		return selectedWorld;
	}

	@Override
	public GuiButton getSelectButton() {
		return guiSelectMap;
	}

	@Override
	public void drawBackground() {
		
	}

}
