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
        short var10 = 128;
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
        }
    }

    /**
     * Create a new portal near an entity.
     */
    @Override
    public boolean makePortal(Entity par2Entity)
    {
        byte var3 = 16;
        double var4 = -1.0D;
        int var6 = MathHelper.floor_double(par2Entity.posX);
        int var7 = MathHelper.floor_double(par2Entity.posY);
        int var8 = MathHelper.floor_double(par2Entity.posZ);
        int var9 = var6;
        int var10 = var7;
        int var11 = var8;
        int var12 = 0;
        int var13 = this.random.nextInt(4);
        int var14;
        double var15;
        int var17;
        double var18;
        int var21;
        int var20;
        int var23;
        int var22;
        int var25;
        int var24;
        int var27;
        int var26;
        int var28;
        double var34;
        double var32;

        for (var14 = var6 - var3; var14 <= var6 + var3; ++var14)
        {
            var15 = (double)var14 + 0.5D - par2Entity.posX;

            for (var17 = var8 - var3; var17 <= var8 + var3; ++var17)
            {
                var18 = (double)var17 + 0.5D - par2Entity.posZ;
                label274:

                for (var20 = world.getActualHeight() - 1; var20 >= 0; --var20)
                {
                    if (world.isAirBlock(var14, var20, var17))
                    {
                        while (var20 > 0 && world.isAirBlock(var14, var20 - 1, var17))
                        {
                            --var20;
                        }

                        for (var21 = var13; var21 < var13 + 4; ++var21)
                        {
                            var22 = var21 % 2;
                            var23 = 1 - var22;

                            if (var21 % 4 >= 2)
                            {
                                var22 = -var22;
                                var23 = -var23;
                            }

                            for (var24 = 0; var24 < 3; ++var24)
                            {
                                for (var25 = 0; var25 < 4; ++var25)
                                {
                                    for (var26 = -1; var26 < 4; ++var26)
                                    {
                                        var27 = var14 + (var25 - 1) * var22 + var24 * var23;
                                        var28 = var20 + var26;
                                        int var29 = var17 + (var25 - 1) * var23 - var24 * var22;

                                        if (var26 < 0 && !world.getBlockMaterial(var27, var28, var29).isSolid() || var26 >= 0 && !world.isAirBlock(var27, var28, var29))
                                        {
                                            continue label274;
                                        }
                                    }
                                }
                            }

                            var32 = (double)var20 + 0.5D - par2Entity.posY;
                            var34 = var15 * var15 + var32 * var32 + var18 * var18;

                            if (var4 < 0.0D || var34 < var4)
                            {
                                var4 = var34;
                                var9 = var14;
                                var10 = var20;
                                var11 = var17;
                                var12 = var21 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (var4 < 0.0D)
        {
            for (var14 = var6 - var3; var14 <= var6 + var3; ++var14)
            {
                var15 = (double)var14 + 0.5D - par2Entity.posX;

                for (var17 = var8 - var3; var17 <= var8 + var3; ++var17)
                {
                    var18 = (double)var17 + 0.5D - par2Entity.posZ;
                    label222:

                    for (var20 = world.getActualHeight() - 1; var20 >= 0; --var20)
                    {
                        if (world.isAirBlock(var14, var20, var17))
                        {
                            while (var20 > 0 && world.isAirBlock(var14, var20 - 1, var17))
                            {
                                --var20;
                            }

                            for (var21 = var13; var21 < var13 + 2; ++var21)
                            {
                                var22 = var21 % 2;
                                var23 = 1 - var22;

                                for (var24 = 0; var24 < 4; ++var24)
                                {
                                    for (var25 = -1; var25 < 4; ++var25)
                                    {
                                        var26 = var14 + (var24 - 1) * var22;
                                        var27 = var20 + var25;
                                        var28 = var17 + (var24 - 1) * var23;

                                        if (var25 < 0 && !world.getBlockMaterial(var26, var27, var28).isSolid() || var25 >= 0 && !world.isAirBlock(var26, var27, var28))
                                        {
                                            continue label222;
                                        }
                                    }
                                }

                                var32 = (double)var20 + 0.5D - par2Entity.posY;
                                var34 = var15 * var15 + var32 * var32 + var18 * var18;

                                if (var4 < 0.0D || var34 < var4)
                                {
                                    var4 = var34;
                                    var9 = var14;
                                    var10 = var20;
                                    var11 = var17;
                                    var12 = var21 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int var30 = var9;
        int var16 = var10;
        var17 = var11;
        int var31 = var12 % 2;
        int var19 = 1 - var31;

        if (var12 % 4 >= 2)
        {
            var31 = -var31;
            var19 = -var19;
        }

        boolean var33;

        if (var4 < 0.0D)
        {
            if (var10 < 70)
            {
                var10 = 70;
            }

            if (var10 > world.getActualHeight() - 10)
            {
                var10 = world.getActualHeight() - 10;
            }

            var16 = var10;

            for (var20 = -1; var20 <= 1; ++var20)
            {
                for (var21 = 1; var21 < 3; ++var21)
                {
                    for (var22 = -1; var22 < 3; ++var22)
                    {
                        var23 = var30 + (var21 - 1) * var31 + var20 * var19;
                        var24 = var16 + var22;
                        var25 = var17 + (var21 - 1) * var19 - var20 * var31;
                        var33 = var22 < 0;
                        world.setBlock(var23, var24, var25, var33 ? Block.obsidian.blockID : 0);
                    }
                }
            }
        }

        for (var20 = 0; var20 < 4; ++var20)
        {
            //world.editingBlocks = true;

            for (var21 = 0; var21 < 4; ++var21)
            {
                for (var22 = -1; var22 < 4; ++var22)
                {
                    var23 = var30 + (var21 - 1) * var31;
                    var24 = var16 + var22;
                    var25 = var17 + (var21 - 1) * var19;
                    var33 = var21 == 0 || var21 == 3 || var22 == -1 || var22 == 3;
                    world.setBlock(var23, var24, var25, var33 ? Block.obsidian.blockID : Block.portal.blockID, 0, 2);
                }
            }

            //world.editingBlocks = false;

            for (var21 = 0; var21 < 4; ++var21)
            {
                for (var22 = -1; var22 < 4; ++var22)
                {
                    var23 = var30 + (var21 - 1) * var31;
                    var24 = var16 + var22;
                    var25 = var17 + (var21 - 1) * var19;
                    world.notifyBlocksOfNeighborChange(var23, var24, var25, world.getBlockId(var23, var24, var25));
                }
            }
        }

        return true;
    }
}
