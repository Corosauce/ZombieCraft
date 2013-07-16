package zombiecraft.Core.Camera;

import net.minecraft.nbt.NBTTagCompound;

import build.SchematicData;
import build.world.Build;

public class CameraPoint implements SchematicData {

	public float Yaw;
	public float Pitch;
	
	public float posX;
	public float posY;
	public float posZ;
	
	public float camAimSpeed = 0.5F;
	public float camMoveSpeed = 0.2F;

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound, Build build) {
		
		
		this.Yaw = par1NBTTagCompound.getFloat("Yaw");
        this.Pitch = par1NBTTagCompound.getFloat("Pitch");
        
        
        if (build != null) {
        	this.posX = build.map_coord_minX + par1NBTTagCompound.getFloat("posX");
            this.posY = build.map_coord_minY + par1NBTTagCompound.getFloat("posY");
            this.posZ = build.map_coord_minZ + par1NBTTagCompound.getFloat("posZ");
		} else {
			this.posX = par1NBTTagCompound.getFloat("posX");
	        this.posY = par1NBTTagCompound.getFloat("posY");
	        this.posZ = par1NBTTagCompound.getFloat("posZ");
		}
        
        this.camAimSpeed = par1NBTTagCompound.getFloat("camAimSpeed");
        this.camMoveSpeed = par1NBTTagCompound.getFloat("camMoveSpeed");
		
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound, Build build) {
		par1NBTTagCompound.setFloat("Yaw", this.Yaw);
        par1NBTTagCompound.setFloat("Pitch", this.Pitch);
        
        if (build != null) {
        	par1NBTTagCompound.setFloat("posX", this.posX - build.map_coord_minX);
            par1NBTTagCompound.setFloat("posY", this.posY - build.map_coord_minY);
            par1NBTTagCompound.setFloat("posZ", this.posZ - build.map_coord_minZ);
        } else {
        	par1NBTTagCompound.setFloat("posX", this.posX);
            par1NBTTagCompound.setFloat("posY", this.posY);
            par1NBTTagCompound.setFloat("posZ", this.posZ);
        }
        
        par1NBTTagCompound.setFloat("camAimSpeed", this.camAimSpeed);
        par1NBTTagCompound.setFloat("camMoveSpeed", this.camMoveSpeed);
	
	}
	
}
