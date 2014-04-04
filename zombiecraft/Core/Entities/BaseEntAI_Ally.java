package zombiecraft.Core.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import CoroUtil.componentAI.AIAgent;
import CoroUtil.diplomacy.DiplomacyHelper;
import CoroUtil.diplomacy.TeamTypes;
import CoroUtil.entity.EnumDiploType;

public class BaseEntAI_Ally extends BaseEntAI
{
	
    public BaseEntAI_Ally(World par1World)
    {
        super(par1World);
        //this.dipl_team = EnumDiploType.COMRADE;
        agent.dipl_info = TeamTypes.getType("comrade");
        //team = 1;
        
        agent.maxReach_Ranged = 16F;
    }
    
    @Override
    protected void updateAITasks() {
    	super.updateAITasks();
    	//agent.maxReach_Ranged = 32F;
    }
    
    @Override
    public void checkAgent() {
		if (agent == null) agent = new AIAgent(this, true);
		agent.entInv.rangedInUseTicksMax = 0;
	}
    
    @Override
    public boolean isBreaking() {
		return false;
	}
    
    @Override
    public boolean isEnemy(Entity entity1) {
    	return DiplomacyHelper.shouldTargetEnt(this, entity1);
    	/*if (entity1 instanceof c_EnhAI) {
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
		}*/
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
