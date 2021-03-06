package zombiecraft.Core.Blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import zombiecraft.Core.Buyables;
import zombiecraft.Forge.ZCServerTicks;
import CoroUtil.tile.ITileInteraction;

public class BlockMysteryBox extends BlockContainer
{
    public BlockMysteryBox()
    {
        super(Material.circuits);
        setBlockBounds(0.05F, 0F, 0.05F, 0.95F, 0.1F, 0.95F);
    }

    public int tickRate()
    {
        return 90;
    }

    public void updateTick(World var1, int var2, int var3, int var4, Random var5) {}

    @Override
    public TileEntity createNewTileEntity(World var1, int meta)
    {
        return new TileEntityMysteryBox();
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public boolean canCollideCheck(int par1, boolean par2) {
    	
    	return true;
    }
    
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3,
    		int par4, Entity par5Entity) {

    	if (!par1World.isRemote) {
	    	if (par5Entity instanceof EntityPlayer) {
	    		TileEntityMysteryBox pp = (TileEntityMysteryBox)par1World.getTileEntity(par2, par3, par4);
		    	
		    	if (pp != null) {
		    		if (pp.purchaseChanceTimeoutCur > 0) {
		    			ItemStack is = ((ItemStack)pp.tileHandler.getObject("renderItemStack"));
		    			if (is != null && Buyables.itemToIndex != null) {
		    				Integer obj = Buyables.itemToIndex.get(is);
		    				if (obj != null) {
		    					ZCServerTicks.zcGame.triggerBuyMenu(par5Entity, par2, par3, par4, (int)obj);
		    				}
		    			}
		    		} else {
		    			ZCServerTicks.zcGame.triggerBuyMenu(par5Entity, par2, par3, par4, -3);
		    		}
		    		//mod_ZombieCraft.zcGame.iMan.showMenuTimeout = 30;
		    		//mod_ZombieCraft.zcGame.iMan.buyString = "Purchase: " + name;
		    	}
	    	}
    	}
    	
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                    EntityPlayer player, int face, float localVecX, float localVecY, float localVecZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null || player.isSneaking()) {
        	return false;
        }
    		
		if (tileEntity instanceof ITileInteraction) {
        	((ITileInteraction) tileEntity).clickedRight(player, face, localVecX, localVecY, localVecZ);
        }
		
		return false;
    	
    }
    
    @Override
    public void onBlockClicked(World par1World, int x, int y, int z,
    		EntityPlayer par5EntityPlayer) {
    	TileEntity tileEntity = par1World.getTileEntity(x, y, z);
        if (tileEntity == null || par5EntityPlayer.isSneaking()) {
        	return;
        }
        if (tileEntity instanceof ITileInteraction) {
        	((ITileInteraction) tileEntity).clickedLeft();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z){
        Random rand = new Random();

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory)) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if (item != null && item.stackSize > 0) {
                float rx = rand.nextFloat() * 0.8F + 0.1F;
                float ry = rand.nextFloat() * 0.8F + 0.1F;
                float rz = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(world,
                    x + rx, y + ry, z + rz,
                    item.copy()/*new ItemStack(item, item.stackSize, item.getItemDamage()*/);

                if (item.hasTagCompound()) {
                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                }

                float factor = 0.05F;
                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entityItem);
                item.stackSize = 0;
            }
        }
    }
}
