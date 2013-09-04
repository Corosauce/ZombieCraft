package zombiecraft.Client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import zombiecraft.Forge.ZombieCraftMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonZC extends GuiButton
{
    /** Button width in pixels */
    //protected int width;

    /** Button height in pixels */
    //protected int height;

    /** The x position of this control. */
    //public int xPosition;

    /** The y position of this control. */
    //public int yPosition;

    /** The string displayed on this control. */
    //public String displayString;

    /** ID for this control. */
    //public int id;

    /** True if this control is enabled, false to disable. */
    //public boolean enabled;

    /** Hides the button completely if false. */
    //public boolean drawButton;
    
    public int texID = 0;
    
    public static final ResourceLocation resGUI = new ResourceLocation(ZombieCraftMod.modID + ":textures/gui/zceditgui.png");

    public GuiButtonZC(int par1, int par2, int par3, int texID)
    {
        this(par1, par2, par3, 20, 20, texID);
    }

    public GuiButtonZC(int par1, int par2, int par3, int par4, int par5, int texID)
    {
    	super(par1, par2, par3, par4, par5, "");
        this.width = 20;
        this.height = 20;
        this.enabled = false;
        this.drawButton = true;
        this.id = par1;
        this.xPosition = par2;
        this.yPosition = par3;
        this.width = par4;
        this.height = par5;
        this.displayString = "";
        this.texID = texID;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean par1)
    {
        byte var2 = 1;

        if (par1)
        {
            var2 = 2;
        }
        else if (!this.enabled)
        {
            var2 = 0;
        }

        return var2;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            FontRenderer var4 = par1Minecraft.fontRenderer;
            Minecraft.getMinecraft().func_110434_K().func_110577_a(resGUI);
            //GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture("/mods/ZombieCraft/textures/menus/zceditgui.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var5 = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            int var6 = this.getHoverState(var5);
            
            
            this.drawTexturedModalRect(this.xPosition, this.yPosition, texID * 20, 0 + var6 * 20, this.width, this.height);
            
            //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0 + var6 * 20, this.width / 2, this.height);
            //this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var6 * 20, this.width / 2, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);
            int var7 = 14737632;

            if (!this.enabled)
            {
                var7 = -6250336;
            }
            else if (var5)
            {
                var7 = 16777120;
            }

            this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var7);
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {}

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int par1, int par2) {}

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        return /*this.enabled && */this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }
}
