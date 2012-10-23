package zombiecraft.Forge;

import zombiecraft.Core.Entities.EntityBullet;
import zombiecraft.Core.GameLogic.ZCGame;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ZCEventHandler {
	        /**
         * The key is the @ForgeSubscribe annotation and the cast of the Event you put in as argument.
         * The method name you pick does not matter. Method signature is public void, always.
         */
        @ForgeSubscribe
        public void entityAttacked(LivingAttackEvent event)
        {
        	//System.out.println("attacked");
        	if (event.entityLiving instanceof EntityPlayer) {
        		//needs to be client side - knockback is called on server side and transmits somehow...
        		/*event.entityLiving.motionX = 0;
        		event.entityLiving.motionY = 0;
        		event.entityLiving.motionZ = 0;*/
        	}
        	//event.useBlock = Event.Result.DENY;
        	//event.useItem = Event.Result.DENY;
        	
        	//event.setCanceled(true);
        	//event.action = PlayerInteractEvent.Action.RIGHT_CLICK_AIR;
        }
        
        @ForgeSubscribe
        public void interact(PlayerInteractEvent event)
        {
        	//System.out.println("attacked");
        	if (event.entityLiving instanceof EntityPlayer) {
        		//needs to be client side - knockback is called on server side and transmits somehow...
        		/*event.entityLiving.motionX = 0;
        		event.entityLiving.motionY = 0;
        		event.entityLiving.motionZ = 0;*/
        	}
        	
        	//event.setCanceled(true);
        	
        	//event.useBlock = Event.Result.DENY;
        	//event.useItem = Event.Result.DENY;
        	
        	//event.action = PlayerInteractEvent.Action.RIGHT_CLICK_AIR;
        }
        
        @ForgeSubscribe
        public void deathEvent(LivingDeathEvent event) {
        	
        	Entity entSource = event.source.getSourceOfDamage();
        	
        	if (entSource instanceof EntityBullet) {
        		entSource = ((EntityBullet)entSource).owner;
        	}
        	
        	if (entSource instanceof EntityPlayer) {
        		ZCGame zcG = ZCGame.instance();
            	if (zcG != null) {
            		zcG.playerKillEvent((EntityPlayer)entSource, event.entity);
            	}
        	}
        }
        
        @ForgeSubscribe
    	@SideOnly(Side.CLIENT)
        public void worldRender(RenderWorldLastEvent event)
        {
        	ZCGame zcG = ZCGame.instance();
        	if (zcG != null) {
        		if (zcG.mapMan.editMode) ZCGame.instance().renderInWorldOverlay();
        	}
        }
	
}
