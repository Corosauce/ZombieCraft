package zombiecraft.Core.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import zombiecraft.Core.GameLogic.ZCGame;


public class EntityWorldHook extends Entity {

    public EntityWorldHook(World var1) {
        super(var1);
        this.setSize(1.0F, 1.0F);
        
        //ZCGame.instance().worldSaver = this;
    }

    public void setEntityDead() {
        super.setDead();
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean isInRangeToRenderDist(double var1) {
        return true;
    }

    @Override
    public void entityInit() {
    	
    }

    public void onUpdate() {
    	super.onUpdate();
    	this.ignoreFrustumCheck = true;
    	
    	if (worldObj.isRemote) return;
    		
    	if (ZCGame.instance().worldSaver == null) {
    		ZCGame.instance().worldSaver = this;
    	} else {
    		//duplication fix
    		if (ZCGame.instance().worldSaver != this) {
    			this.setDead();
    		}
    	}
    	EntityPlayer plEnt = ZCGame.instance().getAlivePlayer();
		if (plEnt != null) {
			this.setPosition(plEnt.posX, 128, plEnt.posZ);
		} else {
			this.setPosition(0, 128, 0);
		}
    }

    public void writeEntityToNBT(NBTTagCompound var1) {
    	
    	//var1.setString("levelName", ZCGame.instance.mapMan.curLevel);
    	
    	var1.setInteger("editToolMode", ZCGame.instance().mapMan.editToolMode);
    	//var1.setInteger("map_coord_minX", ZCGame.instance.zcLevel.map_coord_minX);
    	//var1.setInteger("map_coord_minY", ZCGame.instance.zcLevel.map_coord_minY);
    	//var1.setInteger("map_coord_minZ", ZCGame.instance.zcLevel.map_coord_minZ);
    	
    	/*var1.setInteger("map_sizeX", ZCGame.instance.zcLevel.buildData.map_sizeX);
    	var1.setInteger("map_sizeY", ZCGame.instance.zcLevel.buildData.map_sizeY);
    	var1.setInteger("map_sizeZ", ZCGame.instance.zcLevel.buildData.map_sizeZ);*/
    	
    	ZCGame.instance().writeGameNBT();
    	System.out.println("worldhook nbt write");
    	
    }

    public void readEntityFromNBT(NBTTagCompound var1) {
    	if (!ZCGame.instance().levelHasInit) ZCGame.instance().levelInit();
    	
    	
    	
    	ZCGame.instance().mapMan.editToolMode = var1.getInteger("editToolMode");
    	
    	//ZCGame.instance.zcLevel.map_coord_minX = var1.getInteger("map_coord_minX");
    	//ZCGame.instance.zcLevel.map_coord_minY = var1.getInteger("map_coord_minY");
    	//ZCGame.instance.zcLevel.map_coord_minZ = var1.getInteger("map_coord_minZ");
    	
    	/*ZCGame.instance.zcLevel.buildData.map_sizeX = var1.getInteger("map_sizeX");
    	ZCGame.instance.zcLevel.buildData.map_sizeY = var1.getInteger("map_sizeY");
    	ZCGame.instance.zcLevel.buildData.map_sizeZ = var1.getInteger("map_sizeZ");*/
    	
    	ZCGame.instance().readGameNBT(this.worldObj);
    	
    	System.out.println("worldhook nbt read");
    }
}
