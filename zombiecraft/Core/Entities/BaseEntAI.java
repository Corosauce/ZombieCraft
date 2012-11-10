package zombiecraft.Core.Entities;

import build.render.Overlays;
import zombiecraft.Core.GameLogic.WaveManager;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZCServerTicks;
import CoroAI.*;
import CoroAI.entity.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

public class BaseEntAI extends c_EnhAI
{
	
	//public int team;
	
	public boolean initWaveSettings = true;

	public int curBlockDamage;

	public boolean isBreaking;
	
	public BaseEntAI(World par1World, double x, double y, double z) {
		this(par1World);
		this.setPosition(x, y, z);
	}
	
    public BaseEntAI(World par1World)
    {
        super(par1World);
        
        //team = 0;
        maxReach_Melee = 2.0F;
        
        //System.out.println("REMOVING SERVERMODE CHECK BaseEntAI constructor");
        if (ZCServerTicks.zcGame.serverMode) {
        	this.serverMode = true;
        	
        	maxReach_Melee = 1.2F;
        }
        
        //System.out.println("maxReach_Melee: " + maxReach_Melee);
        
        //Disable ranged attacks
        maxReach_Ranged = 0F;
        
        this.setExp(1);
        //mod_ZombieCraft.zcGame.addTasks(tasks, targetTasks, this);
        
    }
    
    public void leftClickItem(Entity var1) {
    	ZCGame.instance().pvpFixPre();
    	super.leftClickItem(var1);
    	ZCGame.instance().pvpFixPost();
    }
    
    protected void updateAITasks()
    {
        ++this.entityAge;
        //Profiler.startSection("checkDespawn");
        this.despawnEntity();
        //Profiler.endSection();
        //Profiler.startSection("sensing");
        //this.func_48090_aM().func_48481_a();
        //Profiler.endSection();
        //Profiler.startSection("targetSelector");
        //this.targetTasks.onUpdateTasks();
        //Profiler.endSection();
        //Profiler.startSection("goalSelector");
        //this.tasks.onUpdateTasks();
        //Profiler.endSection();
        //Profiler.startSection("navigation");
        this.getNavigator().onUpdateNavigation();
        //Profiler.endSection();
        //Profiler.startSection("mob tick");
        updateAI();
        //this.func_48097_s_();
        //Profiler.endSection();
        //Profiler.startSection("controls");
        this.getMoveHelper().onUpdateMoveHelper();
        this.getLookHelper().onUpdateLook();
        this.getJumpHelper().doJump();
        //Profiler.endSection();
    }
    
    @Override
    public void applyEntityCollision(Entity par1Entity)
    {
    	float randFactor = rand.nextFloat();
    	
        if (par1Entity.riddenByEntity != this && par1Entity.ridingEntity != this)
        {
            double var2 = par1Entity.posX - this.posX;
            double var4 = par1Entity.posZ - this.posZ;
            double var6 = MathHelper.abs_max(var2, var4);

            if (var6 >= 0.01D)
            {
                var6 = (double)MathHelper.sqrt_double(var6);
                var2 /= var6;
                var4 /= var6;
                double var8 = 1.0D / var6;

                if (var8 > 1.0D)
                {
                    var8 = 1.0D;
                }

                var2 *= var8;
                var4 *= var8;
                var2 *= 0.05D;
                var4 *= 0.05D;
                
                var2 *= randFactor;
                var4 *= randFactor;
                
                var2 *= (double)(1.0F - this.entityCollisionReduction);
                var4 *= (double)(1.0F - this.entityCollisionReduction);
                this.addVelocity(-var2, 0.0D, -var4);
                par1Entity.addVelocity(var2, 0.0D, var4);
            }
        }
    }
    
    @Override
    public void updateAI() {
    	super.updateAI();
    }
    
    public float getMoveSpeed() {
    	return this.moveSpeed;
    }
    
    @Override
    public boolean isValidLightLevel() {
    	return true;
    }
    
    @Override
    public boolean getCanSpawnHere()
    {
        return /*this.isValidLightLevel() && */super.getCanSpawnHere();
    }
    
    @Override
    public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return 1.0F;//0.5F - this.worldObj.getLightBrightness(par1, par2, par3);
    }

    public int getMaxHealth()
    {
        return 20;
    }
    
    @Override
    public boolean canDespawn() {
    	return false;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {

    	if (this.initWaveSettings) {
    		if (ZCServerTicks.zcGame.wMan != null) {
    			initWaveSettings = false;
	    		BaseEntAI ent = this;
	            WaveManager wMan = ZCServerTicks.zcGame.wMan;
	            
	            //Wave based difficulty adjustments
	    		ent.setExp((int)(ent.getExp() + (ent.getExp() * (wMan.amp_Exp * wMan.wave_Stage))));
	    		ent.setHealth((int)(ent.getHealth() + (ent.getHealth() * (wMan.amp_Health * wMan.wave_Stage))));
	    		ent.lungeFactor += ent.lungeFactor * (wMan.amp_Lunge * wMan.wave_Stage);
	    		if (ent.lungeFactor > wMan.amp_Lunge_Max) ent.lungeFactor = wMan.amp_Lunge_Max;
	    		
	    		//System.out.println("ent.lungeFactor " + ent.lungeFactor);
	    		System.out.println("ent.getHealth() " + ent.getHealth());
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
