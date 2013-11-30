package zombiecraft.Core.Entities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import CoroAI.c_CoroAIUtil;
import CoroAI.componentAI.jobSystem.JobBase;
import CoroAI.componentAI.jobSystem.JobManager;
import CoroAI.entity.EnumActState;
import CoroAI.entity.EnumInfo;
import CoroAI.entity.EnumJobState;

public class JobSurvivor extends JobBase {
	
	public long huntRange = 24;
	public boolean dontStray = true;
	public boolean xRay = false;
	
	public JobSurvivor(JobManager jm) {
		super(jm);
	}
	
	@Override
	public void tick() {
		jobHunter();
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public boolean shouldContinue() {
		return ai.entityToAttack == null;
	}

	@Override
	public void onLowHealth() {
		super.onLowHealth();
		//if (this.name.equals("Makani")) {
		
		//}
		if (hitAndRunDelay == 0 && ent.getDistanceToEntity(ai.lastFleeEnt) > 3F) {
			//hitAndRunDelay = ent.cooldown_Ranged+1;
			//ent.entityToAttack = ent.lastFleeEnt;
			if (ai.entityToAttack != null) {
				//ent.faceEntity(ent.entityToAttack, 180F, 180F);
				//ent.rightClickItem();
				//ent.attackEntity(ent.entityToAttack, ent.getDistanceToEntity(ent.entityToAttack));
				//System.out.println("H&R " + ent.name + " health: " + ent.getHealth());
			}
		} else {
			//ent.entityToAttack = null;
		}
	}
	
	@Override
	public boolean hookHit(DamageSource ds, int damage) {
		if (isEnemy(ds.getEntity())) {
			ai.entityToAttack = ds.getEntity();
		}
		return super.hookHit(ds, damage);
	}
	
	@Override
	public void setJobItems() {
		
		//c_CoroAIUtil.setItems_JobHunt(ent);
		
		
	}
	
	protected void jobHunter() {
	
		dontStray = false;
		
		//this whole function is crap, redo it bitch
		
		//a use for the states
		
		//responding to alert, so you know to cancel it if alert entity / active target is dead
		
		/*if (tryingToFlee && (onGround || isInWater())) {
			tryingToFlee = false;
			fleeFrom(lastFleeEnt);
		}*/
		
		//huntRange = 24;
		ai.maxDistanceFromHome = 48F;
		
		
		//if (true) return;
		
		//health = 8;
		/*if (health < getMaxHealth() * 0.75F) {
			avoid();
			if (rand.nextInt(5) == 0) entityToAttack = null;
		} else {*/
		setJobState(EnumJobState.IDLE);
		
		if (/*ent.getHealth() > ent.getMaxHealth() * 0.90F && */(ai.entityToAttack == null || ent.worldObj.rand.nextInt(20) == 0)) {
			boolean found = false;
			Entity clEnt = null;
			float closest = 9999F;
	    	List list = ent.worldObj.getEntitiesWithinAABBExcludingEntity(ent, ent.boundingBox.expand(huntRange, huntRange/2, huntRange));
	        for(int j = 0; j < list.size(); j++)
	        {
	            Entity entity1 = (Entity)list.get(j);
	            if(isEnemy(entity1))
	            {
	            	if (xRay || ((EntityLivingBase) entity1).canEntityBeSeen(ent)) {
	            		if (sanityCheck(entity1)/* && entity1 instanceof EntityPlayer*/) {
	            			float dist = ent.getDistanceToEntity(entity1);
	            			if (dist < closest) {
	            				closest = dist;
	            				clEnt = entity1;
	            			}
		            		
		            		//found = true;
		            		//break;
	            		}
	            		//this.hasAttacked = true;
	            		//getPathOrWalkableBlock(entity1, 16F);
	            	}
	            }
	        }
	        if (clEnt != null) {
	        	//ent.huntTarget(clEnt);
	        	ai.entityToAttack = clEnt;
	    		ai.setState(EnumActState.FIGHTING);
	        }
	        /*if (!found) {
	        	setState(EnumKoaActivity.IDLE);
	        }*/
		} else {
			
			if (ai.entityToAttack != null) {
				if (ent.getNavigator().noPath() && (ent.getDistanceToEntity(ai.entityToAttack) > 15F || !ent.canEntityBeSeen(ai.entityToAttack))) {
					//PFQueue.getPath(ent, ent.entityToAttack, ent.maxPFRange);
				}
			}
			
		}
		
		//close proximity preventing code
		if (ai.entityToAttack != null) {
			
			ent.getLookHelper().setLookPositionWithEntity(ai.entityToAttack, 10.0F, (float)ent.getVerticalFaceSpeed());
			
			if (!ent.getNavigator().noPath() && ent.getDistanceToEntity(ai.entityToAttack) < 15F) {
				//ent.getNavigator().setPath(null, 0F);
			}
		}
		
		//}
		ent.prevHealth = ent.getHealth();
	}
	
	
	
	public void hunterHitHook(DamageSource ds, int damage) {
		
		/*if (health < getMaxHealth() / 4 * 3) {
			if (ds.getEntity() != null) {
				lastFleeEnt = ds.getEntity();
				tryingToFlee = true;
				//fleeFrom(ds.getEntity());
			}
		}
		prevKoaHealth = health;*/
	}
	
	public boolean sanityCheckHelp(Entity caller, Entity target) {
		if (ent.getHealth() < 10) {
			return false;
		}
		
		if (dontStray) {
			if (target.getDistance(ai.homeX, ai.homeY, ai.homeZ) > ai.maxDistanceFromHome * 1.5) {
				return false;
			}
		}
		if (ent.worldObj.rand.nextInt(2) == 0) {
			return true;
		}
		return false;
	}
	
	public boolean sanityCheck(Entity target) {
		/*if (ent.getHealth() < 10) {
			return false;
		}*/
		
		if (dontStray) {
			if (target.getDistance(ai.homeX, ai.homeY, ai.homeZ) > ai.maxDistanceFromHome) {
				return false;
			}
		}
		return true;
	}
	
	
	
}
