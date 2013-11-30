package zombiecraft.Client.Entities;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import zombiecraft.Core.Entities.BaseEntAI;
import zombiecraft.Core.Entities.Zombie;
import zombiecraft.Core.GameLogic.ZCGame;
import zombiecraft.Forge.ZombieCraftMod;
import CoroAI.componentAI.ICoroAI;
import build.render.Overlays;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZCComrade extends RenderBiped
{
    public RenderZCComrade()
    {
        super(new ModelBiped(0.0F), 0.5F);
    }

	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation(ZombieCraftMod.modID + ":textures/entities/comrade/skin10.png");
	}
    
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
    	
    	super.doRender(par1Entity, par2, par4, par6, par8, par9);
    	
    	boolean debug = MinecraftServer.getServer() != null && ZCGame.instance().mapMan != null && MinecraftServer.getServer().isSinglePlayer() && ZCGame.instance().mapMan.editMode;
    	
    	//if (debug) renderDebug((BaseEntAI)par1Entity, par2, par4, par6, par8, par9);
    	
    }
    
    public void renderDebug(BaseEntAI par1Entity, double par2, double par4, double par6, float par8, float par9) {
    	
	    Entity sEnt = MinecraftServer.getServer().worldServerForDimension(ZCGame.ZCDimensionID).getEntityByID(par1Entity.entityId);
		
		if (sEnt != null && sEnt instanceof Zombie) {
			Zombie z = (Zombie)sEnt;
			PathEntity pe = ((EntityLiving)sEnt).getNavigator().getPath();
			
			if (pe != null && pe.getCurrentPathLength() > 1) {
				for (int i = 0; i < pe.getCurrentPathLength() - 1; i++) {
					Vec3 vec = pe.getVectorFromIndex(par1Entity, i);
					Vec3 vec2 = pe.getVectorFromIndex(par1Entity, i+1);
					Overlays.renderLineFromToBlock(vec.xCoord, vec.yCoord+0.5F, vec.zCoord, vec2.xCoord, vec2.yCoord+0.5F, vec2.zCoord, 0x00FF00);
				}
			}
		
		
			if (pe != null/*par1Entity.currentAction != null*/) {
				//if () {
					this.renderLivingLabel(par1Entity, String.valueOf(pe.getCurrentPathIndex() + " - " + z.agent.jobMan.getPrimaryJob().state), par2, par4, par6, 999);
				//}
			} else {
				//this.renderLivingLabel(par1Entity, "wat", par2, par4, par6, 999);
			}
		}
		
    }
}
