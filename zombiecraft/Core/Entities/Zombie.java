package zombiecraft.Core.Entities;

import CoroAI.entity.EnumJob;
import CoroAI.entity.JobFindFood;
import net.minecraft.src.*;

public class Zombie extends BaseEntAI_Enemy
{
	
	//public static Entity owner = null;
	
	public Zombie(World par1World, double x, double y, double z) {
		this(par1World);
		this.setPosition(x, y, z);
	}
	
    public Zombie(World par1World)
    {
        super(par1World);
        this.texture = "/zc/entities/zombie.png";
        //this.moveSpeed = 0.23F;
        this.getNavigator().setBreakDoors(true);
        
        addJob(EnumJob.FINDFOOD);
        addJob(EnumJob.INVADER);
        
        maxReach_Ranged = 0;
        
        /*this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIBreakDoor(this));
        //this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
        //this.tasks.addTask(2, new EntityAI_ZA_AttackPersist(this, EntityPlayer.class, this.moveSpeed, false));
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, this.moveSpeed, true));
        this.tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
        this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, this.moveSpeed, false));
        this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));*/
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
        
        //this.targetTasks
        //mod_ZombieCraft.zcGame.addTasks(tasks, targetTasks, this);
        
        //New!
        
        //dont mess with navigator! it will make all pfs bigger ranged! 
        //mod_ZombieCraft.zcGame.setNewNav(this,  new PathNavigate(this, par1World, 64.0F));
        //this.navigator = ;
        
        
    }

    public int getMaxHealth()
    {
        return 20;
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
        return "mob.zombie.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.zombie.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.zombie.death";
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
}
