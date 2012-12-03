package zombiecraft.Forge;

import zombiecraft.Core.ZCItems;
import net.minecraft.src.*;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

final class CreativeTabZC extends CreativeTabs
{
    public CreativeTabZC(int par1, String par2Str)
    {
        super(par1, par2Str);
    }

    @SideOnly(Side.CLIENT)

    /**
     * the itemID for the item to be displayed on the tab
     */
    public int getTabIconItemIndex()
    {
        return ZCItems.itemAk47.shiftedIndex;
    }
}
