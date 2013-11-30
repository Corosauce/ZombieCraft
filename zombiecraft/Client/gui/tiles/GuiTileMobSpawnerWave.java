package zombiecraft.Client.gui.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import zombiecraft.Client.gui.elements.GuiButtonBoolean;
import zombiecraft.Client.gui.elements.GuiTextFieldZC;
import zombiecraft.Core.Blocks.TileEntityMobSpawnerWave;
import CoroAI.packet.PacketHelper;

public class GuiTileMobSpawnerWave extends GuiTileBase {

	TileEntityMobSpawnerWave tEnt;
	
	public GuiTileMobSpawnerWave (InventoryPlayer inventoryPlayer,
			TileEntityMobSpawnerWave tileEntity) {
		super(new ContainerTileMobSpawnerWave(inventoryPlayer, tileEntity));
		tEnt = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		super.drawGuiContainerForegroundLayer(param1, param2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		this.drawString(this.fontRenderer, "ZC Mob Spawner" + (guiCur.equals("main") ? "" : " - SubGUI: " + guiCur), xStart+7, yStart-9, 16777215);
		
		int btnWidth = 80;
		int guiPadding = 8;
		int yDiv = 22;
		int yStart2 = yStart - 9;
		
		if (guiCur.equals("main")) {
			this.drawStringRightAligned(this.fontRenderer, "Custom Entity Spawning", xStart+xSize/3, yStart2+yDiv, 16777215);
			this.drawStringRightAligned(this.fontRenderer, "Custom Entity String", xStart+xSize/3, yStart2+yDiv*2, 16777215);
			
			this.drawStringRightAligned(this.fontRenderer, "Spawn Delay Ticks", xStart+xSize/3, yStart2+yDiv*3, 16777215);
			this.drawStringRightAligned(this.fontRenderer, "Added Random Ticks", xStart+xSize/3, yStart2+yDiv*4, 16777215);
			this.drawStringRightAligned(this.fontRenderer, "First Spawn Delayed", xStart+xSize/3, yStart2+yDiv*5, 16777215);
			
			this.drawString(this.fontRenderer, "Spawn Rules", xStart + xSize - guiPadding - btnWidth, yStart+8+130, 16777215);
		}
		if (guiCur.equals(tEnt.CMD_GUIMODE_STR_WATCH)) {
			this.drawString(this.fontRenderer, "X", xStart+42, yStart+36, 16777215);
			this.drawString(this.fontRenderer, "Y", xStart+42, yStart+60, 16777215);
			this.drawString(this.fontRenderer, "Z", xStart+42, yStart+84, 16777215);
		}
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		if (tEnt.nbtInfoClient.getBoolean("markUpdated")) {
			tEnt.nbtInfoClient.setBoolean("markUpdated", false);
			updateGuiElements();
		}
	}
	
	public void updateGuiElements() {
		NBTTagCompound tileData = tEnt.nbtInfoClient.getCompoundTag("tileData");
		if (guiCur.equals("main")) {
			textBoxesLookup.get(tEnt.nbtStrCustomSpawn).setText(tileData.getString(tEnt.nbtStrCustomSpawn));
			textBoxesLookup.get(tEnt.nbtStrSpawnDelay).setText(tileData.getString(tEnt.nbtStrSpawnDelay));
			textBoxesLookup.get(tEnt.nbtStrSpawnDelayRand).setText(tileData.getString(tEnt.nbtStrSpawnDelayRand));
			
			((GuiButtonBoolean)buttonsLookup.get(tEnt.CMD_BOOL_CUSTOMSPAWNING_STR)).setBoolean(tileData.getBoolean(tEnt.CMD_BOOL_CUSTOMSPAWNING_STR));
			((GuiButtonBoolean)buttonsLookup.get(tEnt.CMD_BOOL_USEDELAYFIRSTSPAWN_STR)).setBoolean(tileData.getBoolean(tEnt.CMD_BOOL_USEDELAYFIRSTSPAWN_STR));
		} else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_PROXIMITY)) {
			((GuiButtonBoolean)buttonsLookup.get(tEnt.CMD_BOOL_ACTPROX)).setBoolean(tileData.getBoolean(tEnt.CMD_BOOL_ACTPROX));
			textBoxesLookup.get(tEnt.nbtStrProxDist).setText(tileData.getString(tEnt.nbtStrProxDist));
		} else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_WATCH)) {
			((GuiButtonBoolean)buttonsLookup.get(tEnt.CMD_BOOL_ACTWATCH)).setBoolean(tileData.getBoolean(tEnt.CMD_BOOL_ACTWATCH));
			textBoxesLookup.get(tEnt.nbtStrWatchX).setText((tileData.getString(tEnt.nbtStrWatchX)));
			textBoxesLookup.get(tEnt.nbtStrWatchY).setText((tileData.getString(tEnt.nbtStrWatchY)));
			textBoxesLookup.get(tEnt.nbtStrWatchZ).setText((tileData.getString(tEnt.nbtStrWatchZ)));
		} else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_WAVE)) {
			((GuiButtonBoolean)buttonsLookup.get(tEnt.CMD_BOOL_ACTWAVE)).setBoolean(tileData.getBoolean(tEnt.CMD_BOOL_ACTWAVE));
			textBoxesLookup.get(tEnt.nbtStrWaveMin).setText((tileData.getString(tEnt.nbtStrWaveMin)));
			textBoxesLookup.get(tEnt.nbtStrWaveMax).setText((tileData.getString(tEnt.nbtStrWaveMax)));
		}
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
		resetGuiElements();

		int guiPadding = 8;
		
        int xStartPadded = xStart + guiPadding - 1;
        int yStartPadded = yStart + guiPadding - 1;
        
        int btnWidth = 80;
        int btnWidthBool = 50;
        int btnHeight = 20;
        int btnHeightAndPadding = 22;
        int padding = 1;
        int btnSpacing = 22;
        
        int rightStart = xSize/3;
        
        if (guiCur.equals("main")) {
        	addButton("close", new GuiSmallButton(CMD_CLOSE, xStart + xSize - guiPadding - btnWidth, yStart + ySize - guiPadding - btnHeight, btnWidth, btnHeight, "Save & Close"));
        } else {
        	addButton("close", new GuiSmallButton(CMD_CLOSE, xStart + xSize - guiPadding - btnWidth, yStart + ySize - guiPadding - btnHeight, btnWidth, btnHeight, "Back"));
        }
        
        if (guiCur.equals("main")) {
        
	        GuiButton button;
	        
	        //addButton("cancel", new GuiSmallButton(CMD_CANCEL, xStart + xSize - guiPadding*2 - btnWidth*2, yStart + ySize - guiPadding - btnHeight, btnWidth, btnHeight, "Cancel"));
	        
	        int yyy = 120;
	        
	        addButton("confProximity", new GuiButton(tEnt.CMD_CONF_PROXIMITY, xStart + xSize - guiPadding - btnWidth, yStartPadded+yyy+btnHeightAndPadding, btnWidth, btnHeight, "Conf. Proximity"));
	        addButton("confWave", new GuiButton(tEnt.CMD_CONF_WAVE, xStart + xSize - guiPadding - btnWidth, yStartPadded+yyy+btnHeightAndPadding*2, btnWidth, btnHeight, "Conf. Wave"));
	        addButton("confWatch", new GuiButton(tEnt.CMD_CONF_WATCH, xStart + xSize - guiPadding - btnWidth, yStartPadded+yyy+btnHeightAndPadding*3, btnWidth, btnHeight, "Conf. Watch"));
	        
	        
	        addButton(tEnt.CMD_BOOL_CUSTOMSPAWNING_STR, new GuiButtonBoolean(tEnt.CMD_BOOL_CUSTOMSPAWNING, xStartPadded + rightStart, yStartPadded, btnWidthBool, btnHeight, "Enabled", "Disabled"));
	        addTextBox(tEnt.nbtStrCustomSpawn, new GuiTextFieldZC(tEnt.nbtStrCustomSpawn, this.fontRenderer, xStartPadded + rightStart, yStartPadded+btnHeightAndPadding*1, 180, 20));
	        addTextBox(tEnt.nbtStrSpawnDelay, new GuiTextFieldZC(tEnt.nbtStrSpawnDelay, this.fontRenderer, xStartPadded + rightStart, yStartPadded+btnHeightAndPadding*2, 30, 20));
	        addTextBox(tEnt.nbtStrSpawnDelayRand, new GuiTextFieldZC(tEnt.nbtStrSpawnDelayRand, this.fontRenderer, xStartPadded + rightStart, yStartPadded+btnHeightAndPadding*3, 30, 20));
	        
	        addButton(tEnt.CMD_BOOL_USEDELAYFIRSTSPAWN_STR, new GuiButtonBoolean(tEnt.CMD_BOOL_USEDELAYFIRSTSPAWN, xStartPadded + rightStart, yStartPadded+btnHeightAndPadding*4, btnWidthBool, btnHeight, "Enabled", "Disabled"));
	        /*NBTTagCompound data = new NBTTagCompound();
	        data.setBoolean("sync", true);
	        PacketHelper.sendClientPacket(PacketHelper.createPacketForTEntCommand(tEnt, data));*/
        } else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_PROXIMITY)) {
        	addButton(tEnt.CMD_BOOL_ACTPROX, new GuiButtonBoolean(tEnt.CMD_BOOL_PROXIMITY, xStartPadded, yStartPadded, btnWidthBool, btnHeight, "Enabled", "Disabled"));
        	addTextBox(tEnt.nbtStrProxDist, new GuiTextFieldZC(tEnt.nbtStrProxDist, this.fontRenderer, xStartPadded, yStartPadded+btnHeightAndPadding*1, 30, 20));
        	//set no text fields in this method, set it in the updater like session gui does, waiting on a packet to come in
            //this.textboxWorldName.setFocused(true);
        	//String initData = "";
        	//initData = String.valueOf(tEnt.nbtInfoClient.getInteger("proximity_dist"));
            //gtf.setText(initData);
        } else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_WATCH)) {
        	addButton(tEnt.CMD_BOOL_ACTWATCH, new GuiButtonBoolean(tEnt.CMD_BOOL_WATCH, xStartPadded, yStartPadded, btnWidthBool, btnHeight, "Enabled", "Disabled"));
        	addTextBox(tEnt.nbtStrWatchX, new GuiTextFieldZC(tEnt.nbtStrWatchX, this.fontRenderer, xStartPadded, yStartPadded+btnHeightAndPadding*1, 30, 20));
        	addTextBox(tEnt.nbtStrWatchY, new GuiTextFieldZC(tEnt.nbtStrWatchY, this.fontRenderer, xStartPadded, yStartPadded+btnHeightAndPadding*2, 30, 20));
        	addTextBox(tEnt.nbtStrWatchZ, new GuiTextFieldZC(tEnt.nbtStrWatchZ, this.fontRenderer, xStartPadded, yStartPadded+btnHeightAndPadding*3, 30, 20));
        } else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_WAVE)) {
        	addButton(tEnt.CMD_BOOL_ACTWAVE, new GuiButtonBoolean(tEnt.CMD_BOOL_WAVE, xStartPadded, yStartPadded, btnWidthBool, btnHeight, "Enabled", "Disabled"));
        	addTextBox(tEnt.nbtStrWaveMin, new GuiTextFieldZC(tEnt.nbtStrWaveMin, this.fontRenderer, xStartPadded, yStartPadded+btnHeightAndPadding*1, 30, 20));
        	addTextBox(tEnt.nbtStrWaveMax, new GuiTextFieldZC(tEnt.nbtStrWaveMax, this.fontRenderer, xStartPadded, yStartPadded+btnHeightAndPadding*2, 30, 20));
        }
        
        NBTTagCompound data = new NBTTagCompound();
        data.setBoolean("sync", true);
        PacketHelper.sendClientPacket(PacketHelper.createPacketForTEntCommand(tEnt, data));
        updateGuiElements();
    }
	
	@Override
	protected void actionPerformed(GuiButton var1)
    {
		
		boolean sendPacket = false;
     
        if (var1 instanceof GuiButtonBoolean) {
        	((GuiButtonBoolean) var1).setBooleanToggle();
        	nbtSendCache.getCompoundTag("tileData").setBoolean(buttonsLookupInt.get(var1.id), ((GuiButtonBoolean) var1).getBoolean());
        }

        //this differing not needed here, since all ids are unique regardless of subguis
        if (guiCur.equals("main")) {
        	
        } else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_PROXIMITY)) {
        	
        } else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_WAVE)) {
        	
        }
        
        if (var1.id == tEnt.CMD_CONF_PROXIMITY || var1.id == tEnt.CMD_CONF_WAVE || var1.id == tEnt.CMD_CONF_WATCH) {
        	//sendPacket = false;
        	guiPrev = guiCur;
        	if (var1.id == tEnt.CMD_CONF_PROXIMITY) {
        		guiCur = tEnt.CMD_GUIMODE_STR_PROXIMITY;
        	} else if (var1.id == tEnt.CMD_CONF_WAVE) {
        		guiCur = tEnt.CMD_GUIMODE_STR_WAVE;
        	} else if (var1.id == tEnt.CMD_CONF_WATCH) {
        		guiCur = tEnt.CMD_GUIMODE_STR_WATCH;
        	}
        	initGui();
        } else {
        	
        }
        
        String guiForPacket = guiCur;
        
        if (var1.id == CMD_CLOSE) {
        	sendPacket = true;
        	if (guiCur.equals("main")) {
        		//mc.displayGuiScreen(null);
        		mc.thePlayer.closeScreen();
        	} else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_PROXIMITY) || guiCur.equals(tEnt.CMD_GUIMODE_STR_WAVE) || guiCur.equals(tEnt.CMD_GUIMODE_STR_WATCH)) {
        		guiCur = guiPrev;
        		initGui();
            }
        	
        }
        
        //plan:
        //send all thats needed for the subgui state
        //tile entity on server side will blindly set the values if player is creative mode etc
        
        if (sendPacket) {
        	int val = 0;
    		String username = "";
            if (mc.thePlayer != null) username = mc.thePlayer.username;
            nbtSendCache.setString("username", username);
            nbtSendCache.setInteger("cmdID", var1.id);
            nbtSendCache.setString("guiCur", guiForPacket);
        	PacketHelper.sendClientPacket(PacketHelper.createPacketForTEntCommand(tEnt, nbtSendCache));
        }
        
        
    }
	
	public int sanitize(int val) {
		return sanitize(val, 0, 9999);
	}
	
	public int sanitize(int val, int min, int max) {
		if (val > max) val = max;
        if (val < min) val = min;
		return val;
	}

}
