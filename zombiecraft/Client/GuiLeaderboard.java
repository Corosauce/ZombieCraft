package zombiecraft.Client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

public class GuiLeaderboard extends GuiScreen
{
    public int ySize;
    public int xSize;
    public int xOffset;
    
    public static int G_RESET = 0;
    public static int G_SAVE = 1;
    public static int G_MODNEXT = 2;
    public static int G_MODPREV = 3;
    public static int G_CLOSE = 9;
    
    public static int curIndex = 0;
    
    public GuiLeaderboard() {
    	super();
    	mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        //++this.updateCounter;
    }

    @Override
    public void drawScreen(int var1, int var2, float var3)
    {
        //this.drawDefaultBackground();
    	
    	int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        
        this.mc.renderEngine.bindTexture("/mods/ZombieCraft/textures/gui/gui256.png");
        this.drawTexturedModalRect(startX, startY/* + 1 * 18 + 17*/, 0, 0, 256, 256);
    	
        List<String> players = new ArrayList();
        List<Integer> scores = new ArrayList();
        
    	//this.drawString(this.fontRenderer, "Config for: " + getConfigData(getCategory()).configInstance.getCategory(), startX + 10, startY + 10, 16777215);
    	//this.drawString(this.fontRenderer, (curIndex+1) + "/" + ModConfig.liveEditConfigs.size(), startX + xSize - 52, startY + 10, 16777215);
    	
    	//this.textboxWorldName.drawTextBox();
        super.drawScreen(var1, var2, var3);
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    @Override
    public void initGui()
    {
    	super.initGui();
    	xSize = 372;
    	ySize = 250;
    	xSize = 248;
    	ySize = 166;
    	int startX = (this.width - this.xSize) / 2;
        int startY = (this.height - this.ySize) / 2;
        
        ScaledResolution var8 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int scaledWidth = var8.getScaledWidth();
        int scaledHeight = var8.getScaledHeight();
        
        System.out.println(height);
    	//this.mc.thePlayer.sendChatMessage("/config update " + getCategory());
    	
        int buttonWidth = 90;
        int buttonHeight = 20;
        int paddingSize = 8;
        int navWidth = 20;
        int navHeight = 20;
        
        this.buttonList.clear();
        
        this.buttonList.add(new GuiButton(G_CLOSE, startX + xSize - (buttonWidth + paddingSize) * 1, startY + ySize - buttonHeight - paddingSize, buttonWidth, buttonHeight, "Close"));
                
    }
    
    @Override
    protected void keyTyped(char par1, int par2)
    {
		super.keyTyped(par1, par2);
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
	}

    @Override
    protected void actionPerformed(GuiButton var1)
    {
        if (var1.id == G_RESET) {
        	
        } else if (var1.id == G_CLOSE) {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        initGui();
    }
    
    @Override
    public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F / 1F;
        float f1 = 0.00390625F / 1F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
        tessellator.draw();
    }
}
