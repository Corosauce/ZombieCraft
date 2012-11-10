package zombiecraft.Core.Entities;

import java.util.List;

import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Items.ItemGun;
import zombiecraft.Forge.ZombieCraftMod;

import net.minecraft.src.*;

public class EntityBullet extends Entity
{
    protected int xTile;
    protected int yTile;
    protected int zTile;
    protected int inTile;
    protected boolean inGround;
    public Entity owner;
    protected int timeInTile;
    protected int timeInAir;
    protected int damage;
    protected float headshotMultiplier;
    protected String firingSound;
    protected float soundRangeFactor;
    protected boolean serverSoundPlayed;
    public boolean serverSpawned;
    public int penetrateCount;
    public Entity lastHit;

    public EntityBullet(World var1)
    {
        super(var1);
        this.soundRangeFactor = 8.0F;
        this.serverSoundPlayed = false;
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.inGround = false;
        this.timeInAir = 0;
        this.setSize(0.0625F, 0.03125F);
        
        fixAim();
    }
    
    public void fixAim() {
    	//visual fix
        float adjust = 70F;
        float dist = 0.5F;
        
        this.posX -= (double)(MathHelper.cos((this.rotationYaw + adjust) / 180.0F * (float)Math.PI) * dist);
        this.posY += 0.1D;
        this.posZ -= (double)(MathHelper.sin((this.rotationYaw + adjust) / 180.0F * (float)Math.PI) * dist);
    }

    public EntityBullet(World var1, double var2, double var4, double var6)
    {
        this(var1);
        this.setPosition(var2, var4, var6);
        this.yOffset = 0.0F;
    }

    public void playServerSound(World var1) {};

    public EntityBullet(World var1, Entity var2, ItemGun var3, float var4, float var5, float var6, float var7, float var8)
    {
        this(var1);
        this.owner = var2;
        this.penetrateCount = var3.hitCount;
        this.damage = var3.damage;
        this.headshotMultiplier = 1;//var3.headshotMultiplier;
        float var9 = var2.rotationYaw;
        float var10 = var9 * 0.017453292F;
        double var11 = (double)(var4 * MathHelper.cos(var10) - var6 * MathHelper.sin(var10));
        double var13 = (double)(var4 * MathHelper.sin(var10) + var6 * MathHelper.cos(var10));
        this.setLocationAndAngles(var2.posX + var11, var2.posY + (double)var2.getEyeHeight() + (double)var5, var2.posZ + var13, var2.rotationYaw + var7, var2.rotationPitch + var8);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.1D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        float var15 = var3.spread;

        /*if (var2 instanceof EntityLiving)
        {
            if (var2 instanceof EntityPlayer)
            {
                float var16 = var3.recoil / (float)var3.useDelay;
                float var17 = var16 / 0.1F;

                if (var17 > 0.0F)
                {
                    var15 = (float)((double)var15 * (1.0D + mod_ZCSdkGuns.currentRecoilV / (double)var17));
                }
            }

            boolean var23 = Math.abs(var2.motionX) > 0.1D || Math.abs(var2.motionY) > 0.1D || Math.abs(var2.motionZ) > 0.1D;

            if (var23)
            {
                var15 *= 2.0F;

                if (var3 instanceof ZCSdkItemGunMinigun)
                {
                    var15 *= 2.0F;
                }
            }

            if (!var2.onGround)
            {
                var15 *= 2.0F;

                if (var3 instanceof ZCSdkItemGunMinigun)
                {
                    var15 *= 2.0F;
                }
            }

            if (var2 instanceof EntityPlayer && var3 instanceof ZCSdkItemGunSniper)
            {
                EntityPlayer var24 = (EntityPlayer)var2;

                if (var23)
                {
                    var15 = (float)((double)var15 + 0.25D);
                }

                if (!var2.onGround)
                {
                    var15 = (float)((double)var15 + 0.25D);
                }

                if (!var24.isSneaking())
                {
                    var15 = (float)((double)var15 + 0.25D);
                }

                if (!mod_ZCSdkGuns.getSniperZoomedIn())
                {
                    var15 = 8.0F;
                }
            }
        }*/

        if (var2.riddenByEntity != null && var2 instanceof EntityPlayer)
        {
            this.owner = var2.riddenByEntity;
        }

        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setBulletHeading(this.motionX, this.motionY, this.motionZ, var3.muzzleVelocity, var15 / 2.0F);
        double var22 = 0.0D;
        double var18 = 0.0D;
        double var20 = 0.0D;

        if (var2.ridingEntity != null)
        {
            var22 = var2.ridingEntity.motionX;
            var18 = var2.ridingEntity.onGround ? 0.0D : var2.ridingEntity.motionY;
            var20 = var2.ridingEntity.motionZ;
        }
        else if (var2.riddenByEntity != null)
        {
            var22 = var2.motionX;
            var18 = var2.onGround ? 0.0D : var2.motionY;
            var20 = var2.motionZ;
        }

        this.motionX += var22;
        this.motionY += var18;
        this.motionZ += var20;
    }

