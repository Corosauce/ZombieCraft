package zombiecraft.Core.towers;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import zombiecraft.Core.Blocks.TileEntityTower;
import CoroUtil.util.CoroUtilEntity;

public class TowerBase {

	//should be able to implement the orders taking interface
	
	public String name = ""; //this is automatically set when created via BuildingMapping, including when tile entity recreates it from nbt
	//public int teamID = -1;
	public TileEntityTower tEnt = null;
	
	//Tower parameters
	public int range = 6;
	public int damage = 5;
	public int fireRate = 40;
	
	//Tile AI stuff
	public float distTargetFind = range;
	public float rotationYaw;
	public EntityLivingBase targetAttack;
	
	//runtime stuff
	public int cooldownFireCur = 0;
	
	public TowerBase() {
		
	}
	
	public void setTarget(EntityLivingBase parTarget) {
		targetAttack = parTarget;
	}
	
	public void dbg(Object obj) {
		System.out.println("RTSDBG " + name + ": " + obj);
	}
	
	public void init(TileEntityTower parTEnt) {
		tEnt = parTEnt;
	}
	
	public void tickUpdate() {
		
		if (cooldownFireCur > 0) cooldownFireCur--;
		
		if (targetAttack != null) {
			if (targetAttack.isDead || CoroUtilEntity.getDistance(targetAttack, tEnt) > range + 1) {
				targetAttack = null;
			}
		}
		
		if (targetAttack == null || tEnt.worldObj.getWorldTime() % 10 == 0) {
			boolean found = false;
			EntityLivingBase clEnt = null;
			double closest = 9999D;
	    	List list = tEnt.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getAABBPool().getAABB(tEnt.xCoord, tEnt.yCoord, tEnt.zCoord, tEnt.xCoord+1, tEnt.yCoord+1, tEnt.zCoord+1).expand(range, range/2, range));
	        for(int j = 0; j < list.size(); j++)
	        {
	        	EntityLivingBase entity1 = (EntityLivingBase)list.get(j);
	            if(isEnemy(entity1))
	            {
	            	if (CoroUtilEntity.canCoordBeSeen(entity1, tEnt.xCoord, tEnt.yCoord+1, tEnt.zCoord)) {
            			double dist = CoroUtilEntity.getDistance(entity1, tEnt);
            			if (dist < closest) {
            				closest = dist;
            				clEnt = entity1;
            			}
	            	}
	            }
	        }
	        if (clEnt != null) {
	        	setTarget(clEnt);
	        }
		}
		
		if (targetAttack != null) {
			if (cooldownFireCur == 0) {
				cooldownFireCur = fireRate;
				shoot(targetAttack);
			}
		}
		
	}
	
	public void shoot(EntityLivingBase parTarget) {
		parTarget.attackEntityFrom(DamageSource.onFire, 5);
	}
	
	public boolean isEnemy(Entity ent) {
		if (!(ent instanceof EntityPlayer)) {
			return true;
		}
		return false;
	}
	
	public void writeToNBT(NBTTagCompound var1)
    {
        //var1.setInteger("teamID", teamID);
        //var1.setBoolean("isBuilt", isBuilt);
    }

    public void readFromNBT(NBTTagCompound var1)
    {
        //if (var1.hasKey("teamID")) teamID = var1.getInteger("teamID");
        //isBuilt = var1.getBoolean("isBuilt");
    }
	
}
