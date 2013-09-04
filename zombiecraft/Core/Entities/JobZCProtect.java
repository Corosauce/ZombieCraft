package zombiecraft.Core.Entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import zombiecraft.Core.ZCItems;
import zombiecraft.Core.ZCUtil;
import zombiecraft.Core.Items.ItemGun;
import CoroAI.entity.EnumJobState;
import CoroAI.entity.JobManager;
import CoroAI.entity.JobProtect;

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
	public void setJobItems() {
		ent.inventory.addItemStackToInventory(new ItemStack(ZCItems.itemSword, 1));
		
		ItemGun spawnGun = (ItemGun)ZCItems.itemDEagle;
		
		ent.inventory.addItemStackToInventory(new ItemStack(spawnGun, 1));
		
		ZCUtil.setAmmoData(ent.fakePlayer.username, spawnGun.ammoType.ordinal(), spawnGun.magSize * 4);
	}
	
	@Override
	public void onIdleTick() {
		if(((ent.getNavigator().getPath() == null || ent.getNavigator().getPath().isFinished()) && ent.rand.nextInt(5) == 0/* || ent.rand.nextInt(80) == 0*/))
        {
        	
        	//System.out.println("home dist: " + ent.getDistance(ent.homeX, ent.homeY, ent.homeZ));
        	if (ent.getDistance(ent.homeX, ent.homeY, ent.homeZ) < ent.maxDistanceFromHome) {
        		if (ent.rand.nextInt(5) == 0) {
        			int randsize = 8;
            		ent.walkTo(ent, ent.homeX+ent.rand.nextInt(randsize) - (randsize/2), ent.homeY+1, ent.homeZ+ent.rand.nextInt(randsize) - (randsize/2),ent.maxPFRange, 600);
        		} else {
        			ent.updateWanderPath();
        		}
        		
        		
        	} else {
        		int randsize = 8;
        		ent.walkTo(ent, ent.homeX+ent.rand.nextInt(randsize) - (randsize/2), ent.homeY+1, ent.homeZ+ent.rand.nextInt(randsize) - (randsize/2),ent.maxPFRange, 600);
        	}
        } else {
        	if (ent.getNavigator().getPath() == null || ent.getNavigator().getPath().isFinished()) {
    			//ent.lookForItems();
        	}
        }
	}
	
	@Override
	public boolean avoid(boolean actOnTrue) {
		return false;
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
			ent.walkTo(ent, pX, pY, pZ, ent.maxPFRange, 600);
			setJobState(EnumJobState.W1);
		} else if (state == EnumJobState.W1) {
			if (ent.getDistanceToEntity(entP)/*ent.getDistance(pX, pY, pZ)*/ <= minDist) {
				//ent.setPathExToEntity(null);
				ent.getNavigator().setPath(null, 0F);
				//ent.faceEntity(entP, 30F, 30F);
				
				//setJobState(EnumJobState.W2);
			} else if (walkingTimeout <= 0 || ent.getNavigator().getPath() == null || ent.getNavigator().getPath().isFinished()) {
				//ent.setPathExToEntity(null);
				ent.walkTo(ent, pX, pY, pZ, ent.maxPFRange, 600);
			}
		} else if (state == EnumJobState.W2) {
			
			
			
		}
	}
	
	@Override
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
	}
	
}
