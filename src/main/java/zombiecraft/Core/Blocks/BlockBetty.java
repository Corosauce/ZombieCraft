package zombiecraft.Core.Blocks;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.packet.Packet60Explosion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import zombiecraft.Core.ZCExplosion;
import zombiecraft.Core.Entities.BaseEntAI_Ally;
import zombiecraft.Core.Entities.BaseEntAI_Enemy;

public class BlockBetty extends Block {
	
	public BlockBetty(Material par3Material) {
		super(par3Material);
		this.setBlockBounds(0F, 0F, 0F, 1F, 0.05F, 1F);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {		
		if (!world.isAirBlock(i,j-1,k) && world.getBlock(i,j-1,k) != this)
		{
			return true;
		} else {
			return false;
		}
    }
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, Block l)
    {	
		super.onNeighborBlockChange(world, i, j, k, l);        
		canBlockStay(world, i, j, k);
    }
	
	@Override
	public boolean canBlockStay(World world, int i, int j, int k)
    {
		if (!world.isAirBlock(i,j-1,k) && world.getBlock(i,j-1,k) != this)
		{
            return true;
		} else {
			world.setBlock(i, j, k, Blocks.air);
			return false;
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k3, Entity entity2)
    {
		if (!world.isRemote && (entity2 instanceof EntityLivingBase && ((EntityLivingBase)entity2).getHealth() > 0 && (entity2 instanceof BaseEntAI_Enemy || (entity2 instanceof EntityLivingBase && !(entity2 instanceof EntityChicken || entity2 instanceof EntityBat || entity2 instanceof BaseEntAI_Ally || entity2 instanceof EntityPlayer))))) {
			//make block gone to prevent weird LOS issues causing no damage on some zombies
			world.setBlock(i, j, k3, Blocks.air);
			explode(world, i, j, k3);
		}
    }
	
	public void explode(World world, int x, int y, int z) {
    	
    	float size = 2.5F;
    	
    	ZCExplosion var11 = new ZCExplosion(world, null, x, y, z, size, 2F);
        var11.isFlaming = false;
        var11.isSmoking = true;
        var11.doExplosionA();
        //var11.doExplosionB(false);
        world.playSoundEffect(x, y, z, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
    	
    	//if (!par10)
        //{
        var11.affectedBlockPositions.clear();
        //}

        Iterator var12 = world.playerEntities.iterator();

        while (var12.hasNext())
        {
            EntityPlayer var13 = (EntityPlayer)var12.next();

            if (var13.getDistanceSq(x, y, z) < 4096.0D)
            {
                ((EntityPlayerMP)var13).playerNetServerHandler.sendPacketToPlayer(new Packet60Explosion(x, y, z, size, var11.affectedBlockPositions, (Vec3)var11.func_77277_b().get(var13)));
            }
        }
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
    public int getRenderType()
    {
        return 1;
    }
}
