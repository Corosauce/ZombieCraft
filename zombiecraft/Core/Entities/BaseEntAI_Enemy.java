package zombiecraft.Core.Entities;

import CoroAI.entity.EnumTeam;
import net.minecraft.src.*;

public class BaseEntAI_Enemy extends BaseEntAI
{
	
	public int deathTimer = 0;
	public int deathTimerMax = 12000;
	
	public BaseEntAI_Enemy(World par1World, double x, double y, double z) {
		this(par1World);
		this.setPosition(x, y, z);
	}
	
    public BaseEntAI_Enemy(World par1World)
    {
        super(par1World);
        this.dipl_team = EnumTeam.HOSTILES;
        
    }
    
    @Override
    public boolean isEnemy(Entity ent) {
    	if (super.isEnemy(ent) && (ent instanceof EntityAnimal || ent instanceof EntityMob || (ent instanceof EntityPlayer && dipl_hostilePlayer))) {
    		return true;
    	} else {
    		return false;
    	}
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

    	if (!worldObj.isRemote) {
    		deathTimer++;
    		if (deathTimer > deathTimerMax) {
    			setDead();
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
