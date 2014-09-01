package zombiecraft.Core.Blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZCServerTicks;

public class BlockPurchasePlate extends BlockContainer
{
    /** The mob type that can trigger this pressure plate. */
    //private EnumMobType triggerMobType;

    public BlockPurchasePlate(/*EnumMobType par3EnumMobType, */Material par4Material)
    {
        super(par4Material);
        //this.triggerMobType = par3EnumMobType;
        this.setTickRandomly(true);
        float var5 = 0.0625F;
        this.setBlockBounds(var5, 0.0F, var5, 1.0F - var5, 0.03125F, 1.0F - var5);
        
        setHardness(0.1F);
        //setStepSound(Block.soundWoodFootstep);
    }
    
    public IIcon getIcon(int par1, int par2)
    {
        return Blocks.mob_spawner.getIcon(par1, par2);
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World world)
    {
        return 20;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_) || BlockFence.func_149825_a(p_149742_1_.getBlock(p_149742_2_, p_149742_3_ - 1, p_149742_4_));
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    //public void onBlockAdded(World par1World, int par2, int par3, int par4) {}

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        boolean var6 = false;

        if (!World.doesBlockHaveSolidTopSurface(par1World, par2, par3 - 1, par4) || BlockFence.func_149825_a(par1World.getBlock(par2, par3 - 1, par4)))
        {
            var6 = true;
        }

        if (var6)
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlock(par2, par3, par4, Blocks.air);
        }
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    	if (par1World.isRemote) return true;
    	if (!ZCGame.instance().canEdit(par5EntityPlayer)) return true;
    	
    	TileEntityPurchasePlate pp = (TileEntityPurchasePlate)par1World.getTileEntity(par2, par3, par4);
    	
    	if (pp != null) {
    		
    		pp.onClicked();
    		
    	}
    	
    	return true;
    }
    
    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
    	
    	if (world.isRemote) return;
    	if (!ZCGame.instance().canEdit(entityplayer)) return;
    	
    	TileEntityPurchasePlate pp = (TileEntityPurchasePlate)world.getTileEntity(i, j, k);
    	
    	if (pp != null) {
    		
    		pp.onClicked();
    		
    	}
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
        	System.out.println("disabled setStateIfMobInteractsWithPlate in PurchasePlate, do we need it?");
            /*if (par1World.getBlockMetadata(par2, par3, par4) != 0)
            {
                this.setStateIfMobInteractsWithPlate(par1World, par2, par3, par4);
            }*/
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityPurchasePlate();
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
    	if (!par1World.isRemote) {
	    	if (par5Entity instanceof EntityPlayer) {
		    	TileEntityPurchasePlate pp = (TileEntityPurchasePlate)par1World.getTileEntity(par2, par3, par4);
		    	
		    	if (pp != null) {
		    		
		    		
		    		ZCServerTicks.zcGame.triggerBuyMenu(par5Entity, par2, par3, par4, pp.itemIndex);
		    		//mod_ZombieCraft.zcGame.iMan.showMenuTimeout = 30;
		    		//mod_ZombieCraft.zcGame.iMan.buyString = "Purchase: " + name;
		    	}
	    	}
    	}
    	
        /*if (!par1World.isRemote)
        {
            if (par1World.getBlockMetadata(par2, par3, par4) != 1)
            {
                this.setStateIfMobInteractsWithPlate(par1World, par2, par3, par4);
            }
        }*/
    }

    /**
     * Called whenever the block is removed.
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block hmm, int hmm2)
    {
        int var5 = par1World.getBlockMetadata(par2, par3, par4);

        if (var5 > 0)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this);
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this);
        }

        super.breakBlock(par1World, par2, par3, par4, hmm, hmm2);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        boolean var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1;
        float var6 = 0.0625F;

        if (var5)
        {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.03125F, 1.0F - var6);
        }
        else
        {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.0625F, 1.0F - var6);
        }
    }

    /**
     * Is this block powering the block on the specified side
     */
    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return par1IBlockAccess.getBlockMetadata(par2, par3, par4) > 0 ? 15 : 0;
    }

    /**
     * Is this block indirectly powering the block on the specified side
     */
    @Override
    public int isProvidingWeakPower(IBlockAccess par1World, int par2, int par3, int par4, int par5)
    {
        return par1World.getBlockMetadata(par2, par3, par4) == 0 ? 0 : /*par5 == 1*/15;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.5F;
        float var2 = 0.125F;
        float var3 = 0.5F;
        this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    @Override
    public int getMobilityFlag()
    {
        return 1;
    }
}
