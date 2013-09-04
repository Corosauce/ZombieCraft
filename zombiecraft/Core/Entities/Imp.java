package zombiecraft.Core.Entities;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import CoroAI.entity.EnumJob;

public class Imp extends BaseEntAI_Enemy
{
	
	//public static Entity owner = null;
	
	public Imp(World par1World, double x, double y, double z) {
		this(par1World);
		this.setPosition(x, y, z);
	}
	
    public Imp(World par1World)
    {
        super(par1World);
        //this.texture = "/mods/ZombieCraft/textures/entities/imp.png";
        //this.moveSpeed = 0.23F;
        this.getNavigator().setBreakDoors(true);
        
        initJobAndStates(EnumJob.INVADER, false);
        
        cooldown_Ranged = 80;
        
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote)
        {
            float var1 = this.getBrightness(1.0F);

            if (var1 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F)
            {
                //this.setFire(8);
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.zombie";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombiehurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombiedeath";
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected void func_48085_j_(int par1)
    {
    	
    }
    
    @Override
    public void rightClickItem() {
    	
    	
    	
    	double var3 = entityToAttack.posX - this.posX;
        double var5 = entityToAttack.boundingBox.minY + (double)(entityToAttack.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
        double var7 = entityToAttack.posZ - this.posZ;
    	
    	float var9 = MathHelper.sqrt_float(this.getDistanceToEntity(entityToAttack)) * 0.1F;
        this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

        for (int var10 = 0; var10 < 1; ++var10)
        {
            EntitySmallFireball var11 = new EntitySmallFireball(this.worldObj, this, var3 + this.rand.nextGaussian() * (double)var9, var5, var7 + this.rand.nextGaussian() * (double)var9);
            var11.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
            this.worldObj.spawnEntityInWorld(var11);
        }
    }
}
