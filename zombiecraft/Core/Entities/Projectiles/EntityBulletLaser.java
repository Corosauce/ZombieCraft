package zombiecraft.Core.Entities.Projectiles;

import java.util.List;

import net.minecraft.src.*;

import zombiecraft.Core.ZCItems;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Forge.ZombieCraftMod;

public class EntityBulletLaser extends EntityBullet
{
    public EntityBulletLaser(World var1)
    {
        super(var1);
        this.setSize(0.5F, 0.5F);
    }

    public EntityBulletLaser(World var1, double var2, double var4, double var6)
    {
        super(var1, var2, var4, var6);
        this.setSize(0.5F, 0.5F);
    }

    public EntityBulletLaser(World var1, Entity var2, ItemGun var3, float var4, float var5, float var6, float var7, float var8)
    {
        super(var1, var2, var3, var4, var5, var6, var7, var8);
        this.setSize(0.5F, 0.5F);
    }

    public void playServerSound(World var1)
    {
        var1.playSoundAtEntity(this, ((ItemGun)ZCItems.itemFlamethrower).firingSound, ((ItemGun)ZCItems.itemFlamethrower).soundRangeFactor, 1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.onEntityUpdate();

        if (this.timeInAir == 30)
        {
            this.setDead();
        }

        this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);

        if (worldObj.isRemote) return;
        
        if (this.inGround)
        {
            int var1 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

            if (var1 != this.inTile)
            {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.timeInTile = 0;
                this.timeInAir = 0;
            }
        }
        else
        {
            ++this.timeInAir;
        }

        Vec3 var16 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var16, var2);
        var16 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (var3 != null)
        {
            var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
        }

        Entity var4 = null;
        List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double var6 = 0.0D;
        Vec3 var8 = null;
        int var9;

        for (var9 = 0; var9 < var5.size(); ++var9)
        {
            Entity var10 = (Entity)var5.get(var9);

            if (var10.canBeCollidedWith() && (var10 != this.owner && (this.owner == null || var10 != this.owner.ridingEntity) || this.timeInAir >= 5) && !this.serverSpawned)
            {
                float var11 = 0.3F;
                AxisAlignedBB var12 = var10.boundingBox.expand((double)var11, (double)var11, (double)var11);
                MovingObjectPosition var13 = var12.calculateIntercept(var16, var2);

                if (var13 != null)
                {
                    double var14 = var16.distanceTo(var13.hitVec);

                    if (var14 < var6 || var6 == 0.0D)
                    {
                        var8 = var13.hitVec;
                        var4 = var10;
                        var6 = var14;
                    }
                }
            }
        }

        if (var4 != null)
        {
            var3 = new MovingObjectPosition(var4);
        }

        if (var3 != null)
        {
            if (var3.entityHit != null)
            {
            	if (ZCUtil.shouldBulletHurt(this, var3.entityHit)) {
	                var9 = this.damage;
	
	                if (this.owner instanceof IMob && var3.entityHit instanceof EntityPlayer)
	                {
	                    if (this.worldObj.difficultySetting == 0)
	                    {
	                        var9 = 0;
	                    }
	
	                    if (this.worldObj.difficultySetting == 1)
	                    {
	                        var9 = var9 / 3 + 1;
	                    }
	
	                    if (this.worldObj.difficultySetting == 3)
	                    {
	                        var9 = var9 * 3 / 2;
	                    }
	                }
	
	                //var9 = this.checkHeadshot(var3, var8, var9);
	
	                if (var3.entityHit instanceof EntityLiving)
	                {
	                    //ZCSdkTools.attackEntityIgnoreDelay((EntityLiving)var3.entityHit, DamageSource.causeThrownDamage(this, this.owner), var9);
	                	((EntityLiving)var3.entityHit).hurtResistantTime = 0;//((EntityLiving)var3.entityHit).maxHurtResistantTime;
                    	var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.owner), var9);
	                }
	                else
	                {
	                    var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.owner), var9);
	                }
	
	                var3.entityHit.setFire(300);
            	}
            }
            else
            {
                this.xTile = var3.blockX;
                this.yTile = var3.blockY;
                this.zTile = var3.blockZ;

                if (false/* && this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile) == Block.ice.blockID && Block.ice.blockHardness < 1000000.0F*/)
                {
                    //Block.ice.onBlockRemoval(this.worldObj, this.xTile, this.yTile, this.zTile);
                }
                else
                {
                    var9 = this.motionX > 0.0D ? 1 : -1;
                    int var18 = this.motionY > 0.0D ? 1 : -1;
                    int var17 = this.motionZ > 0.0D ? 1 : -1;
                    boolean var19 = this.worldObj.getBlockId(this.xTile - var9, this.yTile, this.zTile) == 0 || this.worldObj.getBlockId(this.xTile - var9, this.yTile, this.zTile) == Block.snow.blockID;
                    boolean var20 = this.worldObj.getBlockId(this.xTile, this.yTile - var18, this.zTile) == 0 || this.worldObj.getBlockId(this.xTile, this.yTile - var18, this.zTile) == Block.snow.blockID;
                    boolean var21 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile - var17) == 0 || this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile - var17) == Block.snow.blockID;

                    /*if (var19)
                    {
                        this.worldObj.setBlockWithNotify(this.xTile - var9, this.yTile, this.zTile, Block.fire.blockID);
                    }

                    if (var20)
                    {
                        this.worldObj.setBlockWithNotify(this.xTile, this.yTile - var18, this.zTile, Block.fire.blockID);
                    }

                    if (var21)
                    {
                        this.worldObj.setBlockWithNotify(this.xTile, this.yTile, this.zTile - var17, Block.fire.blockID);
                    }*/
                }
            }

            this.setDead();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        this.setRotationToVelocity();

        if (this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this))
        {
            this.setDead();
        }

        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void setRotationToVelocity()
    {
        float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double var1, double var3, double var5)
    {
        super.setVelocity(var1, var3, var5);
        this.setRotationToVelocity();
    }

    /**
     * Gets how bright this entity is.
     */
    public float getEntityBrightness(float var1)
    {
        return 2.0F;
    }
}
