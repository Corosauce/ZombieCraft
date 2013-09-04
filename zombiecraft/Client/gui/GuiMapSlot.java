package zombiecraft.Client.gui;

import java.util.Date;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.storage.SaveFormatComparator;
import zombiecraft.Core.ZombieSaveRecord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMapSlot extends GuiSlotZC
{
    final GuiSelectZCMap parentWorldGui;

    public GuiMapSlot(GuiSelectZCMap par1GuiSelectZCMap)
    {
        super(par1GuiSelectZCMap.mc, 180, par1GuiSelectZCMap.height, 32, par1GuiSelectZCMap.height - 32, 24);
        //ZCUtil.setPrivateValueBoth(GuiSlot.class, this, "left", "left", 120);
        //ZCUtil.setPrivateValueBoth(GuiSlot.class, this, "right", "right", par1GuiSelectZCMap.width-120);
        this.parentWorldGui = par1GuiSelectZCMap;
    }

    /**
     * Gets the size of the current slot list.
     */
    protected int getSize()
    {
        return GuiSelectZCMap.getList(this.parentWorldGui).size();
    }

    /**
     * the element in the slot that was clicked, boolean for wether it was double clicked or not
     */
    protected void elementClicked(int par1, boolean par2)
    {
    	GuiSelectZCMap.onElementSelected(this.parentWorldGui, par1);
        boolean var3 = GuiSelectZCMap.getSelectedWorld(this.parentWorldGui) >= 0 && GuiSelectZCMap.getSelectedWorld(this.parentWorldGui) < this.getSize();
        GuiSelectZCMap.getSelectButton(this.parentWorldGui).enabled = var3;
        //GuiSelectZCMap.getRenameButton(this.parentWorldGui).enabled = var3;
        //GuiSelectZCMap.getDeleteButton(this.parentWorldGui).enabled = var3;
        //GuiSelectZCMap.func_82312_f(this.parentWorldGui).enabled = var3;

        if (par2 && var3)
        {
            this.parentWorldGui.initZCWorld(par1, false);
        }
    }

    /**
     * returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int par1)
    {
        return par1 == GuiSelectZCMap.getSelectedWorld(this.parentWorldGui);
    }

    /**
     * return the height of the content being scrolled
     */
    protected int getContentHeight()
    {
        return GuiSelectZCMap.getList(this.parentWorldGui).size() * 24;
    }

    protected void drawBackground()
    {
        this.parentWorldGui.drawDefaultBackground();
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
	    ZombieSaveRecord zombiesaverecord = (ZombieSaveRecord)parentWorldGui.getList(parentWorldGui).get(i);
	    parentWorldGui.drawString(parentWorldGui.mc.fontRenderer, zombiesaverecord.getText(), j + 2, k + 1, 0xffffff);
	    parentWorldGui.drawString(parentWorldGui.mc.fontRenderer, zombiesaverecord.getSubText(), j + 2, k + 12, 0x808080);
	    //parentWorldGui.drawString(parentWorldGui.mc.fontRenderer, zombiesaverecord.getSubText2(), j + 2, k + 12 + 10, 0x808080);
    }
    
    protected void drawSlotOld(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
    {
        SaveFormatComparator var6 = (SaveFormatComparator)GuiSelectZCMap.getList(this.parentWorldGui).get(par1);
        String var7 = var6.getDisplayName();

        if (var7 == null || MathHelper.stringNullOrLengthZero(var7))
        {
            var7 = GuiSelectZCMap.func_82313_g(this.parentWorldGui) + " " + (par1 + 1);
        }

        String var8 = var6.getFileName();
        var8 = var8 + " (" + GuiSelectZCMap.func_82315_h(this.parentWorldGui).format(new Date(var6.getLastTimePlayed()));
        var8 = var8 + ")";
        String var9 = "";

        if (var6.requiresConversion())
        {
            var9 = GuiSelectZCMap.func_82311_i(this.parentWorldGui) + " " + var9;
        }
        else
        {
            var9 = GuiSelectZCMap.func_82314_j(this.parentWorldGui)[var6.getEnumGameType().getID()];

            if (var6.isHardcoreModeEnabled())
            {
                var9 = "\u00a74" + StatCollector.translateToLocal("gameMode.hardcore") + "\u00a7r";
            }

            if (var6.getCheatsEnabled())
            {
                var9 = var9 + ", " + StatCollector.translateToLocal("selectWorld.cheats");
            }
        }

        this.parentWorldGui.drawString(this.parentWorldGui.mc.fontRenderer, var7, par2 + 2, par3 + 1, 16777215);
        this.parentWorldGui.drawString(this.parentWorldGui.mc.fontRenderer, var8, par2 + 2, par3 + 12, 8421504);
        this.parentWorldGui.drawString(this.parentWorldGui.mc.fontRenderer, var9, par2 + 2, par3 + 12 + 10, 8421504);
    }
}
