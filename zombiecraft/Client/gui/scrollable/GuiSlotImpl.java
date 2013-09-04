package zombiecraft.Client.gui.scrollable;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSlotImpl extends GuiSlot
{
    //final GuiScrollableTest parentWorldGui;
	public IScrollingGUI guiRef;
    public List<IScrollingElement> elements;

    public GuiSlotImpl(IScrollingGUI parGuiRef, List<IScrollingElement> listRef, int par2, int par3, int par4, int par5, int par6)
    {
    	super(Minecraft.getMinecraft(), par2, par3, par4, par5, par6);
    	elements = listRef;
    	guiRef = parGuiRef;
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return elements.size();
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2)
    {
    	guiRef.onElementSelected(par1);
        boolean var3 = guiRef.getSelectedElement() >= 0 && guiRef.getSelectedElement() < this.getSize();
        guiRef.getSelectButton().enabled = var3;
        //GuiSelectZCMap.getRenameButton(this.parentWorldGui).enabled = var3;
        //GuiSelectZCMap.getDeleteButton(this.parentWorldGui).enabled = var3;
        //GuiSelectZCMap.func_82312_f(this.parentWorldGui).enabled = var3;

        //double click auto run
        if (par2 && var3)
        {
            //this.parentWorldGui.initZCWorld(par1, false);
        }
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return par1 == guiRef.getSelectedElement();
    }

    /**
     * return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return elements.size() * slotHeight;
    }

    protected void drawBackground()
    {
        this.guiRef.drawBackground();
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
	    IScrollingElement element = elements.get(i);
	    mc.fontRenderer.drawStringWithShadow(element.getTitle(), j + 2, k + 0, 0xffffff);
	    //mc.fontRenderer.drawStringWithShadow(element.getExtraInfo(), j + 2, k + 12, 0x808080);
	    //parentWorldGui.drawString(parentWorldGui.mc.fontRenderer, zombiesaverecord.getSubText2(), j + 2, k + 12 + 10, 0x808080);
    }
}
