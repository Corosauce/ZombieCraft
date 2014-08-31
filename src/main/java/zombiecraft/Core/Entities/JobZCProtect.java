package zombiecraft.Core.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import zombiecraft.Core.World.LevelConfig;
import CoroUtil.componentAI.jobSystem.JobManager;
import CoroUtil.componentAI.jobSystem.JobProtect;
import CoroUtil.entity.EnumJobState;

public class JobZCProtect extends JobProtect {
	
	public JobZCProtect(JobManager jm) {
		super(jm);
	}
	
	@Override
	public boolean shouldExecute() {
		return true;
	}
	
	@Override
	public boolean shouldContinue() {
		return true;
	}
	
	@Override
	public void fleeFrom(Entity fleeFrom) {
		super.fleeFrom(fleeFrom);
		//ai.entityToAttack = null;
	}
	
	/*@Override
	public void setJobItems() {
		ent.inventory.addItemStackToInventory(new ItemStack(ZCItems.itemSword, 1));
		
		ItemGun spawnGun = (ItemGun)ZCItems.itemDEagle;
		
		ent.inventory.addItemStackToInventory(new ItemStack(spawnGun, 1));
		
		ZCUtil.setAmmoData(ent.fakePlayer.username, spawnGun.ammoType.ordinal(), spawnGun.magSize * 4);
	}*/
	
	@Override
	public void onIdleTickAct() {
		if(((ent.getNavigator().getPath() == null || ent.getNavigator().getPath().isFinished()) && ent.worldObj.rand.nextInt(5) == 0/* || ent.rand.nextInt(80) == 0*/))
        {
        	
        	//System.out.println("home dist: " + ent.getDistance(ent.homeX, ent.homeY, ent.homeZ));
        	if (ent.getDistance(ai.homeX, ai.homeY, ai.homeZ) < ai.maxDistanceFromHome) {
        		if (ent.worldObj.rand.nextInt(5) == 0) {
        			int randsize = 8;
            		ai.walkTo(ent, ai.homeX+ent.worldObj.rand.nextInt(randsize) - (randsize/2), ai.homeY+1, ai.homeZ+ent.worldObj.rand.nextInt(randsize) - (randsize/2),ai.maxPFRange, 600);
        		} else {
        			ai.updateWanderPath();
        		}
        		
        		
        	} else {
        		int randsize = 8;
        		ai.walkTo(ent, ai.homeX+ent.worldObj.rand.nextInt(randsize) - (randsize/2), ai.homeY+1, ai.homeZ+ent.worldObj.rand.nextInt(randsize) - (randsize/2),ai.maxPFRange, 600);
        	}
        } else {
        	if (ent.getNavigator().getPath() == null || ent.getNavigator().getPath().isFinished()) {
    			//ent.lookForItems();
        	}
        }
	}
	
	@Override
	public boolean avoid(boolean actOnTrue) {
		return super.avoid(actOnTrue);
		
	}
	
	@Override
	public void tick() {
		//consider this function broken after the refactoring!!!!!!!!!!!!!!!!!! FIX ME EVENTUALLY!
		checkPlayer();
		minDist = 3F;
		maxDist = 6F;
		// getClosestPlayerToEntity(ent, 16F);
		EntityPlayer entP;
		entP = ent.worldObj.getPlayerEntityByName(playerName);
		
		if (entP == null) return;
		
		
		
		int pX = (int)(entP.posX-0.5F);
		int pY = (int)entP.posY;
		int pZ = (int)(entP.posZ-0.5F);
		
		if (state == EnumJobState.IDLE) {
			ai.walkTo(ent, pX, pY, pZ, ai.maxPFRange, 600);
			setJobState(EnumJobState.W1);
		} else if (state == EnumJobState.W1) {
			if (ent.getDistanceToEntity(entP)/*ent.getDistance(pX, pY, pZ)*/ <= minDist) {
				//ent.setPathExToEntity(null);
				ent.getNavigator().setPath(null, 0F);
				//ent.faceEntity(entP, 30F, 30F);
				
				//setJobState(EnumJobState.W2);
			} else if (walkingTimeout <= 0 || ent.getNavigator().getPath() == null || ent.getNavigator().getPath().isFinished()) {
				//ent.setPathExToEntity(null);
				ai.walkTo(ent, pX, pY, pZ, ai.maxPFRange, 600);
			}
		} else if (state == EnumJobState.W2) {
			
			
			
		}
	}
	
	@Override
	public boolean shouldTickCloseCombat() {
		return false;
	}
	
	/*@Override
	public void onCloseCombatTick() {
		EntityPlayer entP = ent.worldObj.getPlayerEntityByName(playerName);
		
		if (entP == null || (entP != null && entP.getDistanceToEntity(ent) < maxDist)) {
			ent.getMoveHelper().setMoveTo(ent.entityToAttack.posX, ent.entityToAttack.posY, ent.entityToAttack.posZ, ent.getMoveHelper().getSpeed());
			ent.getDataWatcher().updateObject(20, 1);
			
			//jump over drops
			MovingObjectPosition aim = ent.getAimBlock(-2, true);
	    	if (aim != null) {
	    		if (aim.typeOfHit == EnumMovingObjectType.TILE) {
	    			
	    		}
	    	} else {
	    		if (ent.onGround) {
	    			ent.jump();
	    		}
	    	}
			
	    	if (ent.onGround && ent.isCollidedHorizontally && !ent.isBreaking()) {
	    		ent.jump();
			}
		}
	}*/
	
}
