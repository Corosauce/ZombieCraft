package zombiecraft.Client.gui.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import zombiecraft.Client.gui.elements.GuiTextFieldZC;

public class GuiTileBase extends GuiContainer {

	public ResourceLocation resGUI = new ResourceLocation("zombiecraft:textures/gui/gui512.png");
	public int CMD_CLOSE = 0;
	public int CMD_CANCEL = 1;
	
	public int xCenter;
	public int yCenter;
	public int xStart;
	public int yStart;

	//shall we do gui states for configuring details on each activation method? might be easier to manage visual space
	
	//only closing main gui will it save, others shouldnt matter
	public String guiCur = "main";
	public String guiPrev = "";
	
	//Elements
	public List<GuiTextFieldZC> textBoxes = new ArrayList<GuiTextFieldZC>();
	public HashMap<String, GuiTextFieldZC> textBoxesLookup = new HashMap<String, GuiTextFieldZC>();
	public HashMap<String, GuiButton> buttonsLookup = new HashMap<String, GuiButton>();
	public HashMap<Integer, String> buttonsLookupInt = new HashMap<Integer, String>();
	//public GuiTextField textboxWorldName;
	public NBTTagCompound nbtSendCache = new NBTTagCompound();
	
	public GuiTileBase (ContainerTileBase parContainer) {
		super(parContainer);
		nbtSendCache.setCompoundTag("tileData", new NBTTagCompound());
	}
	
	public void addButton(String lookupName, GuiButton btn) {
		buttonsLookup.put(lookupName, btn);
		buttonsLookupInt.put(btn.id, lookupName);
		buttonList.add(btn);
	}
	
	public void addTextBox(String lookupName, GuiTextFieldZC textBox) {
		textBoxes.add(textBox);
		textBoxesLookup.put(lookupName, textBox);
	}
	
	public void resetGuiElements() {
		buttonList.clear();
		buttonsLookup.clear();
		buttonsLookupInt.clear();
		textBoxes.clear();
		textBoxesLookup.clear();
	}
	
	public void drawStringRightAligned(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5)
    {
		int width = par1FontRenderer.getStringWidth(par2Str);
        par1FontRenderer.drawStringWithShadow(par2Str, par3-width, par4, par5);
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int param1, int param2) {
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    mc.getTextureManager().bindTexture(resGUI);
	    int x = (width - xSize) / 2;
	    int y = (height - ySize) / 2;
	    this.drawTexturedModalRect(x, y, 0, 0, 512, 512);

		for (int i = 0; i < textBoxes.size(); i++) {
			textBoxes.get(i).drawTextBox();
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
		
		for (int i = 0; i < textBoxes.size(); i++) {
			GuiTextFieldZC gtf = textBoxes.get(i);
			if (gtf.isFocused()) {
				gtf.textboxKeyTyped(par1, par2);
				String newVal = gtf.getText();
				//SET YOUR NBT STUFF HERE!!!! DATA IS LOST ONCE THEY HIT BACK!
				nbtSendCache.getCompoundTag("tileData").setString(gtf.name, newVal);
			}
		}
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		
		for (int i = 0; i < textBoxes.size(); i++) {
			textBoxes.get(i).mouseClicked(par1, par2, par3);
		}
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		for (int i = 0; i < textBoxes.size(); i++) {
			textBoxes.get(i).updateCursorCounter();
		}
	}
	
	@Override
    public void initGui()
    {
		
	
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
		
        //this must be last as it depends on the xSize ySize values
        super.initGui();
    }
	
	@Override
	protected void actionPerformed(GuiButton var1)
    {
		//since you cant override return, handling of close button should go in child class of this
    }
	
	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6, int texSize) {
		float f = 1F / (float)texSize;//0.00390625F / 2F;
        float f1 = f;//0.00390625F / 2F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
        tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
        tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
        tessellator.draw();
	}
	
	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
		drawTexturedModalRect(par1, par2, par3, par4, par5, par6, 512);
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
