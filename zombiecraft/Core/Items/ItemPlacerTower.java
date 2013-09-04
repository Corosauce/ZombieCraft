package zombiecraft.Core.Items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.Blocks.TileEntityTower;

public class ItemPlacerTower extends Item
{

    public ItemPlacerTower(int par1)
    {
        super(par1);
        this.maxStackSize = 64;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float f1, float f2, float f3)
    {
    	
    	if (par3World.isRemote) return true;
    	
        if (par7 != 1)
        {
            return false;
        }
        else
        {
            ++par5;
            Block var8;

            var8 = ZCBlocks.tower;
            if (!var8.canPlaceBlockAt(par3World, par4, par5, par6))
            {
                return false;
            }
            else
            {
            	//how will we translate itemdamage/meta to building names? damage num -> name mapping i guess, dont need nbt
            	
            	String towerName = "canon";
            	
                int var9 = MathHelper.floor_double((double)((par2EntityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                par3World.setBlock(par4, par5, par6, var8.blockID, var9, 2);
                TileEntity tEnt = par3World.getBlockTileEntity(par4, par5, par6);
    	    	if (tEnt instanceof TileEntityTower) {
    	    		((TileEntityTower) tEnt).setTowerAndMarkInitReady(towerName);
    	    	}
                --par1ItemStack.stackSize;
                return true;
            }
        }
    }
}
