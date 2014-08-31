package zombiecraft.Core.Dimension;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import zombiecraft.Core.GameLogic.ZCGame;

public class ZCTeleporter extends Teleporter
{
	
	WorldServer world;
	
    public ZCTeleporter(WorldServer worldServer) {
		super(worldServer);
		world = worldServer;
		// TODO Auto-generated constructor stub
	}

	/** A private Random() function in Teleporter */
    private Random random = new Random();

    /**
     * Place an entity in a nearby portal, creating one if necessary.
     */
    @Override
    public void placeInPortal(Entity par2Entity, double par3, double par5, double par7, float par9)
    {
    	
    	
    	if (par2Entity.dimension == ZCGame.ZCDimensionID) {
    		par2Entity.posY = world.getHeightValue((int)par2Entity.posX, (int)par2Entity.posZ) + 0.5D;
    		par2Entity.posX = world.rand.nextInt(50) - 25;
    		par2Entity.posZ = world.rand.nextInt(50) - 25;
    	} else {
    		World worldDest = DimensionManager.getWorld(par2Entity.dimension);
    		if (worldDest != null) {
    			par2Entity.posY = worldDest.getHeightValue((int)par2Entity.posX, (int)par2Entity.posZ) + 0.5D;
    		}
    	}
    	
    	System.out.println("zc tele! " + par2Entity.dimension);
    	//par2Entity.posY = 10;
    	int what = 0;
    }

    /**
     * Place an entity in a nearby portal which already exists.
     */
    @Override
    public boolean placeInExistingPortal(Entity par2Entity, double par3, double par5, double par7, float par9)
    {
    	
    	System.out.println("1.7.x changed placeInExistingPortal to return true, is this accurate? i dont think we even let this call happen");
    	
    	return true;
    	
    	
        /*short var10 = 128;
        double var11 = -1.0D;
        int var13 = 0;
        int var14 = 0;
        int var15 = 0;
        int var16 = MathHelper.floor_double(par2Entity.posX);
        int var17 = MathHelper.floor_double(par2Entity.posZ);
        int var18;
        double var25;

        for (var18 = var16 - var10; var18 <= var16 + var10; ++var18)
        {
            double var19 = (double)var18 + 0.5D - par2Entity.posX;

            for (int var21 = var17 - var10; var21 <= var17 + var10; ++var21)
            {
                double var22 = (double)var21 + 0.5D - par2Entity.posZ;

                for (int var24 = world.getActualHeight() - 1; var24 >= 0; --var24)
                {
                    if (world.getBlockId(var18, var24, var21) == Block.portal.blockID)
                    {
                        while (world.getBlockId(var18, var24 - 1, var21) == Block.portal.blockID)
                        {
                            --var24;
                        }

                        var25 = (double)var24 + 0.5D - par2Entity.posY;
                        double var27 = var19 * var19 + var25 * var25 + var22 * var22;

                        if (var11 < 0.0D || var27 < var11)
                        {
                            var11 = var27;
                            var13 = var18;
                            var14 = var24;
                            var15 = var21;
                        }
                    }
                }
            }
        }

        if (var11 < 0.0D)
        {
            return false;
        }
        else
        {
            double var46 = (double)var13 + 0.5D;
            double var23 = (double)var14 + 0.5D;
            var25 = (double)var15 + 0.5D;
            int var47 = -1;

            if (world.getBlockId(var13 - 1, var14, var15) == Block.portal.blockID)
            {
                var47 = 2;
            }

            if (world.getBlockId(var13 + 1, var14, var15) == Block.portal.blockID)
            {
                var47 = 0;
            }

            if (world.getBlockId(var13, var14, var15 - 1) == Block.portal.blockID)
            {
                var47 = 3;
            }

            if (world.getBlockId(var13, var14, var15 + 1) == Block.portal.blockID)
            {
                var47 = 1;
            }

            int var28 = par2Entity.getTeleportDirection();

            if (var47 > -1)
            {
                int var29 = Direction.rotateLeft[var47];
                int var30 = Direction.offsetX[var47];
                int var31 = Direction.offsetZ[var47];
                int var32 = Direction.offsetX[var29];
                int var33 = Direction.offsetZ[var29];
                boolean var34 = !world.isAirBlock(var13 + var30 + var32, var14, var15 + var31 + var33) || !world.isAirBlock(var13 + var30 + var32, var14 + 1, var15 + var31 + var33);
                boolean var35 = !world.isAirBlock(var13 + var30, var14, var15 + var31) || !world.isAirBlock(var13 + var30, var14 + 1, var15 + var31);

                if (var34 && var35)
                {
                    var47 = Direction.rotateOpposite[var47];
                    var29 = Direction.rotateOpposite[var29];
                    var30 = Direction.offsetX[var47];
                    var31 = Direction.offsetZ[var47];
                    var32 = Direction.offsetX[var29];
                    var33 = Direction.offsetZ[var29];
                    var18 = var13 - var32;
                    var46 -= (double)var32;
                    int var20 = var15 - var33;
                    var25 -= (double)var33;
                    var34 = !world.isAirBlock(var18 + var30 + var32, var14, var20 + var31 + var33) || !world.isAirBlock(var18 + var30 + var32, var14 + 1, var20 + var31 + var33);
                    var35 = !world.isAirBlock(var18 + var30, var14, var20 + var31) || !world.isAirBlock(var18 + var30, var14 + 1, var20 + var31);
                }

                float var36 = 0.5F;
                float var37 = 0.5F;

                if (!var34 && var35)
                {
                    var36 = 1.0F;
                }
                else if (var34 && !var35)
                {
                    var36 = 0.0F;
                }
                else if (var34 && var35)
                {
                    var37 = 0.0F;
                }

                var46 += (double)((float)var32 * var36 + var37 * (float)var30);
                var25 += (double)((float)var33 * var36 + var37 * (float)var31);
                float var38 = 0.0F;
                float var39 = 0.0F;
                float var40 = 0.0F;
                float var41 = 0.0F;

                if (var47 == var28)
                {
                    var38 = 1.0F;
                    var39 = 1.0F;
                }
                else if (var47 == Direction.rotateOpposite[var28])
                {
                    var38 = -1.0F;
                    var39 = -1.0F;
                }
                else if (var47 == Direction.rotateRight[var28])
                {
                    var40 = 1.0F;
                    var41 = -1.0F;
                }
                else
                {
                    var40 = -1.0F;
                    var41 = 1.0F;
                }

                double var42 = par2Entity.motionX;
                double var44 = par2Entity.motionZ;
                par2Entity.motionX = var42 * (double)var38 + var44 * (double)var41;
                par2Entity.motionZ = var42 * (double)var40 + var44 * (double)var39;
                par2Entity.rotationYaw = par9 - (float)(var28 * 90) + (float)(var47 * 90);
            }
            else
            {
                par2Entity.motionX = par2Entity.motionY = par2Entity.motionZ = 0.0D;
            }

            par2Entity.setLocationAndAngles(var46, var23, var25, par2Entity.rotationYaw, par2Entity.rotationPitch);
            return true;
        }*/
    }

    /**
     * Create a new portal near an entity.
     */
    @Override
    public boolean makePortal(Entity par2Entity)
    {
        

        return true;
    }
}
