package zombiecraft.Core.Entities.Projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import zombiecraft.Forge.ZombieCraftMod;

public class EntityGrenade extends EntityItem
{
    protected String bounceSound;
    protected double bounceFactor;
    protected double bounceSlowFactor;
    protected int fuse;
    protected boolean exploded;
    protected double initialVelocity;
    protected static final int FUSE_LENGTH = 50;
    protected static final double MIN_BOUNCE_SOUND_VELOCITY = 0.1D;

    public EntityGrenade(World var1)
    {
        super(var1);
        this.bounceSound = "sdk.grenadebounce";
        this.bounceFactor = 0.15D;
        this.bounceSlowFactor = 0.8D;
        this.initialVelocity = 1.0D;
        this.setSize(0.25F, 0.25F);
        this.exploded = false;
        this.fuse = 50;
        this.yOffset = 0.0F;
        //this.item = new ItemStack(ZCItems.itemGrenade, 1, 0);
    }

    public EntityGrenade(World var1, double var2, double var4, double var6)
    {
        this(var1);
        this.setPosition(var2, var4, var6);
    }

    public EntityGrenade(World var1, EntityLivingBase var2)
    {
        this(var1);
        this.setAngles(var2.rotationYaw, 0.0F);
        double var3 = (double)(-MathHelper.sin(var2.rotationYaw * (float)Math.PI / 180.0F));
        double var5 = (double)MathHelper.cos(var2.rotationYaw * (float)Math.PI / 180.0F);
        this.motionX = this.initialVelocity * var3 * (double)MathHelper.cos(var2.rotationPitch / 180.0F * (float)Math.PI);
        this.motionY = -this.initialVelocity * (double)MathHelper.sin(var2.rotationPitch / 180.0F * (float)Math.PI);
        this.motionZ = this.initialVelocity * var5 * (double)MathHelper.cos(var2.rotationPitch / 180.0F * (float)Math.PI);

        if (var2.ridingEntity != null && var2.ridingEntity instanceof EntityLivingBase)
        {
            var2 = (EntityLivingBase)var2.ridingEntity;
        }

        this.motionX += var2.motionX;
        this.motionY += var2.onGround ? 0.0D : var2.motionY;
        this.motionZ += var2.motionZ;
        this.setPosition(var2.posX + var3 * 0.8D, var2.posY + (double)var2.getEyeHeight(), var2.posZ + var5 * 0.8D);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
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
        double var1 = this.motionX;
        double var3 = this.motionY;
        double var5 = this.motionZ;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        boolean var7 = false;

        if (this.motionX == 0.0D && var1 != 0.0D)
        {
            this.motionX = -this.bounceFactor * var1;
            this.motionY = this.bounceSlowFactor * var3;
            this.motionZ = this.bounceSlowFactor * var5;

            if (Math.abs(var1) > 0.1D)
            {
                var7 = true;
            }
        }

        if (this.motionY == 0.0D && var3 != 0.0D)
        {
            this.motionX = this.bounceSlowFactor * var1;
            this.motionY = -this.bounceFactor * var3;
            this.motionZ = this.bounceSlowFactor * var5;

            if (Math.abs(var3) > 0.1D)
            {
                var7 = true;
            }
        }

        if (this.motionZ == 0.0D && var5 != 0.0D)
        {
            this.motionX = this.bounceSlowFactor * var1;
            this.motionY = this.bounceSlowFactor * var3;
            this.motionZ = -this.bounceFactor * var5;

            if (Math.abs(var5) > 0.1D)
            {
                var7 = true;
            }
        }

        if (var7)
        {
            this.handleBounce();
        }

        this.motionY -= 0.04D;
        this.motionX *= 0.99D;
        this.motionY *= 0.99D;
        this.motionZ *= 0.99D;
        this.handleExplode();
    }

    protected void handleBounce()
    {
        this.worldObj.playSoundAtEntity(this, this.bounceSound, 0.25F, 1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
    }

    protected void handleExplode()
    {
        if (this.fuse-- <= 0)
        {
            this.explode();
        }
    }

    protected void explode()
    {
        if (!this.exploded)
        {
            this.exploded = true;
            Explosion var1 = new Explosion(this.worldObj, (Entity)null, this.posX, (double)((float)this.posY), (double)((float)this.posZ), 3.0F);
            var1.doExplosionA();

            if (ZombieCraftMod.explosionsDestroyBlocks)
            {
                var1.doExplosionB(true);
            }
            else
            {
                this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
            }

            for (int var2 = 0; var2 < 32; ++var2)
            {
                this.worldObj.spawnParticle("explode", this.posX, this.posY, this.posZ, this.worldObj.rand.nextDouble() - 0.5D, this.worldObj.rand.nextDouble() - 0.5D, this.worldObj.rand.nextDouble() - 0.5D);
                this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, this.worldObj.rand.nextDouble() - 0.5D, this.worldObj.rand.nextDouble() - 0.5D, this.worldObj.rand.nextDouble() - 0.5D);
            }

            this.isDead = true;
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound var1)
    {
        super.writeEntityToNBT(var1);
        var1.setByte("Fuse", (byte)this.fuse);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound var1)
    {
        super.readEntityFromNBT(var1);
        this.fuse = var1.getByte("Fuse");
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer var1) {}

    public float getEyeHeight()
    {
        return this.height;
    }
}
