package zombiecraft.Forge;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zombiecraft.Core.ZCItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
    @Override
    public ItemStack getIconItemStack()
    {
        return new ItemStack(ZCItems.itemAk47);
    }
    
    @Override
	public Item getTabIconItem() {
		return getIconItemStack().getItem();
	}
}
