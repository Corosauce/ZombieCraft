package zombiecraft.Core.Blocks;

import java.util.Iterator;

import zombiecraft.Core.Entities.BaseEntAI_Enemy;

import net.minecraft.src.*;

public class BlockBetty extends Block {
	
	public BlockBetty(int par1, int par2, Material par3Material) {
		super(par1, par2, par3Material);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {		
		if (!world.isAirBlock(i,j-1,k) && world.getBlockId(i,j-1,k) != blockID)
		{
			return true;
		} else {
			return false;
		}
    }
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {	
		super.onNeighborBlockChange(world, i, j, k, l);        
		canBlockStay(world, i, j, k);
    }
	
	@Override
	public boolean canBlockStay(World world, int i, int j, int k)
    {
		if (!world.isAirBlock(i,j-1,k) && world.getBlockId(i,j-1,k)!=107)
		{
            return true;
		} else {
			world.setBlockWithNotify(i, j, k, 0);
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
		if (!world.isRemote && entity2 instanceof BaseEntAI_Enemy) {
			explode(world, i, j, k3);
			world.setBlockWithNotify(i, j, k3, 0);
		}
    }
	
	public void explode(World world, int x, int y, int z) {
    	
    	float size = 2.5F;
    	
    	Explosion var11 = new Explosion(world, null, x, y, z, size);
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
