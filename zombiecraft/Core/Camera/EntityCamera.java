package zombiecraft.Core.Camera;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class EntityCamera extends EntityPlayer {

	public EntityCamera(World par1World) {
		super(par1World);
		//username = "Camera";
		// TODO Auto-generated constructor stub
		this.setSize(0.000F, 0.000F);
		username = "camera";
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

	@Override
	public void sendChatToPlayer(String var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}

}
