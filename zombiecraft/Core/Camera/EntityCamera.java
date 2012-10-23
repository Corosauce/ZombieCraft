package zombiecraft.Core.Camera;

import net.minecraft.src.*;

public class EntityCamera extends EntityLiving {

	public EntityCamera(World par1World) {
		super(par1World);
		//username = "Camera";
		// TODO Auto-generated constructor stub
		this.setSize(0.000F, 0.000F);
	}

	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return 999;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		//System.out.println(this.rotationPitch);
	}
	
	@Override
	public void onUpdate() {
		double x = this.posX;
		double y = this.posY;
		double z = this.posZ;
		super.onUpdate();
		this.setPosition(this.posX, y, this.posZ);
		//System.out.println(this.rotationPitch);
	}

}
