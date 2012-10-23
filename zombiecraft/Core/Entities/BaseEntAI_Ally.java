package zombiecraft.Core.Entities;

import CoroAI.entity.EnumTeam;
import CoroAI.entity.c_EnhAI;
import net.minecraft.src.*;

public class BaseEntAI_Ally extends BaseEntAI
{
	
	public BaseEntAI_Ally(World par1World, double x, double y, double z) {
		this(par1World);
		this.setPosition(x, y, z);
	}
	
    public BaseEntAI_Ally(World par1World)
    {
        super(par1World);
        this.dipl_team = EnumTeam.COMRADE;
        //team = 1;
        
    }
    
    @Override
    public boolean isEnemy(Entity entity1) {
    	if (entity1 instanceof c_EnhAI) {
			if (dipl_team != ((c_EnhAI) entity1).dipl_team) {
				return true;
			} else {
				return false;
			}
		} else {
			if(entity1 instanceof EntityMob && !(entity1 == this) && !(entity1 instanceof EntityCreeper || entity1 instanceof EntityEnderman)) {
				return true;
			} else {
				return false;
			}
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