    protected void entityInit() {}

    public void setBulletHeading(double var1, double var3, double var5, float var7, float var8)
    {
        float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
        var1 /= (double)var9;
        var3 /= (double)var9;
        var5 /= (double)var9;
        var1 += this.rand.nextGaussian() * 0.0075D * (double)var8;
        var3 += this.rand.nextGaussian() * 0.0075D * (double)var8;
        var5 += this.rand.nextGaussian() * 0.0075D * (double)var8;
        var1 *= (double)var7;
        var3 *= (double)var7;
        var5 *= (double)var7;
        this.motionX = var1;
        this.motionY = var3;
        this.motionZ = var5;
        float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / Math.PI);
        this.timeInTile = 0;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double var1)
    {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        
        //setDead();

        if (this.worldObj.isRemote && !this.serverSoundPlayed && this.owner != ModLoader.getMinecraftInstance().thePlayer)
        {
            this.playServerSound(this.worldObj);
            this.serverSoundPlayed = true;
        }

        if (this.timeInAir == 80)
        {
            this.setDead();
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var1) * 180.0D / Math.PI);
        }

        if (this.inGround)
        {
            int var16 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

            if (var16 == this.inTile)
            {
                ++this.timeInTile;

                if (this.timeInTile == 200)
                {
                    this.setDead();
                }

                return;
            }

            this.inGround = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
            this.timeInTile = 0;
            this.timeInAir = 0;
        }
        else
        {
            ++this.timeInAir;
        }

        Vec3 var17 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var17, var2);
        var17 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
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
        float var11;
        float var22;
        
        if (!this.worldObj.isRemote) {
	        
        	if (penetrateCount > 0) {
		        for (var9 = 0; var9 < var5.size(); ++var9)
		        {
		            Entity var10 = (Entity)var5.get(var9);
		
		            if (var10.canBeCollidedWith() && (var10 != this.owner && (this.owner == null || var10 != this.owner.ridingEntity) && (this.owner == null || var10 != this.owner.riddenByEntity) || this.timeInAir >= 5) && !this.worldObj.isRemote)
		            {
		                var11 = 0.3F;
		                AxisAlignedBB var12 = var10.boundingBox.expand((double)var11, (double)var11, (double)var11);
		                MovingObjectPosition var13 = var12.calculateIntercept(var17, var2);
		
		                if (var13 != null)
		                {
		                    double var14 = var17.distanceTo(var13.hitVec);
		
		                    if (var14 < var6 || var6 == 0.0D)
		                    {
		                        var8 = var13.hitVec;
		                        var4 = var10;
		                        var6 = var14;
		                        
		                        tryHit(var4);
		                    }
		                }
		            }
		        }
        	} else {
                setDead();
        	}
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        
        //System.out.println(this.motionX);
        
        float var18 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var18) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
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

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        var22 = 1.0F;
        var11 = 0.0F;

        if (this.handleWaterMovement())
        {
            for (int var21 = 0; var21 < 4; ++var21)
            {
                float var23 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var23, this.posY - this.motionY * (double)var23, this.posZ - this.motionZ * (double)var23, this.motionX, this.motionY, this.motionZ);
            }

            var22 = 0.8F;
            var11 = 0.03F;
        }

        this.motionX *= (double)var22;
        this.motionY *= (double)var22;
        this.motionZ *= (double)var22;
        this.motionY -= (double)var11;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    
    public boolean tryHit(Entity var4) {
    	MovingObjectPosition var3 = null;
    	float var22;
    	if (var4 != null)
        {
            var3 = new MovingObjectPosition(var4);
        }
    	
        if (var3 != null)
        {
            int var9 = this.worldObj.getBlockId(var3.blockX, var3.blockY, var3.blockZ);

            if ((var3.entityHit != null && var3.entityHit != lastHit && !var3.entityHit.isDead && var3.entityHit instanceof EntityLiving && ((EntityLiving)var3.entityHit).getHealth() > 0) || (var9 != Block.tallGrass.blockID && !ZCUtil.shouldBulletPassThrough(this, var9)))
            {
                if (var3.entityHit != null)
                {
                	
                	if (ZCUtil.shouldBulletHurt(this, var3.entityHit)) {
	                    int var20 = this.damage;
	
	                    if (this.owner instanceof IMob && var3.entityHit instanceof EntityPlayer)
	                    {
	                        if (this.worldObj.difficultySetting == 0)
	                        {
	                            var20 = 0;
	                        }
	
	                        if (this.worldObj.difficultySetting == 1)
	                        {
	                            var20 = var20 / 3 + 1;
	                        }
	
	                        if (this.worldObj.difficultySetting == 3)
	                        {
	                            var20 = var20 * 3 / 2;
	                        }
	                    }
	
	                    //var20 = this.checkHeadshot(var3, var8, var20);
	
	                    if (this.owner != var3.entityHit) {
	                    	lastHit = var3.entityHit;
		                    if (var3.entityHit instanceof EntityLiving)
		                    {
		                    	((EntityLiving)var3.entityHit).hurtResistantTime = 0;//((EntityLiving)var3.entityHit).maxHurtResistantTime;
		                    	var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.owner), var20);
		                        //ZCSdkTools.attackEntityIgnoreDelay((EntityLiving)var3.entityHit, DamageSource.causeThrownDamage(this, this.owner), var20);
		                    }
		                    else
		                    {
		                        var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.owner), var20);
		                    }
	                    }
                	}
                }
                else
                {
                    this.xTile = var3.blockX;
                    this.yTile = var3.blockY;
                    this.zTile = var3.blockZ;
                    this.inTile = var9;
                    this.motionX = (double)((float)(var3.hitVec.xCoord - this.posX));
                    this.motionY = (double)((float)(var3.hitVec.yCoord - this.posY));
                    this.motionZ = (double)((float)(var3.hitVec.zCoord - this.posZ));
                    var22 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)var22 * 0.05D;
                    this.posY -= this.motionY / (double)var22 * 0.05D;
                    this.posZ -= this.motionZ / (double)var22 * 0.05D;
                    this.inGround = true;

                    if (ZombieCraftMod.bulletsDestroyGlass && (this.inTile == Block.glass.blockID || this.inTile == Block.thinGlass.blockID))
                    {
                        Block var19;

                        if (this.inTile == Block.glass.blockID)
                        {
                            var19 = Block.glass;
                        }
                        else
                        {
                            var19 = Block.thinGlass;
                        }

                        //ZCSdkTools.minecraft.effectRenderer.addBlockDestroyEffects(this.xTile, this.yTile, this.zTile, var19.blockID & 255, Block.glass.blockID >> 8 & 255);
                        this.worldObj.playSound((float)this.xTile + 0.5F, (float)this.yTile + 0.5F, (float)this.zTile + 0.5F, var19.stepSound.getBreakSound(), (var19.stepSound.getVolume() + 1.0F) / 2.0F, var19.stepSound.getPitch() * 0.8F);
                        this.worldObj.setBlockWithNotify(this.xTile, this.yTile, this.zTile, 0);
                        var19.onBlockDestroyedByPlayer(this.worldObj, this.xTile, this.yTile, this.zTile, this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile));
                    }
                }

                
                if (penetrateCount <= 0) {
	                this.worldObj.playSoundAtEntity(this, "sdk.impact", 0.2F, 1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
                } else {
                	penetrateCount--;
                	System.out.println("penetrateCount: " + penetrateCount);
                }
                
            }
        }
        return true;
    }

    /*protected int checkHeadshot(MovingObjectPosition var1, Vec3 var2, int var3)
    {
        Render var4 = RenderManager.instance.getEntityClassRenderObject(var1.entityHit.getClass());

        if (var4 instanceof RenderLiving)
        {
            RenderLiving var5 = (RenderLiving)var4;
            float var6 = 0.0F;
            ModelBox var8;
            ModelBox var9;

            if (var5.mainModel instanceof ModelBiped)
            {
                ModelBiped var7 = (ModelBiped)var5.mainModel;
                var8 = (ModelBox)var7.bipedHead.cubeList.get(0);
                var9 = (ModelBox)var7.bipedRightLeg.cubeList.get(0);
                var6 = (var8.posY2 - var8.posY1) / (var9.posY2 + var7.bipedRightLeg.rotationPointY - (var8.posY1 + var7.bipedHead.rotationPointY));
            }
            else if (var5.mainModel instanceof ModelCreeper)
            {
                ModelCreeper var13 = (ModelCreeper)var5.mainModel;
                var8 = (ModelBox)var13.head.cubeList.get(0);
                var9 = (ModelBox)var13.leg1.cubeList.get(0);
                var6 = (var8.posY2 - var8.posY1) / (var9.posY2 + var13.leg1.rotationPointY - (var8.posY1 + var13.head.rotationPointY));
            }

            if (var6 > 0.0F)
            {
                double var14 = var1.entityHit.boundingBox.maxY;
                double var15 = var1.entityHit.boundingBox.minY;
                double var11 = var14 - var15;

                if (var2.yCoord > var14 - var11 * (double)var6)
                {
                    var3 = Math.round((float)var3 * this.headshotMultiplier);
                }
            }
        }

        return var3;
    }*/

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setShort("xTile", (short)this.xTile);
        var1.setShort("yTile", (short)this.yTile);
        var1.setShort("zTile", (short)this.zTile);
        var1.setByte("inTile", (byte)this.inTile);
        var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        this.xTile = var1.getShort("xTile");
        this.yTile = var1.getShort("yTile");
        this.zTile = var1.getShort("zTile");
        this.inTile = var1.getByte("inTile") & 255;
        this.inGround = var1.getByte("inGround") == 1;
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Will get destroyed next tick
     */
    @Override
    public void setDead()
    {
        super.setDead();
        this.owner = null;
    }
}
