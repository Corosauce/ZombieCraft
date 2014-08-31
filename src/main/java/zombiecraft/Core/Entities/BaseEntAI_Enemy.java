package zombiecraft.Core.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import zombiecraft.Core.ZCItems;
import CoroUtil.diplomacy.DiplomacyHelper;
import CoroUtil.diplomacy.TeamTypes;

public class BaseEntAI_Enemy extends BaseEntAI
{
	
	public int deathTimer = 0;
	public int deathTimerMax = 6000;
	
	public int pickupDropRarity = 30;
	
    public BaseEntAI_Enemy(World par1World)
    {
        super(par1World);
        agent.dipl_info = TeamTypes.getType("hostile");
        
    }
    
    @Override
    public void dropItems() {
    	pickupDropRarity = 40;
    	
    	int chance = worldObj.rand.nextInt(pickupDropRarity);
    	
    	//System.out.println("drop chance roll: " + chance);
    	
    	if (chance == 0) {
    		this.dropItem(getDropItemId(), 1);
    	}
    }
    
    public void dropFewItems(boolean par1, int par2) {
    	
    	
    }
    
    public Item getDropItemId() {
    	int id = worldObj.rand.nextInt(4);
    	if (id == 0) {
    		return ZCItems.itemPickupDoublePoints;
    	} else if (id == 1) {
    		return ZCItems.itemPickupInstaKill;
    	} else if (id == 2) {
    		return ZCItems.itemPickupMaxAmmo;
    	} else {
    		return ZCItems.itemPickupNuke;
    	}
    }
    
    //@Override
    public boolean isEnemy2(Entity ent) {
    	if (super.isEnemy(ent) && (ent instanceof EntityAnimal || ent instanceof EntityMob || (ent instanceof EntityPlayer))) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public boolean isEnemy(Entity entity1) {
    	if (entity1 instanceof EntityAnimal || entity1 instanceof EntityMob || entity1 instanceof EntityPlayer) return true;
    	return DiplomacyHelper.shouldTargetEnt(this, entity1);
		/*if (entity1 instanceof c_EnhAI) {
			if (dipl_team != ((c_EnhAI) entity1).dipl_team) {
				return true;
			} else {
				return false;
			}
		} else {
			//return c_CoroAIUtil.isEnemy(this, entity1);
			return (entity1 instanceof EntityAnimal || entity1 instanceof EntityMob || (entity1 instanceof EntityPlayer));
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
