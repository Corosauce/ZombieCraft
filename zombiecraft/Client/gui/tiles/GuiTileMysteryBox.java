package zombiecraft.Client.gui.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import zombiecraft.Client.gui.elements.GuiButtonBoolean;
import zombiecraft.Client.gui.elements.GuiTextFieldZC;
import zombiecraft.Core.Blocks.TileEntityMysteryBox;
import zombiecraft.Core.World.LevelConfig;
import CoroAI.tile.PacketHelper;

public class GuiTileMysteryBox extends GuiTileBase {

	TileEntityMysteryBox tEnt;
	
	public GuiTileMysteryBox (InventoryPlayer inventoryPlayer,
			TileEntityMysteryBox tileEntity) {
		super(new ContainerTileMysteryBox(inventoryPlayer, tileEntity));
		tEnt = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		super.drawGuiContainerForegroundLayer(param1, param2);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
		
		this.drawString(this.fontRenderer, "Mystery Box" + (guiCur.equals("main") ? "" : " - SubGUI: " + guiCur), xStart+7, yStart-9, 16777215);
		
		if (guiCur.equals("main")) {
			this.drawString(this.fontRenderer, "Custom Entity Spawning", xStart+7, yStart+6, 16777215);
			this.drawString(this.fontRenderer, "Spawn Rules", xStart+7, yStart+8+130, 16777215);
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
		if (guiCur.equals("main")) {
			textBoxesLookup.get(tEnt.nbtStrTimeoutPurchase).setText(tEnt.getData(tEnt.nbtStrTimeoutPurchase));
			textBoxesLookup.get(tEnt.nbtStrTimeoutRandomize).setText(tEnt.getData(tEnt.nbtStrTimeoutRandomize));
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
        int btnHeightAndPadding = 24;
        int padding = 1;
        int btnSpacing = 22;
        
        if (guiCur.equals("main")) {
        	addButton("close", new GuiSmallButton(CMD_CLOSE, xStart + xSize - guiPadding - btnWidth, yStart + ySize - guiPadding - btnHeight, btnWidth, btnHeight, "Save & Close"));
        } else {
        	addButton("close", new GuiSmallButton(CMD_CLOSE, xStart + xSize - guiPadding - btnWidth, yStart + ySize - guiPadding - btnHeight, btnWidth, btnHeight, "Back"));
        }
        
        if (guiCur.equals("main")) {
        	addTextBox(tEnt.nbtStrTimeoutPurchase, new GuiTextFieldZC(tEnt.nbtStrTimeoutPurchase, this.fontRenderer, xStartPadded + 55, yStartPadded+btnHeight-8, 220, 20));
        	addTextBox(tEnt.nbtStrTimeoutRandomize, new GuiTextFieldZC(tEnt.nbtStrTimeoutRandomize, this.fontRenderer, xStartPadded + 55, yStartPadded+btnHeight*2-8, 220, 20));
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
        
        String guiForPacket = guiCur;
        
        if (var1.id == CMD_CLOSE) {
        	sendPacket = true;
        	if (guiCur.equals("main")) {
        		mc.displayGuiScreen(null);
        	}/* else if (guiCur.equals(tEnt.CMD_GUIMODE_STR_PROXIMITY) || guiCur.equals(tEnt.CMD_GUIMODE_STR_WAVE) || guiCur.equals(tEnt.CMD_GUIMODE_STR_WATCH)) {
        		guiCur = guiPrev;
            }*/
        	initGui();
        }
        
        //plan:
        //send all thats needed for the subgui state
        //tile entity on server side will blindly set the values if player is creative mode etc
        
        if (sendPacket) {
        	int val = 0;
    		//String username = "";
            //if (mc.thePlayer != null) username = mc.thePlayer.username;
            //nbtSendCache.setString("username", username);
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
