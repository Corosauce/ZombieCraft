package zombiecraft.Core.Camera;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class EntityCamera extends EntityPlayer {

	public EntityCamera(World par1World) {
		super(par1World, "camera");
		//username = "Camera";
		// TODO Auto-generated constructor stub
		this.setSize(0.000F, 0.000F);
	}

	@Override
	protected void func_110147_ax() {
		super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(999.0D);
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
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {
		// TODO Auto-generated method stub
		
	}

}
