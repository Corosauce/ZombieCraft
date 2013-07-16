package zombiecraft.Core.Entities.Projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

import zombiecraft.Core.ZCItems;

public class EntityGrenadeStun extends EntityGrenade
{
    protected String BOUNCE_SOUND = "sdk.stungrenadebounce";
    static final double MAX_DISTANCE = 32.0D;
    static final double MIN_DISTANCE = 8.0D;
    static final float MAX_ANGLE = 180.0F;
    static final float MIN_PITCH_ANGLE = 15.0F;
    static final float MIN_YAW_ANGLE = 15.0F;
    public static final int MAX_FLASH_TIME_PLAYER = 500;
    public static final int MAX_FLASH_TIME_ENTITY = 200;

    public EntityGrenadeStun(World var1)
    {
        super(var1);
        //this.item = new ItemStack(ZCItems.itemGrenadeStun, 1, 0);
    }

    public EntityGrenadeStun(World var1, double var2, double var4, double var6)
    {
        super(var1, var2, var4, var6);
        //this.item = new ItemStack(ZCItems.itemGrenadeStun, 1, 0);
    }

    public EntityGrenadeStun(World var1, EntityLiving var2)
    {
        super(var1, var2);
        //this.item = new ItemStack(ZCItems.itemGrenadeStun, 1, 0);
    }

    protected void explode()
    {
        if (!this.exploded)
        {
            this.exploded = true;
            this.worldObj.playSoundAtEntity(this, "sdk.stungrenade", 4.0F, 1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
            //mod_SdkFlasher.LightEntity(this.worldObj, this, 15, 2);
            ArrayList var1 = this.getEntityLivingsInRange(32.0D);

            for (int var2 = 0; var2 < var1.size(); ++var2)
            {
                EntityLiving var3 = (EntityLiving)var1.get(var2);

                if (var3.canEntityBeSeen(this))
                {
                    double var4 = this.posX - var3.posX;
                    double var6 = this.posY - var3.posY;
                    double var8 = this.posZ - var3.posZ;
                    float var10 = var3.rotationPitch;
                    float var11 = (float)(Math.atan(Math.sqrt(var4 * var4 + var8 * var8) / var6) * (180D / Math.PI));

                    if (var6 >= 0.0D)
                    {
                        var11 -= 90.0F;
                    }
                    else
                    {
                        var11 += 90.0F;
                    }

                    float var12 = var10 - var11;
                    float var13 = var3.rotationYaw % 360.0F;

                    if (var13 < -180.0F)
                    {
                        var13 += 360.0F;
                    }

                    if (var13 < 0.0F)
                    {
                        var13 *= -1.0F;
                    }
                    else if (var13 < 180.0F)
                    {
                        var13 *= -1.0F;
                    }
                    else
                    {
                        var13 = 360.0F - var13;
                    }

                    float var14;

                    if (var4 >= 0.0D && var8 >= 0.0D)
                    {
                        var14 = (float)(Math.atan(Math.abs(var4 / var8)) * (180D / Math.PI));
                    }
                    else if (var4 >= 0.0D && var8 <= 0.0D)
                    {
                        var14 = 90.0F + (float)(Math.atan(Math.abs(var8 / var4)) * (180D / Math.PI));
                    }
                    else if (var4 <= 0.0D && var8 >= 0.0D)
                    {
                        var14 = -(90.0F - (float)(Math.atan(Math.abs(var8 / var4)) * (180D / Math.PI)));
                    }
                    else
                    {
                        var14 = -(180.0F - (float)(Math.atan(Math.abs(var4 / var8)) * (180D / Math.PI)));
                    }

                    float var15 = var14 - var13;

                    if (var15 > 180.0F)
                    {
                        var15 -= 360.0F;
                    }
                    else if (var15 < -180.0F)
                    {
                        var15 += 360.0F;
                    }

                    var12 = Math.abs(var12);
                    float var16;

                    if (var12 < 15.0F)
                    {
                        var16 = 1.0F;
                    }
                    else if (var12 > 180.0F)
                    {
                        var16 = 0.0F;
                    }
                    else
                    {
                        var16 = 1.0F - (var12 - 15.0F) / 165.0F;
                    }

                    var15 = Math.abs(var15);
                    float var17;

                    if (var15 < 15.0F)
                    {
                        var17 = 1.0F;
                    }
                    else if (var15 > 180.0F)
                    {
                        var17 = 0.0F;
                    }
                    else
                    {
                        var17 = 1.0F - (var15 - 15.0F) / 165.0F;
                    }

                    float var18 = Math.min(var16, var17);
                    float var19 = this.getDistanceToEntity(var3);
                    float var20;

                    if ((double)var19 < 8.0D)
                    {
                        var20 = 1.0F;
                    }
                    else
                    {
                        var20 = 1.0F - (float)(((double)var19 - 8.0D) / 24.0D);
                    }

                    int var21;

                    if (var3 instanceof EntityPlayer)
                    {
                        var21 = Math.round(500.0F * var20 * var18);
                    }
                    else
                    {
                        var21 = Math.round(200.0F * var20);
                    }

                    /*if (!mod_ZCSdkGuns.flashTimes.containsKey(var3) || ((Integer)((ZCPair)mod_ZCSdkGuns.flashTimes.get(var3)).getLeft()).intValue() < var21)
                    {
                        mod_ZCSdkGuns.flashTimes.put(var3, new ZCPair(Integer.valueOf(var21), Float.valueOf(var3.moveSpeed)));
                    }*/

                    if (!(var3 instanceof EntityPlayer))
                    {
                        //var3.moveSpeed = 0.0F;
                        System.out.println("movespeed set broken, fix!");
                        var3.attackTime = var21;
                    }
                }
            }

            this.isDead = true;
        }
    }

    public ArrayList getEntityLivingsInRange(double var1)
    {
        ArrayList var3 = new ArrayList();

        for (int var4 = 0; var4 < this.worldObj.loadedEntityList.size(); ++var4)
        {
            Entity var5 = (Entity)this.worldObj.loadedEntityList.get(var4);

            if (var5 instanceof EntityLiving && var5.isEntityAlive() && this.getDistanceSqToEntity(var5) < var1 * var1)
            {
                var3.add((EntityLiving)var5);
            }
        }

        return var3;
    }

    public ArrayList getPlayersInRange(double var1)
    {
        ArrayList var3 = new ArrayList();

        for (int var4 = 0; var4 < this.worldObj.loadedEntityList.size(); ++var4)
        {
            Entity var5 = (Entity)this.worldObj.loadedEntityList.get(var4);

            if (var5 instanceof EntityPlayer && var5.isEntityAlive() && this.getDistanceSqToEntity(var5) < var1 * var1)
            {
                var3.add((EntityPlayer)var5);
            }
        }

        return var3;
    }
}
