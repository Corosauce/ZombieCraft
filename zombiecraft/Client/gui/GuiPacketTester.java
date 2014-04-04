package zombiecraft.Client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import zombiecraft.Core.Blocks.TileEntityPacketTester;
import zombiecraft.Forge.ZombieCraftMod;
import CoroUtil.packet.PacketHelper;

public class GuiPacketTester extends GuiContainer {

	TileEntityPacketTester tEnt;
	
	public static int B_BUILDSTART = 0;
	
	public ResourceLocation resGUI = new ResourceLocation(ZombieCraftMod.modID + ":textures/gui/gui512.png");
	
	public GuiPacketTester (InventoryPlayer inventoryPlayer,
			TileEntityPacketTester tileEntity) {
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerPacketTester(inventoryPlayer, tileEntity));
		tEnt = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
	    //draw text and stuff here
	    //the parameters for drawString are: string, x, y, color
		
		int xStart = 30;
		int yStart = 18;
		int ySize = 12;
		
		GL11.glPushMatrix();
		float scale = 2;
		GL11.glScalef(scale, scale, scale);
		scale = 0.5F;
		GL11.glScalef(scale, scale, scale);
	    fontRenderer.drawString("Factory", 8, 6, 4210752);
	    
	    fontRenderer.drawString("Start Build", xStart+18, yStart, 4210752);
	    /*fontRenderer.drawString("Charge Time: " + tEnt.shootTicksToCharge, xStart+18, yStart+ySize*1, 4210752);
	    fontRenderer.drawString("Cooldown: " + tEnt.shootTicksBetweenShots, xStart+18, yStart+ySize*2, 4210752);
	    fontRenderer.drawString("Range: " + tEnt.shootRange, xStart+18, yStart+ySize*3, 4210752);
	    fontRenderer.drawString("EU/shot: " + (tEnt.getCostForNextShot()), xStart-3, yStart+ySize*4-1, 4210752);*/
	    fontRenderer.drawString("Fuel/EU", xStart + 101, 6, 4210752);
	    //fontRenderer.drawString(String.valueOf((tEnt.powerEU / 1000)) + "k", xStart + 122, yStart+ySize*4+7, 4210752);
	    
	    //draws "Inventory" or your regional equivalent
	    fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, yStart+ySize*4+7, 4210752);
	    GL11.glPopMatrix();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
	            int par3) {
	    //draw your Gui here, only thing you need to change is the path
	    //int texture = mc.renderEngine.getTexture("/gui/trap.png");
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    //this.mc.renderEngine.bindTexture("/mods/ZombieCraft/textures/gui/gui512.png");
	    mc.getTextureManager().bindTexture(resGUI);
	    int x = (width - xSize) / 2;
	    int y = (height - ySize) / 2;
	    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	    
	    int euX = xSize;
	    int euY = 19;
	    int euWidth = 20;
	    int euHeightMax = 47;
	    //int euHeight = (int)((double)tEnt.powerEU / 10000D * euHeightMax / 100);//50;
	    //int euInverted = euHeightMax - euHeight;
	    
	    //this.drawTexturedModalRect(x + xSize - 24, y + 19 + euInverted, euX, euY + euInverted, euWidth, euHeight);
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
		ScaledResolution var8 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int scaledWidth = var8.getScaledWidth();
        int scaledHeight = var8.getScaledHeight();
        
        //System.out.println(scaledWidth);
        
        int xStart = scaledWidth / 2 - 80;
        int yStart = scaledHeight / 2 - 78;
        
        int btnWidth = 11;
        int btnHeight = 11;
        int padding = 1;
        
        this.buttonList.add(new GuiSmallButton(B_BUILDSTART, xStart+28, yStart+11, btnWidth, btnHeight, "x"));/*
        this.buttonList.add(new GuiSmallButton(B_CHARGEAMOUNT_DOWN, xStart+17, yStart+btnHeight+(btnHeight+padding)*1, btnWidth, btnHeight, "<"));
        this.buttonList.add(new GuiSmallButton(B_CHARGEAMOUNT_UP, xStart+28, yStart+btnHeight+(btnHeight+padding)*1, btnWidth, btnHeight, ">"));
        this.buttonList.add(new GuiSmallButton(B_COOLDOWNAMOUNT_DOWN, xStart+17, yStart+btnHeight+(btnHeight+padding)*2, btnWidth, btnHeight, "<"));
        this.buttonList.add(new GuiSmallButton(B_COOLDOWNAMOUNT_UP, xStart+28, yStart+btnHeight+(btnHeight+padding)*2, btnWidth, btnHeight, ">"));
        this.buttonList.add(new GuiSmallButton(B_RANGE_DOWN, xStart+17, yStart+btnHeight+(btnHeight+padding)*3, btnWidth, btnHeight, "<"));
        this.buttonList.add(new GuiSmallButton(B_RANGE_UP, xStart+28, yStart+btnHeight+(btnHeight+padding)*3, btnWidth, btnHeight, ">"));*/
    }
	
	@Override
	protected void actionPerformed(GuiButton var1)
    {
		int val = 0;
		NBTTagCompound nbt = new NBTTagCompound();
        if (var1.id == B_BUILDSTART) {
        	nbt.setBoolean("buildStart", true);
        }
        
        PacketHelper.sendClientPacket(PacketHelper.createPacketForTEntCommand(tEnt, nbt));
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
