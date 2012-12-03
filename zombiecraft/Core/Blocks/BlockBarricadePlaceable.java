package zombiecraft.Core.Blocks;

import java.util.List;
import java.util.Random;

import zombiecraft.Core.ZCBlocks;
import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.Entities.BaseEntAI_Ally;
import zombiecraft.Core.GameLogic.ZCGame;
import net.minecraft.src.*;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class BlockBarricadePlaceable extends Block
{
	
	int stateToBlockID[];
	
    public BlockBarricadePlaceable(int par1, int[] par2)
    {
        this(par1, par2, Material.wood);
    }

    public BlockBarricadePlaceable(int par1, int[] par2, Material par3Material)
    {
        super(par1, par2[0], par3Material);
        stateToBlockID = par2;
    }
    
    @Override
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
    	int meta = par2;
    	if (meta > 5) meta = 5;
        return stateToBlockID[meta];//this.getBlockTextureFromSide(par1);
    }
    
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving) {
    	par1World.setBlockMetadata(par2, par3, par4, 5);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
    	
    	if (entity instanceof EntityLiving) {
 			entity.motionX = 0.000;
 			if (entity.motionY < 0) entity.motionY *= 0.1;
 			entity.motionZ = 0.000;
 			entity.fallDistance = 0F;
 		}
    	
    	if (world.isRemote) return;
    	
    	int meta = world.getBlockMetadata(i, j, k);
    	
    	
    	
    	if (entity instanceof BaseEntAI && !(entity instanceof BaseEntAI_Ally) && ((BaseEntAI)entity).getHealth() > 0)
		{
			//System.out.println(oldid);
			BaseEntAI ent = (BaseEntAI)entity;
			ent.curBlockDamage++;
			//ent.isJumping = false;
			ent.isBreaking = true;
			ent.motionY = -0.4F;
			ent.getNavigator().setPath(null, 0F);
			
			if (entity instanceof EntityLiving) {
	 			entity.motionX = 0.0;
	 			entity.motionZ = 0.0;
	 		}
			
			//lock em in - fail
			//ent.motionX = 0.0F;
			//ent.motionZ = 0.0F;
			//System.out.println(ent.curBlockDamage);
			
			if (ent.curBlockDamage % 40 == 0) {
				ent.swingItem();
			}
			
			if (ent.curBlockDamage % 130 == 0)
			{
				ent.curBlockDamage = 0;
				
				meta--;
				if (meta == 0) {
					world.setBlock(i, j, k, 0);
				} else {
					world.setBlockMetadata(i, j, k, meta);
				}
				
				//System.out.println("meta: " + meta);
				
				ZCGame.instance().notifyBlockUpdates(i,j,k);
				
				//((BaseEntAI)entity).noMoveTicks = 0;
				//int newid = world.getBlockId(i, j - 1, k);
				
				Random rand = new Random();
				
				//if (newid != oldid) {
		            if(meta == 0) {
		            	world.playSoundAtEntity(entity, "zc.barricadecollapse", 1.0F, 1.0F / rand.nextFloat() * 0.1F + 0.95F);
		            } else {
		            	world.playSoundAtEntity(entity, "zc.woodbreak", 1.0F, 1.0F / rand.nextFloat() * 0.1F + 0.95F);
		            }
				//}
			}
		}
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
    	
    	if (true) return null;
    	
        boolean var5 = this.canConnectFenceTo(par1World, par2, par3, par4 - 1);
        boolean var6 = this.canConnectFenceTo(par1World, par2, par3, par4 + 1);
        boolean var7 = this.canConnectFenceTo(par1World, par2 - 1, par3, par4);
        boolean var8 = this.canConnectFenceTo(par1World, par2 + 1, par3, par4);
        float var9 = 0.375F;
        float var10 = 0.625F;
        float var11 = 0.375F;
        float var12 = 0.625F;
        
        var9 = 0.075F;
        var10 = 0.925F;
        var11 = 0.075F;
        var12 = 0.925F;

        if (var5)
        {
            var11 = 0.0F;
        }

        if (var6)
        {
            var12 = 1.0F;
        }

        if (var7)
        {
            var9 = 0.0F;
        }

        if (var8)
        {
            var10 = 1.0F;
        }

        return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)((float)par2 + var9), (double)par3, (double)((float)par4 + var11), (double)((float)par2 + var10), (double)((float)par3 + 1.5F), (double)((float)par4 + var12));
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        boolean var5 = this.canConnectFenceTo(par1IBlockAccess, par2, par3, par4 - 1);
        boolean var6 = this.canConnectFenceTo(par1IBlockAccess, par2, par3, par4 + 1);
        boolean var7 = this.canConnectFenceTo(par1IBlockAccess, par2 - 1, par3, par4);
        boolean var8 = this.canConnectFenceTo(par1IBlockAccess, par2 + 1, par3, par4);
        float var9 = 0.375F;
        float var10 = 0.625F;
        float var11 = 0.375F;
        float var12 = 0.625F;

        if (var5)
        {
            var11 = 0.0F;
        }

        if (var6)
        {
            var12 = 1.0F;
        }

        if (var7)
        {
            var9 = 0.0F;
        }

        if (var8)
        {
            var10 = 1.0F;
        }

        this.setBlockBounds(var9, 0.0F, var11, var10, 1.0F, var12);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 66;
    }

    /**
     * Returns true if the specified block can be connected by a fence
     */
    public boolean canConnectFenceTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int var5 = par1IBlockAccess.getBlockId(par2, par3, par4);

        if (var5 != this.blockID && var5 != Block.fenceGate.blockID)
        {
            Block var6 = Block.blocksList[var5];
            return var6 != null && var6.blockMaterial.isOpaque() && var6.renderAsNormalBlock() ? var6.blockMaterial != Material.pumpkin : false;
        }
        else
        {
            return true;
        }
    }

    public static boolean isIdAFence(int par0)
    {
        return par0 == Block.fence.blockID || par0 == Block.netherFence.blockID;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        return var5 != 1 && var5 != 0 ? true : super.shouldSideBeRendered(var1, var2, var3, var4, var5);
    }
}
