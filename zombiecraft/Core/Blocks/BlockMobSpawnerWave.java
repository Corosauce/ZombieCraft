package zombiecraft.Core.Blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZombieCraftMod;

public class BlockMobSpawnerWave extends BlockContainer
{
    public BlockMobSpawnerWave(int par1)
    {
        super(par1, Material.circuits);
        
        setHardness(0.1F);
        setStepSound(Block.soundMetalFootstep);
        
        float var5 = 0.0625F;
        this.setBlockBounds(var5, 0.0F, var5, 1.0F - var5, 0.03125F, 1.0F - var5);
        
    }
    
    public Icon getIcon(int par1, int par2)
    {
        return Block.mobSpawner.getIcon(par1, par2);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y,
    		int z, EntityPlayer par5EntityPlayer, int par6, float par7,
    		float par8, float par9) {
    	
    	if (!world.isRemote && ZCGame.instance().canEdit(par5EntityPlayer)) {
    		//par5EntityPlayer.openGui(ZombieCraftMod.instance, -1, world, x, y, z);
    	}
    	
    	return super.onBlockActivated(world, x, y, z, par5EntityPlayer,
    			par6, par7, par8, par9);
    }

    /**
     * Returns the TileEntity used by this block.
     */
    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityMobSpawnerWave();
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
    
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getBoundingBox((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ);
    }
    
    @Override
    public boolean isCollidable() {
    	if (ZCGame.instance() != null && ZCGame.instance().mapMan != null && !ZCGame.instance().mapMan.editMode) return false;
    	return true;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
    	if (ZCGame.instance() != null && ZCGame.instance().mapMan != null && !ZCGame.instance().mapMan.editMode) return null;
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
    	
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	
    	this.setBlockBounds(0, 0.0F, 0, 1.0F, 1F, 1.0F);
    	
    	
        /*boolean var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1;
        float var6 = 0.3625F;

        if (var5)
        {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.53125F, 1.0F - var6);
        }
        else
        {
            this.setBlockBounds(var6, 0.0F, var6, 1.0F - var6, 0.5625F, 1.0F - var6);
        }*/
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public int getMobilityFlag()
    {
        return 1;
    }
}
