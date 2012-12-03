package zombiecraft.Core.Entities;

import CoroAI.entity.EnumJob;
import CoroAI.entity.JobProtect;
import net.minecraft.src.*;

public class Comrade extends BaseEntAI_Ally
{
	
	//public static Entity owner = null;
	
	public Comrade(World par1World, double x, double y, double z) {
		this(par1World);
		this.setPosition(x, y, z);
	}
	
    public Comrade(World par1World)
    {
        super(par1World);
        
        this.texture = "/zc/entities/comrade/skin10.png";
        //this.moveSpeed = 0.35F;
        setMoveSpeed(0.33F);
        this.getNavigator().setBreakDoors(true);
        
        //addJob(EnumJob.FINDFOOD);
        //addJob(EnumJob.PROTECT);
        //addJob(EnumJob.HUNTER);
        
        //addJob(EnumJob.PROTECT);
        //addJob(EnumJob.INVADER);
        
        job.jobTypes.put(EnumJob.PROTECT, new JobZCProtect(job));
        job.jobTypes.put(EnumJob.HUNTER, new JobSurvivor(job));
        
        initJobAndStates(EnumJob.PROTECT, false);
        
        //job.setPrimaryJob(EnumJob.PROTECT);
        //job.clearJobs();
        
        //JobZCProtect
        
        /*this.tasks.addTask(0, new EntityAISwimming(this));
        //this.tasks.addTask(1, new EntityAIAvoidEntity(this, BaseEntAI_Enemy.class, 4.0F, 0.3F, 0.35F));
        //this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTwardsRestriction(this, 0.3F));
        this.tasks.addTask(6, new EntityAIArrowAttack(this, this.moveSpeed, 1, 60));
        this.tasks.addTask(7, new EntityAIWander(this, this.moveSpeed));
        //this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, BaseEntAI_Enemy.class, 16.0F, 0, false));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        //this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
        
        //this.targetTasks
        mod_ZombieCraft.zcGame.addTasks(tasks, targetTasks, this);*/
        
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
        return "";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "";
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return 0;
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
