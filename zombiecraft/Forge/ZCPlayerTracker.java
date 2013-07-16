package zombiecraft.Forge;

import net.minecraft.entity.player.EntityPlayer;

import zombiecraft.Core.GameLogic.ZCGame;
import cpw.mods.fml.common.IPlayerTracker;

public class ZCPlayerTracker implements IPlayerTracker {

	@Override
	public void onPlayerLogin(EntityPlayer player) {

		if (player.dimension == ZCGame.ZCDimensionID) {
			if (!player.capabilities.isCreativeMode) {
				(ZCGame.instance()).mapMan.movePlayerToSpawn(player);
			}
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {

	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {

		if (player.dimension == ZCGame.ZCDimensionID) {
			(ZCGame.instance()).mapMan.movePlayerToLobby(player);
		}
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		
		//System.out.println("RESPAWN EVENT, remote: " + player.worldObj.isRemote + ", DIM: " + player.dimension);
		
		//so it looks like this code doesnt work, its too soon after respawn for this to take effect, or at least doesnt work on smp well
		if (player.dimension == ZCGame.ZCDimensionID) {
			if (ZCGame.instance().gameActive) {
				//(ZCGame.instance()).mapMan.movePlayerToSpawn(player);
			} else {
				//(ZCGame.instance()).mapMan.movePlayerToLobby(player);
			}
			
		} else {
			//System.out.println("player respawn, unhandled, dim: " + player.dimension);
		}
	}

}
