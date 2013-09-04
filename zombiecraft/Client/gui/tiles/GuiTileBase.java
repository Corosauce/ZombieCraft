package zombiecraft.Client.gui.tiles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiTileBase extends GuiContainer {

	public ResourceLocation resGUI = new ResourceLocation("zombiecraft:textures/gui/gui512.png");
	public int CMD_CLOSE = 0;
	
	public int xCenter;
	public int yCenter;
	public int xStart;
	public int yStart;
	
	public GuiTileBase (ContainerTileBase parContainer) {
		super(parContainer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    mc.func_110434_K().func_110577_a(resGUI);
	    int x = (width - xSize) / 2;
	    int y = (height - ySize) / 2;
	    this.drawTexturedModalRect(x, y, 0, 0, 512, 512);
	}
	
	@Override
    public void initGui()
    {
		super.initGui();
	
		xSize = 372;
    	ySize = 250;
		ScaledResolution var8 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int scaledWidth = var8.getScaledWidth();
        int scaledHeight = var8.getScaledHeight();
        
        //System.out.println(scaledWidth);

        xCenter = scaledWidth / 2;
        yCenter = scaledHeight / 2;
        
        xStart = xCenter - xSize/2;
        yStart = yCenter - ySize/2;
        
        int xStartPadded = xStart + 8 - 1;
        int yStartPadded = yStart + 8 - 1;
        
        int btnWidth = 80;
        int btnHeight = 20;
        int padding = 1;
        int btnSpacing = 22;
        
        GuiButton button;
        this.buttonList.add(new GuiSmallButton(CMD_CLOSE, xStartPadded, yStartPadded, btnWidth, btnHeight, "Close"));
		
    }
	
	@Override
	protected void actionPerformed(GuiButton var1)
    {
		//since you cant override return, handling of close button should go in child class of this
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
	
	public int sanitize(int val) {
		return sanitize(val, 0, 9999);
	}
	
	public int sanitize(int val, int min, int max) {
		if (val > max) val = max;
        if (val < min) val = min;
		return val;
	}

}
