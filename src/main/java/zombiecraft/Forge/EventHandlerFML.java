package zombiecraft.Forge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import zombiecraft.Core.GameLogic.ZCGame;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class EventHandlerFML {

	public static World lastWorld = null;
	
	@SubscribeEvent
	public void tickWorld(WorldTickEvent event) {
		if (event.phase == Phase.START) {
			
		}
	}
	
	@SubscribeEvent
	public void tickServer(ServerTickEvent event) {
		
		if (event.phase == Phase.START) {
			ZCServerTicks.onTickInGame();
		}
		
	}
	
	@SubscribeEvent
	public void tickClient(ClientTickEvent event) {
		if (event.phase == Phase.START) {
			ZCKeybindHandler.tickClient();
			ZCClientTicks.onTickInGame();
		}
	}
	
	@SubscribeEvent
	public void tickRenderScreen(RenderTickEvent event) {
		if (event.phase == Phase.END) {
			ZCClientTicks.onRenderTick();
			//ClientTickHandler.onRenderTick();
		}
	}
	
	@SubscribeEvent
	public void playerLogin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (player.dimension == ZCGame.ZCDimensionID) {
			if (!player.capabilities.isCreativeMode) {
				if (!(ZCGame.instance()).gameActive) {
					(ZCGame.instance()).mapMan.movePlayerToLobby(player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void playerChangeDimension(PlayerChangedDimensionEvent event) {
		EntityPlayer player = event.player;
		if (player.dimension == ZCGame.ZCDimensionID) {
			(ZCGame.instance()).mapMan.movePlayerToLobby(player);
		}
	}
	
	@SubscribeEvent
	public void playerRespawn(PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		
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
